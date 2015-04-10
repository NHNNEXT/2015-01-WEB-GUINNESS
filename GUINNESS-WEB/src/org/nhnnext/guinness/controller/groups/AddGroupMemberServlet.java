package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet("/group/add/member")
public class AddGroupMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(AddGroupMemberServlet.class);
	private GroupDao groupDao = GroupDao.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "groupId");

		PrintWriter out = resp.getWriter();
		logger.debug("userId={}, groupId={}", paramsList.get("userId"), paramsList.get("groupId"));
		try {
			if (UserDao.getInstance().readUser(paramsList.get("userId")) == null) {
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
		} catch (MakingObjectListFromJdbcException | SQLException | ClassNotFoundException e) {
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
