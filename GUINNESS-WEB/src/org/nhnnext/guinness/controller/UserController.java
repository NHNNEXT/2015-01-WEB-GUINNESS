package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/create", method=RequestMethod.POST)
	protected String create(HttpServletRequest req, HttpSession session, Model model) throws IOException  {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword",
				"userName");
		User user = new User(paramsList.get("userId"), paramsList.get("userName"), paramsList.get("userPassword"));
		
		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (!constraintViolations.isEmpty()) {
			String signValidErrorMessage = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				signValidErrorMessage = signValidErrorMessage + "<br />" + each.getMessage();
			}
			req.setAttribute("userId", paramsList.get("userId"));
			req.setAttribute("userName", paramsList.get("userName"));
			
			model.addAttribute("signValidErrorMessage", signValidErrorMessage);
			return "index";
		}

		try {
			userDao.createUser(user);
			session.setAttribute("sessionUserId", paramsList.get("userId"));
			session.setAttribute("sessionUserName", paramsList.get("userName"));
			return "groups";
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			return "exception";
		} catch (AlreadyExistedUserIdException e) {
			logger.error("Exception", e);
			model.addAttribute("message", "이미 존재하는 아이디입니다.");
			return "index";
		}
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	protected ModelAndView login(HttpServletRequest req, HttpSession session) throws IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword");
		Map<String, String> viewMap = new HashMap<String, String>();
		
		try {
			User user = userDao.readUser(paramsList.get("userId"));
			if (user == null || !user.getUserPassword().equals(paramsList.get("userPassword"))) {
				viewMap.put("view", "loginFailed");
				return new ModelAndView("jsonView").addObject("jsonData", viewMap);
			}
			session.setAttribute("sessionUserId", user.getUserId());
			session.setAttribute("sessionUserName", user.getUserName());
			viewMap.put("view", "groups");
			return new ModelAndView("jsonView").addObject("jsonData", viewMap);
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			viewMap.put("view", "exception");
			return new ModelAndView("jsonView").addObject("jsonData", viewMap);
		}
	}
	
	@RequestMapping("/logout")
	protected ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/update")
	protected String updateForm() {
		return "updateUser";
	}
	
}