package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired
	private NoteDao noteDao;
	
	@RequestMapping("/notelist/read")
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "targetDate");

		DateTime targetDate = new DateTime(paramsList.get("targetDate")).plusDays(1).minusSeconds(1);
		// 임시 : 캘린더가 만들어지기 전까지 임시로 20년 범위로 가져오기.
		// 추후에는 targetDate에 해당하는 하루치 노트들만 불러올 것.
		DateTime endDate = targetDate.minusYears(10);
		targetDate=targetDate.plusYears(10);
		// 임시 : 여기까지.
		PrintWriter out = resp.getWriter();
		List<Note> noteList = null;
		logger.debug("start endDate={} targetDate={}", endDate, targetDate);
		try {
			noteList = noteDao.readNoteList(paramsList.get("groupId"), endDate.toString(), targetDate.toString());
			logger.debug(noteList.toString());
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
		}
		out.print(new Gson().toJson(noteList));
		out.close();
	}
	
	@RequestMapping("/note/read")
	protected void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");
		Note note = null;
		try {
			note = noteDao.readNote(paramsList.get("noteId"));
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
		}
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(note));
		out.close();
	}
	
	@RequestMapping(value = "/note/create", method = RequestMethod.POST)
	protected void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "noteText",
				"targetDate");
		String targetDate = paramsList.get("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		if (paramsList.get("noteText").equals("")) {
			resp.sendRedirect("/g/" + paramsList.get("groupId"));
			return;
		}
		noteDao.createNote(new Note(paramsList.get("noteText"), targetDate, sessionUserId, paramsList.get("groupId")));
	}
}
