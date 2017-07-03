package com.trivecta.zipryde.view.response;

import java.math.BigDecimal;

public class UserGeoSpatialResponse {
	
	private int userId;
	
	private BigDecimal latitude;
	
	private BigDecimal longitude;
	
	private Number isOnline;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
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
}
