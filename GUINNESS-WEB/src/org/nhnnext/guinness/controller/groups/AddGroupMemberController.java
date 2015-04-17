package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
public class AddGroupMemberController {
	private static final Logger logger = LoggerFactory.getLogger(AddGroupMemberController.class);
	
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/group/add/member", method=RequestMethod.POST)
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "groupId");

		PrintWriter out = resp.getWriter();
		logger.debug("userId={}, groupId={}", paramsList.get("userId"), paramsList.get("groupId"));
		try {
			if (userDao.readUser(paramsList.get("userId")) == null) {
				logger.debug("등록되지 않은 사용자 입니다");
				out.print("unknownUser");
				out.close();
				return;
			}
			if (groupDao.checkJoinedGroup(paramsList.get("userId"), paramsList.get("groupId"))) {
				logger.debug("이미 가입된 사용자 입니다.");
				out.print("joinedUser");
				out.close();
				return;
			}
			groupDao.createGroupUser(paramsList.get("userId"), paramsList.get("groupId"));
			out.print(new Gson().toJson(groupDao.readGroupMember(paramsList.get("groupId"))));
			out.close();
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
