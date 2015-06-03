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
			<i class="fa fa-users"></i><span style="margin-left: 10px;">그룹정보수정</span>
		</h1>
		<div id="profile-panel" class="panel">
			<form:form modelAttribute="group" class="update-form" enctype="multipart/form-data"
				action="/groups/update" method="post">
				<table class="panel-body" style="width:100%">
					<tr>
						<td valign=top id="editBackgroundImage-photoArea">
							<div>
							<a href="#">
								<img id="imageSnap" src="/img/group/${group.groupImage}" onmouseover="mouseOver()" style="border: solid 1px rgb(191, 191, 191);">
								<img id="imageSnapHover" onclick="mouseClick()" onmouseout="mouseOut()" src="/img/group/change_hover.png" style="display:none">
							</a>
							</div>
							<input type="file" id="imageFile" name="backgroundImage" accept="image/x-png, image/gif, image/jpeg" style="display:none;" onchange="changeImage(this)"/> 
							<div>
								<input type="button" class ="background-default" name="background-default" value="기본 이미지" onclick="setInitImage();">
								<input type="button" class ="background-default" name="background-default" value="수정 취소" onclick="setPreImage('${group.groupImage}')">
							</div>
							<form:hidden path="groupImage" value="" readonly="true"/>
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
								<label class="control-label" for="groupCaptainUserId">그룹장 위임</label>
								<form:select path="groupCaptainUserId">
		                             <c:forEach items="${members}" var="member">
		                                   <form:option value="${member.userId}" label="${member.userName} ( ${member.userId} )" />
		                             </c:forEach>
		                        </form:select>
								<span class="info">그룹장만이 위임이 가능합니다.</span>
							</p>
							<p>
								<button id="delete-group-btn" class="btn">그룹삭제</button>
								<span class="info"><strong>[주의]</strong>해당 그룹과 관련된 모든 정보가 삭제됩니다.</span>
							</p>
							<hr />
							<input type="button" class="btn btn-pm-cancle" onclick ="backToNoteList()" value="취소"/>
							<button type="submit" class="btn btn-pm">수정</button>
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
			document.querySelector("#imageSnapHover").style.display = "block";
		}
		function mouseOut() {
			document.querySelector("#imageSnapHover").style.display = "none";
		}
		
		function changeImage(input) {
			if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                	document.getElementById('imageSnap').src = e.target.result;
                	document.querySelector("#groupImage").value = "changed";
                }
                reader.readAsDataURL(input.files[0]);
            }
		}
		// 원래 배경
		function setPreImage(groupId){
			document.querySelector("#imageSnap").src = "/img/group/" + groupId;
			document.querySelector("#groupImage").value = groupId;
		}
		// 배경 없음 
		function setInitImage(){
			document.querySelector("#imageSnap").src = '/img/group/background-default.png';
			document.querySelector("#groupImage").value = "background-default.png";
		}
		function backToNoteList() {
			window.location.href = "/g/"+group.groupId.value;
		}
		
		document.querySelector("#delete-group-btn").addEventListener("mousedown",
				function(e) {
					e.preventDefault();
					var groupId = "${group.groupId}";
					var groupName = "${group.groupName}";
					confirmDelete(groupId, groupName)
				}, false);
		function confirmDelete(groupId, groupName) {
			groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
			var message = "그룹 관련 정보가 모두 삭제됩니다. <br> 그래도 삭제하시겠습니까?";
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
			guinness.restAjax({
				method:"delete",
				url:"/groups/" + groupId,
				statusCode: {
		  			200: function(res) {	// 그룹 삭제 성공
		  				window.location.href = "/groups/form";
		  			},
		  			406: function(res) {	// 그룹 삭제 실패(그룹장이 아닐 경우)
		  				guinness.util.alert("경고", "삭제할 권한이 없습니다.");
		  			}
		  		}
			});
		}
	</script>
</body>
</html>
