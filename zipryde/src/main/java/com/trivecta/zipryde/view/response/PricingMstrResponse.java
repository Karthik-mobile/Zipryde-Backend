package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingMstrResponse {

	private Number pricingMstrId;
	
	PricingTypeResponse pricingTypeResponse;
	
	CabTypeResponse cabTypeResponse;
	
	Double price;
	
	private Number isEnable;

	public Number getPricingMstrId() {
		return pricingMstrId;
	}

	public void setPricingMstrId(Number pricingMstrId) {
		this.pricingMstrId = pricingMstrId;
	}

	public PricingTypeResponse getPricingTypeResponse() {
		return pricingTypeResponse;
	}

	public void setPricingTypeResponse(PricingTypeResponse pricingTypeResponse) {
		this.pricingTypeResponse = pricingTypeResponse;
	}

	public CabTypeResponse getCabTypeResponse() {
		return cabTypeResponse;
	}

	public void setCabTypeResponse(CabTypeResponse cabTypeResponse) {
		this.cabTypeResponse = cabTypeResponse;
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
