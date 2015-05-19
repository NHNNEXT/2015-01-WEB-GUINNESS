package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.CommentService;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comments")
public class CommentController {
	@Resource
	private CommentService commentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(HttpSession session, @RequestParam String commentText, @RequestParam String commentType, @RequestParam String noteId) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (commentText.equals(""))
			return new JsonResult().setSuccess(false);

		Comment comment = new Comment(commentText, commentType, new User(sessionUserId), new Note(noteId));
		return new JsonResult().setSuccess(true).setMapValues(commentService.create(comment));
	}

	@RequestMapping("/{noteId}")
	protected @ResponseBody JsonResult list(@PathVariable String noteId) {
		return new JsonResult().setSuccess(true).setMapValues(commentService.list(noteId));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult update(@PathVariable String commentId, @RequestParam String commentText) {
		return new JsonResult().setSuccess(true).setObject(
				(Comment) commentService.update(commentId, commentText));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String commentId) {
		commentService.delete(commentId);
		return new JsonResult().setSuccess(true);
	}
}
