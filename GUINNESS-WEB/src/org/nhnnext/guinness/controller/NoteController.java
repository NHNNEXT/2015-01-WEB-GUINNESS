package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired
	private GroupDao groupDao;

	@Autowired
	private NoteDao noteDao;

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void setNoteDao(NoteDao noteDao) {
		this.noteDao = noteDao;
	}

	@RequestMapping(value = "/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			model.addAttribute("errorMessage", "비정상적 접근시도.");
			return "illegal";
		}
		String groupName = groupDao.readGroup(groupId).getGroupName();
		List<Note> noteList = getNoteListFromDao(getFormattedCurrentDate(), groupId, null);
		model.addAttribute("noteList", new Gson().toJson(noteList));
		model.addAttribute("groupName", new Gson().toJson(groupName));
		return "notes";
	}
	
	@RequestMapping("/note/list")
	protected @ResponseBody List<Note> reloadNoteList(WebRequest req) throws IOException {
		String userIds = req.getParameter("checkedUserId");
		String groupId = req.getParameter("groupId");
		List<Note> noteList = getNoteListFromDao(req.getParameter("targetDate"), groupId, userIds);
		return noteList;
	}

	private String getFormattedCurrentDate() {
		DateTime now = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		DateTime targetDate = new DateTime(formatter.print(now)).plusDays(1).minusSeconds(1);
		return targetDate.toString();
	}

	private List<Note> getNoteListFromDao(String date, String groupId, String userIds) {
		if (userIds == "")
			return new ArrayList<Note>();
		
		DateTime targetDate = new DateTime(date).plusDays(1).minusSeconds(1);
		DateTime endDate = targetDate.minusYears(10);
		targetDate = targetDate.plusYears(10);
		List<Note> noteList = noteDao.readNoteList(groupId, endDate.toString(), targetDate.toString(), userIds);
		return noteList;
	}

	@RequestMapping("/note/read")
	protected ModelAndView show(WebRequest req) throws IOException {
		String noteId = req.getParameter("noteId");
		Note note = null;
		try {
			note = noteDao.readNote(noteId);
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
		return new ModelAndView("jsonView").addObject("jsonData", note);
	}

	@RequestMapping(value = "/note/create", method = RequestMethod.POST)
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String targetDate = req.getParameter("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		if (noteText.equals("")) {
			return "redirect:/notes/editor?groupId=" + groupId;
		}
		noteDao.createNote(new Note(noteText, targetDate, sessionUserId, groupId));
		return "redirect:/g/" + groupId;
	}
	
	@RequestMapping(value = "/note/delete/{noteId}/{userId}")
	protected ModelAndView delete(@PathVariable String noteId, @PathVariable String userId) {
		logger.debug("userId : " + userId + " noteId : " + noteId);
		if(noteDao.deleteNote(noteId, userId) == 1) {
			return new ModelAndView("jsonView", "jsonData", "success");
		}
		
		return new ModelAndView("jsonView", "jsonData", "fail");
	}
}
