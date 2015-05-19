<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>스터디의 시작, 페이퍼민트</title>
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/datepickr.css">
</head>
<body>
	<%@ include file="./commons/_guest_topnav.jspf"%>

	<div class="modal-container" style="top: 132.5px;">
		<div class="modal-header">
			<div class="modal-title">계정찾기</div>
		</div>
		<div class="modal-body note-modal">
			<div class="note-content">
				<p>
					<span class="attention"><i class="fa fa-exclamation-circle"></i>
						이메일을 입력하세요.</span>
				</p>
			</div>
			<div id="commentListUl"></div>
			<form id="commentForm" action="/user/findPassword" method="post">
				<i class="fa fa-envelope-o" style=""></i> <input type="text"
					class="inputtext" id="identify_email" name="userId"
					style="width: 255px;"> <br>
				<c:if test="${not empty message}">
					<span class="errorMessage"> ${message} </span>
				</c:if>
				<button id="submitComment" class="btn btn-pm">확인</button>
			</form>
		</div>
	</div>
</body>

<script>
	
</script>
</html>
