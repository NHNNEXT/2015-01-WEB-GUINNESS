package org.nhnnext.guinness.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class PreviewDaoTest {
	String noteId;
	String groupId;
	
	@Resource
	PreviewDao previewDao;
	
	@Before
	public void setUp() {
		int id = (int) (Math.random() * 100);
		noteId = Integer.toString(id);
		groupId = RandomFactory.getRandomId(5);
	}
	
	@Test
	public void create() {
		ArrayList<String> attentionList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		attentionList.add("중요한 내용");
		questionList.add("궁금한 내용");
		
		int result = previewDao.create(new Note("31"), new Group("Lnomi"), attentionList, questionList);
		
		assertEquals(1, result);
	}

}
