<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>스터디의 시작, 페이퍼민트</title>
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/datepickr.css">
<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="/js/datepickr.js"></script>
<script src="/js/guinness.js"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div id='note-list-container' class='content wrap'>
		<span id="group-name"></span>
		<div id='create-new-button'>
			<i class="fa fa-plus-circle"></i>
		</div>
		<div id='group-member-container'>
			<form id="addMemberForm" action="/group/add/member" method="post">
				<input type="hidden" name="groupId">
				<input class="inputText" type="text" name="userId">
				<input class="inputBtn" type="submit" value="초대">
			</form>
			<ul id='group-member'>
			</ul>
		</div>
	</div>
	<template id="create-note-template">
		<div id="createNoteForm">
			<input type="hidden" name="groupId" value="">
			<table>
				<tr>
					<td>날짜</td>
					<td><input id="targetDate" name="targetDate" value="" readonly /><i id="datepickr" class="fa fa-calendar"></i></td>
				</tr>
				<tr>
					<td>내용</td>
					<td><textarea id="noteText" style="resize: none" rows="10" cols="145" name="noteText"></textarea></td>
				</tr>
			</table>
			<button id="create-note" class="btn btn-pm">작성</button>
		</div>
	</template>
	<template id="view-note-template">
		<div class="note-content">
		</div>
		<div id="commentListUl">
			
		</div>
		<form id="commentForm" method="post" >
			<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea><br>
			<button id='submitComment' class='btn btn-pm'>확인</button>
		</form>	
	</template>
	<template id="comment-template">
		<li>
			<img class='avatar' class='avatar' src='/img/avatar-default.png'>
			<div class='comment-container'>
				<div><span class='comment-user'></span></div>
				<div class='comment'></div>
			</div>
		</li>
	</template>
	<script>
		window.addEventListener('load', function() {
			var groupId = window.location.pathname.split("/")[2];
			document.querySelector("#addMemberForm input[name='groupId']").value = groupId;
			readNoteList(groupId, guinness.util.today("-"));
			readMember(groupId);
			
			var noteModal = document.querySelector('#create-new-button');
			noteModal.addEventListener('click', function() {
				var bodyTemplate = document.querySelector("#create-note-template").content;
				bodyTemplate = document.importNode(bodyTemplate, true);
				guinness.util.modal({
					header: "새 노트 작성",
					body: bodyTemplate,
					defaultCloseEvent:false
				});
				setNoteModal(groupId);
				document.querySelector('.modal-body').setAttribute('class','modal-body note-modal');
				document.querySelector('.modal-close-btn').addEventListener('click', function(e){
					cancelNoteCreate();
				}, false);
				document.querySelector('.modal-cover').addEventListener('click', function(e){
					if (e.target.className==='modal-cover') {
						cancelNoteCreate();
					}
				}, false);
				document.querySelector('.modal-cover').setAttribute('tabindex',0);
				document.querySelector('.modal-cover').addEventListener('keydown',function(e){
					if(e.keyCode === 27){
						console.log('key');
						cancelNoteCreate();
					}
				},false);
				document.querySelector('#create-note').addEventListener('click', createNote, false);
			}, false);
			document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);

			var groupName = getCookie(groupId);
			document.title = groupName;
			groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			document.querySelector('#group-name').innerHTML = groupName;
		}, false);

		function setNoteModal(groupId) {
			document.querySelector("#targetDate").value = guinness.util.today("-");
			document.querySelector("#createNoteForm input[name='groupId']").value = groupId;
			datepickr('.fa-calendar', {
				dateFormat : 'Y-m-d',
				altInput : document.querySelector('#targetDate')
			});
		}
		
		function cancelNoteCreate(e) {
			if (document.querySelector(".modal-cover #noteText").value != "") {
				guinness.util.alert("취소","작성중인 노트 기록을 취소하시겠습니까?",function() { document.querySelector('.modal-cover').remove(); }, function() {});
				return;
			}
			document.querySelector('.modal-cover').remove();
		}

		function getCookie(sKey) {
			if (!sKey) {
				return undefined;
			}
			return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*"
					+ encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1"))
					|| undefined;
		}
		
		function readNoteList(groupId, targetDate) {
		  guinness.ajax({ 
			  method: "get", 
			  url: "/notelist/read?groupId="+groupId+"&targetDate="+targetDate, 
			  success: 
				function(req) {
				  var json = JSON.parse(req.responseText);
				  if (json.length != 0) {
					  appendNoteList(json);
				  }
				}  
		  });
		}

		function appendNoteList(json) {
			//"새 노트를 작성해주세요" 삭제
			var el = document.querySelector("#empty-message");
			if (el != undefined) {
				el.parentNode.removeChild(el);
			}
			//리스트 초기화
			el = document.querySelectorAll(".diary-list");
			var elLength = el.length;
			if (el != undefined) {
				for (var i = elLength-1; i >= 0; i--) {
			 		el[i].outerHTML = "";
				}
			}
			//날짜별로 들어갈수 있게...
			var newEl = undefined;
			var obj = undefined;
			var out = "";
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var targetDate = obj.targetDate;
				targetDate = targetDate.split(" ");
				targetDate = targetDate[0];
				targetDate = targetDate.replace(/'-'/g, '');
				el = document.querySelector("#day-" + targetDate);
				if (el == undefined) {
					el = document.createElement("ul");
					el.setAttribute("id", "day-" + targetDate);
					el.setAttribute("class", "diary-list");
					newEl = document.createElement("div");
					newEl.setAttribute("class", "diary-date");
					newEl.innerHTML = "<span>" + targetDate + "</span>";
					el.appendChild(newEl);
					document.querySelector('#note-list-container').appendChild(el);
				}
				newEl = document.createElement("a");
				newEl.setAttribute("href", "#");
				newEl.setAttribute("onclick", "readNoteContents(" + obj.noteId + " )");
				out = "";
				out += "<li><img class='avatar' class='avatar' src='/img/avatar-default.png'>";
				out += "<div class='msgContainer'>";
				out += "<span class='userName'>" + obj.userName + "</span>";
				out += "<div class='qhsans'>";
				out += obj.noteText;
				out += "</div></div></li>";
				newEl.innerHTML = out;
				el.appendChild(newEl);
			}
		}

		var currScrollTop;
		function readNoteContents(noteId) {
			currScrollTop = document.body.scrollTop;
			guinness.ajax({
				method: 'get',
				url: '/note/read?noteId=' + noteId,
				success: function(req) {
					var json = JSON.parse(req.responseText);
					showNoteModal(json);
					document.body.scrollTop = currScrollTop;
				}
			});
		}

		function showNoteModal(obj) {
			var bodyTemplate = document.querySelector("#view-note-template").content;
			bodyTemplate = document.importNode(bodyTemplate, true);
			guinness.util.modal({
				header: obj.userName,
				body: bodyTemplate,
				defaultCloseEvent: true
			});
			document.querySelector('.modal-body').setAttribute('class','modal-body note-modal');
			document.querySelector('.note-content').innerHTML = obj.noteText;			
			document.querySelector('#commentForm').addEventListener('submit', function(e) { e.preventDefault(); createComment(obj); }, false);

			readComments(obj.noteId);

			/* 노트 상세보기 할때마다 이벤트 리스너가 생성되므로 주석처리함
			document.body.addEventListener('keydown', function(e) {
				if (e.keyCode === 27) {
					document.querySelector("#contents-window").remove();
				}
			});*/
		}

		function readComments(noteId) {
			var el = document.querySelector('#commentListUl');
			while (el.hasChildNodes()) {
				el.removeChild(el.firstChild);
			}
			guinness.ajax({
				method:"get",
				url:"/comment?noteId="+noteId,
				success: function(req) {
					var json = JSON.parse(req.responseText);
					console.log(json);
					for (var i = 0; i < json.length; i++) {
						obj = json[i];
						document.querySelector('#commentListUl').innerHTML += "<li comment-id='"+obj.commentId+"'><img class='avatar' src='/img/avatar-default.png'>" + obj.commentText + "    "+ obj.createDate +" "+ obj.userName + "</li>";
					}
				}		
			});
		}

		function createComment(obj) {
			var commentText = document.querySelector('#commentText').value;
			var userId = obj.userId;
			var noteId = obj.noteId;
			var param = "commentText=" + commentText + "&commentType=A" + "&userId=" + userId + "&noteId=" + noteId;
			guinness.ajax({
				method:"post",
				url:"/comment/create",
				param:param,
				success: function(req) {
					document.querySelector("#commentText").value="";
					
					var el = document.querySelector('#commentListUl');
					while (el.hasChildNodes()) {
						el.removeChild(el.firstChild);
					}
					var json = JSON.parse(req.responseText);
					console.log(json);
					for (var i = 0; i < json.length; i++) {
						obj = json[i];
						document.querySelector('#commentListUl').innerHTML += "<li comment-id='"+obj.commentId+"'><img class='avatar' src='/img/avatar-default.png'>" + obj.commentText + "    "+ obj.createDate +" "+ obj.userName + "</li>";
					}
				}
			});
		}

		function createNote() {
			var targetDate = document.querySelector('#targetDate').value;
			var groupId = document.querySelector('#createNoteForm input[name="groupId"]').value;
			var noteText = document.querySelector('#noteText').value;
			noteText = noteText.replace(/\n/g,"<br/>");
			if (noteText === "") {
				guinness.util.alert("빈 노트","노트를 작성하지 않았습니다.");
				return;
			}
			var param = "groupId=" + groupId + "&targetDate=" + targetDate + "&noteText=" + noteText;
			guinness.ajax({
				method:"post",
				url:"/note/create",
				param: param,
				success: function(req) {
					document.querySelector(".modal-cover").remove();
					readNoteList(groupId, targetDate);
				}
			});
		}
		
		function addMember(userId, groupId) {
			var userId = document.querySelector('#addMemberForm input[name="userId"]').value;
			var groupId = document.querySelector('#addMemberForm input[name="groupId"]').value;
			guinness.ajax({
				method:"post",
				url:"/group/add/member",
				param:"userId="+userId+"&groupId="+groupId,
				success:
				  function(req) {
					var json = JSON.parse(req.responseText)
					if(json === "unknownUser") guinness.util.alert("멤버추가 실패","사용자를 찾을 수 없습니다!");
					else if(json === "joinedUser") guinness.util.alert("멤버추가 실패","사용자가 이미 가입되어있습니다!");
					else {
						appendMember(json);
					}
					document.querySelector('.inputText').value = "";
			      }	
			});
		}
		
		function readMember(groupId) {
			guinness.ajax({ 
				method:"get", 
				url:"/group/read/member?groupId="+groupId, 
				success: 
				  function(req) {
					appendMember(JSON.parse(req.responseText));
				  } 
			});
		}
		
		function appendMember(json) {
			var el = document.querySelector("#group-member");
			el.innerHTML = "";
			for (var i = 0; i < json.length; i++) {
				var newLi = document.createElement("li");
				newLi.innerHTML = "<input type='checkbox' checked=true>"+json[i].userName;
				el.appendChild(newLi);
			}
		}
	</script>
</body>
</html>
