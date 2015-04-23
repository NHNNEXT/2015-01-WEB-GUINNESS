<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="/js/datepickr.js"></script>
<script src="/js/guinness.js"></script>
<script src="/js/markdown.js"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId"
		value="${sessionUserId}">
	<input type="hidden" name="groupId" value="${groupId}">
	<div id='note-list-container' class='content wrap'>
		<form id="noteForm" action="/note/create" method="post">
			<div id="editorTools">
				<div id="calendar">
					<input id="targetDate" name="targetDate" value="" readonly>
					<i id="datepickr" class="fa fa-calendar"></i>
				</div>
			</div>
			<textarea id="noteTextBox" name="noteText" form="noteForm"></textarea>
			<button type="submit" id="create-note" class="btn btn-pm">작성</button>
		</form>

	</div>
	<script>
		document.querySelector("#targetDate").value = guinness.util.today("-");
		datepickr('#calendar', {
			dateFormat : 'Y-m-d',
			altInput : document.querySelector('#targetDate')
		});
	</script>
</body>
</html>