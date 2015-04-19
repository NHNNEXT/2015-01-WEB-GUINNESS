package org.nhnnext.guinness.controller.comments;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.dao.CommentDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateCommentController {
	private static final Logger logger = LoggerFactory.getLogger(CreateCommentController.class);

	@Autowired
	private CommentDao commentDao;
	
	@RequestMapping(value="/comment/create", method=RequestMethod.POST)
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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
}
