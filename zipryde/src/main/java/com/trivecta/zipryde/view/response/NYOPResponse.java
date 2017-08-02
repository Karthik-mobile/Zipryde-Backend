package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"percentage",
	"price",
	"status",
	"errorMessage"
})
public class NYOPResponse {

	private Number percentage;
	
	private String price;
	
	private Boolean status;
	
	private String errorMessage;
	
	public Number getPercentage() {
		return percentage;
	}

	public void setPercentage(Number percentage) {
		this.percentage = percentage;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
