package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
	@Resource
	private CommentDao commentDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;
	@Resource
	private GroupDao groupDao;

	public List<Map<String, Object>> create(SessionUser sessionUser, Note note, Comment comment) throws UnpermittedAccessGroupException {
		Group group = groupDao.readGroupByNoteId(note.getNoteId());
		if (!groupDao.checkJoinedGroup(sessionUser.getUserId(), group.getGroupId())) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		commentDao.createComment(comment);
		noteDao.increaseCommentCount(comment.getNote().getNoteId());
		createAlarm(comment);
		return commentDao.readCommentListByNoteId(comment.getNote().getNoteId());
	}

	private void createAlarm(Comment comment) {
		Note note = comment.getNote();
		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
		
		if (!comment.checkWriter(noteWriter)) {
			alarmDao.createNewNotes(new Alarm(createAlarmId(), "C", comment.getUser(), noteWriter, note));
		}
	}

	private String createAlarmId() {
		String alarmId = RandomFactory.getRandomId(10);
		if(alarmDao.isExistAlarmId(alarmId)) {
			return createAlarmId();
		}
		return alarmId;
	}

	public List<Map<String, Object>> list(String noteId) {
		return commentDao.readCommentListByNoteId(noteId);
	}

	public Comment update(String commentId, String commentText) {
		commentDao.updateComment(commentId, commentText);
		return commentDao.readCommentByCommentId(commentId);
	}

	public void delete(String commentId) {
		noteDao.decreaseCommentCount(commentId);
		commentDao.deleteComment(commentId);
	}
}
