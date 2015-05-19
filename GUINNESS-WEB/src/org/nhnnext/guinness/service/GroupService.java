package org.nhnnext.guinness.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
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
	@Resource
	private AlarmDao alarmDao;
	
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
	
	public Group readGroup(String groupId) {
		return groupDao.readGroup(groupId);
	}
}
