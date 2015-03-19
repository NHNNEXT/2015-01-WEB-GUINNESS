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
<div id="header">
  <div class='content wrap'>
      <a href='#' id='brand'>
          <img src='http://www.w3.org/html/logo/downloads/HTML5_1Color_White.png'>
      </a>
      <ul class='util'>
        <img src='img/profile_sample.jpg'>
        <a href="#">
          <li>사용자1 님</li>
        </a>
        <a href-="#">
          <li><i class='fa fa-bell'></i></li>
        </a>
        <a href="#">
          <li>로그아웃</li>
        </a>
      </ul>
  </div>
</div>
<div class='content wrap' style='margin-top:100px'>
  <ul id='group-container' class='group-list'>
    <a href="javascript:popupOpen();"><li id='create-new'>새 스터디 그룹 생성...</li></a>
  </ul>
</div>


<script type="text/javascript">
function popupOpen(){
	var popUrl = "/createGroup.jsp";	//팝업창에 출력될 페이지 URL
	var popOption = "width=370, height=360, resizable=no, scrollbars=no, status=no;";    //팝업창 옵션(optoin)
		mywindow = window.open(popUrl,"",popOption);
	}
</script>

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