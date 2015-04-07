package org.nhnnext.guinness.controller.comments;

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
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.CommentDao;

@WebServlet(WebServletUrl.COMMENT_CREATE)
public class CreateCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		String commentText = req.getParameter("commentText");
		String commentType = req.getParameter("commentType");
		String noteId = req.getParameter("noteId");

		//TODO 비어있으면 json 빈문자
		//

		Comment comment = new Comment(commentText, commentType, userId, noteId);
		CommentDao commentDAO = new CommentDao();
		try {
			commentDAO.createcomment(comment);
		} catch (SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, null, null, "/exception.jsp");
			return;
		}
	}
}
