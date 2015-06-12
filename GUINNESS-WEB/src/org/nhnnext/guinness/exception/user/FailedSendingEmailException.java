package org.nhnnext.guinness.exception.user;

public class FailedSendingEmailException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedSendingEmailException() {
		super();
	}

	public FailedSendingEmailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedSendingEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedSendingEmailException(String message) {
		super(message);
	}

	public FailedSendingEmailException(Throwable cause) {
		super(cause);
	}
	
}
