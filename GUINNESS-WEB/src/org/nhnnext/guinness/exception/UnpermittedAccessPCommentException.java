package org.nhnnext.guinness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
public class UnpermittedAccessPCommentException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public UnpermittedAccessPCommentException() {
		super();
	}
	public UnpermittedAccessPCommentException(Throwable cause) {
		super(cause);
	}
	public UnpermittedAccessPCommentException(String message) {
		super(message);
	}
}
