package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	protected ModelAndView create(WebRequest req, HttpSession session) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new ModelAndView("redirect:/");
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String commentText = req.getParameter("commentText");
		String commentType = req.getParameter("commentType");
		String noteId = req.getParameter("noteId");

		try {
			if (!commentText.equals("")) {
				Comment comment = new Comment(commentText, commentType, sessionUserId, noteId);
				commentDao.createComment(comment);
			}
			List<Comment> commentList = commentDao.readCommentListByNoteId(noteId);
			ModelAndView mav = new ModelAndView("jsonView");
			mav.addObject("jsonData", commentList);
			return mav;
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			return new ModelAndView("/WEB-INF/jsp/exception.jsp");
		}
	}

	@RequestMapping("")
	protected ModelAndView list(WebRequest req) {
		String noteId = req.getParameter("noteId");
		List<Comment> commentList = null;
		try {
			commentList = commentDao.readCommentListByNoteId(noteId);
			ModelAndView mav = new ModelAndView("jsonView");
			mav.addObject("jsonData", commentList);
			return mav;
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			return new ModelAndView("/WEB-INF/jsp/exception.jsp");
		}
	}

	@RequestMapping("/{commentId}/delete")
	protected ModelAndView delete(@PathVariable String commentId) {
		commentDao.deleteComment(commentId);
		return new ModelAndView();
	}

	@RequestMapping(value = "/{commentId}/{commentText}", method = RequestMethod.PUT)
	protected ModelAndView update(@PathVariable String commentId, @PathVariable String commentText) {
		commentDao.updateComment(commentId, commentText);
		return new ModelAndView();
	}
}
