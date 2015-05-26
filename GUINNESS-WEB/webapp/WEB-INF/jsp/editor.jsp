<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<link rel="stylesheet" href="/css/markdown.css">
<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
</head>
<body>

	<%@ include file="./commons/_topnav.jspf"%>

	<input type="hidden" id="sessionUserId" name="sessionUserId"
		value="${sessionUser.userId}">
	<div id="note-edit-container" class="content wrap">
		<div id="note-edit-group-name">
			<a href="/g/${group.groupId}"><span id="group-name">${group.groupName}</span></a>
		</div>
		<form id="noteForm" action="/notes" method="post">
			<input type="hidden" id="hiddenGroupId" name="groupId"
				value="${group.groupId}">
			<c:if test="${not empty noteId}">
				<input type="hidden" name="_method" value="put">
				<input type="hidden" name="noteId" value="${noteId}">
			</c:if>
			<div id="editorBox">
				<div id="editorTools">
					<div id="calendar">
						<i id="datepickr" class="fa fa-calendar"></i> <input
							id="noteTargetDate" name="noteTargetDate" value="" readonly>
					</div>
				</div>
				<textarea id="noteTextBox" rows="28" cols="28" name="noteText"
					form="noteForm">${note.noteText}</textarea>
				<div id="view-preview-template">
					<div id="previewBox" class="markdown-body"></div>
				</div>
			</div>
			<a id="escape-note" class="btn btn-pm" href="/g/${group.groupId}">취소</a> <input
				type="submit" class="btn btn-pm" value="작성" />
		</form>
		<button class="btn btn-temp" onclick="tempSave();">임시저장</button>
	</div>
	<input id="hiddenGroupName" type="hidden" value="${group.groupName}" />

	<template id="view-note-template">
		<div class="markdown-body">
			<input type="hidden" class="hiddenUserId" value="" /> <input
				type="hidden" class="hiddenNoteId" value="" />
			<div class="note-content"></div>
			<div id="commentListUl"></div>
			<form id="commentForm" method="post">
				<textarea id="commentText" name="commentText" rows="5" cols="50"></textarea>
				<br>
				<button id="submitComment" class="btn btn-pm">확인</button>
			</form>
		</div>
	</template>
	<template id="comment-template">
		<li>
			<img class="avatar" class="avatar" src="/img/profile/avatar-default.png">
			<div class="comment-container">
				<div class="comment-info">
					<span class="comment-user"></span> <span class="comment-date"></span>
				</div>
				<div class="comment"></div>
				<div class="comment-util"></div>
			</div>
		</li>
	</template>
	<script type="template" id="popupCommentBtnTemplate">
    	<div class="popupCommentBtn">
        	댓글 달기
    	</div>
	</script>

	<script>
		var groupName = "${group.groupName}";
	</script>
	<script src="/js/note.js"></script>
	<script src="/js/datepickr.js"></script>
	<script src="/js/editor.js"></script>
	<script src="/js/pComment.js"></script>
</body>
</html>