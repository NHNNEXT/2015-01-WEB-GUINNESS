package org.nhnnext.guinness.exception.user;

public class NotExistedUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotExistedUserException() {
		super();
	}

	public NotExistedUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotExistedUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotExistedUserException(String message) {
		super(message);
	}

	public NotExistedUserException(Throwable cause) {
		super(cause);
	}
}
