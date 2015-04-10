package org.nhnnext.guinness.controller.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.CommentDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.COMMENT_READ)
public class ReadCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ReadCommentServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");

		List<Comment> commentList = null;
		try {
			commentList = CommentDao.getInstance().readCommentListByNoteId(paramsList.get("noteId"));
		} catch (SQLException | MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error(e.getClass().getSimpleName() + "에서 exception 발생", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(commentList));
		out.close();
	}
}
