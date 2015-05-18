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
import org.springframework.ui.Model;

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
	
	// TODO offline 코드 리뷰 - groupId가 동적으로 변경되어야 하는 이유는?
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
		// TODO offline 코드 리뷰 - 리팩토링 point는?
		// userDao.findUserByUserId(userId) 메서드가 두 번 호출되고 있는데 한번으로 처리하면 안되나?
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

	// TODO offline 코드 리뷰 - 리팩토링 point는? 
	public void readGroup(Model model, String groupId) {
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupName", groupDao.readGroup(groupId).getGroupName());
	}
}
