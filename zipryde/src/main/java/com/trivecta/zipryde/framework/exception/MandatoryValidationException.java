package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class MandatoryValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MandatoryValidationException(String message){
		super(message);
	}

}
