package com.trivecta.zipryde.view.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

	private Number count;
	
	private Double revenueAmount;
	
	private String booking;

	public Number getCount() {
		return count;
	}

	public void setCount(Number count) {
		this.count = count;
	}	

	public Double getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(Double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

}
