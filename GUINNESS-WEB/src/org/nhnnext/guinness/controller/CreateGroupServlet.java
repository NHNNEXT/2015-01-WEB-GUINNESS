package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.ServletName;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDAO;

@WebServlet(ServletName.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -2676389998849949681L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 그룹 캡틴 유저 아이디
		//groupCaptainUserId = (String) req.getSession().getAttribute(SessionKey.SESSION_USERID);
		String groupCaptainUserId="test@guinness.org";
		String groupName = (String)req.getParameter("groupName");

		int isPublic = 0;
		
		if("public".equals(req.getParameter("isPublic"))) {
			isPublic = 1;
		}
	
		Group group = new Group(groupName, groupCaptainUserId, isPublic);
		
		GroupDAO groupDao = new GroupDAO();
		groupDao.createGroup(group);
		
		// Group-User MAP table에 정보 저장
		groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		
		resp.sendRedirect("/groups.jsp");
	}
	
	
}
