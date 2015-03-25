package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletURL.GROUP_DELETE)
public class DeleteGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute(ParameterKey.SESSION_USERID);
		String groupId = (String) req.getParameter("groupId");
		GroupDao groupDao = new GroupDao();
		Group group;
		try {
			group = groupDao.findByGroupId(groupId);
			if(!group.getGroupCaptainUserId().equals(userId)){
				String errorMessage = "삭제 권한 없음";
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
				rd.forward(req, resp);
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups.jsp");
			
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 접근 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 연결 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute(ParameterKey.SESSION_USERID);
		String groupId = (String) req.getParameter("groupId");
		GroupDao groupDao = new GroupDao();
		Group group;
		try {
			group = groupDao.findByGroupId(groupId);
			if(!group.getGroupCaptainUserId().equals(userId)){
				String errorMessage = "삭제 권한 없음";
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
				rd.forward(req, resp);
				return;
			}
			groupDao.deleteGroup(group);
			
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 접근 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			String errorMessage = "데이터베이스 연결 실패";
			req.setAttribute("errorMessage", errorMessage);
			RequestDispatcher rd = req.getRequestDispatcher("/groups.jsp");
			rd.forward(req, resp);
			return;
		}

	}
}
