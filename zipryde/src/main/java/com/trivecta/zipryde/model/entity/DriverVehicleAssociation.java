
package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the driver_vehicle_association database table.
 * 
 */
@Entity
@Table(name="DRIVER_VEHICLE_ASSOCIATION")
@NamedQueries({
	@NamedQuery(name="DriverVehicleAssociation.findAll", query="SELECT d FROM DriverVehicleAssociation d"),
	@NamedQuery(name="DriverVehicleAssociation.findAllByUserId", query="SELECT d FROM DriverVehicleAssociation d where d.user.id = :userId ORDER BY d.toDate DESC"),
	@NamedQuery(name="DriverVehicleAssociation.findActiveAssociationByUserId", 
		query="SELECT d FROM DriverVehicleAssociation d where d.user.id = :userId and DATE(d.fromDate) <= DATE(NOW()) and "
				+ "(d.toDate is null or d.toDate >= NOW()) "),
	@NamedQuery(name="DriverVehicleAssociation.findActiveAssociationByUserIds", 
	query="SELECT d FROM DriverVehicleAssociation d where DATE(d.fromDate) <= DATE(NOW()) and "
			+ "(d.toDate is null or d.toDate >= NOW()) and d.user.id in :userIds "),
	@NamedQuery(name="DriverVehicleAssociation.findByCabTypeAndUserIds", 
		query="SELECT d FROM DriverVehicleAssociation d where d.vehicleDetail.cabType.id = :cabTypeId and DATE(d.fromDate) <= DATE(NOW()) and (d.toDate is null or d.toDate >= NOW()) "
				+ " and d.user.id in :userIds order by d.toDate desc"),
	
})
public class DriverVehicleAssociation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fromDate;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date toDate;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="driverId")
	private User user;

	//bi-directional many-to-one association to VehicleDetail
	@ManyToOne
	@JoinColumn(name="cabId")
	private VehicleDetail vehicleDetail;

	public DriverVehicleAssociation() {
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

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
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

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VehicleDetail getVehicleDetail() {
		return this.vehicleDetail;
	}

	public void setVehicleDetail(VehicleDetail vehicleDetail) {
		this.vehicleDetail = vehicleDetail;
	}

}