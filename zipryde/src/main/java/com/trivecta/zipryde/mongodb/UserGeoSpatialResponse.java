package com.trivecta.zipryde.mongodb;

import java.math.BigDecimal;

public class UserGeoSpatialResponse {

	private Integer userId;
	
	private BigDecimal latitude;
	
	private BigDecimal longitude;


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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
