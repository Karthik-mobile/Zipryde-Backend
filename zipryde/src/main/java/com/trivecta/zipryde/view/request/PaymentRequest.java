package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"paymentId",
	"bookingId",
	"amountPaid",
	"paymentType",
	"paidDateTime"
})
public class PaymentRequest {	
	
	@JsonProperty("paymentId")
	private Number paymentId;
	@JsonProperty("bookingId")
	private Number bookingId;
	@JsonProperty("amountPaid")
	private Double amountPaid;
	@JsonProperty("paymentType")
	private String paymentType;
	@JsonProperty("paidDateTime")
	private String paidDateTime;

	public Number getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Number paymentId) {
		this.paymentId = paymentId;
	}

	public Number getBookingId() {
		return bookingId;
	}

	public void setBookingId(Number bookingId) {
		this.bookingId = bookingId;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaidDateTime() {
		return paidDateTime;
	}

	public void setPaidDateTime(String paidDateTime) {
		this.paidDateTime = paidDateTime;
	}

}