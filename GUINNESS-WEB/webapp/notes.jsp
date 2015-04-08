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
		style="position: absolute; color: #888; top: 45%; width: 100%; text-align: center;">새
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
		/* scrolling navigation */
		window.addEventListener('load', function() {
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
			groupNameLabel.innerHTML = groupName;
		}, false);

		function setNoteModal() {
			document.getElementById("targetDate").value = guinness.util.today("-");
			document.getElementById("noteText").value = "";
		}
		
		datepickr('.fa-calendar', {
			dateFormat: 'Y-m-d',
			altInput: document.getElementById('targetDate')
		});
		
		function getCookie(sKey) {
			if (!sKey) {
				return null;
			}
			return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*"
					+ encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1"))
					|| null;
		}

		function attachGroupId(data) {
			var el = document.getElementById("groupId");
			el.setAttribute("value", data);
			
			var ele = document.querySelector(".groupId");
			ele.setAttribute("value", data);
		}

		function readNoteList(groupId, targetDate) {
			var req = new XMLHttpRequest();
			req.open("GET", "/notelist/read?groupId="+groupId+"&targetDate="+targetDate, true);
			req.onreadystatechange = function() {
				if (req.status === 200 && req.readyState === 4) {
					res = JSON.parse(req.responseText);
					if (res == "") {
						return;
					}
					//노트가 하나이상 있다면 빈 노트 메세지를 지우고 노트와 네비게이션을 출력한다.
					if (document.getElementById("empty-message") != null)
						document.getElementById("empty-message").outerHTML = "";
					appendNoteList(res);
					appendDateNav(res);
				}
			};
			req.send();
		}
		
		//날짜 네비게이션을 생성해준다
		function appendDateNav(json) {
			var newLi = null;
			var dateTag = null;
			var datePoint = null;
			var obj = null;
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var toDate = obj.targetDate;
				toDate = toDate.split(" ");
				toDate = toDate[0];
				toDate = toDate.replace(/'-'/g, '');
				newLi = document.getElementById("to" + toDate);
				if (newLi == null) {
					newLi = document.createElement("li");
					newLi.setAttribute("id", "to" + toDate);
					//오늘 날짜에 포커스 되도록 초기화
					var today = guinness.util.today("-");
					if (toDate == today) {
						newLi.setAttribute("class", "date-nav date-select");
					} else {
						newLi.setAttribute("class", "date-nav");
					}
					dateTag = document.createElement("div");
					dateTag.setAttribute("class", "date-tag");
					dateTag.innerHTML = toDate;
					datePoint = document.createElement("div");
					datePoint.setAttribute("class", "date-point");
					newLi.appendChild(dateTag);
					newLi.appendChild(datePoint);
				}
				document.getElementById("to-date").appendChild(newLi);
			}
			//모든 date-nav 클래스를 가진 개체에 이벤트를 부여한다.
			var dates = document.getElementsByClassName('date-nav');
			for (var i = 0; i < dates.length; i++) {
				dates[i].addEventListener('mouseup', moveToDate, false);
			}
		}

		function moveToDate(e) {
			var location = e.currentTarget.id.replace('to', '');
			var top = document.getElementById('day-' + location);
			var prevSelected = document.getElementsByClassName("date-select")[0];
			prevSelected.className = 'date-nav';
			e.currentTarget.className = 'date-nav date-select';
			if (top != null) {
				$('body').animate({
					scrollTop : top.offsetTop
				}, 500);
			}
		}

		function appendNoteList(json) {
			var el = null;
			/* 			//리스트 초기화
			 el = document.getElementsByClassName("diary-list");
			 var elLength = el.length;
			 for (var i = elLength-1; i >= 0; i--) {
			 el[i].outerHTML = "";
			 } */
			//날짜별로 들어갈수 있게...
			var newEl = null;
			var obj = null;
			var out = "";
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var targetDate = obj.targetDate;
				targetDate = targetDate.split(" ");
				targetDate = targetDate[0];
				targetDate = targetDate.replace(/'-'/g, '');
				el = document.getElementById("day-" + targetDate);
				if (el == null) {
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
			console.log(noteId);
			var req = new XMLHttpRequest();
			var json = null;
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
			
			var commentArea = viewComment();
			

			el.appendChild(innerContainer);
			innerContainer.appendChild(innerHeader);
			innerContainer.appendChild(innerBody);
			innerContainer.appendChild(commentArea);
			document.body.appendChild(el);
			
			document.getElementById('submitComment').addEventListener('mouseup', function() {
				createComment(obj);
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
		
		function viewComment() {
			var el = document.createElement("div");
			el.innerHTML += "<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea><br>";
			el.innerHTML += "<button id='submitComment' class='btn btn-pm' name='submitComment'>답변</button>";
			
			return el;
		}
		
		function createComment(obj) {
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
					guinness.util.alert("답변달기","성공적");
					//TODO 자동 댓글 리스트 불러오기 구현 위
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
			var res = null;

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
