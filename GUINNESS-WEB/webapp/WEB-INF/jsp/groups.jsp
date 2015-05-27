<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>스터디의 시작, 페이퍼민트</title>
<meta charset="utf-8">
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
</head>
<body>
	<script>
		var errorMessage = '${errorMessage}';
		if (errorMessage !== '') {
			guinness.util.alert("비정상적인 접근!", errorMessage);
		}
	</script>

	<%@ include file="./commons/_topnav.jspf"%>

	<input type="hidden" id="sessionUserId" name="sessionUserId"
		value="${sessionUser.userId}">
	<div class="content wrap" style="margin-top: 100px">
		<ul id="group-container" class="group-list">
			<li id="create-new" style="text-align: center;"><i
				class="fa fa-plus-circle" style="font-size: 60px; margin-top: 25px;"></i></li>
		</ul>
	</div>

	<template id="group-card-template"> <a class="group-card"
		href="#">
		<li><span class="group-name"></span>
			<div class="leaveGroup-btn">
				<i class="fa fa-user-times"></i>
			</div> <i class="fa fa-lock"></i> <input name=groupId type="hidden" /></li>
	</a> </template>
	<template id="create-group-template">
	<form id="create-group-form" method="post">
		<div>
			그룹이름 <input type="text" name="groupName" autocomplete="off" required>
		</div>
		<div>
			<input type="radio" name="status" value="F" checked> <label>비공개</label>
			<input type="radio" name="status" value="T"> <label>공개</label>
		</div>
		<br />
		<button class="btn btn-pm">만들기</button>
	</form>
	</template>
	
	<!-- 노트 모달을 위한 템플릿 -->
	<%@ include file="./commons/_note_popup.jspf"%>
	
	<script src="/js/group.js"></script>
</body>
</html>