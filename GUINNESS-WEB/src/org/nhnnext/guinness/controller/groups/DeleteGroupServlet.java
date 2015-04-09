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
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");
		String groupId = req.getParameter("groupId");
		
		try {
			Group group = groupDao.readGroup(groupId);
			if (!group.getGroupCaptainUserId().equals(userId)) {
				Forwarding.doForward(req, resp, "errorMessage", "삭제 권한 없음", "/groups.jsp");
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
