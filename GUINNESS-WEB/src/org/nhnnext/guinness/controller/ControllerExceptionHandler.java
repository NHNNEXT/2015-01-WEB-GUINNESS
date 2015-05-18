package org.nhnnext.guinness.controller;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.FailedAddGroupMemberException;
import org.nhnnext.guinness.exception.FailedDeleteGroupException;
import org.nhnnext.guinness.exception.FailedLoginException;
import org.nhnnext.guinness.exception.FailedMakingGroupException;
import org.nhnnext.guinness.exception.SendMailException;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.exception.UnpermittedDeleteGroupException;
import org.nhnnext.guinness.exception.UserUpdateException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	// 디비 접속 안되었을 경우 예외처리
	@ExceptionHandler(CannotGetJdbcConnectionException.class)
	public ModelAndView cannotGetJdbcConnectionException(CannotGetJdbcConnectionException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("/exception");
		logger.debug("exception: {}", e.getClass().getSimpleName());
		return mav;
	}
	
	// 본인 확인을 위한 메일 전송 시 예외처리
	@ExceptionHandler(SendMailException.class)
	public String sendMailException(SendMailException e) {
		e.printStackTrace();
		return "/exception";
	}
	
	// 회원가입시 중복 아이디 예외처리
	@ExceptionHandler(AlreadyExistedUserIdException.class)
	public ModelAndView alreadyExistedUserIdException(AlreadyExistedUserIdException e) {
		ModelAndView mav = new ModelAndView("/index");
		mav.addObject("user", new User());
		mav.addObject("message", "이미 존재하는 아이디입니다.");
		return mav;
	}
	
	// 로그인 실패시 예외처리
	@ExceptionHandler(FailedLoginException.class)
	public @ResponseBody boolean failedLoginException(FailedLoginException e) {
		return false;
	}
	
	// 회원정보 수정시 예외처리
	@ExceptionHandler(UserUpdateException.class)
	public ModelAndView userUpdateException(UserUpdateException e) {
		RedirectView rv = new RedirectView("/user/form");
		rv.setExposeModelAttributes(false);
		ModelAndView mav = new ModelAndView(rv);
		mav.addObject("message", e.getMessage());
		logger.debug("UserUpdateException:{}", e.getMessage());
		return mav;
	}
	
	// 그룹 생성시 그룹명 길 경우 예외처리
	@ExceptionHandler(FailedMakingGroupException.class )
	public @ResponseBody JsonResult failedMakingGroupException(FailedMakingGroupException e) {
		return new JsonResult().setSuccess(false).setMessage(e.getMessage());
	}
	
	// 그룹 멤버 추가시 예외처리
	@ExceptionHandler(FailedAddGroupMemberException.class)
	public @ResponseBody JsonResult failedAddGroupMemberException(FailedAddGroupMemberException e) {
		return new JsonResult().setSuccess(false).setMessage(e.getMessage());
	}
	
	// 허가되지않은 그룹 접속 시도 시 예외처리
	@ExceptionHandler(UnpermittedAccessGroupException.class)
	public ModelAndView unpermittedAccessGroupException(UnpermittedAccessGroupException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("/illegal");
		mav.addObject("errorMessage", "비정상적 접근시도.");
		return mav;
	}
	
	// 없는 그룹 삭제 시 예외처리
	@ExceptionHandler(FailedDeleteGroupException.class)
	public ModelAndView failedDeleteGroupException(FailedDeleteGroupException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("/exception");
		logger.debug("exception: {}", e.getClass().getSimpleName());
		return mav;
	}
	
	// 삭제 권한 없는 그룹 삭제 시 예외처리
	@ExceptionHandler(UnpermittedDeleteGroupException.class)
	public @ResponseBody JsonResult unpermittedDeleteGroupException(UnpermittedDeleteGroupException e) {
		return new JsonResult().setSuccess(false);
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exception(Exception e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("/exception");
		logger.debug("exception: {}", e.getClass().getSimpleName());
		return mav;
	}
	
	
}
