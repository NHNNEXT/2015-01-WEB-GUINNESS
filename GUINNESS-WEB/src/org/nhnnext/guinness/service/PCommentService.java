package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.dao.PCommentDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.springframework.stereotype.Service;

@Service
public class PCommentService {
	@Resource
	private PCommentDao pCommentDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;
	@Resource
	private GroupDao groupDao;

	public List<Map<String, Object>> create(SessionUser sessionUser, Note note, PComment pComment) throws UnpermittedAccessGroupException {
		Group group = groupDao.readGroupByNoteId(note.getNoteId());
		if (!groupDao.checkJoinedGroup(sessionUser.getUserId(), group.getGroupId())) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		pCommentDao.createPComment(pComment);
		noteDao.increaseCommentCount(pComment.getNote().getNoteId());
		//createAlarm(pComment);
		return pCommentDao.readPCommentListByNoteId(pComment.getNote().getNoteId());
	}

//	private void createAlarm(PComment pComment) {
//		Note note = pComment.getNote();
//		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
//		
//		if (!pComment.checkWriter(noteWriter)) {
//			alarmDao.createNewNotes(new Alarm(createAlarmId(), "P", pComment.getUser(), noteWriter, note));
//		}
//	}
//
//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if(alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}

	public List<Map<String, Object>> list(String noteId) {
		return pCommentDao.readPCommentListByNoteId(noteId);
	}

	public Object update(String commentId, String commentText) {
		pCommentDao.updatePComment(commentId, commentText);
		return pCommentDao.readPCommentByPCommentId(commentId);
	}

	public void delete(String pCommentId) {
		noteDao.decreaseCommentCount(pCommentId);
		pCommentDao.deletePComment(pCommentId);
	}

	public void updateParagraphId(List<Map<String, Object>> pCommentList) {
		for(Map<String, Object> pComment:pCommentList){
			pCommentDao.updatePId(pComment.get("pCommentId").toString(), pComment.get("pId").toString());
		}
	}
}
