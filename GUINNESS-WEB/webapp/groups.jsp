<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>스터디의 시작, 기네스</title>
<meta charset="utf-8">
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<script src="/js/guinness.js"></script>
</head>
<body>
	<%@ include file="/commons/_topnav.jspf"%>
	<div class='content wrap' style='margin-top: 100px'>

		<ul id='group-container' class='group-list'>
			<li id='create-new'>새 스터디 그룹 생성...</li>
		</ul>
	</div>

	<!-- 그룹생성을 위한 Modal -->
	<div id='black-cover' class='modal-cover' style="display: none;">
		<div id='createGroup-container' class='modal-container'>
			<div id='createGroup-header' class='modal-header'>
				<div id='createGroup-title' class='modal-title'>새 스터디 그룹 생성</div>
				<div id='createGroup-close' class='modal-close'>
					<i class='fa fa-remove'></i>
				</div>
			</div>
			<div id='createGroup-body' class='modal-body'>
				<form name="user" method="post" action="/group/create">
					<div>
						그룹이름 <input type="text" name="groupName">
					</div>
					<div>
						<input type="radio" name="isPublic" value="private" checked>비공개
						<input type="radio" name="isPublic" value="public">공개
					</div>
					<br />
					<button id='createGroup-submit' class='btn'>생성</button>
				</form>
			</div>
		</div>
	</div>
	<script>
		window.addEventListener('load', function() {
			var req = new XMLHttpRequest();
			var json = null;
			req.onreadystatechange = function() {
				if (req.readyState == 4) {
					if (req.status == 200) {
						json = JSON.parse(req.responseText);
						createGroup(json);
					} else{
						window.location.href="/exception.jsp"
					}
				}
			};
			req.open('get', '/group/read', true);
			req.send();

			var errorMessage = '${errorMessage}';

			if (errorMessage !== '') {
				guinness.util.alert("비정상적인 접근!", errorMessage);
			}

			var el = document.getElementById('create-new');
			el.addEventListener('mouseup', showModal, false);
			el = document.querySelector(".modal-cover");
			el.onkeydown = function(e) {
				if(e.keyCode == 27) {
					showModal();
				}
			}
			el.addEventListener('mouseup', showModal, false);
			var closeBtn = document.getElementById('createGroup-close');
			closeBtn.addEventListener('mouseup', showModal, false);
		}, false);

		function createGroup(json) {
			var el = document.getElementById('group-container');
			var obj = null;
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				document.cookie = obj.groupId+"="+encodeURI(obj.groupName);
				var newEl = document.createElement("a");
				newEl.setAttribute("href", "/g/" + obj.groupId);
				var deleteBtn = "<a id='deleteGroup-btn' href='#' class='deleteGroup-btn' onclick='confirmDelete(\""
						+ obj.groupId + "\", \"" + obj.groupName + "\")'><i class='fa fa-remove'></i></a>";
				var lockImg = "<i class='fa fa-lock'></i>";
				if(obj.isPublic === 'T') {
					lockImg = "<i class='fa fa-unlock'></i>";
				}
				newEl.innerHTML = "<li>"
						+ "<span>"+obj.groupName+"</span>"
						+ deleteBtn
						+ lockImg
						+ "<input name= groupId type='hidden' value=" + obj.groupId+" /></li>";
				el.appendChild(newEl);
			}
		}

		function showModal() {
			var blkcvr = document.getElementById('black-cover');
			if (blkcvr.style.display == "none") {
				document.body.style.overflow="hidden";
				blkcvr.style.display = "block";
			} else {
				document.body.style.overflow="auto";
				blkcvr.style.display = "none";
			}
		}

		function confirmDelete(groupId, groupName) {
			groupName = (groupName.replace(/</gi, "&lt;")).replace(/>/gi, "&gt;");
			var message = groupName + "을 삭제하시겠습니까?";
			guinness.util.alert("스터디그룹 삭제", message, function() {
				document.body.style.overflow="auto";
				location.href = "/group/delete?groupId=" + groupId;
			}, function() {
				document.body.style.overflow="auto";
				console.log("그룹삭제안함");
			});
		}
		
		function closeModal() {
			
		}
	</script>
</body>
</html>
