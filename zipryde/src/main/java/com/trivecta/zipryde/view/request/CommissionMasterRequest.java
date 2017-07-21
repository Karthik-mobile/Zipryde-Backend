package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "noOfMiles", "noOfTrips", "commissionPercentage" })
public class CommissionMasterRequest {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("noOfMiles")
	private Integer noOfMiles;
	@JsonProperty("noOfTrips")
	private Integer noOfTrips;
	@JsonProperty("commissionPercentage")
	private Double commissionPercentage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNoOfMiles() {
		return noOfMiles;
	}

	public void setNoOfMiles(Integer noOfMiles) {
		this.noOfMiles = noOfMiles;
	}

	public Integer getNoOfTrips() {
		return noOfTrips;
	}

	public void setNoOfTrips(Integer noOfTrips) {
		this.noOfTrips = noOfTrips;
	}

	public Double getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(Double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}

	

}