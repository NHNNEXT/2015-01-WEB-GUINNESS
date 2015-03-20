<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> 노트 작성 </title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="span12">
				<section id="typography">
				<div class="page-header">
					<h1>노트작성</h1>
				</div>
				
				<form name="user" method="post" action="/notes/create">
			
					<table>
						<tr>
							<td>그룹명</td>
							<td><input type="text" name="groupId" value="abcde"></td>
						</tr>
						<tr>
							<td>날짜</td>
							<td><input type="text" name="targetDate" value="${targetDate}"></td>
						</tr>
						<tr>
							<td>내용</td>
							<td><input type="text" name="noteText" value="${noteText}"></td>
						</tr>
					</table>
					<input type="submit" value="작성" />
				</form>
			</div>
		</div>
	</div>

</body>
</html>

