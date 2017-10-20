package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UserValidationException extends Exception{

	String errorCode;
	
	String errorMessage;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserValidationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public UserValidationException(String errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
		this.errorMessage = message;		
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
