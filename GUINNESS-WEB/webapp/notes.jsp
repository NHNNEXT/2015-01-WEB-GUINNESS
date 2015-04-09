<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>스터디의 시작, 페이퍼민트</title>
<%@ include file="/commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/datepickr.css">
<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="/js/datepickr.js"></script>
<script src="/js/guinness.js"></script>
</head>
<body>
	<%@ include file="/commons/_topnav.jspf"%>
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div class='modal-cover' style='display: none'>
		<div class='modal-container'>
			<div class='modal-header'>
				<div class='modal-title'>새 일지 작성</div>
				<div class='modal-close-btn'>
					<i class='fa fa-remove'></i>
				</div>
			</div>
			<div class='modal-body'>
				<div>
					<input id="groupId" type="hidden" name="groupId" value="">
					<table>
						<tr>
							<td>날짜</td>
							<td><input id="targetDate" name="targetDate" value="" readonly /><i id="datepickr" class="fa fa-calendar"></i></td>
						</tr>
						<tr>
							<td>내용</td>
							<td><textarea id="noteText" style="resize: none" rows="10"
									cols="50" name="noteText"></textarea></td>
						</tr>
					</table>
					<button id="create-note" class="btn btn-pm">작성</button>
				</div>
			</div>
		</div>
	</div>
	<div id='note-list-container' class='content wrap'>
		<ul id='to-date' class='time-nav'>
		</ul>
		<span id="group-name"></span>
		<div id='create-new-button'>
			<i class="fa fa-plus-circle"></i>
		</div>
		<div id='group-member-container'>
			<form id="addMemberForm" action="/group/add/member" method="post">
				<input type="hidden" class="groupId" name="groupId">
				<input class="inputText" type="text" name="userId">
				<input class="inputBtn" type="submit" value="추가">
			</form>
			<ul id='group-member'>
			</ul>
		</div>
	</div>

	<script>
		window.addEventListener('load', function() {
			var groupId = window.location.pathname.split("/")[2];
			readNoteList(groupId, guinness.util.today("-"));
			readMember(groupId);
			attachGroupId(groupId);
			
			var noteModal = document.querySelector('#create-new-button');
			noteModal.addEventListener('mouseup', function() {
				guinness.util.showModal();
				setNoteModal();
			}, false);
			document.querySelector('#create-note').addEventListener('mouseup', createNote, false);
			document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);


			var groupNameLabel = document.querySelector('#group-name');
			var groupName = getCookie(groupId);
			document.title = groupName;
			document.querySelector('#group-name').innerHTML = groupName;
		}, false);

		function setNoteModal() {
			document.querySelector("#targetDate").value = guinness.util.today("-");
			document.querySelector("#noteText").value = "";
		}

		datepickr('.fa-calendar', {
			dateFormat : 'Y-m-d',
			altInput : document.querySelector('#targetDate')
		});

		function getCookie(sKey) {
			if (!sKey) {
				return undefined;
			}
			return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*"
					+ encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1"))
					|| undefined;
		}

		function attachGroupId(data) {
			var el = document.querySelector("#groupId");
			el.setAttribute("value", data);

			var ele = document.querySelector(".groupId");
			ele.setAttribute("value", data);
		}

		function readNoteList(groupId, targetDate) {
		  guinness.ajax({ 
			  method: "get", 
			  url: "/notelist/read?groupId="+groupId+"&targetDate="+targetDate, 
			  success: 
				function(req) {
				  var json = JSON.parse(req.responseText);
				  if (json !== "" || json !== undefined) {
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
			if(el != undefined) {
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

		function readNoteContents(noteId) {
			guinness.ajax({
				method: 'get',
				url: '/note/read?noteId=' + noteId,
				success: function(req) {
					var json = JSON.parse(req.responseText);
					showNoteModal(json);
				}
			});
		}

		function showNoteModal(obj) {
			document.body.style.overflow = "hidden";
			var el = document.createElement("div");
			el.setAttribute("id", "contents-window");
			el.setAttribute("class", "note-modal-cover");
			var innerContainer = document.createElement("div");
			innerContainer.setAttribute("class", "modal-container");
			var innerHeader = document.createElement("div");
			innerHeader.setAttribute("class", "modal-header");
			innerHeader.innerHTML += "<div class='modal-title'>" + obj.targetDate + " | " + obj.userName
					+ "</div><div id='contents-close' class='modal-close-btn'><i class='fa fa-remove'></i></div>";
			var innerBody = document.createElement("div");
			innerBody.setAttribute("class", "modal-body");
			innerBody.innerHTML += obj.noteText;
			
			
			var noteId = obj.noteId;
			var userName = obj.userName;
			var commentList = document.createElement("ul");
			commentList.setAttribute("id","commentListUl");

			var commentArea = writeComment();

			el.appendChild(innerContainer);
			innerContainer.appendChild(innerHeader);
			innerContainer.appendChild(innerBody);
			
			innerContainer.appendChild(commentList);
			innerContainer.appendChild(commentArea);
			document.body.appendChild(el);
			readComments(noteId, userName);

			document.querySelector('#submitComment').addEventListener(
					'mouseup', function() {
						createComment(obj, userName);
					}, false);

			var closeBtn = document.querySelector('#contents-close');
			closeBtn.addEventListener('mouseup', function(e) {
				document.body.style.overflow = "auto";
				var el = document.querySelector("#contents-window");
				el.outerHTML = "";
			}, false);

			var closeClick = document.querySelector('#contents-window');
			closeClick.addEventListener('mouseup', function(e) {
				if (e.target.className === 'note-modal-cover') {
					var el = document.querySelector("#contents-window");
					el.outerHTML = "";
				}
			}, false);

			document.body.addEventListener('keydown', function(e) {
				if (e.keyCode === 27) {
					var el = document.querySelector("#contents-window");
					el.outerHTML = "";
				}
			});
		}

		function readComments(noteId, userName) {
			var req = new XMLHttpRequest();
			var json = null;
			var el = document.querySelector('#commentListUl');
			while(el.hasChildNodes()){
				el.removeChild(el.firstChild);
			}
			var out = "";
			var obj = null;
			req.open('get', '/comment/read?noteId=' + noteId, true);
			req.onreadystatechange = function() {
				if (req.readyState === 4) {
					if (req.status === 200) {
						json = JSON.parse(req.responseText);
						for (var i = 0; i < json.length; i++) {
							obj = json[i];
							el.innerHTML += "<li>" + obj.commentText + "    "+ obj.createDate +" "+ userName + "</li>";
						}
					} else {
						window.location.href = "/exception.jsp";
					}
				}
			}
			req.send();
		}

		function writeComment() {
			var el = document.createElement("div");
			el.innerHTML += "<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea><br>";
			el.innerHTML += "<button id='submitComment' class='btn btn-pm' name='submitComment'>답변</button>";
			
			return el;
		}

		function createComment(obj, userName) {
			var req = new XMLHttpRequest();
			var commentText = document.querySelector('#commentText').value;
			var userId = obj.userId;
			var noteId = obj.noteId;
			var param = "commentText=" + commentText + "&commentType=A" + "&userId=" + userId + "&noteId=" + noteId;
			
			req.open("post", "/comment/create", true);
			req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			req.setParameter;
			req.onreadystatechange = function() {
				if (req.status === 200 && req.readyState === 4) {
					document.querySelector('#commentText').value = "";
					readComments(noteId, userName);
				}
			};
			req.send(param);
		}

		function createNote() {
			var req = new XMLHttpRequest();
			var targetDate = document.querySelector('#targetDate').value;
			var groupId = document.querySelector('#groupId').value;
			var noteText = document.querySelector('#noteText').value;
			noteText = noteText.replace(/\n/g,"<br>");
			var param = "groupId=" + groupId + "&targetDate=" + targetDate + "&noteText=" + noteText;
			var res = undefined;

			req.open("post", "/note/create", true);
			req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			req.setParameter;
			req.onreadystatechange = function() {
				if (req.status === 200 && req.readyState === 4) {
					guinness.util.closeModal();
					readNoteList(groupId, targetDate);
				}
			};
			req.send(param);
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
					if(req.responseText === "unknownUser") guinness.util.alert("멤버추가 실패","사용자를 찾을 수 없습니다!");
					if(req.responseText === "joinedUser") guinness.util.alert("멤버추가 실패","사용자가 이미 가입되어있습니다!");
					else {
						appendMember(JSON.parse(req.responseText));
					}
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
