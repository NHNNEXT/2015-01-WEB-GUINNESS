<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error!!!</title>
</head>
<body>
	<div>
		<h1>고객님, 죄송합니다!</h1>
		<c:choose>
			<c:when test="${not empty errorMessage}">
				<p>${errorMessage}</p>
			</c:when>
			<c:otherwise>
				<p>비정상적 접근 시도 입니다.</p>
			</c:otherwise>
		</c:choose>
		<p>
			<a href="/"><button type="button">HOME으로</button></a>
	</div>
</body>
</html>