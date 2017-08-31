package com.trivecta.zipryde.view.response;

import java.math.BigDecimal;

public class UserGeoSpatialResponse {
	
	private Integer userId;
	
	private BigDecimal latitude;
	
	private BigDecimal longitude;
	
	private Integer isOnline;
	
	private String cabType;
	
	private Integer cabTypeId;
	
	private Integer bookingId;
	
	private  String bookingStatusCode;
	
	private  String bookingStatus;
	
	public UserGeoSpatialResponse() {
		//Nothing to do
	}
	
	public UserGeoSpatialResponse(Integer userId,BigDecimal longitude,BigDecimal latitude) {
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public Integer getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Integer cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getBookingStatusCode() {
		return bookingStatusCode;
	}

	public void setBookingStatusCode(String bookingStatusCode) {
		this.bookingStatusCode = bookingStatusCode;
	}


}
