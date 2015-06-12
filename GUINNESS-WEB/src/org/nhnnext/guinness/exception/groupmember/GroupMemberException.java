package org.nhnnext.guinness.exception.groupmember;

public class GroupMemberException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GroupMemberException() {
		super();
	}

	public GroupMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GroupMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupMemberException(String message) {
		super(message);
	}

	public GroupMemberException(Throwable cause) {
		super(cause);
	}

}
