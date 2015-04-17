package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.RandomString;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateGroupController {
	private static final Logger logger = LoggerFactory.getLogger(CreateGroupController.class);

	@Autowired
	private GroupDao groupDao;

	@RequestMapping(value="/group/create", method=RequestMethod.POST)
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupName", "isPublic");

		char isPublic = 'F';
		if ("public".equals(paramsList.get("isPublic")))
			isPublic = 'T';

		Group group = null;
		try {
			group = new Group(paramsList.get("groupName"), groupCaptainUserId, isPublic);
			while (true) {
				String groupId = RandomString.getRandomId(5);
				if (groupDao.readGroup(groupId) == null) {
					group.setGroupId(groupId);
					break;
				}
			}
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}

		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);

		if (!constraintViolation.isEmpty()) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			Forwarding.doForward(req, resp, "errorMessage", errorMessage, "/groups.jsp");
			return;
		}

		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}

		resp.sendRedirect("/groups.jsp");
	}
}
