package org.nhnnext.guinness.exception.user;


public class AlreadyExistedUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AlreadyExistedUserException() {
		super();
	}

	public AlreadyExistedUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlreadyExistedUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistedUserException(String message) {
		super(message);
	}

	public AlreadyExistedUserException(Throwable cause) {
		super(cause);
	}
	
}
