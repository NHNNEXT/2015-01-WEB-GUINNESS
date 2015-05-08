<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>스터디의 시작, 페이퍼민트</title>
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/datepickr.css">
<script src="/js/datepickr.js"></script>
<script src="/js/guinness.js"></script>
<script src="/js/markdown.js"></script>

<!-- 노트 캘린더 -->
<link rel="stylesheet" href="/css/dateRangePickerForBootstrap.css">
<link rel="stylesheet" href="/css/daterangepicker-bs3.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="/js/moment.js"></script>
<script src="/js/daterangepicker.js"></script>





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

		<!-- <input type="text" name="rangeCalendar" value="01/01/2015 - 01/31/2015" /> -->

		<!-- <input type="text" name="defaultCalendar" value="10/24/1984" /> -->
		<div id='calendar-container'>
			<input class="inputBtn"
				type="submit" value="전체보기" onclick="reloadNoteList()" />
			<div id="defaultCalendar" ></div>
		</div>
		

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
		document.title = ${groupName};
		var groupName = (${groupName}.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
		document.querySelector('#group-name').innerHTML = groupName;
		
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
	<!-- <script type="text/javascript">
		$(function() {
		    $('input[name="rangeCalendar"]').daterangepicker();
		});
	</script> -->
	<!-- <script type="text/javascript">
		$(function() {
		    $('input[id="defaultCalendar"]').daterangepicker({
		        singleDatePicker: true,
		        showDropdowns: true
		    }, 
		    function(start, end, label) {
		        var years = moment().diff(start, 'years');
		        alert("You are " + years + " years old.");
		    });
		});
	</script> -->
	<script type="text/javascript">
		$(function() {
		    $('div[id="defaultCalendar"]').daterangepicker({
		        singleDatePicker: true,
		        showDropdowns: true
		    }, 
		    function(start, end, label) {
		    	console.log(start.toISOString(), end.toISOString(), label);
		        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
		    });
		});
		
		document.querySelector('#calendar-container').addEventListener("click", function(e) {
			var targetDate = $('#defaultCalendar').data('daterangepicker').startDate._d.toISOString().substring(0,10);
			reloadNoteList(targetDate);
			}, false);
	</script>
</body>
</html>
