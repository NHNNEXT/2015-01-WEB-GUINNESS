<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<script src="/js/markdown.js"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId"
		value="${sessionUserId}">
	<div id='note-edit-container' class='content wrap'>
	<div id='note-edit-group-name' >
		<a href="/g/${groupId}"><span id="group-name"></span></a></div>
		<form id="noteForm" action="/notes" method="post">
			<input type="hidden" id="hiddenGroupId" name="groupId" value="${groupId}">
			<c:if test="${not empty noteId}">
				<input type="hidden" name="_method" value="put" >
				<input type="hidden" name="noteId" value="${noteId}">
			</c:if>
            <div id="editorBox">
                <div id="editorTools">
                    <div id="calendar">
                        <i id="datepickr" class="fa fa-calendar"></i> <input
                            id="noteTargetDate" name="noteTargetDate" value="" readonly>
                    </div>
                </div>
                <textarea id="noteTextBox" rows="28" cols="28" name="noteText" form="noteForm">${noteText}</textarea>
                <div id="view-preview-template">
                    <div id="previewBox"></div>
                </div>
            </div>
			<a id="escape-note" class="btn btn-pm" href="/g/${groupId}">취소</a>
			<input type="submit" class="btn btn-pm" value="작성" />
		</form>
	</div>
	<script>
	
	 	window.addEventListener('load', function() {
			document.title = ${groupName};
			var groupName = (${groupName}.replace(/</g, "&lt;")).replace(
					/>/g, "&gt;");
			document.querySelector('#group-name').innerHTML = groupName;

			var previewBox = document.querySelector('#previewBox');
			previewBox.innerHTML = new markdownToHtml(document.querySelector('#noteTextBox').value).getHtmlText();
		}, false);
	 
			document.querySelector("#noteTargetDate").value = guinness.util.today("-");
			datepickr('#calendar', {
				dateFormat : 'Y-m-d',
				altInput : document.querySelector('#noteTargetDate')
			});
	
			var textBox=document.querySelector("#noteTextBox");
			textBox.addEventListener('keyup', function(e) {

				var previewBox = document.querySelector('#previewBox');
				previewBox.innerHTML=new markdownToHtml(e.target.value).getHtmlText();
			}, false);
	</script>
</body>
</html>