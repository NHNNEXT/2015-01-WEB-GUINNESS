package org.nhnnext.guinness.exception;

public class FailedDeleteGroupException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedDeleteGroupException() {
		super();
	}

	public FailedDeleteGroupException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedDeleteGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedDeleteGroupException(String message) {
		super(message);
	}

	public FailedDeleteGroupException(Throwable cause) {
		super(cause);
	}
}
