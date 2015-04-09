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

		if (commentText.equals("")) {
			return;
		}

		Comment comment = new Comment(commentText, commentType, userId, noteId);
		try {
			CommentDao.getInstance().createcomment(comment);
		} catch (SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
