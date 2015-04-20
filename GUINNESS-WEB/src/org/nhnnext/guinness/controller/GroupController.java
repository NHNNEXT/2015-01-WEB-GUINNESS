package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.RandomString;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/group")
public class GroupController {
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/add/member", method=RequestMethod.POST)
	protected ModelAndView addGroupMember(HttpServletRequest req, HttpServletResponse resp,HttpSession session) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return new ModelAndView("redirect:/");
		}
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "groupId");
		logger.debug("userId={}, groupId={}", paramsList.get("userId"), paramsList.get("groupId"));
		try {
			if (userDao.readUser(paramsList.get("userId")) == null) {
				logger.debug("등록되지 않은 사용자 입니다");
				ModelAndView mav = new ModelAndView("jsonView");
				mav.addObject("jsonData", "unknownUser");
				return mav;
			}
			if (groupDao.checkJoinedGroup(paramsList.get("userId"), paramsList.get("groupId"))) {
				logger.debug("이미 가입된 사용자 입니다.");
				ModelAndView mav = new ModelAndView("jsonView");
				mav.addObject("jsonData", "joinedUser");
				return mav;
			}
			groupDao.createGroupUser(paramsList.get("userId"), paramsList.get("groupId"));
			ModelAndView mav = new ModelAndView("jsonView");
			mav.addObject("jsonData", groupDao.readGroupMember(paramsList.get("groupId")));
			return mav;
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			return new ModelAndView("/WEB-INF/jsp/exception.jsp");
		}
	}
	
	@RequestMapping("/read/member")
	protected void memberList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			out.print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
					.toJson(groupDao.readGroupMember(req.getParameter("groupId"))));
			out.close();
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
	
	@RequestMapping("/read")
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		List<Group> groupList = null;
		try {
			groupList = groupDao.readGroupList(sessionUserId);
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(groupList));
		out.close();
	}
	
	@RequestMapping("/delete")
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId");

		try {
			Group group = groupDao.readGroup(paramsList.get("groupId"));
			if (!group.getGroupCaptainUserId().equals(sessionUserId)) {
				Forwarding.doForward(req, resp, "errorMessage", "삭제 권한 없음", "/groups");
				return;
			}
			groupDao.deleteGroup(group);
			resp.sendRedirect("/groups");
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	protected void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupName", "isPublic");

		char isPublic = 'F';
		if ("public".equals(paramsList.get("isPublic")))
			isPublic = 'T';

		Group group = null;
		try {
			group = new Group(paramsList.get("groupName"), groupCaptainUserId, isPublic);
			while (true) {
				String groupId = RandomString.getRandomId(5);
				if (groupDao.readGroup(groupId) == null) {
					group.setGroupId(groupId);
					break;
				}
			}
		} catch (MakingObjectListFromJdbcException | ClassNotFoundException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}

		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);

		if (!constraintViolation.isEmpty()) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			Forwarding.doForward(req, resp, "errorMessage", errorMessage, "/groups");
			return;
		}

		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}

		resp.sendRedirect("/groups");
	}
}
