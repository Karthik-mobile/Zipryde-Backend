package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingTypeResponse {

	private Number pricingTypeId;
	
	private String pricingType;

	public Number getPricingTypeId() {
		return pricingTypeId;
	}

	public void setPricingTypeId(Number pricinTypeId) {
		this.pricingTypeId = pricinTypeId;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}
	
}
