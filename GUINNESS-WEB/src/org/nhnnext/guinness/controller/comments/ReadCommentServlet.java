package org.nhnnext.guinness.controller.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.CommentDao;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.COMMENT_READ)
public class ReadCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");
		String noteId = req.getParameter("noteId");

		// 세션이 없을 경우 루트화면으로 이동
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}

		CommentDao commentDao = new CommentDao();
		List<Comment> commentList = null;
		try {
			commentList = commentDao.readCommentListByNoteId(noteId);
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
		createJsonFile(commentList, resp);
	}

	public void createJsonFile(List<Comment> commentList, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();

		sb.append(gson.toJson(commentList));
		out.write(sb.toString());
		out.close();
	}
}
