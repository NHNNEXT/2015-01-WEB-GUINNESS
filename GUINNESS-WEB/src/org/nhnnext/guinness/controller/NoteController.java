package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.service.NoteService;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	
	@Resource
	private NoteService noteService;
	@Resource
	private GroupService groupService;

	@RequestMapping(value = "/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException, UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		noteService.initNotes(model, sessionUserId, groupId);
		return "notes";
	}

	@RequestMapping("/api/notes")
	protected @ResponseBody JsonResult reloadNoteList(WebRequest req) {
		String userIds = req.getParameter("checkedUserId");
		String groupId = req.getParameter("groupId");
		String noteTargetDate = req.getParameter("noteTargetDate"); 
		
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
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String noteTargetDate = req.getParameter("noteTargetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		if (noteText.equals("")) {
			return "redirect:/editor/g/" + groupId;
		}
		noteService.create(sessionUserId, groupId, noteText, noteTargetDate);
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(WebRequest req) {
		String groupId = req.getParameter("groupId");
		String noteId = req.getParameter("noteId");
		String noteText = req.getParameter("noteText");
		String noteTargetDate = req.getParameter("noteTargetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		noteService.update(noteText, noteId, noteTargetDate);
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
	
	@RequestMapping(value = "/editor/g/{groupId}")
	private String createForm(@PathVariable String groupId, Model model) {
		groupService.readGroup(model, groupId);
		return "editor";
	}

	@RequestMapping(value = "/editor/{noteId}")
	private String updateEditor(@PathVariable String noteId, Model model) {
		noteService.updateForm(noteId, model);
		return "editor";
	}
}
