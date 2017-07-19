package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the driver_profile database table.
 * 
 */
@Entity
@Table(name="DRIVER_PROFILE")
@NamedQuery(name="DriverProfile.findAll", query="SELECT d FROM DriverProfile d")
public class DriverProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private Integer defaultPercentage;

	private Integer isOwnVehicle;

	@Lob
	private byte[] licenseBackImage;

	@Lob
	private byte[] licenseFrontImage;

	private String licenseNo;
	
	@Temporal(TemporalType.DATE)
	private Date licenseIssuedOn;
	
	@Temporal(TemporalType.DATE)
	private Date licenseValidUntil;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private String restrictions;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="userId")
	private User user;
	
	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="status")
	private Status status;

	private String comments;
	
	@Lob
	private byte[] driverProfileImage;
	
	private String vehicleNumber;
	
	public DriverProfile() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getDefaultPercentage() {
		return this.defaultPercentage;
	}

	public void setDefaultPercentage(Integer defaultPercentage) {
		this.defaultPercentage = defaultPercentage;
	}

	public Integer getIsOwnVehicle() {
		return this.isOwnVehicle;
	}

	public void setIsOwnVehicle(Integer isOwnVehicle) {
		this.isOwnVehicle = isOwnVehicle;
	}

	public byte[] getLicenseBackImage() {
		return this.licenseBackImage;
	}

	public void setLicenseBackImage(byte[] licenseBackImage) {
		this.licenseBackImage = licenseBackImage;
	}

	public byte[] getLicenseFrontImage() {
		return this.licenseFrontImage;
	}

	public void setLicenseFrontImage(byte[] licenseFrontImage) {
		this.licenseFrontImage = licenseFrontImage;
	}

	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Date getLicenseValidUntil() {
		return this.licenseValidUntil;
	}

	public void setLicenseValidUntil(Date licenseValidUntil) {
		this.licenseValidUntil = licenseValidUntil;
	}

	public Integer getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRestrictions() {
		return this.restrictions;
	}

	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getLicenseIssuedOn() {
		return licenseIssuedOn;
	}

	public void setLicenseIssuedOn(Date licenseIssuedOn) {
		this.licenseIssuedOn = licenseIssuedOn;
	}

	public byte[] getDriverProfileImage() {
		return driverProfileImage;
	}

	public void setDriverProfileImage(byte[] driverProfileImage) {
		this.driverProfileImage = driverProfileImage;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	

}