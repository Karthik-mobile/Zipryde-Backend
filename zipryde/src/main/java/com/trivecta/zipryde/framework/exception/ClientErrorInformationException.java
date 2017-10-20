package com.trivecta.zipryde.framework.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"errorCode",
    "message"
})
public class ClientErrorInformationException {


	@JsonProperty("errorCode")
	private String errorCode;
	
	@JsonProperty("message")
	private String message;
	
	public ClientErrorInformationException(String message){
		this.message = message;
	}
	
	public ClientErrorInformationException(String errorCode,String message){
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}