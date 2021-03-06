package com.trivecta.zipryde.view.request;

import java.math.BigDecimal;

public class GeoLocationRequest {
	
	private String fromLatitude;
	
	private String fromLongitude;
	
	private String toLatitude;
	
	private String toLongitude;
		
	private String distanceInMiles;

	private Integer userId;
	
	private Number isOnline;
	
	public String getFromLatitude() {
		return fromLatitude;
	}

	public void setFromLatitude(String fromLatitude) {
		this.fromLatitude = fromLatitude;
	}

	public String getFromLongitude() {
		return fromLongitude;
	}

	public void setFromLongitude(String fromLongitude) {
		this.fromLongitude = fromLongitude;
	}

	public String getToLatitude() {
		return toLatitude;
	}

	public void setToLatitude(String toLatitude) {
		this.toLatitude = toLatitude;
	}

	public String getToLongitude() {
		return toLongitude;
	}

	public void setToLongitude(String toLongitude) {
		this.toLongitude = toLongitude;
	}

	public String getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(String distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}

	public Number getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Number isOnline) {
		this.isOnline = isOnline;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}	
}
