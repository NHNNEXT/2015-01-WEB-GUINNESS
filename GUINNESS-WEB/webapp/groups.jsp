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
	<div class='modal-cover' style="display: none;">
		<div class='modal-container'>
			<div class='modal-header'>
				<div class='modal-title'>새 스터디 그룹 생성</div>
				<div  class='modal-close-btn'>
					<i class='fa fa-remove'></i>
				</div>
			</div>
			<div class='modal-body'>
				<form name="user" method="post" action="/group/create">
					<div>
						그룹이름 <input type="text" name="groupName">
					</div>
					<div>
						<input type="radio" name="isPublic" value="private" checked>
						<label>비공개</label> <input type="radio" name="isPublic"
							value="public"> <label>공개</label>
					</div>
					<br />
					<button class='btn btn-pm'>생성</button>
				</form>
			</div>
		</div>
	</div>
	<template id='group-card-template'> <a class='group-card'
		href='#'>
		<li><span class='group-name'></span>
			<div class='deleteGroup-btn'>
				<i class='fa fa-remove'></i>
			</div> <i class='fa fa-lock'></i> <input name=groupId type='hidden' /></li>
	</a> </template>
	<script>
		window.addEventListener('load', function() {
			var req = new XMLHttpRequest();
			var json = null;
			req.onreadystatechange = function() {
				if (req.readyState == 4) {
					if (req.status == 200) {
						json = JSON.parse(req.responseText);
						createGroup(json);
					} else {
						window.location.href = "/exception.jsp"
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
			el.addEventListener('mouseup', guinness.util.showModal, false);

		}, false);

		function createGroup(json) {
			var el = document.getElementById('group-container');
			var obj = null;
			var template = document.querySelector("#group-card-template").content;
			var newEl;
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				document.cookie = obj.groupId + "=" + encodeURI(obj.groupName);
				newEl = document.importNode(template, true);
				newEl.querySelector(".group-card").setAttribute("href", "/g/"+obj.groupId);
				newEl.querySelector(".group-name").innerHTML = obj.groupName;
				newEl.querySelector('.deleteGroup-btn').addEventListener("mousedown",function(e){
					e.preventDefault();
					var groupId = e.currentTarget.parentElement.parentElement.getAttribute("href").split("/")[2];
					var groupName = e.currentTarget.parentElement.querySelector(".group-name").innerHTML;
					confirmDelete(groupId, groupName);
				},false);
				if (obj.isPublic === 'T') {
					newEl.querySelector('.fa-lock').setAttribute('class','fa fa-unlock');
				}
				newEl.querySelector('input').setAttribute("value",obj.groupId);
				el.appendChild(newEl);
			}
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