<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>스터디의 시작, 페이퍼민트</title>
<meta charset="utf-8">
<%@ include file="/commons/_favicon.jspf"%>
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
	<template id='group-card-template'> <a class='group-card'
		href='#'>
		<li><span class='group-name'></span>
			<div class='deleteGroup-btn'>
				<i class='fa fa-remove'></i>
			</div> <i class='fa fa-lock'></i><input name=groupId type='hidden' /></li>
	</a> </template>
	<template id='create-group-template'>
		<form id='create-group-form' method="post">
			<div>
				그룹이름 <input type="text" name="groupName">
			</div>
			<div>
				<input type="radio" name="isPublic" value="private" checked>
				<label>비공개</label> <input type="radio" name="isPublic" value="public">
				<label>공개</label>
			</div>
			<br />
			<button class='btn btn-pm'>생성</button>
		</form>
	</template>
	<script>
		window.addEventListener('load', function() {
			guinness.ajax({
				method : "get",
				url : "/group/read",
				success : function(req) {
					appendGroups(JSON.parse(req.responseText));
				}
			});
			var errorMessage = '${errorMessage}';
			if (errorMessage !== '') {
				guinness.util.alert("비정상적인 접근!", errorMessage);
			}
			var el = document.querySelector('#create-new');
			el.addEventListener('mouseup', createGroup, false);

		}, false);
		
		function createGroup() {
			var bodyTemplate = document.querySelector("#create-group-template").content;
			bodyTemplate = document.importNode(bodyTemplate, true);
			guinness.util.modal({
				header : "새 그룹 생성",
				body: bodyTemplate,
				defaultCloseEvent: false
			});
			
			document.querySelector('.modal-close-btn').addEventListener('click', function(e){
				cancelGroupCreate();
			}, false);
			document.querySelector('.modal-cover').addEventListener('click', function(e){
				if (e.target.className==='modal-cover') {
					cancelGroupCreate();
				}
			}, false);
			document.querySelector('.modal-cover').setAttribute('tabindex',0);
			document.querySelector('.modal-cover').addEventListener('keydown',function(e){
				if(e.keyCode === 27){
					console.log('key');
				cancelGroupCreate();
			}},false);
			
			document.querySelector('#create-group-form').addEventListener('submit', function(e){
				e.preventDefault();
				var form = document.querySelector('#create-group-form');
				form.action = "/group/create";
				//check
				if(document.querySelector('.modal-cover input[name="groupName"]').value != ""){
					form.submit(); return;
				}
				guinness.util.alert("경고!","그룹 이름을 입력하세요!");
			},false);
		}
		
		function cancelGroupCreate() {
			if(document.querySelector(".modal-cover input[name='groupName']").value != ""){
				guinness.util.alert("취소","그룹 생성을 취소하시겠습니까?",function(){ document.querySelector('.modal-cover').remove(); }, function(){});
				return;
			}
			document.querySelector('.modal-cover').remove();
		}

		function appendGroups(json) {
			var el = document.querySelector('#group-container');
			var obj = null;
			var template = document.querySelector("#group-card-template").content;
			var newEl;
			for (var i = 0; i < json.length; i++) {
				obj = json[i];
				var groupName = (obj.groupName.replace(/</g, "&lt;")).replace(
						/>/g, "&gt;")
				document.cookie = obj.groupId + "=" + encodeURI(groupName);
				newEl = document.importNode(template, true);
				newEl.querySelector(".group-card").setAttribute("href",
						"/g/" + obj.groupId);
				newEl.querySelector(".group-name").innerHTML = groupName;
				newEl
						.querySelector('.deleteGroup-btn')
						.addEventListener(
								"mousedown",
								function(e) {
									e.preventDefault();
									var groupId = e.currentTarget.parentElement.parentElement
											.getAttribute("href").split("/")[2];
									var groupName = e.currentTarget.parentElement
											.querySelector(".group-name").innerHTML;
									confirmDelete(groupId, groupName);
								}, false);
				if (obj.isPublic === 'T') {
					newEl.querySelector('.fa-lock').setAttribute('class',
							'fa fa-unlock');
				}
				newEl.querySelector('input').setAttribute("value", obj.groupId);
				el.appendChild(newEl);
			}
		}

		function confirmDelete(groupId, groupName) {
			groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			var message = "그룹을 삭제하시겠습니까?";
			guinness.util.alert(groupName, message, function() {
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