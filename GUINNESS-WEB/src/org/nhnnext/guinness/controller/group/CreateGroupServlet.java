package org.nhnnext.guinness.controller.group;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.dao.GroupDAO;
import org.nhnnext.guinness.model.Group;

@WebServlet("/group/create")
public class CreateGroupServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String groupId, groupName, groupCaptainUserId, createDate = null;
		int isPublic = 0;
		
		groupName = (String)req.getParameter("groupName");
		
		System.out.println(req.getParameter("isPublic"));

		if(req.getParameter("isPublic").equals("on")) {
			isPublic = 1;
		}
		
		// 그룹 아이디 생성
		
		groupId="abcdf";
		
		// 그룹 캡틴 유저 아이디
		
		//groupCaptainUserId = (String) req.getSession().getAttribute("userId");
		groupCaptainUserId="test@guinness.org";
		
		System.out.println(groupId);
		System.out.println(groupName);
		System.out.println(groupCaptainUserId);
		System.out.println(createDate);		
		System.out.println(isPublic);

		Group group = new Group(groupId, groupName, groupCaptainUserId, createDate, isPublic);
		
		GroupDAO groupDao = new GroupDAO();
		try {
			groupDao.createGroup(group);
		} catch (SQLException e) {
		}
		
		resp.sendRedirect("/groups.jsp");
		System.out.println("그룹 생성을 성공하였습니다.");
	
	}
	
	
}
