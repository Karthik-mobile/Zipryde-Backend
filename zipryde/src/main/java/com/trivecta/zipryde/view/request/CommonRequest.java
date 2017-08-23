package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"makeId",
	"userType",
	"distanceInMiles",
	"cabTypeId",
	"userId",
	"cabId",
	"noOfPassengers",
	"status",
	"commissionId"
})
public class CommonRequest {

	private Number makeId;
	
	private String userType;
	
	private Number distanceInMiles;
	
	private Number cabTypeId;
	
	private Integer userId;
	
	private Number cabId;
	
	private Number noOfPassengers;

	private String status;
	
	private Number commissionId;
	
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Number getCabId() {
		return cabId;
	}

	public void setCabId(Number cabId) {
		this.cabId = cabId;
	}

	public Number getNoOfPassengers() {
		return noOfPassengers;
	}

	public void setNoOfPassengers(Number noOfPassengers) {
		this.noOfPassengers = noOfPassengers;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Number getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Number commissionId) {
		this.commissionId = commissionId;
	}
	
}
