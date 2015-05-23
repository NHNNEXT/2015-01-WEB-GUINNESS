package org.nhnnext.guinness.service;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.SessionUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class CommentServiceTest {

	@Resource
	TestCommentService testCommentService;
	
	@Test
	public void createAlarmId() {
		
	}
	
	// Transaction Test
	@Test
	public void createAllorNothing() throws UnpermittedAccessGroupException {
		SessionUser sessionUser = new SessionUser("y@y.y", "장영빈", "y@y.y");
		Note note = new Note("1");
		Comment comment = new Comment("test comment", "A", sessionUser, note);
		
		try {
			testCommentService.create(note.getNoteId(), comment);
			fail("CommentServiceTransactionException expected");
		} catch (TesetCommentServiceException e) {
			e.printStackTrace();
		}
			
		//comment에 "test comment"가 생성되었는지 확인
		List<Map<String, Object>> results = testCommentService.list(note.getNoteId());
		for (Map<String, Object> map : results) {
			System.out.println(map);
		}
	}
}
