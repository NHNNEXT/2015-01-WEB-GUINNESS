package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.service.CommentService;
import org.nhnnext.guinness.util.JsonResult;
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
	protected @ResponseBody JsonResult create(HttpSession session, @RequestParam String commentText, @RequestParam String noteId) throws IOException{
		SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
		
		Note note = new Note(noteId);
		if (commentText.equals(""))
			return new JsonResult().setSuccess(false);
		Comment comment = new Comment(commentText, sessionUser, note);
		try {
			return new JsonResult().setSuccess(true).setMapValues(commentService.create(sessionUser, note, comment));
		} catch (UnpermittedAccessGroupException e) {
			return new JsonResult().setSuccess(false).setMessage(e.getMessage()).setLocationWhenFail("/illegal");
		}
	}

	@RequestMapping("/{noteId}")
	protected @ResponseBody JsonResult list(@PathVariable String noteId) {
		return new JsonResult().setSuccess(true).setMapValues(commentService.list(noteId));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult update(@PathVariable String commentId, @RequestParam String commentText) {
		return new JsonResult().setSuccess(true).setObject(commentService.update(commentId, commentText));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String commentId) {
		commentService.delete(commentId);
		return new JsonResult().setSuccess(true);
	}
}
