package org.nhnnext.guinness.exception.note;

public class UnpermittedAccessNotesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnpermittedAccessNotesException() {
		super();
	}

	public UnpermittedAccessNotesException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnpermittedAccessNotesException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnpermittedAccessNotesException(String message) {
		super(message);
	}

	public UnpermittedAccessNotesException(Throwable cause) {
		super(cause);
	}
}
