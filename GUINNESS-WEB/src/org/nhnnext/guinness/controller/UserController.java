package org.nhnnext.guinness.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.nhnnext.guinness.exception.user.FailedUpdateUserException;
import org.nhnnext.guinness.exception.user.JoinValidationException;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.UserService;
import org.nhnnext.guinness.util.JSONResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST)
	protected String create(@Valid User user, BindingResult result, HttpServletResponse response) {
		if(result.hasErrors()) {
			throw new JoinValidationException(extractValidationMessages(result));
        }
		userService.join(user);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return "sendEmail";
	}
	
	@RequestMapping("/confirm/{keyAddress}")
	protected String confirm(@PathVariable String keyAddress, HttpSession session) {
		SessionUser sessionUser = userService.confirm(keyAddress).createSessionUser();
		session.setAttribute("sessionUser", sessionUser);
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected ResponseEntity<Object> login(@RequestParam String userId, @RequestParam String userPassword, HttpSession session) {
		SessionUser sessionUser = (userService.login(userId, userPassword)).createSessionUser();
		session.setAttribute("sessionUser", sessionUser);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.ACCEPTED);
	}
	
	@RequestMapping("/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/form")
	protected String updateForm(Model model, HttpSession session) {
		return "updateUserCheck";
	}
	
	@RequestMapping(value = "/update/check", method = RequestMethod.POST)
	protected String updateUserCheck(@RequestParam String userPassword, HttpSession session, Model model){
		SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
		boolean result = userService.checkUpdatePassword(sessionUser.getUserId(), userPassword);
		if (!result) {
			model.addAttribute("errorMessage", "비밀번호가 다릅니다.");
			return "updateUserCheck";
		}
		model.addAttribute("user", new User());
		return "updateUser";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(User user, @RequestParam String userAgainPassword, HttpSession session, @RequestParam("profileImage") MultipartFile profileImage, Model model) {
		if(!user.isCorrectPassword(userAgainPassword))
			throw new FailedUpdateUserException("비밀번호가 다릅니다.");
		String rootPath = session.getServletContext().getRealPath("/");
		userService.update(user, rootPath, profileImage);
		session.setAttribute("sessionUser", user.createSessionUser());
		model.addAttribute("infoMessage", "회원정보수정에 성공하였습니다.");
		return "/groups";
	}

	@RequestMapping("/findPasswordForm")
	protected String findPasswordForm(HttpSession session) {
		return "findPassword";
	}
	
	@RequestMapping(value = "/findPassword", method = RequestMethod.POST)
	protected String findPassword(@RequestParam("userId") String userId, Model model) {
		userService.initPassword(userId);
		model.addAttribute("message", "임시 비밀번호를 이메일로 보내드렸습니다.");
		return "sendEmail";
	}

	private List<Map<String, Object>> extractValidationMessages(BindingResult result) {
		List<Map<String, Object>> messages = new ArrayList<>();
		List<ObjectError> list = result.getAllErrors();
		for (ObjectError e : list) {
			String element = e.getCodes()[0].split("\\.")[2];
			Map<String, Object> map = new HashMap<>();
			map.put("id", element+"-message");
			map.put("message", result.getFieldError(element).getDefaultMessage());
			messages.add(map);
		}
		return messages;
	}
}
