package org.nhnnext.guinness.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.model.Preview;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

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
		attentionList.add("중요한 내용");
		questionList.add("궁금한 내용");
		System.out.println(attentionList.toString());
	}
	
	@Test
	public void read() {
		List<Preview> previews = previewDao.initReadPreviews("DEaAd");
		for (Preview preview : previews) {
			System.out.println(preview);
		}
		assertNotNull(previews);
	}
	
	@Test
	public void readAndConvertJson() {
		List<Preview> previews = previewDao.initReadPreviews("DEaAd");
		for (Preview preview : previews) {
			System.out.println(new Gson().toJson(preview));
		}
		assertNotNull(previews);
	}
	
	@Test
	public void update() {
		ArrayList<String> attentionList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		attentionList.add("수정된 강조");
		questionList.add("수정된 궁금증");
		int result = previewDao.update("6", "2015-05-21 14:00:00.0", attentionList, questionList);
		List<Preview> previews = previewDao.initReadPreviews("DEaAd");
		for (Preview preview : previews) {
			System.out.println(new Gson().toJson(preview));
		}
		assertEquals(1, result);
	}
	
	@Test
	public void testName() throws Exception {
		String str = "[111,222,333]";
		List<String> list = Arrays.asList(str.substring(1, str.length()-1).split(","));
		for (String string : list) {
			System.out.println(string);
		}
	}
}
