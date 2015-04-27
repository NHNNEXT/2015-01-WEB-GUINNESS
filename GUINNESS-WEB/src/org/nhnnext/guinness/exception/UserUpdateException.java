package org.nhnnext.guinness.exception;

public class UserUpdateException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserUpdateException() {
		super();
	}

	public UserUpdateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserUpdateException(String message) {
		super(message);
	}

	public UserUpdateException(Throwable cause) {
		super(cause);
	}
	
}
