package org.nhnnext.guinness.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.dao.PreviewDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class PreviewServiceTest {
	@Resource
	PreviewService previewService;
	@Resource
	PreviewDao previewDao;
	
	@Test
	public void matchByRegex() {
		String givenText = "!!!여기는 중요한거입니다!!! "
				+ "???이거는 뭔가요??? "
				+ "# 일반 텍스트입니다. 이거는 테이블에서 무시 \n !!!두번째 중요!!!";
		ArrayList<String> attentionList = previewService.match(givenText, "!{3}[^n]{1,}!{3}", "!{3}");
		ArrayList<String> questionList = previewService.match(givenText, "\\?{3}[^n]{1,}\\?{3}", "\\?{3}");
		
		System.out.println(attentionList);
		System.out.println(questionList);
		
	}
}
