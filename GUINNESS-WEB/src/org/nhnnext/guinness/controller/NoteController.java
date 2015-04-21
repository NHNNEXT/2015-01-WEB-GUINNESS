package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired
	private NoteDao noteDao;
	
	@Autowired
	private GroupDao groupDao;

	@RequestMapping(value = "/g/{url}")
	protected String notesRouter(@PathVariable String url, HttpSession session, Model model) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return "redirect:/";
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!groupDao.checkJoinedGroup(sessionUserId, url)) {
			model.addAttribute("errorMessage", "비정상적 접근시도.");
			return "illegal";
		}
		return "notes";
	}
	
	@RequestMapping("/notelist/read")
	protected ModelAndView readNoteList(HttpServletRequest req) throws IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "targetDate");
		DateTime targetDate = new DateTime(paramsList.get("targetDate")).plusDays(1).minusSeconds(1);
		// 임시 : 캘린더가 만들어지기 전까지 임시로 20년 범위로 가져오기.
		// TODO 추후에는 targetDate에 해당하는 하루치 노트들만 불러올 것.
		DateTime endDate = targetDate.minusYears(10);
		targetDate=targetDate.plusYears(10);
		// 임시 : 여기까지.
		List<Note> noteList = null;
		try {
			noteList = noteDao.readNoteList(paramsList.get("groupId"), endDate.toString(), targetDate.toString());
		} catch (Exception e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
		return new ModelAndView("jsonView").addObject("jsonData", noteList);
	}
	
	@RequestMapping("/note/read")
	protected ModelAndView show(HttpServletRequest req) throws IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");
		Note note = null;
		try {
			note = noteDao.readNote(paramsList.get("noteId"));
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
		return new ModelAndView("jsonView").addObject("jsonData", note);
	}
	
	@RequestMapping(value = "/note/create", method = RequestMethod.POST)
	protected String create(HttpSession session, HttpServletRequest req) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return "redirect:/";
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "noteText",
				"targetDate");
		String targetDate = paramsList.get("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		if (paramsList.get("noteText").equals("")) {
			return "redirect:/g/"+paramsList.get("groupId");
		}
		noteDao.createNote(new Note(paramsList.get("noteText"), targetDate, sessionUserId, paramsList.get("groupId")));
		return "redirect:/g/"+paramsList.get("groupId");
	}
}
