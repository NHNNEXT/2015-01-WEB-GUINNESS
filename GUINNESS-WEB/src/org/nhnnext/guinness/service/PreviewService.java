package org.nhnnext.guinness.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		previewDao.create(new Note(noteId), new Group(groupId), match(noteText, "!{3}[^\n]{1,}!{3}", "!{3}"), 
				match(noteText, "\\?{3}[^\n]{1,}\\?{3}", "\\?{3}"));
	}
	
	public void updatePreview(String noteId, String noteText) {
		previewDao.update(noteId, match(noteText, "!{3}[^\n]{1,}!{3}", "!{3}"), match(noteText, "\\?{3}[^\n]{1,}\\?{3}", "\\?{3}"));
	}
	
	public ArrayList<String> match(String givenText, String regex, String regexToBeRemoved) {
		givenText = givenText.trim();
		ArrayList<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(givenText);
		
		while(matcher.find()) {
			list.add(matcher.group().replaceAll(regexToBeRemoved, " "));
		}
		
		return list;
	}
}
