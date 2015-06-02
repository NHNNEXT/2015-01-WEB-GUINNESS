package org.nhnnext.guinness.exception.groupmember;

public class FailedAddingGroupMemberException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedAddingGroupMemberException() {
		super();
	}

	public FailedAddingGroupMemberException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedAddingGroupMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedAddingGroupMemberException(String message) {
		super(message);
	}

	public FailedAddingGroupMemberException(Throwable cause) {
		super(cause);
	}
}
