package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"percentage",
	"price"
})
public class NYOPResponse {

	private Number percentage;
	
	private String price;
	
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
}
