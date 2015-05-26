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
<link rel="stylesheet" href="/css/markdown.css">

<!-- 노트 캘린더 -->
<link rel="stylesheet" href="/css/dateRangePickerForBootstrap.css">
<link rel="stylesheet" href="/css/daterangepicker-bs3.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>
	<div id="backImg" style="background-repeat:no-repeat; margin-top: -50px;
	 background-size:cover; position:fixed; width:100%; height:100%;"></div>
	
	<%@ include file="./commons/_topnav.jspf"%>
	
	<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUser.userId}">
	<h1 id="empty-message"
		style="position: absolute; color: #888; top: 300px; width: 100%; text-align: center;">새
		노트를 작성해주세요</h1>
	<div id="group-header" class="content wrap" style="margin-top:50px; padding:10px 0;">
			<a style="display:inline-block" href="/g/${groupId}"><span id="group-name"></span></a>
	</div>
	<div id="note-list-container" class="content wrap">
		<div id="create-note">
			<a href="/notes/editor/g/${groupId}">
				<button id="create-new-button">
					<i class="fa fa-plus-circle"></i>
				</button>
			</a>
		</div>

		<div id="left-menu-container" class="side-menu-container">
			<div id="calendar-container">
				<div id="defaultCalendar"></div>
			</div>
			<div id="summary-container">
				<span class="menu-title attention"><i class="fa fa-exclamation"></i>  정보</span>
				<ul id="attention-list" style="list-style:none; margin:5px 0 10px 0;"></ul>
				<span class="menu-title question"><i class="fa fa-question"></i>  질문</span>
				<ul id="question-list" style="list-style:none;"></ul>
			</div>
		</div>

		<div id="group-member-container" class="side-menu-container">
			<form id="addMemberForm">
				<span style="font-weight:bold;">멤버추가</span><br/>
				<input type="hidden" name="groupId">
				<input class="inputText" type="text" name="userId">
				<input class="inputBtn" type="submit" value="초대">
				<span class="addMemberAlert" style="visibility:hidden;">멤버추가메세지</span>
			</form>
			<div id="group-member-list">
				<span style="font-weight:bold;">멤버관리</span><br/>
				<table id="group-member">
				</table>
			</div>
			<div>
				<input id="groupSettingBtn"class="inputBtn" style="right: 10px; visibility:hidden; cursor: default; width: 30%; float:right;" type="submit" value="그룹설정" onclick="groupUpdate()">
			</div>
		</div>
	</div>

	<template id="view-note-template">
		<div class="markdown-body">
			<input type="hidden" class="hiddenUserId" value=""/>
			<input type="hidden" class="hiddenNoteId" value=""/>
			<div class="note-content"></div>
			<input class="hidden-note-content" type="hidden" value="" />
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
	<template id="member-template">
		<tr>
			<td class="member-info" style="width:140px; display:inline-block;">
				<div class="member-name" style="font-weight:bold;">멤버이름</div>
				<div class="member-id" style="color:#888; font-size:9px;">멤버아이디</div>
			</td>
			<td class="member-util" style="font-size:15px; display:inline-block;">
				<ul>
					<li>
						<i class="fa fa-eye"></i>
						<span class="info">노트 숨기기</span>
					</li>
					<input style="display:none;" type="checkbox" class="memberChk" checked=true value="">
					<li class="member-delete" style="visibility:hidden;">
						<i class="fa fa-times"></i>
						<span class="info">멤버제외</span>
					</li>
				</ul>
			</td>
		</tr>
	</template>
        
    <script type="template" class="pCommentTemplate">
		<div class="pCommentBox" draggable="true">
			<div id="pCommentCancel">
				<i class="fa fa-remove"></i>
			</div>
			<div class="inputP" contenteditable="true"></div>
			<div class="setUp">확인</div>
		</div>
    </script>
        
    <script type="template" class="pCommentListTemplate">
        <div class="pCommentListBox">
            <div id="pCommentBoxCancel"><i class="fa fa-thumb-tack"></i></div>
            <ul class="pCommentList"></ul>
        </div>
    </script>
        
    <script type="template" class="aPCommentTemplate">
        <li class="aPComment" id="pCommentId">
            <input type="hidden" p-id="pId" sameCount="sameSenCount" sameIndex="sameSenIndex"/ selectText="selectedText">
            <div class="userProfile">
                <img src="userImage">
                <div>userName<span>userId</span></div><br>
                <div class="pCommentCreateDate">createDate</div>
            </div>
            <div class="pComment-text">pCommentText</div>
            <div class="controll">
                <a href="#" class="update">수정</a>
                <a href="#" class="delete">삭제</a>
            </div>
        </li>
    </script>
    
    <script type="template" id="popupCommentBtnTemplate">
    	<div class="popupCommentBtn">
        	댓글 달기
    	</div>
	</script>  
	<script>
	document.title = "${group.groupName}";
	var groupName = ("${group.groupName}".replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	document.querySelector("#group-name").innerHTML = groupName;
	var bJoinedUser = false;
	var groupCaptainUserId = "${group.groupCaptainUserId}";
	const groupId = window.location.pathname.split("/")[2];
	window.addEventListener("load", function() {
		var groupImage = "${group.groupImage}";
		
		var userId = document.getElementById("sessionUserId").value;
		if(userId === groupCaptainUserId){
			document.getElementById("groupSettingBtn").style.visibility = "visible";
		}
		if (groupImage !== "") {
			window.document.body.querySelector("#backImg").style.backgroundImage=
			"url('/img/group/" + groupImage + "')";
		}
		document.querySelector("#addMemberForm input[name='groupId']").value = groupId;
		readMember(groupId);
		document.querySelector("#addMemberForm").addEventListener("submit", function(e) { e.preventDefault(); addMember(); }, false);
		document.title = "${group.groupName}";
		document.querySelector("#group-name").innerHTML = groupName;
		var json = ${noteList};
		appendNoteList(json);
		appendMarkList(json);
		var elCreateBtn = document.querySelector("#create-new-button");

	}, false);
	
	window.addEventListener("scroll", function(e) {
		infiniteScroll();
		sideMenuFlow();
		refreshCalendar();
	}, false);
	
	$(function() {
	    $("#defaultCalendar").daterangepicker({
	        singleDatePicker: true,
	        showDropdowns: true
	    },
	    function(start, end, label) {
	    	console.log(start.toISOString(), end.toISOString(), label);
	        $("#reportrange span").html(start.format("MMMM D, YYYY") + " - " + end.format("MMMM D, YYYY"));
	    });
	    //<input id="allShow" type="submit" value="전체노트 보기" onclick="reloadNoteList()" />
	    var allShowButton = guinness.createElement({
	    	name : "input",
			attrs : {
				id : "allShow",
				class : "inputBtn",
				type : "submit",
				value : "오늘",
				onclick : "reloadNoteList()"
			}
	    });
	    $("#calendar-container").append(allShowButton);
	});

	document.querySelector("#calendar-container").addEventListener("click", function(e) {
		if (e.target.getAttribute("class") === null || e.target.getAttribute("class").indexOf("available") === -1)
			return;
		var noteTargetDate = $("#defaultCalendar").data("daterangepicker").startDate._d.toISOString().substring(0,10)+ " 23:59:59";
		reloadNoteList(noteTargetDate);
	}, false);
	
	var sideMenuContainers = document.querySelectorAll(".side-menu-container");
	function sideMenuFlow() {
		if (window.scrollY > 70) {
			sideMenuContainers[0].style.top = sideMenuContainers[1].style.top = (window.scrollY - 70)+"px";
		} else {
			sideMenuContainers[0].style.top = sideMenuContainers[1].style.top = "0px";
		}
	}
	
	var prevDay = "";
	function refreshCalendar() {
		var noteDates = document.querySelectorAll("div.note-date");
		var currDay;
		for (var i = 0; i < noteDates.length; i++) {
			if(window.scrollY > noteDates[i].parentNode.offsetTop && window.scrollY < noteDates[i].parentNode.offsetTop+noteDates[i].parentNode.clientHeight){
				currDay = noteDates[i].textContent;
			}
		}
		if (prevDay !== currDay && currDay !== undefined) {
			var date = currDay.split("-");
			//yearChange
			if ($(".calendar.first .yearselect option[selected='selected']").attr("value") !== date[0]) {
				$(".calendar.first .yearselect").val(date[0]).trigger("change");
			}
			//monthChange
			if ($(".calendar.first .monthselect option[selected='selected']").attr("value") !== date[1]-1+"") {
				$(".calendar.first .monthselect").val(date[1]-1).trigger("change");
			}
			//dayChange
			$(".calendar.first table tbody td.active").removeClass("active");
			var days = $(".calendar.first table tbody td.available");
			for (var i = 0; i < days.length; i++) {
				if (days[i].textContent === date[2]) {
					days[i].className += " active";
				}
			}
		}
	}
	
	function groupUpdate() {
		window.location.href = "/groups/update/form/"+groupId;
	}
	</script>
	<script src="/js/note.js"></script>
	<script src="/js/datepickr.js"></script>
	<script src="/js/moment.js"></script>
	<script src="/js/daterangepicker.js"></script>
	<script src="/js/pComment.js"></script>
</body>
</html>
