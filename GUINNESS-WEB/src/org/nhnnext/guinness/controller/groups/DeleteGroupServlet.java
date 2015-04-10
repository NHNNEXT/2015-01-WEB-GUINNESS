package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.exception.SessionUserIdNotFoundException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(WebServletUrl.GROUP_DELETE)
public class DeleteGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeleteGroupServlet.class);
	private GroupDao groupDao = GroupDao.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId");
		
		try {
			String sessionUserId = ServletRequestUtil.checkSessionAttribute(req, resp);
			Group group = groupDao.readGroup(paramsList.get("groupId"));
			if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
				Forwarding.doForward(req, resp, "errorMessage", "삭제 권한 없음", "/groups.jsp");
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException | ClassNotFoundException | MakingObjectListFromJdbcException e) {
			logger.error(e.getClass().getName() + "에서 exception 발생", e);
			Forwarding.forwardForException(req, resp);
			return;
		} catch (SessionUserIdNotFoundException e) {
			return;
		}
	}
}
