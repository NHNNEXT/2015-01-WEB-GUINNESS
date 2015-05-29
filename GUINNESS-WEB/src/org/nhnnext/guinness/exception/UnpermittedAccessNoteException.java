package org.nhnnext.guinness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
public class UnpermittedAccessNoteException extends Exception{
	private static final long serialVersionUID = 1L;
	public UnpermittedAccessNoteException() {
		super();
	}
	public UnpermittedAccessNoteException(Throwable cause) {
		super(cause);
	}
	public UnpermittedAccessNoteException(String message) {
		super(message);
	}
}
