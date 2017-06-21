package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"makeId",
	"userType",
	"distanceInMiles",
	"cabTypeId"
})
public class CommonRequest {

	private Number makeId;
	
	private String userType;
	
	private Number distanceInMiles;
	
	private Number cabTypeId;

	public Number getMakeId() {
		return makeId;
	}

	public void setMakeId(Number makeId) {
		this.makeId = makeId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Number getDistanceInMiles() {
		return distanceInMiles;
	}

	public void setDistanceInMiles(Number distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}

	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}
}
