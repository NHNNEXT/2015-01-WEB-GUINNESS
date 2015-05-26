package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.service.PCommentService;
import org.nhnnext.guinness.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pComments")
public class PCommentController {
	private static final Logger logger = LoggerFactory.getLogger(PCommentController.class);
	
	@Resource
	private PCommentService pCommentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(HttpSession session, @RequestParam String pId,
			@RequestParam String sameSenCount, @RequestParam String sameSenIndex, @RequestParam String pCommentText,
			@RequestParam String selectedText, @RequestParam String noteId) throws IOException {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		if (pCommentText.equals("")) {
			return new JsonResult().setSuccess(false);
		}
		try {
			Note note = new Note(noteId);
			PComment pComment = new PComment(pId, sameSenCount, sameSenIndex, pCommentText, selectedText, sessionUser, note);
			return new JsonResult().setSuccess(true).setObject(pCommentService.create(sessionUser, note, pComment));
		} catch (UnpermittedAccessGroupException e) {
			return new JsonResult().setSuccess(false).setMessage(e.getMessage()).setLocationWhenFail("/illegal");
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected @ResponseBody JsonResult list(@PathVariable String pId, @PathVariable String noteId) {
		return new JsonResult().setSuccess(true).setMapValues(pCommentService.list(pId, noteId));
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult update(@PathVariable String commentId, @RequestParam String commentText) {
		return new JsonResult().setSuccess(true).setObject((PComment) pCommentService.update(commentId, commentText));
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String commentId) {
		pCommentService.delete(commentId);
		return new JsonResult().setSuccess(true);
	}
}
