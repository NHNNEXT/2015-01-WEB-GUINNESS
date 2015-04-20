package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.CommentDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentDao commentDao;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	protected void create(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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
			commentDao.createComment(comment);
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
	
	@RequestMapping(value="/read")
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");

		List<Comment> commentList = null;
		try {
			commentList = commentDao.readCommentListByNoteId(paramsList.get("noteId"));
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(commentList));
		out.close();
	}
}
