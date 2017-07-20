package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the vehicle_detail database table.
 * 
 */
@Entity
@Table(name="VEHICLE_DETAIL")
@NamedQueries({
	@NamedQuery(name="VehicleDetail.findAll", query="SELECT v FROM VehicleDetail v"),
	@NamedQuery(name="VehicleDetail.findByVIN", query="SELECT v FROM VehicleDetail v where v.vin = :vin"),
	@NamedQuery(name="VehicleDetail.findByLicensePlateNumber", query="SELECT v FROM VehicleDetail v where v.licensePlateNo = :licensePlateNo"),
	@NamedQuery(name="VehicleDetail.findAvailableVehicle", query="SELECT v FROM VehicleDetail v where v.isEnable = 1 and v.id not in "
			+ "(SELECT d.vehicleDetail.id FROM DriverVehicleAssociation d where d.toDate is null or DATE(d.toDate) > DATE(NOW()))")
})
public class VehicleDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String accessories;

	private String color;

	private String comments;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private String insuranceCompany;

	@Lob
	private byte[] insuranceImage;

	private String insuranceNo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date insuranceValidUntil;

	private Integer isEnable;

	private String licensePlateNo;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private Integer seatingCapacity;

	@Temporal(TemporalType.DATE)
	private Date manufacturedYear;

	//bi-directional many-to-one association to CabPermit
	@OneToMany(mappedBy="vehicleDetail")
	private List<CabPermit> cabPermits;

	//bi-directional many-to-one association to DriverVehicleAssociation
	@OneToMany(mappedBy="vehicleDetail")
	private List<DriverVehicleAssociation> driverVehicleAssociations;

	//bi-directional many-to-one association to CabType
	@ManyToOne
	@JoinColumn(name="cabTypeId")
	private CabType cabType;

	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="status")
	private Status status;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="cabOwnerId")
	private User user;

	//bi-directional many-to-one association to Model
	@ManyToOne
	@JoinColumn(name="modelId")
	private Model model;
	
	private String vin;
	
	private String vehicleNumber;

	public VehicleDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessories() {
		return this.accessories;
	}

	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getInsuranceCompany() {
		return this.insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public byte[] getInsuranceImage() {
		return this.insuranceImage;
	}

	public void setInsuranceImage(byte[] insuranceImage) {
		this.insuranceImage = insuranceImage;
	}

	public String getInsuranceNo() {
		return this.insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public Date getInsuranceValidUntil() {
		return this.insuranceValidUntil;
	}

	public void setInsuranceValidUntil(Date insuranceValidUntil) {
		this.insuranceValidUntil = insuranceValidUntil;
	}

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getLicensePlateNo() {
		return this.licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
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

	public Integer getSeatingCapacity() {
		return this.seatingCapacity;
	}

	public void setSeatingCapacity(Integer seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public Date getManufacturedYear() {
		return this.manufacturedYear;
	}

	public void setManufacturedYear(Date manufacturedYear) {
		this.manufacturedYear = manufacturedYear;
	}

	public List<CabPermit> getCabPermits() {
		return this.cabPermits;
	}

	public void setCabPermits(List<CabPermit> cabPermits) {
		this.cabPermits = cabPermits;
	}

	public CabPermit addCabPermit(CabPermit cabPermit) {
		getCabPermits().add(cabPermit);
		cabPermit.setVehicleDetail(this);

		return cabPermit;
	}

	public CabPermit removeCabPermit(CabPermit cabPermit) {
		getCabPermits().remove(cabPermit);
		cabPermit.setVehicleDetail(null);

		return cabPermit;
	}

	public List<DriverVehicleAssociation> getDriverVehicleAssociations() {
		return this.driverVehicleAssociations;
	}

	public void setDriverVehicleAssociations(List<DriverVehicleAssociation> driverVehicleAssociations) {
		this.driverVehicleAssociations = driverVehicleAssociations;
	}

	public DriverVehicleAssociation addDriverVehicleAssociation(DriverVehicleAssociation driverVehicleAssociation) {
		getDriverVehicleAssociations().add(driverVehicleAssociation);
		driverVehicleAssociation.setVehicleDetail(this);

		return driverVehicleAssociation;
	}

	public DriverVehicleAssociation removeDriverVehicleAssociation(DriverVehicleAssociation driverVehicleAssociation) {
		getDriverVehicleAssociations().remove(driverVehicleAssociation);
		driverVehicleAssociation.setVehicleDetail(null);

		return driverVehicleAssociation;
	}

	public CabType getCabType() {
		return this.cabType;
	}

	public void setCabType(CabType cabType) {
		this.cabType = cabType;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
}