package org.nhnnext.guinness.exception;

public class AlreadyExistedUserIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public AlreadyExistedUserIdException() {
		super();
	}

	public AlreadyExistedUserIdException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlreadyExistedUserIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistedUserIdException(String message) {
		super(message);
	}

	public AlreadyExistedUserIdException(Throwable cause) {
		super(cause);
	}
	
}
