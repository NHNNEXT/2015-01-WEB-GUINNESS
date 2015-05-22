package org.nhnnext.guinness.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.GroupUpdateException;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.exception.UnpermittedDeleteGroupException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		Group group = new Group(createGroupId(), groupName, groupCaptainUserId, isPublic, null);
		groupDao.createGroup(group);
		groupDao.createGroupUser(group.getGroupCaptainUserId(), group.getGroupId());
		return group;
	}

	private String createGroupId() {
		String groupId = RandomFactory.getRandomId(5);
		if (groupDao.isExistGroupId(groupId)) {
			return createGroupId();
		}
		return groupId;
	}

	public void delete(String groupId, String userId) throws FailedDeleteGroupException,
			UnpermittedDeleteGroupException {
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

	public void inviteGroupMember(String sessionUserId, String userId, String groupId)
			throws FailedAddGroupMemberException, UnpermittedAccessGroupException {
		if (!groupDao.checkJoinedGroup(sessionUserId, groupId)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		if (userDao.findUserByUserId(userId) == null)
			throw new FailedAddGroupMemberException("사용자를 찾을 수 없습니다!");
		if (groupDao.checkJoinedGroup(userId, groupId))
			throw new FailedAddGroupMemberException("이미 가입되어 있습니다!");
		if (alarmDao.checkGroupAlarms(userId, groupId))
			throw new FailedAddGroupMemberException("가입 요청 대기중 입니다!");
		Alarm alarm = new Alarm(createAlarmId(), "I", (new User(sessionUserId)).createSessionUser(), new User(userId),
				new Group(groupId));
		alarmDao.createGroupInvitation(alarm);
	}

	public User addGroupMember(String userId, String groupId) throws FailedAddGroupMemberException {
		groupDao.createGroupUser(userId, groupId);
		return userDao.findUserByUserId(userId);
	}

	public void leaveGroup(String userId, String groupId) throws GroupUpdateException {
		checkPossibleDeleteGroupMember(userId, groupDao.readGroup(groupId));
		groupDao.deleteGroupUser(userId, groupId);
	}

	public void deleteGroupMember(String sessionUserId, String userId, String groupId) throws GroupUpdateException {
		Group group = groupDao.readGroup(groupId);
		if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
			throw new GroupUpdateException("그룹장만이 추방이 가능합니다.");
		}
		checkPossibleDeleteGroupMember(userId, group);
		groupDao.deleteGroupUser(userId, groupId);
	}

	private void checkPossibleDeleteGroupMember(String userId, Group group) throws GroupUpdateException {
		if (!groupDao.checkJoinedGroup(userId, group.getGroupId())) {
			throw new GroupUpdateException("그룹멤버가 아닙니다.");
		}
		if (userId.equals(group.getGroupCaptainUserId())) {
			throw new GroupUpdateException("그룹장은 탈퇴가 불가능합니다.");
		}
	}

	public List<Map<String, Object>> groupMembers(String groupId) {
		return groupDao.readGroupMemberForMap(groupId);
	}

	public Group readGroup(String groupId) {
		return groupDao.readGroup(groupId);
	}

	private String createAlarmId() {
		String alarmId = RandomFactory.getRandomId(10);
		if (alarmDao.isExistAlarmId(alarmId)) {
			return createAlarmId();
		}
		return alarmId;
	}

	public void update(String sessionUserId, Group group, String rootPath, MultipartFile groupImage)
			throws GroupUpdateException {
		Group dbGroup = groupDao.readGroup(group.getGroupId());
		if (!sessionUserId.equals(dbGroup.getGroupCaptainUserId())) {
			throw new GroupUpdateException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userDao.findUserByUserId(group.getGroupCaptainUserId()) == null) {
			throw new GroupUpdateException("존재하지 않는 사용자입니다.");
		}
		if (!groupDao.checkJoinedGroup(group.getGroupCaptainUserId(), group.getGroupId())) {
			throw new GroupUpdateException("그룹멤버가 아닙니다.");
		}
		try {
			group.setGroupImage(dbGroup.getGroupImage());
			if (!groupImage.isEmpty()) {
				String fileName = group.getGroupId();
				groupImage.transferTo(new File(rootPath + "img/group/" + fileName));
				group.setGroupImage(fileName);
			}
		} catch (IOException e) {
			throw new GroupUpdateException("잘못된 형식입니다.");
		}

		groupDao.updateGroup(group);
	}
}
