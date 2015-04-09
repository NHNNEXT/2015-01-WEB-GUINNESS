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
	private GroupDao groupDao = GroupDao.getInstance();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String groupId = req.getParameter("groupId");
		PrintWriter out = resp.getWriter();
		logger.debug("userId={}, groupId={}", userId, groupId);
		try {
			if (UserDao.getInstance().readUser(userId) == null) {
				logger.debug("등록되지 않은 사용자 입니다");
				out.print("unknownUser");
				out.close();
				return;
			}
			if (groupDao.checkJoinedGroup(userId, groupId)) {
				logger.debug("이미 가입된 사용자 입니다.");
				out.print("joinedUser");
				out.close();
				return;
			}
			groupDao.createGroupUser(userId, groupId);
			out.print(new Gson().toJson(groupDao.readGroupMember(groupId)));
			out.close();
		} catch (MakingObjectListFromJdbcException | SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
