<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="/js/datepickr.js"></script>
<script src="/js/guinness.js"></script>
<script src="/js/markdown.js"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUserId}">
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div id='note-list-container' class='content wrap'>
		<a href="/g/${groupId}"><span id="group-name"></span></a>
		<form id="notes-create-form" action="/note/editor" method="get">
			<input id="groupId" type="hidden" name="groupId" value="">
			<button id='create-new-button' type="submit">
				<i class="fa fa-plus-circle"></i>
			</button>
		</form>
		<div id='group-member-container'>
			<form id="addMemberForm" action="/group/add/member" method="post">
				<input type="hidden" name="groupId"> <input
					class="inputText" type="text" name="userId"> <input
					class="inputBtn" type="submit" value="초대">
			</form>
			<input class="memberAllClick" type="checkbox" checked=true
				onclick="allCheckMember()" />전체선택 <input class="inputBtn"
				type="submit" value="확인" onclick="reloadNoteList()" />

			<ul id='group-member'>
			</ul>
		</div>
	</div>
	<template id="view-note-template">
	<div class="note-content"></div>
	<div id="commentListUl"></div>
	<form id="commentForm" method="post">
		<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea>
		<br>
		<button id='submitComment' class='btn btn-pm'>확인</button>
	</form>
	</template>
	<template id="comment-template">
	<li><img class='avatar' class='avatar'
		src='/img/profile/avatar-default.png'>
		<div class='comment-container'>
			<div class='comment-info'>
				<span class='comment-user'></span> <span class='comment-date'></span>
			</div>
			<div class='comment'></div>
			<div class='comment-util'></div>
		</div></li>
	</template>
	<script>
		window.addEventListener('load', function() {
			var groupId = window.location.pathname.split("/")[2];
			document.querySelector("#addMemberForm input[name='groupId']").value = groupId;
			document.querySelector("#groupId").value = groupId;
			readMember(groupId);
			
			document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);

			document.title = ${groupName};
			var groupName = (${groupName}.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			document.querySelector('#group-name').innerHTML = groupName;
			appendNoteList(${noteList});

			
			_setMemberListPosition();
		}, false);

		window.addEventListener('resize', function() {
			_setMemberListPosition();
		}, false);

		function _setMemberListPosition() {
			
			var elCreateNoteBtn = document.querySelector('#create-new-button');
			var rect = elCreateNoteBtn.getBoundingClientRect();

			var elMemberList = document.querySelector('#group-member-container');
			elMemberList.style.top = rect.top+"px";
			elMemberList.style.left = rect.right+290+"px";
		}
		
		function cancelNoteCreate(e) {
			if (document.querySelector(".modal-cover #noteText").value != "") {
				guinness.util.alert("취소","작성중인 노트 기록을 취소하시겠습니까?",function() { document.querySelector('.modal-cover').remove(); }, function() {});
				return;
			}
			document.querySelector('.modal-cover').remove();
		}
		
		function readNoteList(groupId, targetDate) {
		  guinness.ajax({ 
			  method: "get", 
			  url: "/note/list?groupId="+groupId+"&targetDate="+targetDate, 
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
			el = document.querySelectorAll(".note-list");
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
					el.setAttribute("class", "note-list");
					newEl = document.createElement("div");
					newEl.setAttribute("class", "note-date");
					newEl.innerHTML = "<span>" + targetDate + "</span>";
					el.appendChild(newEl);
					document.querySelector('#note-list-container').appendChild(el);
				}
				var noteText=new markdownToHtml(obj.noteText).getHtmlText();
				var attention = noteText.match(/<span class="attention">.{1,}<\/span>/g);
				if (attention!==null) {
					attention = attention.join('<br />');
				}
				var question = noteText.match(/<span class="question">.{1,}<\/span>/g);
				if (question!==null){
					question = question.join('<br />');	
				}
				var tag = noteText.match(/<span class="tag">.{1,}<\/span>/g);
				if (tag!==null) {
					tag = tag.join(' ');
				}
				newEl = document.createElement("a");
				newEl.setAttribute("id", obj.noteId);
				newEl.setAttribute("href", "#");
				out = "";
				out += "<li><img class='avatar' class='avatar' src='/img/profile/"+obj.userImage+"'>";

				var userId = document.getElementById("sessionUserId").value;
				if(userId === obj.userId) {
					out += "<div class='note-util'><i class='fa fa-pencil'></i><i class='fa fa-trash'></i></div>";
				}				
				out += "<div class='content-container'>";
				out += "<div><span class='userName'>" + obj.userName + "</span><span class='userId'>" + obj.userId + "</span></div>";
				out += "<div><span class='note-date'>" + obj.targetDate + "</span></div>";
				if (attention!==null) {out += attention+'<br />'}
				if (question!==null) {out += question+'<br />'}
				if (tag!==null) {out += tag+'<br />'}
				out += "<div><i class='fa fa-comments'> " + obj.commentCount + "</i></div></div></li>";
				newEl.innerHTML = out;
				el.appendChild(newEl);
				document.getElementById(obj.noteId).addEventListener("click", function(e) {
					if(e.target.className === "fa fa-trash") {
						confirmDeleteNote(this.getAttribute("id"));
					} else if(e.target.className === "fa fa-pencil") {
						window.location.href = "/note/editor?noteId=" + this.getAttribute("id") + "&groupId=" + document.querySelector("#groupId").value;
					} else {
						readNoteContents(this.getAttribute("id"));
					}
				}, false);
			}
		}


		function confirmDeleteNote(noteId) {
			var message = "노트를 삭제하시겠습니까?";
				guinness.util.alert("노트 삭제", message, function() {
					document.body.style.overflow = "auto";
					deleteNote(noteId);
				}, function() {
					document.body.style.overflow = "auto";
                	return;
			});
		}

		function deleteNote(noteId) {
			guinness.ajax({
				method: "delete",
				url: "/notes/" + noteId,
				success: function(req) {
					var json = JSON.parse(req.responseText);
					if(json === "success") {
						// TODO reload방식 말고 removeChild를 이용해서 삭제할 것
						// 날짜에 노트가 1개 있을 경우 날짜도 같이 지워져야 한다
						document.location.reload(true);
					}
				}
			});
		}

		var currScrollTop;
		function readNoteContents(noteId) {
			currScrollTop = document.body.scrollTop;
			guinness.ajax({
				method: 'get',
				url: '/notes/' + noteId,
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
				defaultCloseEvent: false,
				whenCloseEvent: function(){
					reloadNoteList();
				}
			});
			document.querySelector('.modal-body').setAttribute('class','modal-body note-modal');
			document.querySelector('.note-content').innerHTML = new markdownToHtml(obj.noteText).getHtmlText();
			document.querySelector('#commentForm').addEventListener('submit', function(e) { e.preventDefault(); createComment(obj); }, false);

			readComments(obj);
		}

		function readComments(obj) {
			var userId = document.getElementById("sessionUserId").value;
			var noteId = obj.noteId;
			guinness.ajax({
			  method : "get",
			  url : "/comment?noteId=" + noteId,
			  success : function(req) {
			    appendComment(JSON.parse(req.responseText));
			  }
			});
		}
		
		function appendComment(json) {
			var el = document.querySelector('#commentListUl');
			var userId = document.getElementById("sessionUserId").value;
			while (el.hasChildNodes()) {
				el.removeChild(el.firstChild);
			}
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var commentTemplate = document.querySelector("#comment-template").content;
				commentTemplate = document.importNode(commentTemplate, true);
				var commentList = document.querySelector('#commentListUl');
				commentList.appendChild(commentTemplate);
				var commentEl = commentList.querySelector('li:last-child');
				commentEl.setAttribute('id', 'cmt-'+obj.commentId);
				commentEl.querySelector('.comment-user').innerHTML = obj.userName;
				commentEl.querySelector('.comment-date').innerHTML = obj.createDate;
				commentEl.querySelector('.comment').innerHTML = obj.commentText;
				commentEl.querySelector('.avatar').setAttribute("src","/img/profile/"+obj.userImage);
				if (userId === obj.userId) {
					commentEl.querySelector('.comment-util').innerHTML = "<div class='default-utils'><a onclick='showEditInputBox(&quot;"+ obj.commentText + "&quot; , &quot;"+ obj.commentId + "&quot;)'>수정</a><a href='#' onclick='deleteComment(&quot;" + obj.commentId + "&quot;)'>삭제</a></div>"
				}
			}
		}
		
		function updateComment(commentId, commentText){
			guinness.ajax({
				method:"put",
				url:"/comment/" + commentId + "/" + commentText,
				success: function(req) {
					var json = JSON.parse(req.responseText);
					var el = document.querySelector("#cmt-"+commentId);
					el.querySelector('.comment').innerHTML = json.commentText;
					el.querySelector('.comment-date').innerHTML = json.createDate;
					el.querySelector('.comment').setAttribute('contentEditable',false);
					el.querySelectorAll('.comment-update').remove();
					el.querySelector('.default-utils').show();
				}
			});
		}
		
		function deleteComment(commentId){
			guinness.ajax({
				method:"get",
				url:"/comment/" + commentId + "/delete",
				success: function(req) {
					document.querySelector('#cmt-'+commentId).remove();
				}
			});
		}
		
		function showEditInputBox(commentText, commentId) {
			var el = document.querySelector('#cmt-'+commentId);
			el.querySelector('.default-utils').hide();
			el.querySelector('.comment').setAttribute('contentEditable',true);
			var updateButton = guinness.createElement({
				name: "a",
				attrs: {
					'class': "comment-update"
				},
				content:"확인"
			});
			var cancelButton = guinness.createElement({
				name: "a",
				attrs: {
					'class': "comment-update"
				},
				content:"취소"
			});
			updateButton.addEventListener('click', function(){
				var el = document.querySelector('#cmt-'+commentId);
				var commentText = el.querySelector('.comment').textContent;
				updateComment(commentId, commentText);
			},false);
			cancelButton.addEventListener('click', function(){
				var el = document.querySelector('#cmt-'+commentId);
				el.querySelector('.comment').setAttribute('contentEditable',false);
				el.querySelector('.comment').innerHTML = commentText;
				el.querySelectorAll('.comment-update').remove();
				el.querySelector('.default-utils').show();
			},false);
			el.querySelector('.comment-util').appendChild(updateButton);
			el.querySelector('.comment-util').appendChild(cancelButton);
		}

		function createComment(obj) {
			var commentText = document.querySelector('#commentText').value;
			if(commentText !== ""){
				var userId = document.getElementById("sessionUserId").value;
				var noteId = obj.noteId;
				var commentType = "A";
				guinness.ajax({
					method:"put",
					url:"/comment/create/" + commentText + "/" + commentType + "/" + noteId,
					success: function(req) {
						appendComment(JSON.parse(req.responseText));
						document.querySelector('#commentText').value ="";
					}
				});
			}
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
				url:"/group/read/member/"+groupId, 
				success: 
				  function(req) {
					if(JSON.parse(req.responseText).success){
						appendMember(JSON.parse(req.responseText).listValues);
					}else{
						 window.location.href = JSON.parse(req.responseText).locationWhenFail;
					}
				  } 
			});
		}

		function appendMember(json) {
			var el = document.querySelector("#group-member");
			el.innerHTML = "";
			for (var i = 0; i < json.length; i++) {
				var newLi = document.createElement("li");
				newLi.innerHTML = "<input type='checkbox' class='memberChk' checked=true value="+json[i].userId+" onclick='OnOffMemberAllClickBtn()'>"+json[i].userName+"<br/>"+"("+json[i].userId+")";
				el.appendChild(newLi);
			}
		}
		
		function allCheckMember(){
			
			var objs = document.querySelectorAll(".memberChk");
			var allchk = document.querySelector(".memberAllClick");
			
			for(var i=0; i<objs.length; i++){
				objs[i].checked=allchk.checked;
			}
		}
		function OnOffMemberAllClickBtn(){
			var objs = document.querySelectorAll(".memberChk");
			var allchk = document.querySelector(".memberAllClick");
			var existUnchecked=false;
			
			for(var i=0; i<objs.length; i++){
				if(objs[i].checked === false){
					existUnchecked=true;
					break;
				}
			}
			if(existUnchecked=== false){
				allchk.checked=true;
			}
			else{
				allchk.checked=false;
			}
		}
		
		function reloadNoteList(){
			var groupId = window.location.pathname.split("/")[2];
			var targetDate = guinness.util.today("-");
			var objs = document.querySelectorAll(".memberChk");
			var array=new Array();

			for(var i=0; i<objs.length; i++){
				if(objs[i].checked === true)
					array.push("'"+objs[i].value+"'");
			}
			guinness.ajax({ 
				method:"post", 
				url:"/note/list",
				param:'groupId='+groupId+'&targetDate='+targetDate+'&checkedUserId='+array,
				success: 
				  function(req) {
					appendNoteList(JSON.parse(req.responseText));
				  } 
			});
		}
	</script>
</body>
</html>
