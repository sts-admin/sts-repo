/**
 * 
 */
package com.sts.core.exception;


public class StsCoreRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7690332309949219145L;

	public StsCoreRuntimeException(String message){
		super(message);
	}

	public StsCoreRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
