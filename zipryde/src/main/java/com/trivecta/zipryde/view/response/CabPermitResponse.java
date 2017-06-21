package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"cabPermitId",
	"cabPermitNumber",
	"cabPermitValidUntil" 
})
public class CabPermitResponse {

	private Number cabPermitId;
	
	private String cabPermitNumber;
	
	private String cabPermitValidUntil;

	public String getCabPermitNumber() {
		return cabPermitNumber;
	}

	public void setCabPermitNumber(String cabPermitNumber) {
		this.cabPermitNumber = cabPermitNumber;
	}

	public String getCabPermitValidUntil() {
		return cabPermitValidUntil;
	}

	public void setCabPermitValidUntil(String cabPermitValidUntil) {
		this.cabPermitValidUntil = cabPermitValidUntil;
	}

	public Number getCabPermitId() {
		return cabPermitId;
	}

	public void setCabPermitId(Number cabPermitId) {
		this.cabPermitId = cabPermitId;
	}
	
}
