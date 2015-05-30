package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.NotExistedUserIdException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.service.UserService;
import org.nhnnext.guinness.util.JsonResponse;
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
	@Resource
	private UserService userService;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	protected @ResponseBody JsonResponse create(@Valid User user, BindingResult result) throws AlreadyExistedUserIdException, SendMailException, FailedAddGroupMemberException {
		Map<String, Object> messages = new HashMap<String, Object>();
		// 유효성 검사
		if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError e : list) {
            	String element = e.getCodes()[0].split("\\.")[2];
            	messages.put(element+"_message", result.getFieldError(element).getDefaultMessage());
            }
            return new JsonResponse().setSuccess(false).setLocation("index").setJson(messages);
        }
		userService.join(user);
		return new JsonResponse().setSuccess(true).setLocation("/user/sendEmail");
	}
	
	@RequestMapping("/sendEmail")
	protected String emailCheck() {
		return "sendEmail";
	}

	@RequestMapping("/confirm/{keyAddress}")
	protected String confirm(@PathVariable String keyAddress, HttpSession session) {
		SessionUser sessionUser = userService.confirm(keyAddress).createSessionUser();
		session.setAttribute("sessionUser", sessionUser);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected @ResponseBody boolean login(@RequestParam String userId, @RequestParam String userPassword, HttpSession session) throws FailedLoginException {
		SessionUser sessionUser = (userService.login(userId, userPassword)).createSessionUser();
		session.setAttribute("sessionUser", sessionUser);
		return true;
	}
	
	@RequestMapping("/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/form")
	protected String updateForm(Model model, HttpSession session) throws IOException {
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
	protected String updateUser(User user, @RequestParam String userAgainPassword, HttpSession session, @RequestParam("profileImage") MultipartFile profileImage, Model model) throws UserUpdateException {
		if(!user.isCorrectPassword(userAgainPassword))
			throw new UserUpdateException("비밀번호가 다릅니다.");
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
	protected String findPassword(@RequestParam("userId") String userId, Model model) throws NotExistedUserIdException, SendMailException {
		userService.initPassword(userId);
		model.addAttribute("message", "임시 비밀번호를 이메일로 보내드렸습니다.");
		return "sendEmail";
	}
}
