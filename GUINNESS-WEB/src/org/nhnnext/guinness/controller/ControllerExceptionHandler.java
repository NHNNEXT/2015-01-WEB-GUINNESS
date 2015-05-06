package org.nhnnext.guinness.controller;

import javax.mail.MessagingException;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
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
	
	@ExceptionHandler(AlreadyExistedUserIdException.class)
	public ModelAndView alreadyExistedUserIdException(AlreadyExistedUserIdException e) {
		ModelAndView mav = new ModelAndView("/index");
		mav.addObject("user", new User());
		mav.addObject("message", "이미 존재하는 아이디입니다.");
		logger.debug("AlreadyExistedUserIdException");
		return mav;
	}
	
	@ExceptionHandler(UserUpdateException.class)
	public ModelAndView userUpdateException(UserUpdateException e) {
		RedirectView rv = new RedirectView("/user");
		rv.setExposeModelAttributes(false);
		ModelAndView mav = new ModelAndView(rv);
		mav.addObject("message", e.getMessage());
		logger.debug("UserUpdateException");
		return mav;
	}
	
	@ExceptionHandler(MessagingException.class)
	public ModelAndView exception(MessagingException e) {
		ModelAndView mav = new ModelAndView("/exception");
		logger.debug("exception: {}", e.getClass().getSimpleName());
		e.printStackTrace();
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
