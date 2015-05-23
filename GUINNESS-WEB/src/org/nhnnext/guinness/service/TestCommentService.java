package org.nhnnext.guinness.service;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/*
 * Transaction 테스트를 위한 Test Service Class
 */
@Service
@Transactional
public class TestCommentService extends CommentService {

	@Resource
	NoteDao noteDao;
	@Resource
	CommentDao commentDao;
	
	/*
	 *  comment 생성 후 note의 countOfComment를 증가할 때 에러가 발생할 경우
	 */
	public void create(String noteId, Comment comment) {
		commentDao.createComment(comment);
		if(true) throw new TesetCommentServiceException();
		noteDao.increaseCommentCount(noteId);
	}
}
