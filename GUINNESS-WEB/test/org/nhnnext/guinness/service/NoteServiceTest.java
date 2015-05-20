package org.nhnnext.guinness.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jdk.nashorn.internal.objects.annotations.Setter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.util.RandomFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class NoteServiceTest {
	
	@Resource
	NoteService noteService;

	@Test
	public void refine() {
		//given
		String givenText = "   !!!첫번째 중요!!! ???첫번째 물음??? #이거는 일반 텍스트 "
				+ "!!!두번째 중요!!! **볼드체** ???두번째 질문???    ";
		ArrayList<String> attentionList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		
		//when
		attentionList = noteService.extractText(givenText, '!');
		questionList = noteService.extractText(givenText, '?');
		noteService.createPreview("31", "Lnomi", attentionList, questionList);
		
		// then
		System.out.println(attentionList);
		System.out.println(questionList);
	}
	
	@Test
	public void regex() {
		String givenText = "!!!여기는 중요한거입니다!!! ???이거는 뭔가요??? # 일반 텍스트입니다. 이거는 테이블에서 무시 \n !!!두번째 중요!!!";
		Pattern pattern = Pattern.compile("(!{3})([^\n]{1,})(!{3})");
		Matcher matcher = pattern.matcher(givenText);
		if(matcher.find()) {
			System.out.println(matcher.group());
		}
	}
}
