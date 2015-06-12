package org.nhnnext.guinness.exception.user;

public class FailedUpdateUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedUpdateUserException() {
		super();
	}

	public FailedUpdateUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedUpdateUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedUpdateUserException(String message) {
		super(message);
	}

	public FailedUpdateUserException(Throwable cause) {
		super(cause);
	}
	
}
