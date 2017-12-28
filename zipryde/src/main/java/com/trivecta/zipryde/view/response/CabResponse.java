package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"cabId",
	"cabTypeId", 
	"cabType",
	"makeId", 
	"modelId",
	"yearOfManufactured",
	"licensePlateNo",
	"color",
	"seatingCapacity", 
	"accessories", 
	"enableCab",
	"insuranceCompanyName", 
	"insuranceNumber", 
	"insuranceValidUntil",
	"vin",
	"vehicleNumber",
	"status",
	"comments",
	"cabPermitResponse",
	"cabImage"
})
public class CabResponse {
	
	private Number cabId;
	
	private Number cabTypeId;
	
	private String cabType;
	
	private Number makeId;
	
	private Number modelId;
	
	//MM-DD-YYYY
	private String yearOfManufactured;
	
	private String licensePlateNo;
	
	private String color;
	
	private Number seatingCapacity;
	
	private String accessories;
	
	private Number enableCab;
	
	private String insuranceCompanyName;
	
	private String insuranceNumber;
	
	//MM-DD-YYYY
	private String insuranceValidUntil;
	
	private String vin;
	
	private String vehicleNumber;
	
	private String status;

	private String comments;
	
	private CabPermitResponse  cabPermitResponse;
	
	/* MAIL Changes : ZipRyde App Changes to be compliant with TX State Requirements */
	private String cabImage;

	public Number getCabId() {
		return cabId;
	}

	public void setCabId(Number cabId) {
		this.cabId = cabId;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Number getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(Number seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public String getAccessories() {
		return accessories;
	}

	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}

	public Number getEnableCab() {
		return enableCab;
	}

	public void setEnableCab(Number enableCab) {
		this.enableCab = enableCab;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getInsuranceValidUntil() {
		return insuranceValidUntil;
	}

	public void setInsuranceValidUntil(String insuranceValidUntil) {
		this.insuranceValidUntil = insuranceValidUntil;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public CabPermitResponse getCabPermitResponse() {
		return cabPermitResponse;
	}

	public void setCabPermitResponse(CabPermitResponse cabPermitResponse) {
		this.cabPermitResponse = cabPermitResponse;
	}

	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public String getYearOfManufactured() {
		return yearOfManufactured;
	}

	public void setYearOfManufactured(String yearOfManufactured) {
		this.yearOfManufactured = yearOfManufactured;
	}

	public Number getMakeId() {
		return makeId;
	}

	public void setMakeId(Number makeId) {
		this.makeId = makeId;
	}

	public Number getModelId() {
		return modelId;
	}

	public void setModelId(Number modelId) {
		this.modelId = modelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getCabImage() {
		return cabImage;
	}

	public void setCabImage(String cabImage) {
		this.cabImage = cabImage;
	}
	
}
