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
	<div id='note-edit-container' class='content wrap'>
		<form id="noteForm" action="/note/create" method="post">
			<input type="hidden" name="groupId" value="${groupId}">
			<div id="editorTools">
				<div id="calendar">
					<i id="datepickr" class="fa fa-calendar"></i>
					<input id="targetDate" name="targetDate" value="" readonly>
				</div>
			</div>
			<textarea id="noteTextBox" name="noteText" form="noteForm"></textarea>
			<div id="view-preview-template">
				<div id="previewBox"></div>
			</div>
			<a id="escape-note" class="btn btn-pm" href="/g/${groupId}">취소</a>
			<button type="button" id="create-note" class="btn btn-pm" onclick="createNote()">작성</button>
		</form>
	</div>
	<script>
		document.querySelector("#targetDate").value = guinness.util.today("-");
		datepickr('#calendar', {
			dateFormat : 'Y-m-d',
			altInput : document.querySelector('#targetDate')
		});

		var textBox=document.querySelector("#noteTextBox");
		textBox.addEventListener('keyup', function(e) {
			var previewBox = document.querySelector('#previewBox');
			previewBox.innerHTML=new markdownToHtml(e.target.value).getHtmlText();
		}, false);
		
		function createNote(e) {
			if(document.querySelector("#noteTextBox").value !== ""){
				document.querySelector("#noteForm").submit();
			}
		}
	</script>
</body>
</html>