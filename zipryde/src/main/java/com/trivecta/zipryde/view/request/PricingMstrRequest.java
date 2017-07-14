package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingMstrRequest {

	private Number pricingMstrId;
	
	private Number cabTypeId;
	
	private Number  pricingTypeId;
	
	private Double price;

	private Number isEnable;

	public Number getPricingMstrId() {
		return pricingMstrId;
	}

	public void setPricingMstrId(Number pricingMstrId) {
		this.pricingMstrId = pricingMstrId;
	}

	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public Number getPricingTypeId() {
		return pricingTypeId;
	}

	public void setPricingTypeId(Number pricingTypeId) {
		this.pricingTypeId = pricingTypeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Number getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Number isEnable) {
		this.isEnable = isEnable;
	}
	
	
	
	
	
	
		
}
