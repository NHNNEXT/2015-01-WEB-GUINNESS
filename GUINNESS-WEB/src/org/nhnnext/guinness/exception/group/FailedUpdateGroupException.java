package org.nhnnext.guinness.exception.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FailedUpdateGroupException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedUpdateGroupException() {
		super();
	}

	public FailedUpdateGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedUpdateGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedUpdateGroupException(String message) {
		super(message);
	}

	public FailedUpdateGroupException(Throwable cause) {
		super(cause);
	}

}
