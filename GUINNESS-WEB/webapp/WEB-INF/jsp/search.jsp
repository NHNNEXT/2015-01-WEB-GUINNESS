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
<script src="/js/${functionSelect}"></script>
</head>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUserId}">
	<input type="hidden" id="noteId" name="noteId" value="${noteId}">
	<div id='note-list-container' class='content wrap'>
		<ul class="note-list">
			<li></li>
		</ul>
	</div>
            
    <script type="template" class="pCommentTemplate">
        <div class="pCommentBox">
            <p class="inputP" contenteditable="true">Leave here</p>
            <p><span>확인</span><span>취소</span></p>
        </div>
    </script>
        
	<script type="template" class="noteTemplate" >
			<img class="avatar" >
			<div class="content-container">
				<div>
					<span class="userName"></span>
					<span class="userId"></span>
				</div>
				<div>
					<span class="note-date"></span>
				</div>
					<div class="noteText"></div>
				<div>
					<i class="fa fa-comments"></i>
				</div>
			</div>
	</script>
</body>
</html>
