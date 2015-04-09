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
	private GroupDao groupDao = GroupDao.getInstance();
	
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String groupCaptainUserId = (String) session.getAttribute("sessionUserId");
		String groupName = req.getParameter("groupName");

		char isPublic = 'F';
		if ("public".equals(req.getParameter("isPublic")))
			isPublic = 'T';

		Group group = null;
		try {
			group = new Group(groupName, groupCaptainUserId, isPublic);
		} catch (MakingObjectListFromJdbcException | SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}

		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);

		if (constraintViolation.size() > 0) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			Forwarding.doForward(req, resp, "errorMessage", errorMessage, "/groups.jsp");
			return;
		}

		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}

		resp.sendRedirect("/groups.jsp");
	}
}
