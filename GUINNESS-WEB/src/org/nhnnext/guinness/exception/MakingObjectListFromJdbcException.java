package org.nhnnext.guinness.exception;

public class MakingObjectListFromJdbcException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MakingObjectListFromJdbcException() {
		super();
	}

	public MakingObjectListFromJdbcException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MakingObjectListFromJdbcException(String message, Throwable cause) {
		super(message, cause);
	}

	public MakingObjectListFromJdbcException(String message) {
		super(message);
	}

	public MakingObjectListFromJdbcException(Throwable cause) {
		super(cause);
	}

}
