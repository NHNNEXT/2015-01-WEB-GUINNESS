package org.nhnnext.guinness.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class AutoMatchParagraphComments {

	@Autowired
	private static CommentDao commentDao;

	public static ModelAndView updateParagraphText(String noteText, String noteId) {

		List<Comment> commentList = null;

		try {
			commentList = commentDao.readCommentListByNoteId(noteId);
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			return new ModelAndView("/WEB-INF/jsp/exception.jsp");
		}

		List<String> wordDic = makeDic(noteText);

		String[] pNoteText = splitParagraph(noteText); // 문단 구분

		int[][] pVector = new int[pNoteText.length][];

		for (int i = 0; i < pNoteText.length; i++) {
			String[] pWord = splitWord(pNoteText[i]);
			pVector[i] = getVector(wordDic, pWord);
		}

		Map<Integer, Double> score = new HashMap<Integer, Double>();

		int index = 0;
		for (Comment comment : commentList) {
			String[] wordsOfCommentParagraphText = splitWord(comment.getParagraphText());
			int[] cVector = getVector(wordDic, wordsOfCommentParagraphText);

			for (int k = 0; k < pNoteText.length; k++) {
				score.put(index, cosineSimilarity(pVector[k], cVector));
			}

			
			Iterator it = sortByValue(score).iterator();
			comment.setParagraphText(pNoteText[(Integer) it.next()]);
			index++;
		}

		return null;
	}

	private static String[] splitWord(String text) {
		return text.split("( )+|( )*(\r\n)+");
	}

	private static String[] splitParagraph(String text) {
		return text.split("(  )( )+(\r\n)*|( )*(\r\n)+");
	}

	private static int[] getVector(List<String> wordDic, String[] pWord) {
		int[] vector = new int[wordDic.size()];
		for (String word : pWord) {
			vector[wordDic.indexOf(word)]++;
		}
		return vector;
	}

	private static List<String> makeDic(String noteText) {
		noteText = noteText.replace("\r\n", ""); 						// 단순 개행은 삭제.
		String[] wordText = noteText.split("( )+|( )*(\r\n)+"); 		// 띄어쓰기 단위로 단어 구분.
		Set<String> wordSet = new TreeSet<String>();
		for (String word : wordText) {
			wordSet.add(word);
		}
		return new ArrayList<String>(wordSet); 							// word 사전 구성.(정렬)
	}

	private static double cosineSimilarity(int[] textA, int[] textB) { 	// 유사도 계산

		int innerProduct=0;
		
		for(int i=0; i<textA.length; i++){
			innerProduct += textA[i] * textB[i];
		}
		
		double normA, normB;
		
		int sumOfSquare=0;
		for(Integer value : textA){
			sumOfSquare += value*value;
		}
		normA = Math.sqrt(sumOfSquare);
		
		sumOfSquare=0;
		for(Integer value : textB){
			sumOfSquare += value*value;
		}
		normB = Math.sqrt(sumOfSquare);
		
		return innerProduct/(normA*normB);
	}

    public static List sortByValue(final Map map){
        List list = new ArrayList();
        list.addAll(map.keySet());
        Collections.sort(list,new Comparator(){
            public int compare(Object o1,Object o2){
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v1).compareTo(v2);
            }
        });
        Collections.reverse(list); 								// 주석시 오름차순
        return list;
    }
}


