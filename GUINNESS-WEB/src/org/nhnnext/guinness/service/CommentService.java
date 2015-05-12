package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private AlarmDao alarmDao;
	
	public List<Map<String, Object>> create(Comment comment){
		commentDao.createComment(comment);
		noteDao.increaseCommentCount(comment.getNote().getNoteId());
		
		if(!(comment.getUser().getUserId()).equals(noteDao.readNote(comment.getNote().getNoteId()).getUser().getUserId())) {
			String alarmId = null;
			Alarm alarm = null;
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (!alarmDao.isExistAlarmId(alarmId)) {
					alarm = new Alarm(alarmId, "C", comment.getUser(), noteDao.readNote(comment.getNote().getNoteId()).getUser(), comment.getNote());
					break;
				}
			}
			alarmDao.create(alarm);
		}
		return commentDao.readCommentListByNoteId(comment.getNote().getNoteId());
	}
	
	public List<Map<String, Object>> list(String noteId){
		return commentDao.readCommentListByNoteId(noteId);
	}
	
	public Object update(String commentId, String commentText){
		commentDao.updateComment(commentId, commentText);
		return commentDao.readCommentByCommentId(commentId);
	}
	
	public void delete(String commentId){
		noteDao.decreaseCommentCount(commentId);
		commentDao.deleteComment(commentId);
	}
}
