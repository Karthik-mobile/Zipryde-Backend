package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
public class SessionExpiredException extends Exception{

	private static final long serialVersionUID = 1L;

	public SessionExpiredException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionExpiredException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SessionExpiredException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SessionExpiredException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SessionExpiredException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
