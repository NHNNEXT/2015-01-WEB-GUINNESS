package org.nhnnext.guinness.controller.notes;

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
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletUrl.NOTELIST)
public class NotesRouter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		try {
			String url = req.getRequestURI().split("/")[2];
			if (!GroupDao.getInstance().checkJoinedGroup(userId, url)) {
				Forwarding.doForward(req, resp, "errorMessage", "비정상적 접근시도.", "/illegal.jsp");
				return;
			}
		} catch (SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
		Forwarding.doForward(req, resp, "/notes.jsp");
	}
}
