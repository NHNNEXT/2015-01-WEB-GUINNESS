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
		String givenText = "???!!!abc!!!???";
		ArrayList<String> importantList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		
		//when
		importantList = findText(givenText, '!');
		questionList = findText(givenText, '?');
		
		// then
		System.out.println(importantList);
		System.out.println(questionList);
		System.out.println(givenText);
	}

	private ArrayList<String> findText(String givenText, char ch) {
		int flag = 0;
		int beginIndex = 0;
		int endIndex = 0;
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < givenText.length(); i++) {
			if(givenText.charAt(i) == ch) {
				flag++;
				continue;
			}
			if(flag == 3 && beginIndex == 0) {
				beginIndex = i;
			}
			if(flag == 6) {
				endIndex = i - 3;
				list.add(givenText.substring(beginIndex, endIndex));
				System.out.println(givenText.substring(i));
				flag = 0;
				beginIndex = 0;
				endIndex = 0;
			}
		}
		return list;
	}
}
