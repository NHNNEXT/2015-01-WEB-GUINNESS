package org.nhnnext.guinness.controller;

import org.nhnnext.guinness.exception.groupmember.GroupMemberException;
import org.nhnnext.guinness.exception.user.AlreadyExistedUserException;
import org.nhnnext.guinness.exception.user.FailedLoginException;
import org.nhnnext.guinness.exception.user.FailedUpdateUserException;
import org.nhnnext.guinness.exception.user.JoinValidationException;
import org.nhnnext.guinness.exception.user.NotExistedUserException;
import org.nhnnext.guinness.util.JSONResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
	// 회원가입시 중복 아이디 예외처리
	@ExceptionHandler(AlreadyExistedUserException.class)
	public ResponseEntity<Object> alreadyExistedUserIdException(AlreadyExistedUserException e) {
		return JSONResponseUtil.getJSONResponse("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
	}
	
	// 회원가입시 유효성 검사 예외처리
	@ExceptionHandler(JoinValidationException.class)
	public ResponseEntity<Object> joinValidationException(JoinValidationException e) {
		return JSONResponseUtil.getJSONResponse(e.getExtractValidationMessages(), HttpStatus.PRECONDITION_FAILED);
	}
	
	// 로그인 실패시 예외처리
	@ExceptionHandler(FailedLoginException.class)
	public ResponseEntity<Object> failedLoginException(FailedLoginException e) {
		return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	// 회원정보 수정시 예외처리
	@ExceptionHandler(FailedUpdateUserException.class)
	public ModelAndView userUpdateException(FailedUpdateUserException e) {
		RedirectView rv = new RedirectView("/user/form");
		rv.setExposeModelAttributes(false);
		ModelAndView mav = new ModelAndView(rv);
		mav.addObject("message", e.getMessage());
		logger.debug("UserUpdateException:{}", e.getMessage());
		return mav;
	}
	
//	// 그룹정보 수정시 예외처리 API전달
//	@ExceptionHandler(GroupUpdateException.class)
//	public ResponseEntity<Object> groupUpdateException(GroupUpdateException e) {
//		return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//	}
	
	// 그룹 탈퇴 시 예외처리
	// 그룹 추방 시 예외처리
	@ExceptionHandler(GroupMemberException.class)
	public ResponseEntity<Object> groupMemberException(GroupMemberException e) {
		return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	// 그룹 멤버 추가 실패 시 예외처리
	@ExceptionHandler(GroupMemberException.class)
	public ResponseEntity<Object> failedAddGroupMemberException(GroupMemberException e) {
		return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}

	// 비밀번호 찾기시 미존재 아이디 예외처리
	@ExceptionHandler(NotExistedUserException.class)
	public ModelAndView notExistedUserIdException(NotExistedUserException e) {
		ModelAndView mav = new ModelAndView("/findPassword");
		mav.addObject("message", "존재하지 않는 이메일입니다.");
		return mav;
	}
}
