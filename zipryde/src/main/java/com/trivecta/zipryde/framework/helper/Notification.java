package com.trivecta.zipryde.framework.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"notificationType",
	"bookingId",
	"offeredPrice",
	"suggestedPrice",
	"fromLocation",
	"toLocation",
	"distanceInMiles",
	"userName",
	"driverName",
	"driverId",
	"userId",
	"vehicleNumber"
})
public class Notification {
	
	private String notificationType;

	private  Integer bookingId;
	
	private double offeredPrice;
	
	private double suggestedPrice;
	
	private String fromLocation;
	
	private String toLocation;
	
	private double distanceInMiles;
	
	private String userName;
	
	private String driverName;
	
	private Integer  driverId;
	
	private Integer userId;

	private String vehicleNumber;

	
	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public double getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(double suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public double getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(double distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	
}
