package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedMakingGroupException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.RandomFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class GroupController {
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping("/groups")
	public String list() throws IOException {
		return "groups";
	}

	@RequestMapping("/api/groups")
	protected @ResponseBody JsonResult list(HttpSession session) throws IOException {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(groupDao.readGroupListForMap(userId));
	}

	@RequestMapping(value = "/groups", method = RequestMethod.POST)
	protected @ResponseBody JsonResult create(WebRequest req, HttpSession session, Model model) throws IOException, FailedMakingGroupException {
		String isPublic = ("public".equals((String) req.getParameter("isPublic"))) ? "T" : "F";
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		String groupName = req.getParameter("groupName");
		Group group = new Group(groupName, groupCaptainUserId, isPublic);
		while (true) {
			String groupId = RandomFactory.getRandomId(5);
			if (groupDao.readGroup(groupId) == null) {
				group.setGroupId(groupId);
				break;
			}
		}

		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);
		if (!constraintViolation.isEmpty()) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			throw new FailedMakingGroupException(errorMessage);
		}

		groupDao.createGroup(group);
		groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		return new JsonResult().setSuccess(true).setObject(group);
	}
	
	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE )
	protected @ResponseBody JsonResult delete(@PathVariable String groupId, HttpSession session, Model model) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		logger.debug("groupId: {}", groupId);
		Group group = groupDao.readGroup(groupId);
		if (group == null) {
			throw new Exception();
		}
		if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
			model.addAttribute("errorMessage", "삭제 권한 없음");
			return new JsonResult().setSuccess(false);
		}
		groupDao.deleteGroup(groupId);
		
		return new JsonResult().setSuccess(true);
	}
	
	@RequestMapping(value = "/groups/members", method = RequestMethod.POST)
	protected @ResponseBody JsonResult addGroupMember(WebRequest req) throws FailedAddGroupMemberException {
		String userId = req.getParameter("userId");
		String groupId = req.getParameter("groupId");
		
		if (userDao.findUserByUserId(userId) == null) 
			throw new FailedAddGroupMemberException("사용자를 찾을 수 없습니다!");
		if (groupDao.checkJoinedGroup(userId, groupId)) 
			throw new FailedAddGroupMemberException("사용자가 이미 가입되어있습니다!");
		
		groupDao.createGroupUser(userId, groupId);
		return new JsonResult().setSuccess(true).setObject(userDao.findUserByUserId(userId));
	}

	@RequestMapping("/groups/members/{groupId}")
	protected @ResponseBody JsonResult listGroupMember(@PathVariable String groupId) {
		return new JsonResult().setSuccess(true).setMapValues(groupDao.readGroupMemberForMap(groupId));
	}
}
