package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.exception.SessionUserIdNotFoundException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;

@WebServlet(WebServletUrl.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupDao groupDao = GroupDao.getInstance();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String groupCaptainUserId = null;
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupName","isPublic");

		char isPublic = 'F';
		if ("public".equals(paramsList.get("isPublic")))
			isPublic = 'T';

		Group group = null;
		try {
			groupCaptainUserId = ServletRequestUtil.checkSessionAttribute(req, resp);
			group = new Group(paramsList.get("groupName"), groupCaptainUserId, isPublic);
		} catch (MakingObjectListFromJdbcException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		} catch (SessionUserIdNotFoundException e) {
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
		} catch (SQLException | ClassNotFoundException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}

		resp.sendRedirect("/groups.jsp");
	}
}
