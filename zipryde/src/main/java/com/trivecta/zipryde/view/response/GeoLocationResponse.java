package com.trivecta.zipryde.view.response;

public class GeoLocationResponse {

	private String fromLatitude;
	
	private String fromLongitude;
	
	private String toLatitude;
	
	private String toLongitude;
		
	private Number distanceInMiles;

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

	public Number getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(Number distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}	
}