package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UserValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserValidationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserValidationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserValidationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
