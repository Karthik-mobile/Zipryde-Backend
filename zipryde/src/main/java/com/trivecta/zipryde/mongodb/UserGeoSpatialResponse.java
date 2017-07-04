package com.trivecta.zipryde.mongodb;

import java.math.BigDecimal;

public class UserGeoSpatialResponse {

	private String userId;
	
	private BigDecimal latitude;
	
	private BigDecimal longitude;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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
}
