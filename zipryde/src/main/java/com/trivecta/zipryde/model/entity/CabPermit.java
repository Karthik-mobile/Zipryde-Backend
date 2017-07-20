package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cab_permit database table.
 * 
 */
@Entity
@Table(name="CAB_PERMIT")
@NamedQuery(name="CabPermit.findAll", query="SELECT c FROM CabPermit c")
public class CabPermit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String permitNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date permitValidUntil;

	//bi-directional many-to-one association to VehicleDetail
	@ManyToOne
	@JoinColumn(name="cabId")
	private VehicleDetail vehicleDetail;

	public CabPermit() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermitNumber() {
		return this.permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}

	public Date getPermitValidUntil() {
		return this.permitValidUntil;
	}

	public void setPermitValidUntil(Date permitValidUntil) {
		this.permitValidUntil = permitValidUntil;
	}

	public VehicleDetail getVehicleDetail() {
		return this.vehicleDetail;
	}

	public void setVehicleDetail(VehicleDetail vehicleDetail) {
		this.vehicleDetail = vehicleDetail;
	}

}