package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the zipryde_configurations database table.
 * 
 */
@Entity
@Table(name="ZIPRYDE_CONFIGURATIONS")
@NamedQueries({
	@NamedQuery(name="ZiprydeConfiguration.findAll", query="SELECT z FROM ZiprydeConfiguration z"),
	@NamedQuery(name="ZiprydeConfiguration.findByType", query="SELECT z FROM ZiprydeConfiguration z where z.type = :type")
})
public class ZiprydeConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String accessKey;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private String type;

	private String url;
	
	private String accoutSID;
	
	private String twilioNo;
	

	public ZiprydeConfiguration() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessKey() {
		return this.accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccoutSID() {
		return accoutSID;
	}

	public void setAccoutSID(String accoutSID) {
		this.accoutSID = accoutSID;
	}

	public String getTwilioNo() {
		return twilioNo;
	}

	public void setTwilioNo(String twilioNo) {
		this.twilioNo = twilioNo;
	}

}