package com.sts.core.exception;

public class UnknownServiceProvider extends Exception {

	private static final long serialVersionUID = -7323647162714265154L;

	public UnknownServiceProvider(String message){
		super(message);
	}

	public UnknownServiceProvider(String message, Throwable cause){
		super(message, cause);
	}
}
