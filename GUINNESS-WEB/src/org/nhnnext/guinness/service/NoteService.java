package org.nhnnext.guinness.service;

import java.util.List;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
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
	
	public Note readNote(String noteId) {
		return noteDao.readNote(noteId);
	}

	public void create(String sessionUserId, String groupId, String noteText, String noteTargetDate) throws UnpermittedAccessGroupException {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException();
		}
		String noteId = ""+noteDao.createNote(new Note(noteText, noteTargetDate, new User(sessionUserId), new Group(groupId)));
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
	}

	public void update(String noteText, String noteId, String noteTargetDate) {
		noteDao.updateNote(noteText, noteId, noteTargetDate);
		previewService.updatePreview(noteId, noteText);
	}

	public int delete(String noteId) {
		return noteDao.deleteNote(noteId);
	}
	
	public boolean checkJoinedGroup(String groupId, String sessionUserId) throws UnpermittedAccessGroupException {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		return true;
	}
}
