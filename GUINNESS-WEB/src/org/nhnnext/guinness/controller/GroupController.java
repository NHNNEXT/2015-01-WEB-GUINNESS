package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.FailedMakingGroupException;
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
import org.springframework.web.context.request.WebRequest;

@Controller
public class GroupController {
	@Resource
	private GroupService groupService;

	@RequestMapping("/groups")
	public String list() throws IOException {
		return "groups";
	}

	@RequestMapping("/api/groups")
	protected @ResponseBody JsonResult list(HttpSession session) throws IOException {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(groupService.readGroups(userId));
	}

	@RequestMapping(value = "/groups", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(WebRequest req, HttpSession session, Model model) throws IOException, FailedMakingGroupException {
		String isPublic = ("public".equals((String) req.getParameter("isPublic"))) ? "T" : "F";
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupName = req.getParameter("groupName");
		Group group = groupService.create(groupName, groupCaptainUserId, isPublic);
		return new JsonResult().setSuccess(true).setObject(group);
	}
	
	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE )
	protected @ResponseBody JsonResult delete(@PathVariable String groupId, HttpSession session, Model model) throws FailedDeleteGroupException, IOException, UnpermittedDeleteGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		groupService.delete(groupId, sessionUserId);
		return new JsonResult().setSuccess(true);
	}
	
	@RequestMapping(value = "/groups/members", method = RequestMethod.POST)
	protected @ResponseBody JsonResult addGroupMember(@RequestParam String userId, @RequestParam String groupId) throws FailedAddGroupMemberException {
		//TODO offline 코드 리뷰 - 리팩토링 point는?
		//String userId = req.getParameter("userId");
		//String groupId = req.getParameter("groupId");
		User user = groupService.addGroupMember(userId, groupId);		
		return new JsonResult().setSuccess(true).setObject(user);
	}


	@RequestMapping("/groups/members/{groupId}")
	protected @ResponseBody JsonResult listGroupMember(@PathVariable String groupId) {
		return new JsonResult().setSuccess(true).setMapValues(groupService.groupMembers(groupId));
	}
}
