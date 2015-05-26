<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<style>
#profile-check-password {
  outline: 1px solid #dadada;
  width: 400px;
  margin: 0 auto;
  padding: 30px;
}

#profile-check-password label {
    display: block;
    margin-bottom: 30px;
    font-weight: bold;
    font-size: 20px;
}

#profile-check-password input {
	width: 100%;
	height: 30px;
	outline: 1px solid #dadada;
	border-radius: 5px;
	font-size: 15px;
}

#profile-check-password button {
    display: block;
    margin-top: 30px;
    width: 100%;
    min-height: 40px;
    border: none;
    border-radius: 5px;
    height: 30px;
    font-size: 15px;
    color: white;
    background-color: rgb(124, 196, 181);
}

</style>
<body>

	<%@ include file="./commons/_topnav.jspf"%>
  
	<div class="content wrap" style="margin-top: 80px">
		<form id="profile-check-password" action="/user/update/check" method="post" class="panel">
			<label>비밀번호 재확인 </label> 
			<input id="check-password" type="password" name="userPassword" autofocus>
			<span class="errorMessage" id="checkPasswordErrorMessage">${errorMessage}</span>
			<button id="profile-check-password-button">확인</button>
		</form>
	</div>
</body>
</html>
