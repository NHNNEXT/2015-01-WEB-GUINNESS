package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.RandomString;
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
@RequestMapping("/group")
public class GroupController {
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/add/member", method = RequestMethod.POST)
	protected ModelAndView addGroupMember(WebRequest req, HttpSession session) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new ModelAndView("redirect:/");
		}

		String userId = req.getParameter("userId");
		String groupId = req.getParameter("groupId");
		try {
			if (userDao.readUser(userId) == null) {
				return new ModelAndView("jsonView", "jsonData", "unknownUser");
			}
			if (groupDao.checkJoinedGroup(userId, groupId)) {
				return new ModelAndView("jsonView", "jsonData", "joinedUser");
			}
			groupDao.createGroupUser(userId, groupId);
			return new ModelAndView("jsonView", "jsonData", groupDao.readGroupMember(groupId));
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			return new ModelAndView("exception");
		}
	}

	@RequestMapping("/read/member/{groupId}")
	protected @ResponseBody JsonResult<User> memberList(@PathVariable String groupId, HttpSession session)
			throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new JsonResult<User>(false, "/");
		}
		return new JsonResult<User>(true, groupDao.readGroupMember(groupId));
	}

	@RequestMapping("/read")
	protected ModelAndView list(HttpSession session) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new ModelAndView("redirect:/");
		}

		String userId = ServletRequestUtil.getUserIdFromSession(session);
		try {
			return new ModelAndView("jsonView", "jsonData", groupDao.readGroupList(userId));
		} catch (Exception e) {
			logger.error("Exception", e);
			return new ModelAndView("/exception");
		}
	}

	@RequestMapping("/delete")
	protected String delete(WebRequest req, HttpSession session, Model model) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return "redirect:/";
		}

		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		try {
			Group group = groupDao.readGroup(req.getParameter("groupId"));
			if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
				model.addAttribute("errorMessage", "삭제 권한 없음");
				return "groups";
			}
			groupDao.deleteGroup(group);
			return "redirect:/";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "exception";
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException,
			ClassNotFoundException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return "redirect:/";
		}

		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		char isPublic = 'F';
		if ("public".equals(req.getParameter("isPublic")))
			isPublic = 'T';

		Group group = null;
		try {
			group = new Group(req.getParameter("groupName"), groupCaptainUserId, isPublic);
			while (true) {
				String groupId = RandomString.getRandomId(5);
				if (groupDao.readGroup(groupId) == null) {
					group.setGroupId(groupId);
					break;
				}
			}
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			return "exception";
		}

		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);
		if (!constraintViolation.isEmpty()) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			model.addAttribute("errorMessage", errorMessage);
			return "groups";
		}

		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (Exception e) {
			logger.error("Exception", e);
			return "exception";
		}
		return "redirect:/";
	}
}
