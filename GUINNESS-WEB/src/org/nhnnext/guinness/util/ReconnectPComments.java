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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconnectPComments {
	private static final Logger logger = LoggerFactory.getLogger(ReconnectPComments.class);

	public static List<Map<String, Object>> UpdateParagraphId(String[] originParagraph, String[] editedParagraph,
			List<Map<String, Object>> pCommentList) {
		
		double minSimilarity = 0.5; // 유사도가 50% 이하일 때 부분코멘트가 가리키는 문단 Id는 -1로!
		List<String> wordDic = makeDic(originParagraph, editedParagraph, pCommentList);

		// 문단별 벡터 배열 만들기
		int[][] originParagraphWrodVector = new int[originParagraph.length][];
		int[][] editedParagraphWrodVector = new int[editedParagraph.length][];

		for (int i = 0; i < originParagraph.length; i++) {
			String[] originWords = splitWord(originParagraph[i]);
			originParagraphWrodVector[i] = getVector(wordDic, originWords);
		}
		for (int i = 0; i < editedParagraph.length; i++) {
			String[] editedWords = splitWord(editedParagraph[i]);
			editedParagraphWrodVector[i] = getVector(wordDic, editedWords);
		}

		for (Map<String, Object> pComment : pCommentList) {
			Map<Integer, Double> score = new HashMap<Integer, Double>();
			int pId = (int) pComment.get("pId");
			if(pId == -1){
				continue;
			}
			for (int k = 0; k < editedParagraph.length; k++) {
				score.put(k, cosineSimilarity(editedParagraphWrodVector[k], originParagraphWrodVector[pId - 1]));
			}
			Iterator<Integer> it = sortByValue(score).iterator();
			int key = it.next();

			if (score.get(key) < minSimilarity) {
				pComment.put("pId", -1);
			} else {
				pComment.put("pId", key + 1);
			}
		}
		return pCommentList;
	}

	private static String[] splitWord(String text) {
		return text.split("( )+|( )*(\r\n)+");
	}

	private static int[] getVector(List<String> wordDic, String[] pWord) {
		int index;
		int[] vector = new int[wordDic.size()];
		for (String word : pWord) {
			index = wordDic.indexOf(word);
			if (index != -1) {
				vector[wordDic.indexOf(word)]++;
			}
		}
		return vector;
	}

	private static List<String> makeDic(String[] originParagraph, String[] editedParagraph,
			List<Map<String, Object>> pCommentList) {
		Set<String> wordSet = new TreeSet<String>();

		String allSentence = "";
		for (String sen : originParagraph) {
			allSentence += sen + " ";
		}
		for (String sen : editedParagraph) {
			allSentence += sen + " ";
		}
		for (Map<String, Object> pComment : pCommentList) {
			allSentence += pComment.get("pCommentText") + " ";
		}

		String[] noteWordText = splitWord(allSentence); // 띄어쓰기 단위로 단어 구분.
		for (String word : noteWordText) {
			wordSet.add(word);
		}
		return new ArrayList<String>(wordSet); // word 사전 구성.(정렬)
	}

	private static double cosineSimilarity(int[] textA, int[] textB) { // 유사도 계산
		int innerProduct = 0;
		double normA, normB;
		int sumOfSquare = 0;

		for (int i = 0; i < textA.length; i++) {
			innerProduct += textA[i] * textB[i];
		}
		for (Integer value : textA) {
			sumOfSquare += value * value;
		}
		normA = Math.sqrt(sumOfSquare);

		sumOfSquare = 0;
		for (Integer value : textB) {
			sumOfSquare += value * value;
		}
		normB = Math.sqrt(sumOfSquare);

		return innerProduct / (normA * normB);
	}

	@SuppressWarnings("unchecked")
	public static List<Integer> sortByValue(final Map<Integer, Double> map) {
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(map.keySet());
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);
				return ((Comparable<Object>) v1).compareTo(v2);
			}
		});
		Collections.reverse(list); // 주석시 오름차순
		return list;
	}

}
