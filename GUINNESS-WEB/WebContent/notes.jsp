<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>스터디의 시작, 기네스</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
  <link rel="stylesheet" href="css/mainStyle.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
</head>
<body>
<%@ include file="./commons/_topnav.jspf" %>
<div class='content wrap' style='outline:1px solid red; margin-top:100px'>
  <ul class='time-nav'>
    <li id='to20150311' class='date-nav date-select' ><div class='date-tag'>3월 11일</div><div class='date-point'></div></li>
    <li id='to20150310' class='date-nav'><div class='date-tag'>3월 10일</div><div class='date-point'></div></li>
    <li id='to20150309' class='date-nav'><div class='date-tag'>3월 9일</div><div class='date-point'></div></li>
    <li id='to20150308' class='date-nav'><div class='date-tag'>3월 8일</div><div class='date-point'></div></li>
    <li id='to20150307' class='date-nav'><div class='date-tag'>지난 주</div><div class='date-point'></div></li>
    <li id='to20150201' class='date-nav'><div class='date-tag'>지난 달</div><div class='date-point'></div></li>
    <li id='to20140101' class='date-nav'><div class='date-tag'>2014년</div><div class='date-point'></div></li>
  </ul>
  <ul class='diary-list'>
    <div id='day-20150311' class='diary-date'>2015년 3월 11일</div>
    <a href="#">
        <li>
            <img src='img/avatar-default.png'>
            <div class='msgContainer'>
                <span class='userName'>Edward Scott</span>
                <div class='qhsans'>첫 JSP 학습
                    -WTP 설정
                    -일반적으로 서버는 멀티스레드 환경이라는 것!
                    -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                    -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                    ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                    </div>
            </div>
        </li>
    </a>
    <a href="#">
        <li>
            <img src='img/avatar-default.png'>
            <div class='msgContainer'>
                <span class='userName'>Ventaas Quitara</span>
                <div class='qhsans'>첫 JSP 학습
                    -WTP 설정
                    -일반적으로 서버는 멀티스레드 환경이라는 것!
                    -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                    -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                    ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                    </div>
            </div>
        </li>
    </a>
    <a href="#">
        <li>
            <img src='img/profile_sample2.jpg'>
            <div class='msgContainer'>
                <span class='userName'>Rosa Dias</span>
                <div class='qhsans'>첫 JSP 학습
                    -WTP 설정
                    -일반적으로 서버는 멀티스레드 환경이라는 것!
                    -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                    -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                    ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                    </div>
            </div>
        </li>
    </a>
  </ul>
  <ul class='diary-list'>
    <div id='day-20150309' class='diary-date'>2015년 3월 9일</div>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Edward Scott</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Ventaas Quitara</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Rosa Dias</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
  </ul>
  <ul class='diary-list'>
    <div id='day-20150308' class='diary-date'>2015년 3월 8일</div>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Edward Scott</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Ventaas Quitara</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
      <a href="#">
          <li>
              <img src='img/avatar-default.png'>
              <div class='msgContainer'>
                  <span class='userName'>Rosa Dias</span>
                  <div class='qhsans'>첫 JSP 학습
                      -WTP 설정
                      -일반적으로 서버는 멀티스레드 환경이라는 것!
                      -서블릿은 싱글 인스턴스로 생성되어 쓰레드가 재사용하기 때문에 이것에 유의하도록
                      -따라서 필드(전역변수)에서 개발하게 되면 값을 공유하기때문에 Private한 값은 메서드 안에서 선언하여 사용한다...

                      ? form 에서 post 방식으로 전송된 한글이 깨지는 현상

                  </div>
              </div>
          </li>
      </a>
  </ul>
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
  },false);
</script>
</body>
</html>