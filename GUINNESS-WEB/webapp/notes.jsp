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
		style="position: absolute; color: #888; top: 25%; width: 100%; text-align: center;">새
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
		<ul id='group-members' class='member-nav'>
			<form action="/group/add/member" method="post">
				<input type="hidden" class="groupId" name="groupId">
				<input type="text" name="userId">
				<input type="submit" value="추가">
			</form>
		</ul>
	</div>

	<script>
		window.addEventListener('load', function() {
			var groupId = window.location.pathname.split("/")[2];
			readNoteList(groupId, guinness.util.today("-"));
			attachGroupId(groupId);
			
			var noteModal = document.getElementById('create-new-button');
			noteModal.addEventListener('mouseup', function() {
				guinness.util.showModal();
				setNoteModal();
			}, false);
			document.getElementById('create-note').addEventListener('mouseup', createNote, false);

			var groupId = window.location.pathname.split("/")[2];
			var targetDate = guinness.util.today("-");
			readNoteList(groupId, targetDate);
			attachGroupId(groupId);

			var groupNameLabel = document.getElementById('group-name');
			var groupName = getCookie(groupId);
			document.title = groupName;
			document.querySelector('#group-name').innerHTML = groupName;
		}, false);

		function setNoteModal() {
			document.getElementById("targetDate").value = guinness.util.today("-");
			document.getElementById("noteText").value = "";
		}

		datepickr('.fa-calendar', {
			dateFormat : 'Y-m-d',
			altInput : document.getElementById('targetDate')
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
			var el = undefined;
			//리스트 초기화
			 el = document.getElementsByClassName("diary-list");
			 var elLength = el.length;
			 for (var i = elLength-1; i >= 0; i--) {
			 el[i].outerHTML = "";
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
				el = document.getElementById("day-" + targetDate);
				if (el == undefined) {
					el = document.createElement("ul");
					el.setAttribute("id", "day-" + targetDate);
					el.setAttribute("class", "diary-list");
					newEl = document.createElement("div");
					newEl.setAttribute("class", "diary-date");
					newEl.innerHTML = "<span>" + targetDate + "</span>";
					el.appendChild(newEl);
					document.getElementById('note-list-container').appendChild(el);
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

		function showCreateNoteModal() {
			var blkcvr = document.getElementById('black-cover-note');
			if (blkcvr.style.display == "none") {
				blkcvr.style.display = "block";
				document.body.style.overflow = "hidden";
				document.getElementById("datepickr").setAttribute("value",
						guinness.util.today("-"));
				document.getElementById("noteText").value = "";
			} else {
				document.body.style.overflow = "auto";
				blkcvr.style.display = "none";
			}

			var closeBtn = document.getElementById('createNote-close');
			closeBtn.addEventListener('mouseup', showCreateNoteModal, false);

			document.body.addEventListener('keydown', function(e) {
				if (e.keyCode === 27) {
					showCreateNoteModal();
				}
			});
		}

		function readNoteContents(noteId) {
			var req = new XMLHttpRequest();
			var json = undefined;
			req.onreadystatechange = function() {
				if (req.readyState === 4) {
					if (req.status === 200) {
						json = JSON.parse(req.responseText);
						showNoteModal(json);
					} else {
						window.location.href = "/exception.jsp";
					}
				}
			}
			req.open('get', '/note/read?noteId=' + noteId, true);
			req.send();
		}

		function showNoteModal(json) {
			document.body.style.overflow = "hidden";
			var obj = json[0];
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

			document.getElementById('submitComment').addEventListener(
					'mouseup', function() {
						createComment(obj, userName);
					}, false);

			var closeBtn = document.getElementById('contents-close');
			closeBtn.addEventListener('mouseup', function(e) {
				document.body.style.overflow = "auto";
				var el = document.getElementById("contents-window");
				el.outerHTML = "";
				delete el;
			}, false);

			var closeClick = document.getElementById('contents-window');
			closeClick.addEventListener('mouseup', function(e) {
				if (e.target.className === 'note-modal-cover') {
					var el = document.getElementById("contents-window");
					el.outerHTML = "";
					delete el;
				}
			}, false);

			document.body.addEventListener('keydown', function(e) {
				if (e.keyCode === 27) {
					var el = document.getElementById("contents-window");
					el.outerHTML = "";
					delete el;
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
			var commentText = document.getElementById('commentText').value;
			var userId = obj.userId;
			var noteId = obj.noteId;
			var param = "commentText=" + commentText + "&commentType=A" + "&userId=" + userId + "&noteId=" + noteId;
			
			req.open("post", "/comment/create", true);
			req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			req.setParameter;
			req.onreadystatechange = function() {
				if (req.status === 200 && req.readyState === 4) {
					document.getElementById('commentText').value = "";
					readComments(noteId, userName);
				}
			};
			req.send(param);
		}

		function createNote() {
			var req = new XMLHttpRequest();
			var targetDate = document.getElementById('targetDate').value;
			var groupId = document.getElementById('groupId').value;
			var noteText = document.getElementById('noteText').value;
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
	</script>
</body>
</html>
