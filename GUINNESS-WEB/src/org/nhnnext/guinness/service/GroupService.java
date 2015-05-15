package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.UnpermittedDeleteGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	@Resource
	private GroupDao groupDao;
	@Resource
	private UserDao userDao;
	
	public List<Map<String, Object>> readGroups(String userId) {
		return groupDao.readGroupListForMap(userId);
	}
	
	public Group create(String groupName, String groupCaptainUserId, String isPublic) {
		Group group = new Group(createGroupId(), groupName, groupCaptainUserId, isPublic);
		groupDao.createGroup(group);
		groupDao.createGroupUser(group.getGroupCaptainUserId(), group.getGroupId());
		return group;
	}
	
	private String createGroupId() {
		String groupId = RandomFactory.getRandomId(5);
		if(groupDao.isExistGroupId(groupId)) {
			return createGroupId();
		}
		return groupId;
	}

	public void delete(String groupId, String userId) throws FailedDeleteGroupException, UnpermittedDeleteGroupException {
		logger.debug("groupId: {}", groupId);
		Group group = groupDao.readGroup(groupId);
		if (group == null) {
			throw new FailedDeleteGroupException();
		}
		if (!group.checkCaptain(userId)) {
			throw new UnpermittedDeleteGroupException();
		}
		groupDao.deleteGroup(groupId);		
	}

	public User addGroupMember(String userId, String groupId)throws FailedAddGroupMemberException {
		if (userDao.findUserByUserId(userId) == null) 
			throw new FailedAddGroupMemberException("사용자를 찾을 수 없습니다!");
		if (groupDao.checkJoinedGroup(userId, groupId)) 
			throw new FailedAddGroupMemberException("사용자가 이미 가입되어있습니다!");
		groupDao.createGroupUser(userId, groupId);
		return userDao.findUserByUserId(userId);
	}

	public List<Map<String, Object>> groupMembers(String groupId) {
		return groupDao.readGroupMemberForMap(groupId);
	}

//
//	public List<Map<String, Object>> create(Comment comment) {
//		commentDao.createComment(comment);
//		noteDao.increaseCommentCount(comment.getNote().getNoteId());
//		createAlarm(comment);
//		return commentDao.readCommentListByNoteId(comment.getNote().getNoteId());
//	}
//
//	private void createAlarm(Comment comment) {
//		Note note = comment.getNote();
//		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
//		if (!comment.checkWriter(noteWriter)) {
//			alarmDao.create(new Alarm(createAlarmId(), "C", comment.getUser(), noteWriter, note));
//		}
//	}
//
//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if(alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}
//	
//	// TODO 코드리뷰
////	private String createAlarmId() {
////		String alarmId;
////		while (alarmDao.isExistAlarmId(alarmId = RandomFactory.getRandomId(10)));
////		return alarmId;
////	}
//
//	public List<Map<String, Object>> list(String noteId) {
//		return commentDao.readCommentListByNoteId(noteId);
//	}
//
//	public Object update(String commentId, String commentText) {
//		commentDao.updateComment(commentId, commentText);
//		return commentDao.readCommentByCommentId(commentId);
//	}
//
//	public void delete(String commentId) {
//		noteDao.decreaseCommentCount(commentId);
//		commentDao.deleteComment(commentId);
//	}
}
