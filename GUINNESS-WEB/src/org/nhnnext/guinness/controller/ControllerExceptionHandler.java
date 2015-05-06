package org.nhnnext.guinness.controller;

import java.util.HashMap;
import java.util.Map;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
	// 본인 확인을 위한 메일 전송 시 예외처리
	@ExceptionHandler(SendMailException.class)
	public ModelAndView sendMailException(SendMailException e) {
		e.printStackTrace();
		return new ModelAndView("/exception");
	}
	
	// 회원가입 시 해당 계정의 가입 기록 확인 
	@ExceptionHandler(AlreadyExistedUserIdException.class)
	public ModelAndView alreadyExistedUserIdException(AlreadyExistedUserIdException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("/index");
		mav.addObject("user", new User());
		mav.addObject("message", "이미 존재하는 아이디입니다.");
		return mav;
	}
	
	// 로그인 실패시 예외처리
	@ExceptionHandler(FailedLoginException.class)
	public ModelAndView failedLoginException(FailedLoginException e) {
		e.printStackTrace();
		Map<String, String> viewMap = new HashMap<String, String>();
		viewMap.put("view", "loginFailed");
		return new ModelAndView("jsonView").addObject("jsonData", viewMap);
	}
	
	// 회원정보 수정시 예외처리
	@ExceptionHandler(UserUpdateException.class)
	public ModelAndView userUpdateException(UserUpdateException e) {
		RedirectView rv = new RedirectView("/user");
		rv.setExposeModelAttributes(false);
		ModelAndView mav = new ModelAndView(rv);
		mav.addObject("message", e.getMessage());
		logger.debug("UserUpdateException:{}", e.getMessage());
		return mav;
	}


	@ExceptionHandler(Exception.class)
	public ModelAndView exception(Exception e) {
		ModelAndView mav = new ModelAndView("/exception");
		e.printStackTrace();
		logger.debug("exception: {}", e.getClass().getSimpleName());
		return mav;
	}
	
}
