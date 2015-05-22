package org.nhnnext.guinness.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.PreviewDao;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.Preview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PreviewService {
	private static final Logger logger = LoggerFactory.getLogger(PreviewService.class);
	
	@Resource
	private PreviewDao previewDao;
	@Resource
	private GroupDao groupDao;
	
	public List<Preview> initNotes(String sessionUserId, String groupId) throws UnpermittedAccessGroupException {
		Group group = groupDao.readGroup(groupId);
		if (!group.checkStatus() && !groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException("비정상적 접근시도.");
		}
		return previewDao.initReadPreviews(groupId);
	}
	
	public List<Preview> reloadPreviews(String groupId, String noteTargetDate) {
		List<Preview> list = previewDao.reloadPreviews(groupId, noteTargetDate);
		logger.debug("list: {}", list.size());
		return list;
	}
	
	public void createPreview(String noteId, String groupId, String noteText) {
		previewDao.create(new Note(noteId), new Group(groupId), extractText(noteText, '!'), extractText(noteText, '?'));
	}
	
	public void updatePreview(String noteId, String noteText) {
		previewDao.update(noteId, extractText(noteText, '!'), extractText(noteText, '?'));
	}
	
	public ArrayList<String> extractText(String givenText, char ch) {
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
