package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.RandomFactory;
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

import com.google.gson.Gson;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	
	@Resource
	private GroupDao groupDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;

	@RequestMapping(value = "/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException, UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException();
		}
		model.addAttribute("groupName", groupDao.readGroup(groupId).getGroupName());
		model.addAttribute("noteList", new Gson().toJson(getNoteListFromDao(null, groupId, null)));
		return "notes";
	}

	@RequestMapping("/api/notes")
	protected @ResponseBody List<Note> reloadNoteList(WebRequest req) {
		String userIds = req.getParameter("checkedUserId");
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate"); 
		if("undefined".equals(targetDate))
			targetDate = null;
		if(userIds == null || groupId == null) {
			return new ArrayList<Note>();
		}
		// TODO js에서 날짜처리하는 부분을 문자열 처리가 아닌 숫자데이터 처리로 변경하기 
		// 현재는 js에서 받아서 파싱 할 때 생성일자 데이터를 형식에 맞게 보내기 위해서 Note객체로 보내준다.
		return getNoteListFromDao(targetDate, groupId, userIds);
	}

	private List<Note> getNoteListFromDao(String date, String groupId, String userIds) {
		DateTime targetDate = new DateTime(date).plusDays(1).minusSeconds(1);
		DateTime endDate = targetDate.minusDays(1).plusSeconds(1);
		if (date == null) {
			endDate = targetDate.minusYears(10);
			targetDate = targetDate.plusYears(10);
		}
		return noteDao.readNoteList(groupId, endDate.toString(), targetDate.toString(), userIds);
	}

	@RequestMapping("/notes/{noteId}")
	protected @ResponseBody JsonResult show(@PathVariable String noteId) {
		return new JsonResult().setSuccess(true).setObject(noteDao.readNote(noteId));
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String targetDate = req.getParameter("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		if (noteText.equals("")) {
			return "redirect:/notes/editor?groupId=" + groupId;
		}
		
		String noteId = ""+noteDao.createNote(new Note(noteText, targetDate, sessionUserId, groupId));
		
		String alarmId = null;
		Alarm alarm = null;
		String noteWriter = noteDao.readNote(noteId).getUserId();
		List<User> groupMembers = groupDao.readGroupMember(groupId);
		for (User user : groupMembers) {
			if (user.getUserId().equals(sessionUserId)) {
				continue;
			}
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (alarmDao.read(alarmId) == null) {
					alarm = new Alarm(alarmId, user.getUserId(), noteWriter, noteId, "에 새 글을 작성하였습니다.", "N");
					break;
				}
			}
			alarmDao.create(alarm);
		}
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(WebRequest req) {
		String groupId = req.getParameter("groupId");
		String noteId = req.getParameter("noteId");
		String noteText = req.getParameter("noteText");
		noteDao.updateNote(noteText, noteId);
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String noteId) {
		logger.debug(" noteId : " + noteId);
		if (noteDao.deleteNote(noteId) == 1) {
			return new JsonResult().setSuccess(true).setObject(noteId);
		}
		return new JsonResult().setSuccess(false);
	}
}
