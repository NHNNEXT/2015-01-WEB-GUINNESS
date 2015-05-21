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
	transition-property: background-color;
	transition-duration: .5s;
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
	color: #ff5a5a;
}
</style>
<body>
	<%@ include file="./commons/_topnav.jspf"%>
	<div class='content wrap' style='margin-top: 80px'>
		<h1>
			<i class='fa fa-user'></i><span style='margin-left: 10px;'>그룹정보수정</span>
		</h1>
		<div id='profile-panel' class='panel'>
			<form:form modelAttribute="group" cssClass="temp" enctype='multipart/form-data'
				action="/groups/update" method="post">
				
				<td valign=top id='editBackgroundImage-photoArea'
							style='width: 200px; text-align: center;'><img
							class='background' src="/img/group/${group.groupImage}"> <input
							type="file" name="backgroundImage"
							accept="image/x-png, image/gif, image/jpeg" /></td>
				
				<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUser.userId}" readonly>
				<form:hidden path="groupId" value="${group.groupId}" readonly="true"/>
				<div class="control-group">
					<label class="control-label" for="groupName">그룹명 변경</label>
					<div class="controls">
						<form:input path="groupName" autocomplete="off"
							required="required" value="${group.groupName}" />
					</div>
					<span class="info">그룹명을 입력하세요.</span>
				</div>
				<div class="control-group">
					<label class="control-label" for="status">공개 설정</label>
					<div class="controls">
						<form:radiobutton path="status" value="F" />
						<label>비공개</label>
						<form:radiobutton path="status" value="T" />
						<label>공개</label>
					</div>
					<span class="info">공개 설정시 그룹원이 아니어도 입장이 가능합니다.</span>
				</div>
				<div class="control-group">
					<label class="control-label" for="groupCaptainUserId">그룹장
						위임</label>
					<div class="controls">
						<form:input path="groupCaptainUserId" />
					</div>
					<span class="info">그룹장만이 위임이 가능합니다.</span>
				</div>
				<%-- <div class="control-group">
						<label class="control-label" for="email">그룹 삭제</label>
						<div class="controls">
							<form:input path="email" />
						</div>
						<span class="info">그룹장만이 삭제가 가능합니다.</span>
					</div> --%>
				<div class="control-group">
					<div class="controls">
						<button type="submit" class="btn btn-primary">수정</button>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<script>
		function validCheck() {
			var userName = document.querySelector("input[name='userName']").value;
			var userPassword = document.querySelector("input[name='userPassword']").value;
			var userAgainPassword = document.querySelector("input[name='userAgainPassword']").value;
			var isValid = true;
			if (userName === "") {
				document.querySelector("input[name='userName']").style.backgroundColor = "#ff5a5a";
				document.querySelector("input[name='userName']~span.errorMessage").innerHTML = "이름은 필수사항 입니다.";
				isValid = false;
			}
			if (userName.length > 25) {
				document.querySelector("input[name='userName']").style.backgroundColor = "#ff5a5a";
				document.querySelector("input[name='userName']~span.errorMessage").innerHTML = "이름은 25자 이하만 가능합니다."
				isValid = false;
			}
			if (!(userPassword === "" && userAgainPassword === "") && (userPassword !== userAgainPassword)) {
				document.querySelector("input[name='userAgainPassword']").style.backgroundColor = "#ff5a5a";
				document.querySelector("input[name='userAgainPassword']~span.errorMessage").innerHTML = "비밀번호가 다릅니다!";
				isValid = false;
			}
			if (!(isValid))
				return;
			document.querySelector("#editProfile-form").submit();
		}

		document.querySelector("input[name='userName']").addEventListener('click', function() {
			this.style.backgroundColor = "#fff";
			this.parentNode.querySelector("span.errorMessage").innerHTML = "";
		});
		document.querySelector("input[name='userAgainPassword']").addEventListener('click', function() {
			this.style.backgroundColor = "#fff";
			this.parentNode.querySelector("span.errorMessage").innerHTML = "";
		});
	</script>
</body>
</html>
