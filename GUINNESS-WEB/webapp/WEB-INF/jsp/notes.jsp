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
			<a style="display:inline-block" href="/g/${groupId}"><span id="group-name"></span></a><i class="fa fa-cog" style="font-size:20px;" onclick="groupUpdate();"></i>
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
				<ul id="question-list" style="list-style:none; margin:5px 0 10px 0;"></ul>
			</div>
		</div>

		<div id="group-member-container" class="side-menu-container">
			
			<form id="addMemberForm">
				<span class="addMemberTitle" style="font-weight:bold;">멤버추가</span><br/>	
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
		</div>
	</div>

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
	
    <!-- 노트 모달을 위한 템플릿 -->
	<%@ include file="./commons/_note_popup.jspf"%> 
    
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

		
		getDateExistNotes();
	}, false);
	
	window.addEventListener("scroll", function(e) {
		infiniteScroll();
		sideMenuFlow();
		resizeSideMenu();
		refreshCalendar();
	}, false);
	

	window.addEventListener('resize',function(){
		resizeSideMenu();
	},false);
	
	$(function() {
	    $("#defaultCalendar").daterangepicker({
	        singleDatePicker: true,
	        showDropdowns: false
	    },
	    function(start, end, label) {
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
		if (e.target.getAttribute("class") === null || e.target.getAttribute("class").indexOf("available") === -1 || e.target.getAttribute("class").indexOf("existNote") === -1)
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
			var days = $(".calendar.first table tbody td.existNote");
			for (var i = 0; i < days.length; i++) {
				var day = (date[2] < 10) ? date[2].substring(1,2) : date[2];
				if (days[i].textContent === day) {
					days[i].className += " active";
				}
			}
		}
	}
	
	function groupUpdate() {
		window.location.href = "/groups/update/form/"+groupId;
	}
	
	var nullCheckMonth;
	function getDateExistNotes(year,month){ //;; select null exist notes day
		var noteTargetYear = $("#defaultCalendar").data("daterangepicker").startDate._d.toISOString().substring(0,4);
		var noteTargetMonth = $("#defaultCalendar").data("daterangepicker").startDate._d.toISOString().substring(5,7);
		if(year !== undefined)
			noteTargetYear = year;
		if(month !== undefined)
			noteTargetMonth = month+1;
		var lastDate = ( new Date( noteTargetYear,noteTargetMonth, 1) ).toISOString().substring(0,10)+ " 23:59:59";
		guinness.ajax({
	        method: "get",
	        url: "/notes/getNullDay/" + groupId + "/" + lastDate,
	        success: function (req) {
	            var json = JSON.parse(req.responseText);
	            nullCheckMonth = json.objectValues;
	            if (json.success === true) {
	            	setNullCheck(nullCheckMonth);
	            }
	        }
	    });
	}
	function setNullCheck(nullCheckMonth){
		var td = document.querySelectorAll(".available");
		var flagStart = false;
		var i = 0;
		for(t in td) {
			   if(td[t].innerText === "1"){
				   flagStart = true;
			   }
			   if(flagStart === true){
				   if(nullCheckMonth[i] === true){
					   td[t].className = td[t].className+" noNote";
				   }
				   else{
					   td[t].className = td[t].className+" existNote";
				   }
				   i++;
				  if(nullCheckMonth.length === i)
					   break;
			   }
		}
	}
	
	</script>
	<script src="/js/datepickr.js"></script>
	<script src="/js/moment.js"></script>
	<script src="/js/daterangepicker.js"></script>
</body>
</html>
