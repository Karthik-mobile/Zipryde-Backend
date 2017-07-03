package com.trivecta.zipryde.view.response;

public class CabTypeResponse {

	private Number cabTypeId;
	
	private String engineType;

	private int isEnable;

	private int level;

	private String type;

	private Double pricePerUnit;
	
	private Number seatingCapacity;
	
	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Number getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(Number seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	
}
