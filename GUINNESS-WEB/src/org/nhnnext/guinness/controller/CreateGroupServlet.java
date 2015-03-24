package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.common.MyValidatorFactory;
import org.nhnnext.guinness.common.SessionKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDAO;

@WebServlet(WebServletURL.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -2676389998849949681L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String groupCaptainUserId = (String)session.getAttribute(SessionKey.SESSION_USERID);
		String groupName = (String)req.getParameter("groupName");
		
		int isPublic = 0;
		if("public".equals(req.getParameter("isPublic")))
			isPublic = 1;
		
		Group group = new Group(groupName, groupCaptainUserId, isPublic);
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);
		
		if(constraintViolation.size() > 0){
			String errorMessage = constraintViolation.iterator().next().getMessage();
			System.out.println(constraintViolation.iterator().next().getPropertyPath()+" : "+errorMessage);
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}
		GroupDAO groupDao = new GroupDAO();
		groupDao.createGroup(group);
		groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		
		resp.sendRedirect("/groups.jsp");
	}
}
