package org.nhnnext.guinness.util;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.dao.NoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class AutoMatchParagraphCommentsTest {

	
	@Autowired
	private NoteDao noteDao;
	
	
	@Test
	public void test() {
		String noteText = noteDao.readNote("5").getNoteText();
	
//		String pNoteText[] = noteText.split("(  )( )+(\r\n)*|( )*(\r\n)+");	// 문단 구분 
//		
//		int i=1;
//		for(String note : pNoteText){
//			
//			System.out.println("노트"+i+" : "+ note);
//			i++;
//		}
		int i=1;
		
		noteText = noteText.replace("\r\n", "");
		String wordText[] = noteText.split("( )+|( )*(\r\n)+");
		
		int k=1;
		for(String note : wordText){
			
			System.out.println("단어"+k+" : "+ note);
			k++;
		}
		
		Set<String> wordSet = new TreeSet<String>();
		
		for(String word : wordText){
			wordSet.add(word);
		}
		
		System.out.println("워드셋 크기 : "+ wordSet.size());
		
		for(String word:wordSet){
			System.out.println("정렬"+i+" : "+ word);
			i++;
		}
		
	
	}

}
