package org.nhnnext.guinness.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.dao.ConfirmDao;
import org.nhnnext.guinness.dao.UserDao;
import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.UserService;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	@Autowired
	private ConfirmDao confirmDao;

	@RequestMapping("/")
	protected String init(Model model) {
		model.addAttribute("user", new User());
		return "index";
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	protected String create(Model model, User user) throws AlreadyExistedUserIdException, SendMailException {
		// 유효성 체크
		if (!extractViolationMessage(model, user)) {
			return "index";
		}
		userService.create(user);
		return "sendEmail";
	}

	@RequestMapping(value = "/user/confirm/{keyAddress}")
	protected String confirm(@PathVariable String keyAddress, HttpSession session) {
		User user = userService.confirm(keyAddress);
		saveUserInfoInSession(session, user);
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected ModelAndView login(WebRequest req, HttpSession session) throws FailedLoginException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		User user = userService.login(userId, userPassword);

		saveUserInfoInSession(session, user);

		Map<String, String> viewMap = new HashMap<String, String>();
		viewMap.put("view", "groups");
		return new ModelAndView("jsonView").addObject("jsonData", viewMap);
	}

	@RequestMapping("/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/user")
	protected String updateForm(Model model) {
		model.addAttribute("user", new User());
		return "updateUser";
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	protected String updateUser(WebRequest req, HttpSession session, Model model, User user,
			@RequestParam("profileImage") MultipartFile profileImage) throws UserUpdateException {
		// 유효성 체크
		String userOldPassword = req.getParameter("userOldPassword");
		if ("".equals(user.getUserPassword()))
			user.setUserPassword(userOldPassword);
		if (!extractViolationMessage(model, user))
			throw new UserUpdateException("잘못된 형식입니다.");

		String rootPath = session.getServletContext().getRealPath("/");
		userService.update(user, userOldPassword, model, rootPath, profileImage);

		// session refresh...
		saveUserInfoInSession(session, user);
		return "redirect:/groups";
	}

	private void saveUserInfoInSession(HttpSession session, User user) {
		session.setAttribute("sessionUserId", user.getUserId());
		session.setAttribute("sessionUserName", user.getUserName());
		session.setAttribute("sessionUserImage", user.getUserImage());
	}

	private boolean extractViolationMessage(Model model, User user) {
		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (!constraintViolations.isEmpty()) {
			String signValidErrorMessage = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				signValidErrorMessage = signValidErrorMessage + "<br />" + each.getMessage();
			}
			model.addAttribute("signValidErrorMessage", signValidErrorMessage);
			logger.debug("violation error");
			return false;
		}
		return true;
	}
}
