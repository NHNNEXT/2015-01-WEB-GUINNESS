<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
  <title>스터디의 시작, 기네스</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
  <link rel="stylesheet" href="css/mainStyle.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
</head>
<body>
<%@ include file="./commons/_topnav.jspf" %>
<div class='content wrap' style='margin-top:100px'>
  <ul id='group-container' class='group-list'>
    <a href="#group/create"><li id='create-new'>새 스터디 그룹 생성...</li></a>
  </ul>
</div>
<script>
  window.addEventListener('load',function(){
     var req = new XMLHttpRequest();
     var json = null;
     req.onreadystatechange = function(){
         if(req.readyState == 4){
           if(req.status == 200){
              json  = JSON.parse(req.responseText);
              createGroup(json);
           }
         }
     };
     req.open('get','./a.json',true);
     req.send();
  }, false);

  function createGroup(json){
    var el = document.getElementById('group-container');
    var obj = null;
    for(var i = 0; i < json.length; i++){
        obj = json[i];
        var newEl = document.createElement("a");
        newEl.setAttribute("href",obj.url);
        newEl.innerHTML = "<li>"+obj.name+"</li>";
        el.appendChild(newEl);
    }
  }

</script>
</body>
</html>