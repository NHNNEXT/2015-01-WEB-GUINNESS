package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletURL.GROUP_DELETE)
public class DeleteGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute(ParameterKey.SESSION_USERID);
		String groupId = req.getParameter("groupId");
		GroupDao groupDao = new GroupDao();
		Group group;
		try {
			group = groupDao.findByGroupId(groupId);
			if (!group.getGroupCaptainUserId().equals(userId)) {
				Forwarding.forwardForError(req, resp, "errorMessage", "삭제 권한 없음", "/groups.jsp");
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, "errorMessage", "접속이 원활하지 않습니다.", "/exception.jsp");
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute(ParameterKey.SESSION_USERID);
		String groupId = req.getParameter("groupId");
		GroupDao groupDao = new GroupDao();
		Group group;
		try {
			group = groupDao.findByGroupId(groupId);
			if (!group.getGroupCaptainUserId().equals(userId)) {
				Forwarding.forwardForError(req, resp, "errorMessage", "삭제 권한 없음", "/groups.jsp");
				return;
			}
			groupDao.deleteGroup(group);

		} catch (SQLException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, null, null, "/exception.jsp");
			return;
		}
	}
}
