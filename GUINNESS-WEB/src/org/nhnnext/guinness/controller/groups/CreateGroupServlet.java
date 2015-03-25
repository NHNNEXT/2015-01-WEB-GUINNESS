package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
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
import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletURL.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -2676389998849949681L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String groupCaptainUserId = (String)session.getAttribute(ParameterKey.SESSION_USERID);
		String groupName = (String)req.getParameter("groupName");
		
		// 그룹 공개/비공개 여부 판단 
		int isPublic = 0;
		if("public".equals(req.getParameter("isPublic")))
			isPublic = 1;
		
		// 그룹 클래스 생성 
		Group group = null;
		try {
			group = new Group(groupName, groupCaptainUserId, isPublic);
		} catch (Exception e) {
			// 그룹 생성 실패
			e.printStackTrace();
			String errorMessage = "그룹 생성 실패";

			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}
		
		// 유효성 검사 
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);
		
		if(constraintViolation.size() > 0){
			String errorMessage = constraintViolation.iterator().next().getMessage();

			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}
		
		// 그룹 다오 생성 
		GroupDao groupDao = new GroupDao();
		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 접근 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 연결 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}
		resp.sendRedirect("/groups.jsp");
	}
}
