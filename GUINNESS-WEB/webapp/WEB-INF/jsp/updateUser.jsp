<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<title>스터디의 시작, 페이퍼민트</title>
<meta charset="utf-8">
<%@ include file="./commons/_favicon.jspf"%>
<link rel="stylesheet"
	href="http://fonts.googleapis.com/earlyaccess/nanumgothic.css">
<link rel="stylesheet" href="/css/mainStyle.css">
<link rel="stylesheet" href="/css/update.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
</head>
<body>

	<%@ include file="./commons/_topnav.jspf"%>

	<div class="content wrap" style="margin-top: 80px">
		<h1>
			<i class="fa fa-user"></i><span style="margin-left: 10px;">회원정보수정</span>
		</h1>
		<div id="profile-panel" class="panel">
			<form:form modelAttribute="user" class="update-form"
				enctype="multipart/form-data" method="post" action="/user/update">
				<table class="panel-body" style="width: 100%">
					<tr>
						<td valign=top id="editProfile-photoArea" style="width: 200px; text-align: center;">
							<!-- new -->
							<div>
								<a href="#">
									<img class="avatar" id="avatarSanp" src="/img/profile/${sessionUser.userImage}" onmouseover="mouseOver()" style="border: solid 1px rgb(191, 191, 191);" >
									<img class="avatar" id="avatarHover" onclick="mouseClick()" onmouseout="mouseOut()" src="/img/group/user_hover.png" style="display:none">
								</a>
							</div>
							<input id="imageFile" type="file" name="profileImage" accept="image/x-png, image/gif, image/jpeg" style="display:none;" onchange="changeImage(this)"/>
							<div>
								<input type="button" class ="background-default" name="background-default" value="원래대로" onclick="setPreImage('${sessionUser.userImage}')">
								<input type="button" class ="background-default" name="background-default" value="배경초기화" onclick="setInitImage();">
							</div>
							<form:hidden path="userImage" value="${sessionUser.userImage}" readonly="true"/>
							<!-- new -->
						</td>
						
						<td valign=top style="padding-left: 25px;">
							<form:hidden path="userId" value="${sessionUser.userId}" />
							<p>
								<label for="userName">사용자 이름</label>
								<form:input path="userName" autocomplete="off" required="required" value="${sessionUser.userName}" />
								<span class="info">
									<strong>[필수사항]</strong>스터디메이트들과의 소통을 위한 이름을 입력하세요.
								</span>
								<span class="errorMessage"></span><br/>
							</p>
							<p>
								<label for="userPassword">비밀번호 변경</label>
								<form:password path="userPassword" placeholder="" />
								<span class="info">비밀번호를 변경하시려면 새로운 비밀번호를 입력하세요.</span>
							</p>
							<p>
								<label for="userAgainPassword">비밀번호 확인</label> 
								<input name="userAgainPassword" type="password" placeholder="" />
								<span class="info">비밀번호를 확인하기 위해 한번 더 입력하세요.</span>
								<span class="errorMessage"></span><br/>
							</p> 
							<hr />
							<input type="button" class="btn btn-pm-cancle" onclick ="backToNoteList()" value="취소"/>
							<input type="button" class="btn btn-pm" onclick="validCheck()" value="수정" />
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	
	<script>
	function mouseClick() {
		document.getElementById('imageFile').click();
	}
	function mouseOver() {
		document.querySelector("#avatarHover").style.display = "block";
	}
	function mouseOut() {
		document.querySelector("#avatarHover").style.display = "none";
	}
	
	function changeImage(input) {
		if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
            	document.getElementById('avatarSanp').src = e.target.result;
            	document.querySelector("#userImage").value = "changed";
            }
            reader.readAsDataURL(input.files[0]);
        }
	}
	// 원래 배경
	function setPreImage(userId){
		document.querySelector("#avatarSanp").src = "/img/profile/" + userId;
		document.querySelector("#userImage").value = userId;
	}
	// 배경 없음 
	function setInitImage(){
		document.querySelector("#avatarSanp").src = '/img/avatar-default.png';
		document.querySelector("#userImage").value = "avatar-default.png";
	}
	function backToNoteList() {
		window.location.href = "/";
	}
	function validCheck() {
		var userName = document.querySelector("input[name='userName']").value;
		var userPassword = document.querySelector("input[name='userPassword']").value;
		var userAgainPassword = document.querySelector("input[name='userAgainPassword']").value;
		var isValid = true;
		if (userName === "") {
			document.querySelector("input[name='userName']").style.backgroundColor="#ff5a5a";
			document.querySelector("input[name='userName']~span.errorMessage").innerHTML = "이름은 필수사항 입니다.";
			isValid = false;
		}
		if (userName.length > 25) {
			document.querySelector("input[name='userName']").style.backgroundColor="#ff5a5a";
			document.querySelector("input[name='userName']~span.errorMessage").innerHTML = "이름은 25자 이하만 가능합니다."
			isValid = false;
		}
		if (!(userPassword === "" && userAgainPassword === "") && (userPassword !== userAgainPassword)) {
			document.querySelector("input[name='userAgainPassword']").style.backgroundColor="#ff5a5a";
			document.querySelector("input[name='userAgainPassword']~span.errorMessage").innerHTML = "비밀번호가 다릅니다!";
			isValid = false;
		}
		if (!(isValid)) return;
		document.querySelector(".update-form").submit();
	}
	
	document.querySelector("input[name='userName']").addEventListener("click",function(){
		this.style.backgroundColor="#fff";
		this.parentNode.querySelector("span.errorMessage").innerHTML = "";
	});
	document.querySelector("input[name='userAgainPassword']").addEventListener("click",function(){
		this.style.backgroundColor="#fff";
		this.parentNode.querySelector("span.errorMessage").innerHTML = "";
	});
	</script>
</body>
</html>
