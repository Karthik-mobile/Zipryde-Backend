package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "commissionId", "driverName", "commissionAmount", "calcualtedDate", "paidDate", "status" })
public class CommissionResponse {

	@JsonProperty("commissionId")
	private Integer commissionId;
	@JsonProperty("driverName")
	private String driverName;
	@JsonProperty("commissionAmount")
	private Double commissionAmount;
	@JsonProperty("calcualtedDate")
	private String calcualtedDate;
	@JsonProperty("paidDate")
	private String paidDate;
	@JsonProperty("status")
	private String status;

	public Integer getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Integer commissionId) {
		this.commissionId = commissionId;
	}
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public String getCalcualtedDate() {
		return calcualtedDate;
	}

	public void setCalcualtedDate(String calcualtedDate) {
		this.calcualtedDate = calcualtedDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
