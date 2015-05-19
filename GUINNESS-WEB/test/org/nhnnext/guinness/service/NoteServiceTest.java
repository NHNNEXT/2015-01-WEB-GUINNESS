package org.nhnnext.guinness.service;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class NoteServiceTest {
	@Test
	public void create() {
		//given
		String givenText = "   !!!첫번째 중요!!! ???첫번째 물음??? #이거는 일반 텍스트 "
				+ "!!!두번째 중요!!! **볼드체** ???두번째 질문???    ";
		ArrayList<String> importantList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		
		//when
		importantList = findText(givenText, '!');
		questionList = findText(givenText, '?');
		
		// then
		System.out.println(importantList);
		System.out.println(questionList);
	}

	private ArrayList<String> findText(String givenText, char ch) {
		givenText = givenText.trim();
		int flag = 0;
		int len = givenText.length();
		int beginIndex = 0;
		int endIndex = 0;
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < len; i++) {
			if(givenText.charAt(i) == ch) {
				flag++;
			}
			if(flag == 3 && beginIndex == 0) {
				beginIndex = i + 1;
			}
			if(flag == 6) {
				endIndex = i - 2;
				list.add(givenText.substring(beginIndex, endIndex));
				flag = 0;
				beginIndex = 0;
				endIndex = 0;
			}
		}
		return list;
	}
}
