package org.nhnnext.guinness.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.dao.PreviewDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;

@Service
public class NoteService {
	@Resource
	private GroupDao groupDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;
	@Resource
	private PreviewDao previewDao;
	
	public void initNotes(Model model, String sessionUserId, String groupId) throws UnpermittedAccessGroupException {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException();
		}
		model.addAttribute("groupName", groupDao.readGroup(groupId).getGroupName());
		model.addAttribute("noteList", new Gson().toJson(getNoteListFromDao(null, groupId, null)));
	}
	
	public List<Map<String, Object>> reloadNotes(String userIds, String groupId, String noteTargetDate) {
		return getNoteListFromDao(noteTargetDate, groupId, userIds);
	}
	
	private List<Map<String, Object>> getNoteListFromDao(String date, String groupId, String userIds) {
		DateTime noteTargetDate = new DateTime(date).plusDays(1).minusSeconds(1);
		DateTime endDate = noteTargetDate.minusDays(1).plusSeconds(1);
		if (date == null) {
			endDate = noteTargetDate.minusYears(10);
			noteTargetDate = noteTargetDate.plusYears(10);
		}
		// targetDate의 포맷을 위한 변경
		List<Map<String, Object>> list = noteDao.readNoteListForMap(groupId, endDate.toString(), noteTargetDate.toString(), userIds);
		for (Map<String, Object> map : list)
			map.replace("noteTargetDate", map.get("noteTargetDate").toString());
		return list;
	}

	public Note readNote(String noteId) {
		return noteDao.readNote(noteId);
	}

	public void create(String sessionUserId, String groupId, String noteText, String noteTargetDate) {
		String noteId = ""+noteDao.createNote(new Note(noteText, noteTargetDate, new User(sessionUserId), new Group(groupId)));
		
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
		createPreview(noteId, groupId, extractText(noteText, '!'), extractText(noteText, '?'));
	}

	public ArrayList<String> extractText(String givenText, char ch) {
		givenText = givenText.trim();
		int flag = 0;
		int len = givenText.length();
		int beginIndex = 0;
		int endIndex = 0;
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < len; i++) {
			if(givenText.charAt(i) == ch) {
				flag++;
			}
			if(flag == 3 && beginIndex == 0) {
				beginIndex = i + 1;
			}
			if(flag == 6) {
				endIndex = i - 2;
				list.add(givenText.substring(beginIndex, endIndex));
				flag = 0;
				beginIndex = 0;
				endIndex = 0;
			}
		}
		return list;
	}

	public void update(String noteText, String noteId, String noteTargetDate) {
		noteDao.updateNote(noteText, noteId, noteTargetDate);		
	}

	public int delete(String noteId) {
		return noteDao.deleteNote(noteId);
	}

	public void updateForm(String noteId, Model model) {
		Note note = noteDao.readNote(noteId);
		Group group = groupDao.readGroup(note.getGroup().getGroupId());
		model.addAttribute("noteText", note.getNoteText());
		model.addAttribute("groupId", group.getGroupId());
		model.addAttribute("groupName", new Gson().toJson(group.getGroupName()));
		model.addAttribute("noteId", noteId);
	}

	public void createPreview(String noteId, String groupId, ArrayList<String> attentionList,
			ArrayList<String> questionList) {
		previewDao.create(new Note(noteId), new Group(groupId), attentionList, questionList);
	}
}
