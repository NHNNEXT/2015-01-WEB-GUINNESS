package org.nhnnext.guinness.exception;

public class UnpermittedAccessGroupException extends Exception{
	private static final long serialVersionUID = 1L;
	public UnpermittedAccessGroupException() {
		super();
	}
	public UnpermittedAccessGroupException(Throwable cause) {
		super(cause);
	}
	public UnpermittedAccessGroupException(String message) {
		super(message);
	}
}
