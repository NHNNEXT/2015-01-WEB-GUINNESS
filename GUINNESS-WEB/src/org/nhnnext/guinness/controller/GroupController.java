package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.group.FailedUpdateGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.util.JSONResponseUtil;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	protected ResponseEntity<Object> list(HttpSession session) throws IOException {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return JSONResponseUtil.getJSONResponse(groupService.readGroups(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam String status, @RequestParam String groupName,
			HttpSession session, Model model) throws IOException {
		if(groupName.length() > 15)
			return JSONResponseUtil.getJSONResponse("그룹명은 15자 이내로 가능합니다" , HttpStatus.PRECONDITION_FAILED);
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		Group group = groupService.create(groupName, groupCaptainUserId, status);
		return JSONResponseUtil.getJSONResponse(group, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable String groupId, HttpSession session, Model model) throws IOException {
		groupService.delete(groupId, ServletRequestUtil.getUserIdFromSession(session));
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@RequestParam String userId, @RequestParam String groupId,
			@RequestParam String sessionUserId) {
		groupService.inviteGroupMember(sessionUserId, userId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam String userId, @RequestParam String groupId) {
		Group group = groupService.addGroupMember(userId, groupId);
		return JSONResponseUtil.getJSONResponse(group, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@RequestParam String groupId, @RequestParam String sessionUserId) {
		groupService.joinGroupMember(sessionUserId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/leave", method = RequestMethod.POST)
	protected ResponseEntity<Object> leave(@RequestParam String sessionUserId, @RequestParam String groupId) {
		groupService.leaveGroup(sessionUserId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(@RequestParam String sessionUserId, @RequestParam String userId, @RequestParam String groupId) {
		groupService.deleteGroupMember(sessionUserId, userId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping("/members/{groupId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable String groupId) {
		return JSONResponseUtil.getJSONResponse(groupService.groupMembers(groupId), HttpStatus.OK);
	}

	@RequestMapping("/update/form/{groupId}")
	protected String updateForm(@PathVariable String groupId, Model model, HttpSession session) throws IOException {
		Group group = groupService.readGroup(groupId);
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!sessionUserId.equals(group.getGroupCaptainUserId())) {
			throw new FailedUpdateGroupException("그룹장만이 그룹설정이 가능합니다.");
		}
		model.addAttribute("group", group);
		model.addAttribute("members", groupService.groupMembers(groupId));
		return "updateGroup";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam String sessionUserId, @RequestParam("backgroundImage") 
			MultipartFile backgroundImage , HttpSession session,Group group) {
		if (group.getGroupName().equals("")) {
			throw new FailedUpdateGroupException("그룹명이 공백입니다.");	// 잘못된 접근
		}
		String rootPath = session.getServletContext().getRealPath("/");
		groupService.update(sessionUserId, group, rootPath, backgroundImage);
		return "redirect:/g/" + group.getGroupId();
	}
}
