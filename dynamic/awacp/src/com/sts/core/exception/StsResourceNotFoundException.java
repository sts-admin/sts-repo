package com.sts.core.exception;

public class StsResourceNotFoundException extends StsCoreException {

	private static final long serialVersionUID = -7323647162714265154L;

	public StsResourceNotFoundException(String message){
		super(message);
	}

	public StsResourceNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
