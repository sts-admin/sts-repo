package com.sts.core.exception;

public class StsCoreException extends Exception {

	private static final long serialVersionUID = -7323647162714265154L;

	public StsCoreException(String message){
		super(message);
	}

	public StsCoreException(String message, Throwable cause){
		super(message, cause);
	}
}
