package org.nhnnext.guinness.exception.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
public class UnpermittedAccessGroupException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public UnpermittedAccessGroupException() {
		super();
	}
	public UnpermittedAccessGroupException(Throwable cause) {
		super(cause);
	}
	public UnpermittedAccessGroupException(String message) {
		super(message);
	}
}
