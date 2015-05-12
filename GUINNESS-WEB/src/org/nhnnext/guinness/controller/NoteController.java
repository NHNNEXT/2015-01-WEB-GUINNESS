package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
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
	protected @ResponseBody JsonResult reloadNoteList(WebRequest req) {
		String userIds = req.getParameter("checkedUserId");
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate"); 
		if("undefined".equals(targetDate))
			targetDate = null;
		if(userIds == null || groupId == null) {
			return new JsonResult().setSuccess(false).setMapValues(new ArrayList<Map<String, Object>>());
		}
		return new JsonResult().setSuccess(true).setMapValues(getNoteListFromDao(targetDate, groupId, userIds));
	}
	
	private List<Map<String, Object>> getNoteListFromDao(String date, String groupId, String userIds) {
		DateTime targetDate = new DateTime(date).plusDays(1).minusSeconds(1);
		DateTime endDate = targetDate.minusDays(1).plusSeconds(1);
		if (date == null) {
			endDate = targetDate.minusYears(10);
			targetDate = targetDate.plusYears(10);
		}
		// targetDate의 포맷을 위한 변경
		List<Map<String, Object>> list = noteDao.readNoteListForMap(groupId, endDate.toString(), targetDate.toString(), userIds);
		for (Map<String, Object> map : list)
			map.replace("targetDate", map.get("targetDate").toString());
		return list;
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
		
		String noteId = ""+noteDao.createNote(new Note(noteText, targetDate, new User(sessionUserId), new Group(groupId)));
		
		String alarmId = null;
		Alarm alarm = null;
		String noteWriter = noteDao.readNote(noteId).getUser().getUserId();
		List<User> groupMembers = groupDao.readGroupMember(groupId);
		for (User reader : groupMembers) {
			if (reader.getUserId().equals(sessionUserId)) {
				continue;
			}
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (!alarmDao.isExistAlarmId(alarmId)) {
					alarm = new Alarm(alarmId, "N", new User(noteWriter), reader, new Note(noteId));
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
