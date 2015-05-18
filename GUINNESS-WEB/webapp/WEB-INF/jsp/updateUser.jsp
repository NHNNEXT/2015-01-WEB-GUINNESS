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
<link rel="stylesheet" href="/css/font-awesome.min.css">
</head>
<style>
#editProfile-photoArea img.avatar {
	width: 200px;
	height: 200px;
	background-color: #558;
	border-radius: 5px;
	overflow: hidden;
	margin-bottom: 10px;
}

#editProfile-form p {
	margin-top: 0px;
}

#editProfile-form label {
	display: block;
	font-weight: bold;
}

#editProfile-form input[type='text'], #editProfile-form input[type='password']
	{
	width: 200px;
	padding: 10px;
	font-size: 15px;
	border-radius: 4px;
	margin-bottom: 4px;
}

#editProfile-form input[name='userPhoneNumber'] {
	width: 150px;
}

#editProfile-form span.info {
	display: block;
	font-size: 12px;
	color: #9e9ea6;
}

#editProfile-form span.info strong {
	color: #ff5a5a
}
</style>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<div class='content wrap' style='margin-top: 80px'>
		<h1>
			<i class='fa fa-user'></i><span style='margin-left: 10px;'>회원정보수정</span>
		</h1>
		<div id='profile-check-password' class='panel'>
			<label>비밀번호를 입력해주세요 </label> <input id="check-password"
				type="password" name="password">
			<button id="profile-check-password-button">확인</button>
			<label id="checkPasswordErrorMessage"></label>
		</div>
		<div id='profile-panel' class='panel' hidden="true">
			<form:form modelAttribute="user" id='editProfile-form'
				enctype='multipart/form-data' method='post' action='/user/update'>
				<table id='editProfile-panel-body' class='panel-body'
					style='width: 100%'>
					<tr>
						<td valign=top id='editProfile-photoArea'
							style='width: 200px; text-align: center;'><img
							class='avatar' src="/img/profile/${sessionUser.userImage}"> <input
							type="file" name="profileImage"
							accept="image/x-png, image/gif, image/jpeg" /></td>
						<td valign=top id='editProfile-profileArea'
							style='padding-left: 25px;'><form:hidden path="userId"
								value="${sessionUser.userId}" />
							<p>
								<label for="userName">사용자 이름</label>
								<form:input path="userName" autocomplete="off"
									required="required" value="${sessionUser.userName}" />
								<span class="info"><strong>[필수사항]</strong>스터디메이트들과의 소통을
									위한 이름을 입력하세요.</span>
							</p>
							<p>
								<label for="userPassword">비밀번호 변경</label>
								<form:password path="userPassword" placeholder='' />
								<span class="info">비밀번호를 변경하시려면 새로운 비밀번호를 입력하세요.</span>
							</p>
							<p>
								<label for="userAgainPassword">비밀번호 확인</label> <input
									name="userAgainPassword" type="password" placeholder='' /> <span
									class="info">비밀번호를 확인하기 위해 한번 더 입력하세요.</span>
							</p> <c:if test="${not empty signValidErrorMessage}">
								<span class="errorMessage"> ${signValidErrorMessage} <br /></span>
							</c:if>
							<hr />
							<button type="submit" class="btn btn-pm">수정</button></td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	<script type="text/javascript">
		window.addEventListener('load', function() {
			document.querySelector('#profile-check-password-button')
					.addEventListener("click", function(e) {
						passwordCheck(e);
					}, false);
			var checkPasswordBox = document.querySelector('#check-password');
			
			checkPasswordBox.addEventListener("keyup", function(e) {
				if (e.keyCode === 13) {
					passwordCheck(e);
				}
			}, false);
			
			checkPasswordBox.focus();
		}, false);

		function passwordCheck(e) {
			guinness
					.ajax({
						method : "post",
						url : "/user/update/check",
						param : "password="
								+ document.querySelector('#check-password').value,
						success : function(req) {
							if (JSON.parse(req.responseText).success === false) {
								document
										.querySelector('#checkPasswordErrorMessage').innerHTML = JSON
										.parse(req.responseText).message;
								return;
							}
							document.querySelector('#profile-panel').hidden = false;
							document.querySelector('#profile-check-password').hidden = true;
						}
					});
		}
	</script>
</body>
</html>
