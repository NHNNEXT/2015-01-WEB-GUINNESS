package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.MyValidatorFactory;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletUrl.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String groupCaptainUserId = (String) session.getAttribute("sessionUserId");
		String groupName = req.getParameter("groupName");

		// 그룹 공개/비공개 여부 판단
		char isPublic = 'F';
		if ("public".equals(req.getParameter("isPublic")))
			isPublic = 'T';

		// 그룹 클래스 생성
		Group group = null;
		try {
			group = new Group(groupName, groupCaptainUserId, isPublic);
		} catch (MakingObjectListFromJdbcException | SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}

		// 유효성 검사
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);

		if (constraintViolation.size() > 0) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			Forwarding.doForward(req, resp, "errorMessage", errorMessage, "/groups.jsp");
			return;
		}

		// 그룹 다오 생성
		GroupDao groupDao = new GroupDao();
		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}

		resp.sendRedirect("/groups.jsp");
	}

}
