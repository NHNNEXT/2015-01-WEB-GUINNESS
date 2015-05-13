package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Resource
	private CommentDao commentDao;
	@Resource
	private NoteDao noteDao;
	@Resource
	private AlarmDao alarmDao;

	public List<Map<String, Object>> create(Comment comment) {
		commentDao.createComment(comment);
		noteDao.increaseCommentCount(comment.getNote().getNoteId());
		createAlarm(comment);
		return commentDao.readCommentListByNoteId(comment.getNote().getNoteId());
	}

	private void createAlarm(Comment comment) {
		Note note = comment.getNote();
		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
		if (!comment.checkWriter(noteWriter)) {
			alarmDao.create(new Alarm(createAlarmId(), "C", comment.getUser(), noteWriter, note));
		}
	}

	private String createAlarmId() {
		String alarmId = RandomFactory.getRandomId(10);
		if(alarmDao.isExistAlarmId(alarmId)) {
			return createAlarmId();
		}
		return alarmId;
	}
	
	// TODO 코드리뷰
//	private String createAlarmId() {
//		String alarmId;
//		while (alarmDao.isExistAlarmId(alarmId = RandomFactory.getRandomId(10)));
//		return alarmId;
//	}

	public List<Map<String, Object>> list(String noteId) {
		return commentDao.readCommentListByNoteId(noteId);
	}

	public Object update(String commentId, String commentText) {
		commentDao.updateComment(commentId, commentText);
		return commentDao.readCommentByCommentId(commentId);
	}

	public void delete(String commentId) {
		noteDao.decreaseCommentCount(commentId);
		commentDao.deleteComment(commentId);
	}
}
