package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.RandomFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CommentController {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private AlarmDao alarmDao;
	
	@RequestMapping(value = "/comments", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(HttpSession session, WebRequest req) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String commentText = req.getParameter("commentText");
		String commentType = req.getParameter("commentType");
		String noteId = req.getParameter("noteId");
		
		if (commentText.equals(""))
			return new JsonResult().setSuccess(false).setMapValues(commentDao.readCommentListByNoteId(noteId));
		
		Comment comment = new Comment(commentText, commentType, sessionUserId, noteId);
		commentDao.createComment(comment);
		noteDao.increaseCommentCount(noteId);
		
		if(!sessionUserId.equals(noteDao.readNote(noteId).getUserId())) {
			String alarmId = null;
			Alarm alarm = null;
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (alarmDao.read(alarmId) == null) {
					alarm = new Alarm(alarmId, noteDao.readNote(noteId).getUserId(), sessionUserId, noteId, "의 노트에 댓글을 남겼습니다", "C");
					break;
				}
			}
			alarmDao.create(alarm);
		}
		return new JsonResult().setSuccess(true).setMapValues(commentDao.readCommentListByNoteId(noteId));
	}

	@RequestMapping("/comments/{noteId}")
	protected @ResponseBody JsonResult list(@PathVariable String noteId, WebRequest req) {
		List<Map<String, Object>> list = commentDao.readCommentListByNoteId(noteId);
		// createDate의 포맷을 위한 변경
		for (Map<String, Object> map : list)
			map.replace("createDate", map.get("createDate").toString());
		return new JsonResult().setSuccess(true).setMapValues(list);
	}

	@RequestMapping(value = "/comments/{commentId}", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult update(@PathVariable String commentId, WebRequest req) {
		String commentText = req.getParameter("commentText");
		commentDao.updateComment(commentId, commentText);
		return new JsonResult().setSuccess(true).setObject(commentDao.readCommentByCommentId(commentId));
	}
	
	@RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String commentId) {
		noteDao.decreaseCommentCount(commentId);
		commentDao.deleteComment(commentId);
		return new JsonResult().setSuccess(true);
	}
}
