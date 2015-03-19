<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form name="user" method="post" action="/group/create">
		<div>
			그룹이름 <input type="text" name="groupName" value="" />
		</div>
		<div>
			공개여부 <input type="checkbox" name="isPublic">
		</div>
		<input type="submit" value="확인" />
	</form>
</body>
</html>