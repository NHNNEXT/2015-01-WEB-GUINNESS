package org.nhnnext.guinness.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.NotExistedUserIdException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.UserService;
import org.nhnnext.guinness.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	protected String create(@Valid User user, BindingResult result, Model model) throws AlreadyExistedUserIdException, SendMailException {
		// 유효성 검사
		if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError e : list) {
            	String element = e.getCodes()[0].split("\\.")[2];
            	model.addAttribute(element+"_message", result.getFieldError(element).getDefaultMessage());
            	logger.debug("field: {}, message: {}", element, result.getFieldError(element).getDefaultMessage());
            }
            return "index";
        }
		userService.join(user);
		return "sendEmail";
	}

	@RequestMapping("/confirm/{keyAddress}")
	protected String confirm(@PathVariable String keyAddress, HttpSession session) {
		SessionUser sessionUser = userService.confirm(keyAddress).createSessionUser();
		saveUserInfoInSession(session, sessionUser);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected @ResponseBody boolean login(@RequestParam String userId, 
			@RequestParam String userPassword, HttpSession session) throws FailedLoginException {
		SessionUser sessionUser = (userService.login(userId, userPassword)).createSessionUser();
		saveUserInfoInSession(session, sessionUser);
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
	protected @ResponseBody JsonResult updateUserCheck(@RequestParam String userPassword, HttpSession session){
		String userId = ((SessionUser)session.getAttribute("sessionUser")).getUserId();
		boolean result = userService.checkUpdatePassword(userId, userPassword);
		if(!result)
			return new JsonResult().setSuccess(result).setMessage("비밀번호를 확인해주세요");
		return new JsonResult().setSuccess(result); 
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam String userAgainPassword, @RequestParam("profileImage") MultipartFile profileImage, HttpSession session, Model model, User user) throws UserUpdateException {
		if(!user.isCorrectPassword(userAgainPassword))
			throw new UserUpdateException("비밀번호가 다릅니다.");
		String rootPath = session.getServletContext().getRealPath("/");
		userService.update(user, rootPath, profileImage);
		saveUserInfoInSession(session, user.createSessionUser());
		return "redirect:/groups/form";
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
	
	private void saveUserInfoInSession(HttpSession session, SessionUser sessionUser) {
		session.setAttribute("sessionUser", sessionUser);
	}
}
