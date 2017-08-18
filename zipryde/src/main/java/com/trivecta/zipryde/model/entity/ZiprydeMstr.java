package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the zipryde_mstr database table.
 * 
 */
@Entity
@Table(name="ZIPRYDE_MSTR")
@NamedQueries({
	@NamedQuery(name="ZiprydeMstr.findAll", query="SELECT z FROM ZiprydeMstr z"),
	@NamedQuery(name="ZiprydeMstr.findByType", query="SELECT z FROM ZiprydeMstr z where z.type = :type")
})
public class ZiprydeMstr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "`type`")
	private String type;

	@Column(name = "`value`")
	private String value;

	public ZiprydeMstr() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



}