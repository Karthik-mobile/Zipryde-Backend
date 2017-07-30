package com.trivecta.zipryde.view.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "userId", "userType", "firstName", "lastName", "mobileNumber", "password", "alternateNumber",
		"emailId", "driverProfileId", "licenseNo", "licenseIssuedOn", "licenseValidUntil", "defaultPercentageAccepted",
		"isEnable", "status", "comments", "restriction", "deviceToken", "licenseFrontImage", "licenseBackImage", "userImage"
		// "licenseImages"
})
public class UserRequest {

	@JsonProperty("userId")
	private Number userId;

	@JsonProperty("userType")
	private String userType;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("password")
	private String password;

	@JsonProperty("alternateNumber")
	private String alternateNumber;

	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("driverProfileId")
	private Number driverProfileId;

	@JsonProperty("licenseNo")
	private String licenseNo;

	@JsonProperty("licenseIssuedOn")
	private String licenseIssuedOn;

	@JsonProperty("licenseValidUntil")
	private String licenseValidUntil;

	@JsonProperty("defaultPercentageAccepted")
	private Number defaultPercentageAccepted;

	@JsonProperty("isEnable")
	private Number isEnable;

	@JsonProperty("status")
	private String status;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("restriction")
	private String restriction;

	@JsonProperty("licenseFrontImage")
	MultipartFile licenseFrontImage;

	@JsonProperty("licenseBackImage")
	MultipartFile licenseBackImage;

	@JsonProperty("userImage")
	MultipartFile userImage;

	@JsonProperty("deviceToken")
	private String deviceToken;
	
	public Number getUserId() {
		return userId;
	}

	public void setUserId(Number userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Number getDriverProfileId() {
		return driverProfileId;
	}

	public void setDriverProfileId(Number driverProfileId) {
		this.driverProfileId = driverProfileId;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseValidUntil() {
		return licenseValidUntil;
	}

	public void setLicenseValidUntil(String licenseValidUntil) {
		this.licenseValidUntil = licenseValidUntil;
	}

	public Number getDefaultPercentageAccepted() {
		return defaultPercentageAccepted;
	}

	public void setDefaultPercentageAccepted(Number defaultPercentageAccepted) {
		this.defaultPercentageAccepted = defaultPercentageAccepted;
	}

	public String getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getLicenseIssuedOn() {
		return licenseIssuedOn;
	}

	public void setLicenseIssuedOn(String licenseIssuedOn) {
		this.licenseIssuedOn = licenseIssuedOn;
	}

	public Number getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Number isEnable) {
		this.isEnable = isEnable;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public MultipartFile getLicenseFrontImage() {
		return licenseFrontImage;
	}

	public void setLicenseFrontImage(MultipartFile licenseFrontImage) {
		this.licenseFrontImage = licenseFrontImage;
	}

	public MultipartFile getLicenseBackImage() {
		return licenseBackImage;
	}

	public void setLicenseBackImage(MultipartFile licenseBackImage) {
		this.licenseBackImage = licenseBackImage;
	}

	public MultipartFile getUserImage() {
		return userImage;
	}

	public void setUserImage(MultipartFile userImage) {
		this.userImage = userImage;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

}
