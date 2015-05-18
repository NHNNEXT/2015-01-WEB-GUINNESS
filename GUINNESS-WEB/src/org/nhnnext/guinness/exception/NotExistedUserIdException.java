package org.nhnnext.guinness.exception;

public class NotExistedUserIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotExistedUserIdException() {
		super();
	}

	public NotExistedUserIdException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotExistedUserIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotExistedUserIdException(String message) {
		super(message);
	}

	public NotExistedUserIdException(Throwable cause) {
		super(cause);
	}
}
