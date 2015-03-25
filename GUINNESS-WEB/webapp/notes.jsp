<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
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
			<input id="groupId" type="hidden" name="groupId" value="">
			<table>
				<tr>
					<td>날짜</td>
					<td><input id="datepickr" name="targetDate"></td>
				</tr>
				<tr>
					<td>내용</td>
					<td><textarea style="resize:none" rows="10" cols="50" name="noteText"></textarea></td>
				</tr>
			</table>
			<input type="submit" class='btn' value="작성" />
		</form>
    </div>
  </div>
</div>
<div id='note-list-container' class='content wrap'>
  <ul id='to-date' class='time-nav'>
  </ul>
</div>
<script>
  /* scrolling navigation */
  window.addEventListener('load',function() {
    var el = document.getElementById('create-new-button');
    el.addEventListener('mouseup',createNote,false);
    el = document.getElementById('createNote-close');
    el.addEventListener('mouseup',createNote,false);
    
    var groupId = window.location.pathname.split("/")[2];
    var targetDate = guinness.util.today("-");
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
	  	  appendTimeNav(res);
	  	}
	  };
	  req.send();
  }
  function appendTimeNav(json) {
	  var newLi = null;
	  var dateTag = null;
	  var datePoint = null;
	  var obj = null;
	  for(var i = 0; i< json.length; i++) {
		  obj = json[i];
		  var toDate = obj.targetDate;
		  toDate = toDate.split(" ");
		  toDate = toDate[0];
		  toDate = toDate.replace(/'-'/g,'');
		  newLi = document.getElementById("to"+toDate);
		  if(newLi == null) {
			  newLi = document.createElement("li");
			  newLi.setAttribute("id", "to"+toDate);
			  newLi.setAttribute("class", "date-nav");
			  dateTag = document.createElement("div");
			  dateTag.setAttribute("class","date-tag");
			  dateTag.innerHTML = toDate;
			  datePoint = document.createElement("div");
			  datePoint.setAttribute("class","date-point");
			  newLi.appendChild(dateTag);
			  newLi.appendChild(datePoint);
		  }
		  document.getElementById("to-date").appendChild(newLi);  
	  }
	  
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
