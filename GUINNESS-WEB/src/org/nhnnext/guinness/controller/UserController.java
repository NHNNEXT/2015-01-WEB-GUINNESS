package org.nhnnext.guinness.controller;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.NotExistedUserIdException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.UserService;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected String create(Model model, User user) throws AlreadyExistedUserIdException, SendMailException {
		// 유효성 체크
		if (!extractViolationMessage(model, user)) {
			return "index";
		}
		userService.join(user);
		return "sendEmail";
	}

	@RequestMapping("/confirm/{keyAddress}")
	protected String confirm(@PathVariable String keyAddress, HttpSession session) {
		User user = userService.confirm(keyAddress);
		saveUserInfoInSession(session, user);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected @ResponseBody boolean login(WebRequest req, HttpSession session) throws FailedLoginException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		User user = userService.login(userId, userPassword);
		saveUserInfoInSession(session, user);
		return true;
	}
	
	@RequestMapping("/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/form")
	protected String updateForm(Model model) {
		model.addAttribute("user", new User());
		return "updateUser";
	}
	
	@RequestMapping(value = "/update/check", method = RequestMethod.POST)
	protected @ResponseBody JsonResult updateUserCheck(HttpSession session, WebRequest req){
		String userPassword = req.getParameter("password");
		String userId = (String) session.getAttribute("sessionUserId");
		boolean result = userService.checkUpdatePassword(userId, userPassword);
		if(!result)
			return new JsonResult().setSuccess(result).setMessage("비밀번호를 확인해주세요");
		return new JsonResult().setSuccess(result); 
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(WebRequest req, HttpSession session, Model model, User user,
			@RequestParam("profileImage") MultipartFile profileImage) throws UserUpdateException {
		String userAgainPassword = req.getParameter("userAgainPassword");
		if(!user.isCorrectPassword(userAgainPassword))
			throw new UserUpdateException("비밀번호가 다릅니다.");

		String rootPath = session.getServletContext().getRealPath("/");
		userService.update(user, model, rootPath, profileImage);

		saveUserInfoInSession(session, user);
		return "redirect:/groups";
	}

	@RequestMapping("/findPasswordForm")
	protected String findPasswordForm(HttpSession session) {
		return "findPassword";
	}
	
	@RequestMapping(value = "/findPassword", method = RequestMethod.POST)
	protected String findPassword(@RequestParam("userId") String userId, Model model) throws NotExistedUserIdException, SendMailException {
		userService.initPassword(userId);
		model.addAttribute("message", "임시 비밀번호를 이메일로 보내드렸습니다.");
		return "sendEmail";
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
