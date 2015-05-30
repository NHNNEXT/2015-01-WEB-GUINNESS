package org.nhnnext.guinness.exception;

public class FailedMakingGroupException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedMakingGroupException() {
		super();
	}

	public FailedMakingGroupException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedMakingGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedMakingGroupException(String message) {
		super(message);
	}

	public FailedMakingGroupException(Throwable cause) {
		super(cause);
	}
	
}
