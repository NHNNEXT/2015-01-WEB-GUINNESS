<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="/js/datepickr.js"></script>
<script src="/js/markdown.js"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUserId}">
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div id='note-list-container' class='content wrap'>
		<a href="/g/${groupId}"><span id="group-name"></span></a>
		<form id="notes-create-form" action="/note/editor" method="get">
			<input id="groupId" type="hidden" name="groupId" value="">
			<button id='create-new-button' type="submit">
				<i class="fa fa-plus-circle"></i>
			</button>
		</form>
		<div id='group-member-container'>
			<form id="addMemberForm" action="/group/add/member" method="post">
				<input type="hidden" name="groupId"> <input
					class="inputText" type="text" name="userId"> <input
					class="inputBtn" type="submit" value="초대">
			</form>
			<input class="memberAllClick" type="checkbox" checked=true
				onclick="allCheckMember()" />전체선택 <input class="inputBtn"
				type="submit" value="확인" onclick="reloadNoteList()" />

			<ul id='group-member'>
			</ul>
		</div>
	</div>
	<template id="view-note-template">
	<div class="note-content"></div>
	<div id="commentListUl"></div>
	<form id="commentForm" method="post">
		<textarea id='commentText' name='commentText' rows='5' cols='50'></textarea>
		<br>
		<button id='submitComment' class='btn btn-pm'>확인</button>
	</form>
	</template>
	<template id="comment-template">
	<li><img class='avatar' class='avatar'
		src='/img/profile/avatar-default.png'>
		<div class='comment-container'>
			<div class='comment-info'>
				<span class='comment-user'></span> <span class='comment-date'></span>
			</div>
			<div class='comment'></div>
			<div class='comment-util'></div>
		</div></li>
	</template>
	<script>
	window.addEventListener('load', function() {
		var groupId = window.location.pathname.split("/")[2];
		document.querySelector("#addMemberForm input[name='groupId']").value = groupId;
		document.querySelector("#groupId").value = groupId;
		readMember(groupId);
		
		document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);
		
		guinness.ajax({
			method : "get",
			url : "/group/read/"+groupId,
			success : function(req) { 
				var groupName = JSON.parse(req.responseText).groupName.replace(/</g, "&lt;").replace(/>/g, "&gt;");
				document.title = groupName;
				document.querySelector('#group-name').innerHTML = groupName;				
			}
		});
		appendNoteList(${noteList});
		_setMemberListPosition();
	}, false);
	</script>
	<script src="/js/note.js"></script>
</body>
</html>
