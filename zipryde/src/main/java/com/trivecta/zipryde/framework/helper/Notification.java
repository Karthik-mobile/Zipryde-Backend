package com.trivecta.zipryde.framework.helper;

public class Notification {
	
	private String notificationType;

	private Integer bookingId;
	
	private String title;
	
	private String body;
	
	private boolean isDriver;
	
	private String ziprydeConfigType;
	
	private Integer commissionId;

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isDriver() {
		return isDriver;
	}

	public void setDriver(boolean isDriver) {
		this.isDriver = isDriver;
	}

	public String getZiprydeConfigType() {
		return ziprydeConfigType;
	}

	public void setZiprydeConfigType(String ziprydeConfigType) {
		this.ziprydeConfigType = ziprydeConfigType;
	}

	public Integer getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Integer commissionId) {
		this.commissionId = commissionId;
	}
	
}
