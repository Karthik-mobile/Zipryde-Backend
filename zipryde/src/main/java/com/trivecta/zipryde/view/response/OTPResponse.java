package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"mobileNumber",
	"otp",
	"validity",
	"otpStatus"
})
public class OTPResponse {

	@JsonProperty("mobileNumber")
	private String mobileNumber;
	
	@JsonProperty("otp")
	private String otp;
		
	@JsonProperty("validity")
	private String validity;

	@JsonProperty("otpStatus")
	private String otpStatus;
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(String otpStatus) {
		this.otpStatus = otpStatus;
	}
}