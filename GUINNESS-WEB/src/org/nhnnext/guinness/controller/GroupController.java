package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.FailedMakingGroupException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
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
import org.springframework.web.servlet.ModelAndView;

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
	protected @ResponseBody List<Group> list(HttpSession session) throws IOException {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return groupDao.readGroupList(userId);
	}
	
	@RequestMapping(value = "/groups", method = RequestMethod.POST)
	protected @ResponseBody List<Group> create(WebRequest req, HttpSession session, Model model) throws IOException, FailedMakingGroupException {
		char isPublic = ("public".equals((String)	req.getParameter("isPublic"))) ? 'T' : 'F';
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
		List<Group> result = new ArrayList<Group>();
		result.add(group);
		return result;
	}
	
	@RequestMapping("/groups/delete/{groupId}")
	protected String delete(@PathVariable String groupId, WebRequest req, HttpSession session, Model model) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		logger.debug("groupId: {}", groupId);
		Group group = groupDao.readGroup(groupId);
		if(group == null) {
			// 비정상적인 접근
			throw new Exception();
		}
		if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
			model.addAttribute("errorMessage", "삭제 권한 없음");
			return "groups";
		}
		groupDao.deleteGroup(group);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/group/add/member", method = RequestMethod.POST)
	protected ModelAndView addGroupMember(WebRequest req, HttpSession session)
			throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new ModelAndView("redirect:/");
		}

		String userId = req.getParameter("userId");
		String groupId = req.getParameter("groupId");
		try {
			if (userDao.findUserByUserId(userId) == null) {
				return new ModelAndView("jsonView", "jsonData", "unknownUser");
			}
			if (groupDao.checkJoinedGroup(userId, groupId)) {
				return new ModelAndView("jsonView", "jsonData", "joinedUser");
			}
			groupDao.createGroupUser(userId, groupId);
			return new ModelAndView("jsonView", "jsonData",
					groupDao.readGroupMember(groupId));
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
	}

	@RequestMapping("/group/read/member/{groupId}")
	protected @ResponseBody JsonResult<User> memberList(
			@PathVariable String groupId, HttpSession session)
			throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new JsonResult<User>(false, "/");
		}
		return new JsonResult<User>(true, groupDao.readGroupMember(groupId));
	}


	@RequestMapping("/group/read/{groupId}")
	protected ModelAndView groupInfo(@PathVariable String groupId) {
		return new ModelAndView("jsonView", "jsonData",
				groupDao.readGroup(groupId));
	}

	@RequestMapping(value = { "/groups/form", "/groups/createForm" })
	public String createForm() {
		return "form";
	}

	@RequestMapping(value = { "/groups/{groupId}/form" })
	public String updateForm(@PathVariable Long groupId) {
		return "form";
	}

	@RequestMapping("/groups/{groupId}")
	public String show(@PathVariable Long groupId) {
		return "show";
	}
}
