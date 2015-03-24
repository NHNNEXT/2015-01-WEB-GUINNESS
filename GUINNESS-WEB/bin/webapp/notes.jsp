<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <title>스터디의 시작, 기네스</title>
  <link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
  <link rel="stylesheet" href="/css/mainStyle.css">
  <link rel="stylesheet" href="/css/font-awesome.min.css">
  <link rel="stylesheet" href="/css/datepickr.min.css">
  <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="/js/datepickr.min.js"></script>
  <script src="/js/guinness.js"></script>
</head>
<body>
<%@ include file="/commons/_topnav.jspf" %>
<button id='create-new-button'>새 일지 작성</button>

<div id='black-cover-note' class='modal-cover' style='display:none'>
  <div id='createNote-container' class='modal-container'>
    <div id='createNote-header' class='modal-header'>
      <div id='createNote-title' class='modal-title'>새 일지 작성</div>
      <div id='createNote-close' class='modal-close'><i class='fa fa-remove'></i></div>
    </div>
    <div id='createNote-body' class='modal-body'>
	    <form name="user" method="post" action="/note/create">
			<table>
				<tr>
					<input id="groupId" type="hidden" name="groupId" value="">
					<td>날짜</td>
					<td><input id="datepickr" name="targetDate"></td>
					<!-- <td><input type="text" name="targetDate" value="${targetDate}"></td>-->
				</tr>
				<tr>
					<td>내용</td>
					<!-- <td><input type="text" name="noteText" value="${noteText}"></td>-->
					<td><textarea style="resize:none" rows="10" cols="50" name="noteText" value="${noteText}"></textarea></td>
				</tr>
			</table>
			<input type="submit" class='btn' value="작성" />
		</form>
    </div>
  </div>
</div>
<div id='note-list-container' class='content wrap' style='outline:1px solid red; margin-top:100px'>
  <ul class='time-nav'>
    <li id='to20150311' class='date-nav date-select' ><div class='date-tag'>3월 11일</div><div class='date-point'></div></li>
    <li id='to20150310' class='date-nav'><div class='date-tag'>3월 10일</div><div class='date-point'></div></li>
    <li id='to20150309' class='date-nav'><div class='date-tag'>3월 9일</div><div class='date-point'></div></li>
    <li id='to20150308' class='date-nav'><div class='date-tag'>3월 8일</div><div class='date-point'></div></li>
    <li id='to20150307' class='date-nav'><div class='date-tag'>지난 주</div><div class='date-point'></div></li>
    <li id='to20150201' class='date-nav'><div class='date-tag'>지난 달</div><div class='date-point'></div></li>
    <li id='to20140101' class='date-nav'><div class='date-tag'>2014년</div><div class='date-point'></div></li>
  </ul>
  <!-- 
  <ul id='day-20150311' class='diary-list'>
    <div class='diary-date'>
      <span>2015년 3월 11일</span>
      <i style='float:right;' class='fa fa-pencil'>새 노트 작성</i>	
    </div>
  </ul>
   -->
</div>
<script>
  /* scrolling navigation */
  window.addEventListener('load',function() {
    var dates = document.getElementsByClassName('date-nav');
    for (var i = 0; i < dates.length; i++) {
      dates[i].addEventListener('mouseup', function(e) {
        var location = e.currentTarget.id.replace('to','');
        var top = document.getElementById('day-'+location);
        if (top != null) {
          $('body').animate({scrollTop: top.offsetTop}, 500);
        }
      },false);
    }
    var el = document.getElementById('create-new-button');
    el.addEventListener('mouseup',createNote,false);
    el = document.getElementById('createNote-close');
    el.addEventListener('mouseup',createNote,false);
    
    var groupId = window.location.pathname.split("/")[2];
    var targetDate = "2015-03-11";
    readNoteList(groupId,targetDate);
    attachGroupId(groupId);
  },false);
  
  function attachGroupId(data) {
	var el = document.getElementById("groupId");
	el.setAttribute("value", data); 
  }
  
  function readNoteList(groupId, targetDate) {
	  var req = new XMLHttpRequest();
	  req.open("GET","/notelist/read?groupId="+groupId+"&targetDate="+targetDate,true);
	  req.onreadystatechange = function() {
	  	if (req.status === 200 && req.readyState === 4) {
	  	  res =  JSON.parse(req.responseText);
	  	  appendNoteList(res);
	  	}
	  };
	  req.send();
  }
  
  function appendNoteList(json) {
	  //날짜별로 들어갈수 있게...
	  var el = null;
	  var newEl = null;
	  var obj = null;
	  var out = "";
	  for(var i = 0; i < json.length; i++) {
		  obj = json[i];
		  var targetDate = obj.targetDate;
		  targetDate = targetDate.split(" ");
		  targetDate = targetDate[0];
		  targetDate = targetDate.replace(/'-'/g,'');
		  el = document.getElementById("day-"+targetDate);
		  if (el == null) {
			el = document.createElement("ul");
			el.setAttribute("id","day-"+targetDate);
			el.setAttribute("class","diary-list");
			newEl = document.createElement("div");
			newEl.setAttribute("class","diary-date");
			newEl.innerHTML = "<span>"+targetDate+"</span><i style='float:right; cursor:pointer;' class='fa fa-pencil'></i>";
			el.appendChild(newEl);
			document.getElementById('note-list-container').appendChild(el);
		  }
		  newEl = document.createElement("a");
		  newEl.setAttribute("href","/note/read/"+obj.noteId);
		  out = "";
		  out += "<li><img class='avatar' class='avatar' src='/img/avatar-default.png'>";
          out += "<div class='msgContainer'>";
          out += "<span class='userName'>"+obj.userName+"</span>";
          out += "<div class='qhsans'>";
          out += obj.noteText;
          out += "</div></div></li>";
          newEl.innerHTML = out;
          el.appendChild(newEl);
	  }
  }
	  
  function createNote(e) {
    var blkcvr = document.getElementById('black-cover-note');
    if (blkcvr.style.display == "none") {
  	blkcvr.style.display = "block";
    } else {
      blkcvr.style.display = "none";
    }
  }
	  
  datepickr('#datepickr', { dateFormat: 'Y-m-d'});
</script>
</body>
</html>
