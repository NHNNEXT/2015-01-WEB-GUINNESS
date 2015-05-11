package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.CommentDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	
	@Resource
	private GroupDao groupDao;

	@Resource
	private NoteDao noteDao;
	
	@Resource
	private CommentDao commentDao;
	
	@Resource
	private AlarmDao alarmDao;

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void setNoteDao(NoteDao noteDao) {
		this.noteDao = noteDao;
	}

	@RequestMapping(value = "/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			model.addAttribute("errorMessage", "비정상적 접근시도.");
			return "illegal";
		}
		String groupName = groupDao.readGroup(groupId).getGroupName();
		List<Note> noteList = getNoteListFromDao(null, groupId, null);
		model.addAttribute("noteList", new Gson().toJson(noteList));
		model.addAttribute("groupName", new Gson().toJson(groupName));
		return "notes";
	}

	@RequestMapping(value = "/note/list", method = RequestMethod.POST)
	protected @ResponseBody List<Note> reloadNoteList(WebRequest req) throws IOException {
		String userIds = req.getParameter("checkedUserId");
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate");
		if(targetDate.equals("undefined")){
			targetDate = null;
		}
		List<Note> noteList = getNoteListFromDao(targetDate, groupId, userIds);
		return noteList;
	}

	private List<Note> getNoteListFromDao(String date, String groupId, String userIds) {
		if (userIds == "")
			return new ArrayList<Note>();
		DateTime targetDate = new DateTime(date).plusDays(1).minusSeconds(1);
		DateTime endDate = targetDate.minusDays(1).plusSeconds(1);
		if (date == null) {
			endDate = targetDate.minusYears(10);
			targetDate = targetDate.plusYears(10);
		}
		List<Note> noteList = noteDao.readNoteList(groupId, endDate.toString(), targetDate.toString(), userIds);
		return noteList;
	}

	@RequestMapping("/notes/{noteId}")
	protected ModelAndView show(@PathVariable String noteId) throws IOException {
		Note note = null;
		try {
			note = noteDao.readNote(noteId);
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
		return new ModelAndView("jsonView").addObject("jsonData", note);
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String targetDate = req.getParameter("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		if (noteText.equals("")) {
			return "redirect:/notes/editor?groupId=" + groupId;
		}
		
		String noteId = ""+noteDao.createNote(new Note(noteText, targetDate, sessionUserId, groupId));
		
		String alarmId = null;
		Alarm alarm = null;
		String noteWriter = noteDao.readNote(noteId).getUserId();
		List<User> groupMembers = groupDao.readGroupMember(groupId);
		for (User user : groupMembers) {
			if (user.getUserId().equals(sessionUserId)) {
				continue;
			}
			while (true) {
				alarmId = RandomFactory.getRandomId(10);
				if (alarmDao.read(alarmId) == null) {
					alarm = new Alarm(alarmId, user.getUserId(), noteWriter, noteId, "그룹에 새 글을 작성하였습니다.", "N");
					break;
				}
			}
			alarmDao.create(alarm);
		}
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(WebRequest req) {
		String groupId = req.getParameter("groupId");
		String noteId = req.getParameter("noteId");
		String noteText = req.getParameter("noteText");
		updateParagraphText(noteText, noteId);
		noteDao.updateNote(noteText, noteId);
		return "redirect:/g/" + groupId;
	}


	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected ModelAndView delete(@PathVariable String noteId) {
		logger.debug(" noteId : " + noteId);
		if (noteDao.deleteNote(noteId) == 1) {
			return new ModelAndView("jsonView", "jsonData", "success");
		}

		return new ModelAndView("jsonView", "jsonData", "fail");
	}
	
	
	public ModelAndView updateParagraphText(String noteText, String noteId) {

		List<Comment> commentList = null;
		
		try {
			commentList = commentDao.readParagraphCommentListByNoteId(noteId);
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			return new ModelAndView("/WEB-INF/jsp/exception.jsp");
		}
		
		logger.debug("코멘트는 몇개? : {}", commentList.size());
		
		List<String> wordDic = makeDic(noteText, commentList);

		String[] pNoteText = splitParagraph(noteText); // 문단 구분

		int[][] pVector = new int[pNoteText.length][];

		for (int i = 0; i < pNoteText.length; i++) {
			String[] pWord = splitWord(pNoteText[i]);
			pVector[i] = getVector(wordDic, pWord);
		}

		int index=1;
		for (Comment comment : commentList) {
			Map<Integer, Double> score = new HashMap<Integer, Double>();
			String[] wordsOfCommentParagraphText = splitWord(comment.getParagraphText());
			int[] cVector = getVector(wordDic, wordsOfCommentParagraphText);
			logger.debug("{}번째 코멘트.", index);
			for (int k = 0; k < pNoteText.length; k++) {
				logger.debug("{}번째, 스코어 : {}", k, cosineSimilarity(pVector[k], cVector));
				score.put(k, cosineSimilarity(pVector[k], cVector));
			}
			
			logger.debug("스코어 : {}", score);
			Iterator it = sortByValue(score).iterator();
			int key = (Integer) it.next();
			logger.debug("인덱스? : {}", key);
			logger.debug("점수 : {}", score.get(key));
			logger.debug("수정된 문단 텍스트 : {}", pNoteText[key]);
			logger.debug("수정 전 commentParagraphText : {}", comment.getParagraphText());
			comment.setParagraphText(pNoteText[key]);
			logger.debug("수정 후 commentParagraphText : {}", comment.getParagraphText());
			commentDao.updateParagraphComment(comment.getCommentId(), comment.getParagraphText());
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
		int index;
		int[] vector = new int[wordDic.size()];
		for (String word : pWord) {
			index = wordDic.indexOf(word);
			if(index!=-1){									// 인덱스 확인 필요.
				vector[wordDic.indexOf(word)]++;
			}
		}
		return vector;
	}

	private static List<String> makeDic(String noteText, List<Comment> commentList) {
		Set<String> wordSet = new TreeSet<String>();
		noteText = noteText.replace("\r\n", ""); 			// 단순 개행은 삭제.
		String[] noteWordText = splitWord(noteText); 		// 띄어쓰기 단위로 단어 구분.
		for (String word : noteWordText) {
			wordSet.add(word);
		}
		for(Comment comment : commentList){
			String[] commetWordText = splitWord(comment.getParagraphText());
			for (String word : commetWordText) {
				wordSet.add(word);
			}
		}
		return new ArrayList<String>(wordSet); 							// word 사전 구성.(정렬)
	}

	private static double cosineSimilarity(int[] textA, int[] textB) { 	// 유사도 계산

		int innerProduct=0;
		double normA, normB;
		int sumOfSquare=0;
		
		for(int i=0; i<textA.length; i++){
			innerProduct += textA[i] * textB[i];
		}
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
        Collections.reverse(list); // 주석시 오름차순
        return list;
    }
}
