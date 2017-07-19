package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

	private Number count;

	public Number getCount() {
		return count;
	}

	public void setCount(Number count) {
		this.count = count;
	}
	
	private Double revenueAmount;

	public Double getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(Double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}
	
	

	
}
