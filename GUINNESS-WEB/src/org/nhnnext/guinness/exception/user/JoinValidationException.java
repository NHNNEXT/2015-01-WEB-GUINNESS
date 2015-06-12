package org.nhnnext.guinness.exception.user;



public class JoinValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Object extractValidationMessages;
	
	public JoinValidationException() {
		super();
	}

	public JoinValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JoinValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JoinValidationException(String message) {
		super(message);
	}

	public JoinValidationException(Throwable cause) {
		super(cause);
	}

	public JoinValidationException(Object extractValidationMessages) {
		this.extractValidationMessages = extractValidationMessages;
	}

	public Object getExtractValidationMessages() {
		return extractValidationMessages;
	}

	public void setExtractValidationMessages(Object extractValidationMessages) {
		this.extractValidationMessages = extractValidationMessages;
	}
	
	
}
