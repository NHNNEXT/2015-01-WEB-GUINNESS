package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.comment.UnpermittedAccessPCommentException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.service.PCommentService;
import org.nhnnext.guinness.util.JSONResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pComments")
public class PCommentController {
	@Resource
	private PCommentService pCommentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(HttpSession session, @RequestParam String pId,
			@RequestParam String sameSenCount, @RequestParam String sameSenIndex, @RequestParam String pCommentText,
			@RequestParam String selectedText, @RequestParam String noteId) throws IOException{
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		if (pCommentText.equals("")) {
			return JSONResponseUtil.getJSONResponse("잘못된 요청입니다.", HttpStatus.PRECONDITION_FAILED);
		}
		Note note = new Note(noteId);
		PComment pComment = new PComment(pId, sameSenCount, sameSenIndex, pCommentText, selectedText, sessionUser, note);
		return JSONResponseUtil.getJSONResponse(pCommentService.create(sessionUser, note, pComment), HttpStatus.CREATED);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(@RequestParam String pId, @RequestParam String noteId) {
		return JSONResponseUtil.getJSONResponse(pCommentService.list(pId.replace("pId-", ""), noteId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/readCountByP", method = RequestMethod.GET)
	protected ResponseEntity<Object> readCountByP(@RequestParam String noteId) {
		return JSONResponseUtil.getJSONResponse(pCommentService.countByPGroupPCommnent(noteId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
	protected ResponseEntity<Object> update(HttpSession session, @PathVariable String pCommentId, @RequestParam String commentText) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			return JSONResponseUtil.getJSONResponse((PComment) pCommentService.update(pCommentId, commentText, sessionUser), HttpStatus.CREATED);
		} catch (UnpermittedAccessPCommentException e) {
			return JSONResponseUtil.getJSONResponse("수정할 권한이 없습니다.", HttpStatus.PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(HttpSession session, @PathVariable String pCommentId) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			pCommentService.delete(pCommentId, sessionUser);
			return JSONResponseUtil.getJSONResponse("코멘트가 삭제되었습니다.", HttpStatus.NO_CONTENT);
		} catch (UnpermittedAccessPCommentException e) {
			return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
	}
}
