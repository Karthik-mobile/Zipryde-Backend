
package com.trivecta.zipryde.view.response;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "userId", "userType", "firstName", "lastName", "mobileNumber", "alternateNumber", "emailId",
		"driverProfileId", "licenseNo", "licenseIssuedOn", "licenseValidUntil", "licenseFrontImage", "licenseBackImage",
		"defaultPercentageAccepted", "isLoggedIn", "isEnable", "status", "comments", "restriction", "userImage" })

public class UserResponse {

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

	// Format : MM-dd-YYYY
	@JsonProperty("licenseValidUntil")
	private String licenseValidUntil;

	@JsonProperty("defaultPercentageAccepted")
	private Number defaultPercentageAccepted;

	@JsonProperty("isLoggedIn")
	private Number isLoggedIn;

	@JsonProperty("isEnable")
	private Number isEnable;

	@JsonProperty("status")
	private String status;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("restriction")
	private String restriction;

	@JsonProperty("licenseFrontImage")
	String licenseFrontImage;

	@JsonProperty("licenseBackImage")
	String licenseBackImage;

	@JsonProperty("userImage")
	String userImage;

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

	public Number getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Number isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
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

	public String getLicenseFrontImage() {
		return licenseFrontImage;
	}

	public void setLicenseFrontImage(String licenseFrontImage) {
		this.licenseFrontImage = licenseFrontImage;
	}

	public String getLicenseBackImage() {
		return licenseBackImage;
	}

	public void setLicenseBackImage(String licenseBackImage) {
		this.licenseBackImage = licenseBackImage;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

}