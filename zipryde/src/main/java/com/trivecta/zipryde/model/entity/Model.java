package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the model database table.
 * 
 */
@Entity
@Table(name="MODEL")
@NamedQueries({
	@NamedQuery(name="Model.findAll", query="SELECT m FROM Model m where m.isEnable = 1"),
	@NamedQuery(name="Model.findByMakeId", query="SELECT m FROM Model m where m.isEnable =1 and  m.make.id = :makeId")
})
public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer isEnable;

	private String model;

	//bi-directional many-to-one association to Make
	@ManyToOne
	@JoinColumn(name="makeId")
	private Make make;

	//bi-directional many-to-one association to VehicleDetail
	@OneToMany(mappedBy="model")
	private List<VehicleDetail> vehicleDetails;

	public Model() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Make getMake() {
		return this.make;
	}

	public void setMake(Make make) {
		this.make = make;
	}

	public List<VehicleDetail> getVehicleDetails() {
		return this.vehicleDetails;
	}

	public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetail addVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().add(vehicleDetail);
		vehicleDetail.setModel(this);

		return vehicleDetail;
	}

	public VehicleDetail removeVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().remove(vehicleDetail);
		vehicleDetail.setModel(null);

		return vehicleDetail;
	}

}