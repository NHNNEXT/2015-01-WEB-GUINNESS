<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>스터디의 시작, 페이퍼민트</title>
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
						<input type="radio" name="isPublic" value="private" checked>
						<label>비공개</label>
						<input type="radio" name="isPublic" value="public">
						<label>공개</label>
					</div>
					<br />
					<button id='createGroup-submit' class='btn btn-pm'>생성</button>
				</form>
			</div>
		</div>
	</div>
	<template id='group-card-template'>
		<a class='group-card' href='#'>
			<li>
				<span class='group-name'></span>
				<div class='deleteGroup-btn'><i class='fa fa-remove'></i></div>
				<i class='fa fa-lock'></i>
				<input name= groupId type='hidden' />
			</li>
		</a>
	</template>
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
			el.addEventListener('mouseup', toggleModal, false);
			var closeClick = document.querySelector('.modal-cover');
			closeClick.addEventListener('mouseup', function(e) {
				if(e.target.className === 'modal-cover') {
					toggleModal();
				}
			}, false);
			
		}, false);

		function createGroup(json) {
			var el = document.getElementById('group-container');
			var obj = null;
			var template = document.querySelector("#group-card-template").content;
			var newEl;
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var groupName = (obj.groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
				document.cookie = obj.groupId + "=" + encodeURI(obj.groupName);
				newEl = document.importNode(template, true);
				newEl.querySelector(".group-card").addEventListener("click",function(e){
					e.preventDefault();
					window.location.href="/g/"+obj.groupId;	
				},false);
				newEl.querySelector(".group-name").innerHTML = groupName;
				newEl.querySelector('.deleteGroup-btn').addEventListener("mousedown",function(){
					confirmDelete(obj.groupId,obj.groupName);
				},false);
				if (obj.isPublic === 'T') {
					newEl.querySelector('.fa-lock').setAttribute('class','fa fa-unlock');
				}
				newEl.querySelector('input').setAttribute("value",obj.groupId);
				el.appendChild(newEl);
			}
		}

		function toggleModal() {
			var blkcvr = document.getElementById('black-cover');
			if (blkcvr.style.display == "none") {
				document.body.style.overflow = "hidden";
				blkcvr.style.display = "block";
			} else {
				document.body.style.overflow = "auto";
				blkcvr.style.display = "none";
			}
			
			var closeBtn = document.getElementById('createGroup-close');
			closeBtn.addEventListener('mouseup', toggleModal, false);
			
			document.body.addEventListener('keydown', function(e) {
				if(e.keyCode === 27) {
					toggleModal();
				}
			}, false);
		}

		function confirmDelete(groupId, groupName) {
			groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			var message = "그룹을 삭제하시겠습니까?";
			guinness.util.alert(groupName , message, function() {
				document.body.style.overflow = "auto";
				location.href = "/group/delete?groupId=" + groupId;
			}, function() {
				document.body.style.overflow = "auto";
				console.log("그룹삭제안함");
			});
		}
	</script>
</body>
</html>
