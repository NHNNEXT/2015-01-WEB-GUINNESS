package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;

@WebServlet("/g/*")
public class NotesRouter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		try {
			String url = req.getRequestURI().split("/")[2];
			if (!GroupDao.getInstance().checkJoinedGroup(sessionUserId, url)) {
				Forwarding.doForward(req, resp, "errorMessage", "비정상적 접근시도.", "/illegal.jsp");
				return;
			}
			Forwarding.doForward(req, resp, "/notes.jsp");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
