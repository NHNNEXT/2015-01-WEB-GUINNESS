package org.nhnnext.guinness.exception;

public class GroupUpdateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GroupUpdateException() {
		super();
	}

	public GroupUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GroupUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupUpdateException(String message) {
		super(message);
	}

	public GroupUpdateException(Throwable cause) {
		super(cause);
	}

}
