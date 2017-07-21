package com.trivecta.zipryde.view.response;

import java.math.BigDecimal;

public class UserGeoSpatialResponse {
	
	private Number userId;
	
	private BigDecimal latitude;
	
	private BigDecimal longitude;
	
	private Number isOnline;
	
	private String cabType;
	
	private Number cabTypeId;

	public UserGeoSpatialResponse() {
		//Nothing to do
	}
	
	public UserGeoSpatialResponse(Integer userId,BigDecimal longitude,BigDecimal latitude) {
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Number getUserId() {
		return userId;
	}

	public void setUserId(Number userId) {
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

	public Number getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Number isOnline) {
		this.isOnline = isOnline;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}


}
