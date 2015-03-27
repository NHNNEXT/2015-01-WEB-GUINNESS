package org.nhnnext.guinness.exception;

public class MakingObjectListFromJdbcException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MakingObjectListFromJdbcException(Exception e) {
		super(e.getMessage());
	}

}
