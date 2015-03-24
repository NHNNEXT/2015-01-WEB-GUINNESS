<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:if test="${not empty sessionUserId}">
	<c:redirect url="/groups.jsp" />
</c:if>
<title>스터디의 시작, 기네스</title>
<meta charset="utf-8">
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
</head>
<body>
	<div id="jumbotron-container">
		<div class='content wrap'>
			<div id="jumbotron">
				<h1>스터디의 시작, 기네스</h1>
				<div class='subscribe'>기네스에서 나만의 학습 일지를 작성하고, 스터디 멤버들과 함께 그룹을 생성하여 학습 일지를 공유하거나 언제 어디에서나 피드백을 받으세요!</div>
				<form id="signup-form" method="post" action="/user/create"
					class='form-group' style='margin: 0 auto; display: block;'>
					<c:if test="${not empty signValidErrorMessage}">
						<span class="errorMessage"> ${signValidErrorMessage } <br />
						</span>
					</c:if>
					<input class='text' type="text" name="userId" placeholder="이메일" value="${userId}" required>
					<c:if test="${not empty message}">
						<span class="errorMessage"> ${message} </span>
					</c:if>
					<input class='text' type="text" name="userName" placeholder="이름" value="${userName}" required>
					<input class='text' type="password" name="userPassword" placeholder="비밀번호" required><br />
					<button type="submit" class='btn'>회원가입</button>
				</form>
				<div id="login-form" class='form-group'
					style='margin: 0 auto; display: none;'>
					<input id='login-userId' class='text' type="text" name="userId" placeholder="이메일" required>
					<span id="login-error-message" class="errorMessage"></span>
						<input id='login-userPassword' class='text' type="password" name="userPassword" placeholder="비밀번호" required><br />
					<button onclick="loginCheck()" class='btn'>로그인</button>
				</div>
				<div id='login-util'>
					<div id="switchForm">
						<span id='label-login' style="display: block">로그인</span>
						<span id='label-signUp' style="display: none">회원가입</span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/js/LoginForm.js"></script>
</body>
</html>
