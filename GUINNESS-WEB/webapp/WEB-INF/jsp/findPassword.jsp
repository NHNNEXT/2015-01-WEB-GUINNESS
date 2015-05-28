<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>스터디의 시작, 페이퍼민트</title>
	<%@ include file="./commons/_favicon.jspf"%>
	<link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
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
		<div class="modal-body" style="width: 325px; padding-top: 5px;">
			<div style="border-bottom: 2px solid #f8f8f8; height: 30px;">
				<p>
					<span>이메일을 입력하세요.</span>
				</p>
			</div>
			<form id="commentForm" action="/user/findPassword" method="post" >
				<i class="fa fa-envelope-o" style="font-size: 18px;"></i> <input type="text"
					class="inputtext" id="identify_email" name="userId"
					style="width: 255px; margin: 5px; padding: 5px">
				<span id="identify_email-message" class="errorMessage" style="font-size: 13px"> ${message} </span>
				<br>
				<a class="btn btn-pm" style="text-align: center; width: 99px; height: 19px; background: gray;" href="/">취소</a>
				<button id="submitComment" class="btn btn-pm">확인</button>
			</form>
		</div>
	</div>
</body>

<script type="text/javascript" src="/js/joinCheck.js"></script>
<script>
	window.addEventListener('load', function() {
		joinCheck.setEmailValidation("identify_email", "identify_email-message");
	});
</script>
</html>
