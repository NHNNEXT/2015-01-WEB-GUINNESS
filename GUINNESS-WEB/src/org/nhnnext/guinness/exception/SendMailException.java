package org.nhnnext.guinness.exception;

public class SendMailException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SendMailException() {
		super();
	}

	public SendMailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SendMailException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendMailException(String message) {
		super(message);
	}

	public SendMailException(Throwable cause) {
		super(cause);
	}
	
}
