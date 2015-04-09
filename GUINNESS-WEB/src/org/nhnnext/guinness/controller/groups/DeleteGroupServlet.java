package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.ServletRequestUtil;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletUrl.GROUP_DELETE)
public class DeleteGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupDao groupDao = GroupDao.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sessionUserId = ServletRequestUtil.checkSessionAttribute(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId");
		
		try {
			Group group = groupDao.readGroup(paramsList.get("groupId"));
			if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
				Forwarding.doForward(req, resp, "errorMessage", "삭제 권한 없음", "/groups.jsp");
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException | ClassNotFoundException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
