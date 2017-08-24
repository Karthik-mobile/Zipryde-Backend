package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class UserAlreadyLoggedInException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyLoggedInException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyLoggedInException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyLoggedInException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyLoggedInException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyLoggedInException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
