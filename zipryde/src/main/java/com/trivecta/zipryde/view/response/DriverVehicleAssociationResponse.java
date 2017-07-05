package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverVehicleAssociationResponse {


	private  Number driverVehicleId;
	
	private  Number cabId;
	
	private String  vin;
	
	private String licensePlateNumber;
	
	private Number driverId;
	
	private String driverName;
	
	private String fromDate;
	
	private String toDate;

	public Number getDriverVehicleId() {
		return driverVehicleId;
	}

	public void setDriverVehicleId(Number driverVehicleId) {
		this.driverVehicleId = driverVehicleId;
	}

	public Number getCabId() {
		return cabId;
	}

	public void setCabId(Number cabId) {
		this.cabId = cabId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Number getDriverId() {
		return driverId;
	}

	public void setDriverId(Number driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
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

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

}
