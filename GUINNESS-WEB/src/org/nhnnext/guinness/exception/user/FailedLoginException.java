package org.nhnnext.guinness.exception.user;

public class FailedLoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedLoginException() {
		super();
	}

	public FailedLoginException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedLoginException(String message) {
		super(message);
	}

	public FailedLoginException(Throwable cause) {
		super(cause);
	}
	
}
