package org.nhnnext.guinness.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.group.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteService {
	@Resource
	private GroupDao groupDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;
	@Resource
	private PreviewService previewService;
	@Resource
	private TempNoteService tempNoteService;
	@Resource
	private PCommentService pCommentService;

	public Note readNote(String sessionUserId, String noteId) {
		return noteDao.readNote(noteId);
	}

	public void create(String sessionUserId, String groupId, String noteText, String noteTargetDate, String tempNoteId) {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException();
		}
		String noteId = "" + noteDao.createNote(new Note(noteText, noteTargetDate, new User(sessionUserId), new Group(groupId)));
		String alarmId = null;
		Alarm alarm = null;
		SessionUser sessionUser = noteDao.readNote(noteId).getUser().createSessionUser();
		List<User> groupMembers = groupDao.readGroupMember(groupId);
		for (User reader : groupMembers) {
			if (reader.getUserId().equals(sessionUserId)) {
				continue;
			}
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (!alarmDao.isExistAlarmId(alarmId)) {
					alarm = new Alarm(alarmId, "N", sessionUser, reader, new Note(noteId));
					break;
				}
			}
			alarmDao.createNewNotes(alarm);
		}
		previewService.createPreview(noteId, groupId, noteText);
		tempNoteService.delete(Long.parseLong(tempNoteId));
	}

	public void update(String noteText, String noteId, String noteTargetDate, List<Map<String, Object>> pCommentList) {
		noteDao.updateNote(noteText, noteId, noteTargetDate);
		previewService.updatePreview(noteId, noteText);
		pCommentService.updateParagraphId(pCommentList);
	}

	public int delete(String noteId) {
		alarmDao.deleteGroupByNoteId(noteId);
		return noteDao.deleteNote(noteId);
	}

	public boolean checkJoinedGroup(String groupId, String sessionUserId) {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		return true;
	}

	public List<Boolean> readNullDay(String groupId, String lastDate) throws ParseException {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime date = dtf.parseDateTime(lastDate);
		date = date.withTime(0, 0, 0, 0);
		date = date.withDayOfMonth(1);
		String startDate = dtf.print(date);
		List<String> list = noteDao.readNotesByDate(groupId, startDate, lastDate);
		Collections.sort(list);
		List<Boolean> nullDay = new ArrayList<Boolean>();
		for(int i=0;i < dtf.parseDateTime(lastDate).getDayOfMonth(); i++){
			nullDay.add(true);
		}
		for (String string : list) {
			nullDay.set(dtf.parseDateTime(string).getDayOfMonth()-1, false);
		}
		return nullDay;
	}
}
