package org.nhnnext.guinness.exception;

public class UnpermittedDeleteGroupException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnpermittedDeleteGroupException() {
		super();
	}

	public UnpermittedDeleteGroupException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnpermittedDeleteGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnpermittedDeleteGroupException(String message) {
		super(message);
	}
}
