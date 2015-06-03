package org.nhnnext.guinness.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.group.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.group.FailedUpdateGroupException;
import org.nhnnext.guinness.exception.groupmember.FailedAddingGroupMemberException;
import org.nhnnext.guinness.exception.groupmember.GroupMemberException;
import org.nhnnext.guinness.model.Alarm;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class GroupService {
	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	@Resource
	private GroupDao groupDao;
	@Resource
	private UserDao userDao;
	@Resource
	private AlarmDao alarmDao;

	public List<Map<String, Object>> readGroups(String userId) {
		return groupDao.readGroups(userId);
	}

	public Group create(String groupName, String groupCaptainUserId, String status) {
		Group group = new Group(createGroupId(), groupName, groupCaptainUserId, status);
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

	public void delete(String groupId, String userId) {
		logger.debug("groupId: {}", groupId);
		Group group = groupDao.readGroup(groupId);
		if (group == null) {
			throw new FailedDeleteGroupException("그룹이 존재하지 않습니다.");
		}
		if (!group.checkCaptain(userId)) {
			throw new FailedDeleteGroupException("그룹장만 그룹을 삭제할 수 있습니다.");
		}
		groupDao.deleteGroup(groupId);
		alarmDao.deleteGroupByGroupId(groupId);
	}

	public void inviteGroupMember(String sessionUserId, String userId, String groupId) {
		// TODO sessionUserId가 groupId에 가입이 되어있지 않을경우 
		//throw new UnpermittedAccessGroupException();
		if (userDao.findUserByUserId(userId) == null)
			throw new FailedAddingGroupMemberException("사용자를 찾을 수 없습니다!");
		if (groupDao.checkJoinedGroup(userId, groupId))
			throw new FailedAddingGroupMemberException("이미 가입되어 있습니다!");
		if (alarmDao.checkGroupAlarms(userId, groupId))
			throw new FailedAddingGroupMemberException("가입 요청 대기중 입니다!");
		Alarm alarm = new Alarm(createAlarmId(), "I", (new User(sessionUserId)).createSessionUser(), new User(userId),
				new Group(groupId));
		alarmDao.createGroupInvitation(alarm);
	}

	public void joinGroupMember(String sessionUserId, String groupId) {
		// TODO groupId가 공개 그룹이 아닐경우
		//throw new UnpermittedAccessGroupException();
		if (alarmDao.checkJoinedGroupAlarms(sessionUserId, groupId))
			throw new FailedAddingGroupMemberException("가입 승인 대기중 입니다!");
		String groupUserCaptionId = groupDao.findGroupCaptianUserId(groupId);
		Alarm alarm = new Alarm(createAlarmId(), "J", (new User(sessionUserId)).createSessionUser(), new User(groupUserCaptionId),
				new Group(groupId));
		alarmDao.createGroupInvitation(alarm);
	}
	
	public Group addGroupMember(String userId, String groupId) {
		groupDao.createGroupUser(userId, groupId);
		return groupDao.readGroup(groupId);
	}

	public void leaveGroup(String userId, String groupId) {
		if (!groupDao.checkJoinedGroup(userId, groupId)) {
			throw new GroupMemberException("그룹멤버가 아닙니다.");
		}
		if (userId.equals(groupDao.readGroup(groupId).getGroupCaptainUserId())) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		groupDao.deleteGroupUser(userId, groupId);
	}

	public void deleteGroupMember(String sessionUserId, String userId, String groupId) {
		Group group = groupDao.readGroup(groupId);
		if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
			throw new GroupMemberException("그룹장만이 추방이 가능합니다.");
		}
		if (userId.equals(groupDao.readGroup(groupId).getGroupCaptainUserId())) {
			throw new GroupMemberException("그룹장은 탈퇴가 불가능합니다.");
		}
		groupDao.deleteGroupUser(userId, groupId);
	}
	
	public List<Map<String, Object>> groupMembers(String groupId) {
		return groupDao.readGroupMembers(groupId);
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

	public void update(String sessionUserId, Group group, String rootPath, MultipartFile groupImage) {
		Group dbGroup = groupDao.readGroup(group.getGroupId());
		if (!sessionUserId.equals(dbGroup.getGroupCaptainUserId())) {
			throw new FailedUpdateGroupException("그룹장만이 그룹설정이 가능합니다.");
		}
		if (userDao.findUserByUserId(group.getGroupCaptainUserId()) == null) {
			throw new FailedUpdateGroupException("존재하지 않는 사용자입니다.");
		}
		if (!groupDao.checkJoinedGroup(group.getGroupCaptainUserId(), group.getGroupId())) {
			throw new FailedUpdateGroupException("그룹멤버가 아닙니다.");
		}

		boolean isDefaultImage = "background-default.png".equals(group.getGroupImage());
		boolean isChangedImage = group.getGroupId().equals(group.getGroupImage());
		
		if(!isDefaultImage && !isChangedImage && !groupImage.isEmpty()) {
			try {
				String fileName = group.getGroupId();
				groupImage.transferTo(new File(rootPath + "img/group/" + fileName));
				group.setGroupImage(fileName);
			} catch (IOException e) {
				throw new FailedUpdateGroupException("잘못된 형식입니다.");
			}
		}
		groupDao.updateGroup(group);
	}
}
