package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.dao.PCommentDao;
import org.nhnnext.guinness.exception.comment.UnpermittedAccessPCommentException;
import org.nhnnext.guinness.exception.group.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PCommentService {
	@Resource
	private PCommentDao pCommentDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;
	@Resource
	private GroupDao groupDao;

	public PComment create(SessionUser sessionUser, Note note, PComment pComment) {
		Group group = groupDao.readGroupByNoteId(note.getNoteId());
		if (!groupDao.checkJoinedGroup(sessionUser.getUserId(), group.getGroupId())) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
		String pCommentId = pCommentDao.createPComment(pComment);
		noteDao.increaseCommentCount(pComment.getNote().getNoteId());
		
		if(!sessionUser.getUserId().equals(noteWriter.getUserId())){
			alarmDao.createNewNotes(new Alarm(createAlarmId(), "P", pComment.getSessionUser(), noteWriter, pComment.getNote()));
		}
		return pCommentDao.readByPCommentId(pCommentId);
	}

	private String createAlarmId() {
		String alarmId = RandomFactory.getRandomId(10);
		if(alarmDao.isExistAlarmId(alarmId)) {
			return createAlarmId();
		}
		return alarmId;
	}

	public List<PComment> list(String pId, String noteId) {
		return pCommentDao.readListByPId(pId, noteId);
	}
	
	public List<Map<String, Object>> listByNoteId( String noteId) {
		return pCommentDao.readListByNoteId(noteId);
	}
	
	public List<Map<String, Object>> countByPGroupPCommnent(String noteId) {
		return pCommentDao.countByPGroupPCommnent(noteId);
	}

	public Object update(String pCommentId, String pCommentText, SessionUser sessionUser) {
		PComment pComment = pCommentDao.readByPCommentId(pCommentId);
		if (!sessionUser.getUserId().equals(pComment.getSessionUser().getUserId())) {
			throw new UnpermittedAccessPCommentException("수정할 권한이 없는 코멘트입니다.");
		}
		pCommentDao.updatePComment(pCommentId, pCommentText);
		return pCommentDao.readByPCommentId(pCommentId);
	}

	public void delete(String pCommentId, SessionUser sessionUser) {
		PComment pComment = pCommentDao.readByPCommentId(pCommentId);
		if (!sessionUser.getUserId().equals(pComment.getSessionUser().getUserId())) {
			throw new UnpermittedAccessPCommentException("삭제할 권한이 없는 코멘트입니다.");
		}
		noteDao.decreaseCommentCountByPComment(pCommentId);
		pCommentDao.deletePComment(pCommentId);
	}

	public void updateParagraphId(List<Map<String, Object>> pCommentList) {
		for(Map<String, Object> pComment:pCommentList){
			pCommentDao.updatePId(pComment.get("pCommentId").toString(), pComment.get("pId").toString());
		}
	}
}
