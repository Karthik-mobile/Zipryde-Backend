package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the app_version database table.
 * 
 */
@Entity
@Table(name="APP_VERSION")
@NamedQueries({
	@NamedQuery(name="AppVersion.findAll", query="SELECT a FROM AppVersion a"),
	@NamedQuery(name="AppVersion.findByMobileOS", 
		query="SELECT a FROM AppVersion a where a.appMobileOS = :appMobileOS"),
	@NamedQuery(name="AppVersion.findByMobileOSAppName", 
	query="SELECT a FROM AppVersion a where a.appMobileOS = :appMobileOS and UPPER(a.appName) = UPPER(:appName)")
})
public class AppVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String appMobileOS;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private Integer modifedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private String appName;

	private String versionNumber;
	
	private Integer buildNo;
	
	private Integer isEnableValidation;

	public AppVersion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppMobileOS() {
		return this.appMobileOS;
	}

	public void setAppMobileOS(String appMobileOS) {
		this.appMobileOS = appMobileOS;
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

	public Integer getModifedBy() {
		return this.modifedBy;
	}

	public void setModifedBy(Integer modifedBy) {
		this.modifedBy = modifedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getIsEnableValidation() {
		return isEnableValidation;
	}

	public void setIsEnableValidation(Integer isEnableValidation) {
		this.isEnableValidation = isEnableValidation;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(Integer buildNo) {
		this.buildNo = buildNo;
	}

}