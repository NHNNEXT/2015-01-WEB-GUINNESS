package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.markdown4j.Markdown4jProcessor;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.service.NoteService;
import org.nhnnext.guinness.util.DateTimeUtil;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	
	@Resource
	private NoteService noteService;
	@Resource
	private GroupService groupService;

	@RequestMapping("/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException, UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		noteService.initNotes(model, sessionUserId, groupId);
		return "notes";
	}

	@RequestMapping("/notes/reload")
	protected @ResponseBody JsonResult reloadNoteList(@RequestParam("checkedUserId") String userIds, 
			@RequestParam String noteTargetDate, @RequestParam String groupId, WebRequest req) {
		if("undefined".equals(noteTargetDate))
			noteTargetDate = null;
		if(userIds == null || groupId == null) {
			return new JsonResult().setSuccess(false).setMapValues(new ArrayList<Map<String, Object>>());
		}
		return new JsonResult().setSuccess(true).setMapValues(noteService.reloadNotes(userIds, groupId, noteTargetDate));
	}

	@RequestMapping("/notes/{noteId}")
	protected @ResponseBody JsonResult show(@PathVariable String noteId) {
		return new JsonResult().setSuccess(true).setObject(noteService.readNote(noteId));
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(@RequestParam String groupId, @RequestParam String noteText, 
			@RequestParam String noteTargetDate, HttpSession session, Model model) throws IOException, UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/g/" + groupId;
		}
		noteService.create(sessionUserId, groupId, noteText, DateTimeUtil.addCurrentTime(noteTargetDate));
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(@RequestParam String groupId, @RequestParam String noteId, 
			@RequestParam String noteTargetDate, @RequestParam String noteText) {
		noteService.update(noteText, noteId, DateTimeUtil.addCurrentTime(noteTargetDate));
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String noteId) {
		logger.debug(" noteId : " + noteId);
		if (noteService.delete(noteId) == 1) {
			return new JsonResult().setSuccess(true).setObject(noteId);
		}
		return new JsonResult().setSuccess(false);
	}
	
	@RequestMapping("/notes/editor/g/{groupId}")
	private String createForm(@PathVariable String groupId, Model model, HttpSession session) throws UnpermittedAccessGroupException, IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		noteService.checkJoinedGroup(groupId, sessionUserId);
		model.addAttribute(groupService.readGroup(groupId));
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable String noteId, Model model) {
		noteService.updateForm(noteId, model);
		return "editor";
	}
	
	@RequestMapping(value="/notes/editor/preview", method=RequestMethod.POST)
	private @ResponseBody JsonResult preview(@RequestParam String markdown) throws IOException {
		String html = new Markdown4jProcessor().process(markdown);
		return new JsonResult().setSuccess(true).setMessage(html);
	}
}
