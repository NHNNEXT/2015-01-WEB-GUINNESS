<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:if test="${not empty sessionUser.userId}">
	<c:redirect url="/groups/form" />
</c:if>
<title>스터디의 시작, 페이퍼민트</title>
<meta charset="utf-8">
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
</head>
<body>
	<div id="jumbotron-container">
		<div class='content wrap'>
			<div id="jumbotron">
				<h1>스터디의 시작, 페이퍼민트</h1>
				<div class='subscribe'>페이퍼민트에서 나만의 학습 노트를 작성하고, 스터디 멤버들과 함께
					그룹을 생성하여 학습 노트를 공유하거나 언제 어디에서나 피드백을 받으세요!</div>
				<form:form commandName="user" id="signup-form" method="post"
					action="/user" class='form-group' style='margin: 0 auto; display: none;'>
					<c:if test="${not empty signValidErrorMessage}">
						<span class="errorMessage"> ${signValidErrorMessage } <br />
						</span>
					</c:if>
					<form:input id="join-userEmail" cssClass='text' path="userId" placeholder="이메일"
						autocomplete="off" value="${userId}" required="required" />
					<span id="join-userEmail-message" class="errorMessage"></span>
					<form:input id="join-userName" cssClass='text' path="userName" placeholder="이름"
						autocomplete="off" value="${userName}" required="required" />
					<span id="join-userName-message" class="errorMessage"></span>
					<form:password id="join-userPassword" cssClass='text' path="userPassword"
						placeholder="비밀번호" required="required" />
					<span id="join-userPassword-message" class="errorMessage"></span>
					<button type="submit" class='btn btn-pm'>회원가입</button>
				</form:form>
				<form method="post" id="login-form" class='form-group'
					style='margin: 0 auto; display: block;'>
					<input id='login-userId' class='text' type="text" name="userId"
						placeholder="이메일" required> <span id="login-error-message"
						class="errorMessage"></span> <input id='login-userPassword'
						class='text' type="password" name="userPassword"
						placeholder="비밀번호" required><br />
					<button class='btn btn-pm'>로그인</button>
				</form>
				<div id='login-util'>
					<div class="switchForm">
						<span id='label-login' style="display: none">로그인</span> <span
							id='label-signUp' style="display: block">회원가입</span>
					</div>
					<div class="switchForm">
						<a class="text" href="/user/findPasswordForm"
							style="text-decoration: none; padding: 5px">비밀번호를 잊으셨나요?</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/js/joinCheck.js"></script>
	<script type="text/javascript" src="/js/guinness.js"></script>
	<script type="text/javascript" src="/js/LoginForm.js"></script>
</body>
</html>
