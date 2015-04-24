package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/create", method=RequestMethod.POST)
	protected String create(WebRequest req, HttpSession session, Model model) throws IOException  {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		String userName = req.getParameter("userName");
		User user = new User(userId, userName, userPassword);
		
		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (!constraintViolations.isEmpty()) {
			String signValidErrorMessage = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				signValidErrorMessage = signValidErrorMessage + "<br />" + each.getMessage();
			}
			model.addAttribute("signValidErrorMessage", signValidErrorMessage);
			return "index";
		}

		try {
			userDao.createUser(user);
			session.setAttribute("sessionUserId", userId);
			session.setAttribute("sessionUserName", userName);
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
	protected ModelAndView login(WebRequest req, HttpSession session) throws IOException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		
		Map<String, String> viewMap = new HashMap<String, String>();
		
		try {
			User user = userDao.readUser(userId);
			if (user == null || !user.getUserPassword().equals(userPassword)) {
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
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value="/update")
	protected String updateForm() {
		return "updateUser";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	protected String updateUser(WebRequest req, HttpSession session, Model model) throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String userId = (String) session.getAttribute("sessionUserId");
		String userName = req.getParameter("userName");
		String userNewPassword = req.getParameter("userNewPassword");
		String userPassword = req.getParameter("userPassword");
		if (userNewPassword.equals("")) {
			userNewPassword = userPassword;
		}
		User user = new User(userId, userName, userNewPassword );
		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (!constraintViolations.isEmpty()) {
			String message = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				message = message + "<br />" + each.getMessage();
			}
			model.addAttribute("message", message);
			return "updateUser";
		}
		if (!userDao.readUser(userId).getUserPassword().equals(userPassword)) {
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "updateUser";
		}
		try {
			userDao.updateUser(user);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message", "잘못된 형식입니다.");
		}
		session.setAttribute("sessionUserName", userName);
		return "groups";
	}
}