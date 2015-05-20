package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.nhnnext.guinness.util.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class PreviewTest {
	private static final Logger logger = LoggerFactory.getLogger(PreviewTest.class);
	
	String noteId;
	String groupId;
	
	@Before
	public void setUp() {
		int id = (int) (Math.random() * 100);
		noteId = Integer.toString(id);
		groupId = RandomFactory.getRandomId(5);

	}

	@Test
	public void constructor() {
		ArrayList<String> attentionText = new ArrayList<String>();
		ArrayList<String> questionText = new ArrayList<String>();
		attentionText.add("중요한 내용");
		attentionText.add("중요한 내용2");
		questionText.add("궁금한 내용");
		System.out.println(new Gson().toJson(attentionText));
		Preview preview = new Preview(new Note(noteId), new Group(groupId), attentionText, questionText);
		logger.debug("preview : {}", preview);
		assertNotNull(preview);
	}

}
