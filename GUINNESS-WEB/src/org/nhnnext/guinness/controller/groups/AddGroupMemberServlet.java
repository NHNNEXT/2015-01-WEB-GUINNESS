package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.controller.notes.ReadNoteListServlet;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.model.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.GROUP_ADD_MEMBER)
public class AddGroupMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDao userDao = new UserDao();
		GroupDao groupDao = new GroupDao();
		String userId = req.getParameter("userId");
		String groupId = req.getParameter("groupId");
		Gson gson = new Gson();
		PrintWriter out = resp.getWriter();
		logger.debug("userId={}, groupId={}", userId, groupId);
		try {
			if (!userDao.checkExistUserId(userId)) {
				logger.debug("등록되지 않은 사용자 입니다");
				Forwarding.forwardForError(req, resp, "errorMessage", "등록되지 않은 사용자 입니다", "/exception.jsp");
				return;
			}
			if (groupDao.checkJoinedGroup(userId, groupId)) {
				logger.debug("이미 가입된 사용자 입니다.");
				Forwarding.forwardForError(req, resp, "errorMessage", "이미 가입된 사용자 입니다.", "/exception.jsp");
				return;
			}
			groupDao.createGroupUser(userId, groupId);
			out.print(gson.toJson(groupDao.readUserListByGroupId(groupId)));
			out.close();
		} catch (MakingObjectListFromJdbcException | SQLException e) {
			Forwarding.forwardForError(req, resp, "errorMessage", "데이터 베이스 접근이 잘못되었습니다.", "/exception.jsp");
			return;
		}
	}
}
