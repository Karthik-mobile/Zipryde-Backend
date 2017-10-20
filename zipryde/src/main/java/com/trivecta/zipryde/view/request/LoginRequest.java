package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {

	@JsonProperty("userType")
	private String userType;
	
	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("password")
	private String password;	

	@JsonProperty("emailId")
	private String emailId;	

	@JsonProperty("deviceToken")
	private String deviceToken;
	
	@JsonProperty("overrideSessionToken")
	private Integer overrideSessionToken;
	
	private String appName;
	
	private  String versionNumber;
	
	private String mobileOS;
	
	private Integer buildNo;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Integer getOverrideSessionToken() {
		return overrideSessionToken;
	}

	public void setOverrideSessionToken(Integer overrideSessionToken) {
		this.overrideSessionToken = overrideSessionToken;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getMobileOS() {
		return mobileOS;
	}

	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}

	public Integer getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(Integer buildNo) {
		this.buildNo = buildNo;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
