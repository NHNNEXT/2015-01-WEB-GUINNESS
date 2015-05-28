<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>환영합니다</title>
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
</head>
<body>
	<%@ include file="./commons/_guest_topnav.jspf"%>
	<div class="sendmail-content">
		<c:choose>
			<c:when test="${not empty message}">
				<h1> ${message} </h1>
			</c:when>
			<c:otherwise>
				<h1>회원가입에 성공하였습니다. 메일 인증 후 사용 가능합니다.</h1>
			</c:otherwise>
		</c:choose>
		<a href="/">홈으로 돌아가기</a><br><br>
		<a href="http://${mailSite}" target="_blank" onclick="location.href='/'">메일 확인하기</a>
	</div>
</body>
</html>