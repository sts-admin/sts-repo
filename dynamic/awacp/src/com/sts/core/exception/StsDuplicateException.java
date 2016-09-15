package com.sts.core.exception;

public class StsDuplicateException extends StsCoreException {

	private static final long serialVersionUID = -7323647162714265154L;

	public StsDuplicateException(String message){
		super(message);
	}

	public StsDuplicateException(String message, Throwable cause){
		super(message, cause);
	}
}
