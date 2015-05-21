package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.FailedMakingGroupException;
import org.nhnnext.guinness.exception.GroupUpdateException;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.exception.UnpermittedDeleteGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/groups")
public class GroupController {
	@Resource
	private GroupService groupService;

	@RequestMapping("/form")
	public String list() throws IOException {
		return "groups";
	}

	@RequestMapping("")
	protected @ResponseBody JsonResult list(HttpSession session) throws IOException {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(groupService.readGroups(userId));
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(@RequestParam String isPublic, @RequestParam String groupName,
			HttpSession session, Model model) throws IOException, FailedMakingGroupException {
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		Group group = groupService.create(groupName, groupCaptainUserId, isPublic);
		return new JsonResult().setSuccess(true).setObject(group);
	}

	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String groupId, HttpSession session, Model model)
			throws FailedDeleteGroupException, IOException, UnpermittedDeleteGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		groupService.delete(groupId, sessionUserId);
		return new JsonResult().setSuccess(true);
	}

	@RequestMapping(value = "/members/invite", method = RequestMethod.POST)
	protected @ResponseBody JsonResult inviteGroupMember(@RequestParam String userId, @RequestParam String groupId,
			@RequestParam String sessionUserId) throws FailedAddGroupMemberException {
		try {
			groupService.inviteGroupMember(sessionUserId, userId, groupId);
		} catch (UnpermittedAccessGroupException e) {
			return new JsonResult().setSuccess(false).setMessage(e.getMessage());
		}
		return new JsonResult().setSuccess(true);
	}

	@RequestMapping(value = "/members/accept", method = RequestMethod.POST)
	protected @ResponseBody JsonResult acceptGroupMember(@RequestParam String userId, @RequestParam String groupId)
			throws FailedAddGroupMemberException {
		User user = groupService.addGroupMember(userId, groupId);
		return new JsonResult().setSuccess(true).setObject(user);
	}

	@RequestMapping(value = "/members/leave", method = RequestMethod.DELETE)
	protected String leave(@RequestParam String sessionUserId, @RequestParam String groupId)
			throws GroupUpdateException {
		groupService.deleteGroupMember(sessionUserId, groupId);
		return "/groups";
	}

	// TODO 그룹멤버 추방 구현.
	//

	@RequestMapping("/members/{groupId}")
	protected @ResponseBody JsonResult listGroupMember(@PathVariable String groupId) {
		return new JsonResult().setSuccess(true).setMapValues(groupService.groupMembers(groupId));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam String sessionUserId, Group group) throws GroupUpdateException {
		if (group.getGroupName().equals("")) {
			throw new GroupUpdateException("그룹명이 공백입니다.");
		}
		groupService.update(sessionUserId, group);
		return "/g/" + group.getGroupId();
	}
}
