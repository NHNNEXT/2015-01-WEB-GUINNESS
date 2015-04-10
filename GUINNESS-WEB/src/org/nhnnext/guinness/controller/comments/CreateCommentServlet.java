package org.nhnnext.guinness.controller.comments;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.CommentDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;

@WebServlet(WebServletUrl.COMMENT_CREATE)
public class CreateCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "commentText", "commentType",
				"noteId");
		if (paramsList.get("commentText").equals("")) {
			return;
		}

		try {
			Comment comment = new Comment(paramsList.get("commentText"), paramsList.get("commentType"), sessionUserId,
					paramsList.get("noteId"));
			CommentDao.getInstance().createcomment(comment);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
