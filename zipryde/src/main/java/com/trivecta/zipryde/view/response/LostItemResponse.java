package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"lostItemId",
	"bookingId",
	"crnNumber",
	"userMobileNumber",
	"driverMobileNumber",
	"comments"
})
public class LostItemResponse {

	private Integer lostItemId;
	
	private Integer bookingId;
	
	private String crnNumber;
	
	private String userMobileNumber;
	
	private String driverMobileNumber;
	
	private String comments;

	public Integer getLostItemId() {
		return lostItemId;
	}

	public void setLostItemId(Integer lostItemId) {
		this.lostItemId = lostItemId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public String getDriverMobileNumber() {
		return driverMobileNumber;
	}

	public void setDriverMobileNumber(String driverMobileNumber) {
		this.driverMobileNumber = driverMobileNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCrnNumber() {
		return crnNumber;
	}

	public void setCrnNumber(String crnNumber) {
		this.crnNumber = crnNumber;
	}
	
}
