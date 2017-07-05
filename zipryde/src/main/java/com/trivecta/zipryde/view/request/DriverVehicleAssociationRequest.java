package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"driverVehicleId",
	"cabId",
	"driverId",
	"fromDate",
	"toDate"
})

public class DriverVehicleAssociationRequest {

	private  Number driverVehicleId;
	
	private  Number cabId;
	
	private Number driverId;
	
	private String fromDate;
	
	private String toDate;

	public Number getCabId() {
		return cabId;
	}

	public void setCabId(Number cabId) {
		this.cabId = cabId;
	}

	public Number getDriverId() {
		return driverId;
	}

	public void setDriverId(Number driverId) {
		this.driverId = driverId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Number getDriverVehicleId() {
		return driverVehicleId;
	}

	public void setDriverVehicleId(Number driverVehicleId) {
		this.driverVehicleId = driverVehicleId;
	}
}
