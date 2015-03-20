<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>스터디의 시작, 기네스</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
  <link rel="stylesheet" href="/css/mainStyle.css">
</head>
<body>
<div id="jumbotron-container">
  <div class='content wrap'>
    <div id="jumbotron">
      <h1>스터디의 시작, 기네스</h1>
      <div class='subscribe'>기네스에서 나만의 학습 일지를 작성하고, 스터디 멤버들과 함께 그룹을 생성하여 학습 일지를 공유하거나 언제 어디에서나 피드백을 받으세요!</div>
      <form method="post" action="/users/login" class='form-group' style='margin:0 auto;'>
          <input class='text' type="text" name="userId" placeholder="이메일">
          <input class='text' type="password" name="userPassword" placeholder="비밀번호"><br/>
          <button type="submit" class='btn'>로그인</button>
      </form>
      <div id='login-util'>
        <a href="/"><span>회원가입</span></a><!-- 
        <a><span style='border-right:1px solid white; border-left:1px solid white;'>아이디 찾기</span></a>
        <a><span>비밀번호 찾기</span></a> -->
      </div>
    </div>
  </div>
</div>
</body>
</html>
