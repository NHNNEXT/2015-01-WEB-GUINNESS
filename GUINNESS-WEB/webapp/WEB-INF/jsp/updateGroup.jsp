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
#editBackgroundImage-photoArea img.background {
	width: 200px;
	margin-bottom: 10px;
}

#editProfile-form p {
	margin-top: 0px;
}

#editProfile-form label {
	display: block;
	font-weight: bold;
}

#editProfile-form input[type="text"], #editProfile-form input[type="password"]
	{
	width: 200px;
	padding: 10px;
	font-size: 15px;
	border-radius: 4px;
	margin-bottom: 4px;
	transition-property: background-color;
	transition-duration: .5s;
	
}

#editProfile-form input[name="userPhoneNumber"] {
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

	<div class="content wrap" style="margin-top: 80px">
		<h1>
			<i class="fa fa-users"></i><span style="margin-left: 10px;">그룹정보수정</span>
		</h1>
		<div id="profile-panel" class="panel">
			<form:form modelAttribute="group" id="editProfile-form" cssClass="temp" enctype="multipart/form-data"
				action="/groups/update" method="post">
				<table class="panel-body" style="width:100%">
					<tr>
						<td valign=top id="editBackgroundImage-photoArea"
									style="width: 200px; text-align: center;">
							<img class="background" src="/img/group/${group.groupId}"> 
							<input type="file" name="backgroundImage" accept="image/x-png, image/gif, image/jpeg" />
						</td>
						
						<td style="padding-left: 25px;">
							<input type="hidden" id="sessionUserId" name="sessionUserId" value="${sessionUser.userId}" readonly>
							<form:hidden path="groupId" value="${group.groupId}" readonly="true"/>
							<p>
								<label class="control-label" for="groupName">그룹명 변경</label>
								<form:input path="groupName" autocomplete="off"
											required="required" value="${group.groupName}" />
								<span class="info">그룹명을 입력하세요.</span>
							</p>
							<p>
								<label class="control-label" for="status">공개 설정</label>
								<form:radiobutton path="status" value="F" />
								<span>비공개(<i class="fa fa-lock"></i>)</span>
								<form:radiobutton path="status" value="T" />
								<span>공개(<i class="fa fa-unlock"></i>)</span>
								<span class="info">공개 설정시 그룹원이 아니어도 입장이 가능합니다.</span>
							</p>
							<p>
								<label class="control-label" for="groupCaptainUserId">그룹장
										위임</label>
								<form:input path="groupCaptainUserId" />
								<span class="info">그룹장만이 위임이 가능합니다.</span>
							</p>
							<p>
								<span id="delete-group" style="background: red;width: 70px; margin: 0px; padding: 7px"class="btn">그룹삭제</span>
								<span class="info" style="margin-top: 12px;"><strong>[주의]</strong>해당 그룹과 관련된 모든 정보가 삭제됩니다.</span>
							</p>
							<hr />
							<button type="submit" class="btn btn-pm">수정</button>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	
	<script>
		/* function validCheck() {
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
		}); */
		
		document.querySelector("#delete-group").addEventListener("mousedown",
				function(e) {
					e.preventDefault();
					var groupId = "${group.groupId}";
					var groupName = "${group.groupName}";
					confirmDelete(groupId, groupName)
				}, false);
		function confirmDelete(groupId, groupName) {
			groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			var message = "그룹을 삭제하시겠습니까?";
			guinness.util.alert(groupName, message,
				function() {
					document.body.style.overflow = "auto";
					deleteGroup(groupId);
				},
				function() {
					document.body.style.overflow = "auto";
		            return;
				}
			);
		}

		function deleteGroup(groupId) {
			guinness.ajax({
				method:"delete",
				url:"/groups/" + groupId,
				success: function(req) {
					if(JSON.parse(req.responseText).success !== true) {
						guinness.util.alert("경고", "삭제할 권한이 없습니다.");
						return;
					}
					window.location.href = "/groups/form";
				}
			});
		}
	</script>
</body>
</html>
