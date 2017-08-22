package com.trivecta.zipryde.framework.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
	@org.springframework.web.bind.annotation.ExceptionHandler({ MandatoryValidationException.class })
    protected ResponseEntity<Object> handleMandatoryException(Exception e, WebRequest request) {
		
        ClientErrorInformationException error = new ClientErrorInformationException(e.getMessage());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler({ UserValidationException.class })
    protected ResponseEntity<Object> handleUserValidationException(Exception e, WebRequest request) {
		
        ClientErrorInformationException error = new ClientErrorInformationException(e.getMessage());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNAUTHORIZED, request);
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler({ NoResultEntityException.class })
    protected ResponseEntity<Object> handleNoResultException(Exception e, WebRequest request) {
		
        ClientErrorInformationException error = new ClientErrorInformationException(e.getMessage());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
    }
	
	@org.springframework.web.bind.annotation.ExceptionHandler({ UserAlreadyLoggedInException.class })
    protected ResponseEntity<Object> handleUserAlreadyLoggedInException(Exception e, WebRequest request) {
		
        ClientErrorInformationException error = new ClientErrorInformationException(e.getMessage());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers,HttpStatus.CONFLICT, request);
    }
	
}
