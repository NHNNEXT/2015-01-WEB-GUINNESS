package org.nhnnext.guinness.exception;

public class FailedAddGroupMemberException extends Exception {
	private static final long serialVersionUID = 1L;

	public FailedAddGroupMemberException() {
		super();
	}

	public FailedAddGroupMemberException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedAddGroupMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedAddGroupMemberException(String message) {
		super(message);
	}

	public FailedAddGroupMemberException(Throwable cause) {
		super(cause);
	}
}
