package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="USER")
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u  order by u.id desc"),
	@NamedQuery(name="User.findByUserType", query="SELECT u FROM User u where u.userType.type = :userType and u.isDeleted = 0 ORDER BY u.id DESC"),
	@NamedQuery(name="User.findByMobileNo", query="SELECT u FROM User u where u.mobileNumber = :mobileNumber and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByMobileNoAndUserType", 
		query="SELECT u FROM User u where u.mobileNumber = :mobileNumber and u.userType.type = :userType and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByEmailIdAndUserType", 
		query="SELECT u FROM User u where u.emailId = :emailId and u.userType.type = :userType and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByMobileNoPsswdAndUserType", 
		query="SELECT u FROM User u where u.mobileNumber = :mobileNumber and u.password = :password and u.userType.type = :userType and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByEmailIdPsswdAndUserType", 
		query="SELECT u FROM User u where u.emailId = :emailId and u.password = :password and u.userType.type = :userType and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByMobileNoPsswdUserTypeIsEnable", 
		query="SELECT u FROM User u where u.mobileNumber = :mobileNumber and u.password = :password and u.userType.type = :userType and u.isEnable = :isEnable and u.isDeleted = 0 "),
	@NamedQuery(name="User.findByTypeAndStatus", 
		query="SELECT u FROM User u where u.userType.type = :userType and u.driverProfile.status.status = :status and u.isDeleted = 0 order by u.id desc"),
	@NamedQuery(name="User.countByTypeAndStatus", 
		query="SELECT count(u) FROM User u where u.userType.type = :userType and u.driverProfile.status.status = :status and u.isDeleted = 0"),
	/*@NamedQuery(name="User.findByApprovedAndEnabled", 
		query="SELECT u FROM User u where u.userType.type = :userType and u.isEnable = :isEnable and u.driverProfile.status.status = :status order by u.id desc"),*/
	@NamedQuery(name="User.findByOnline", 
			query="SELECT u FROM User u where u.userType.type = :userType and u.isEnable = 1 and u.isDeleted = 0 and "
					+ "u.id in (select us.userId FROM UserSession us where us.isActive = 1 and us.logOutDateTime is null)"),
	@NamedQuery(name="User.countByOnline", 
		query="SELECT count(u) FROM User u where u.userType.type = :userType and u.isEnable = 1 and u.isDeleted = 0 and "
			+ "u.id in (select us.userId FROM UserSession us where us.isActive = 1 and us.logOutDateTime is null)"),
	@NamedQuery(name="User.findUnAssignedDriver", 
		query="SELECT u FROM User u where u.userType.type = :userType and u.driverProfile.status.status in :status and u.isDeleted = 0 order by u.id desc"),
	@NamedQuery(name="User.countByUnAssignedDriver", 
		query="SELECT count(u) FROM User u where u.userType.type = :userType and u.driverProfile.status.status in :status and u.isDeleted = 0")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String alternateNumber;

	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private String emailId;

	private String firstName;

	private Integer isEnable;
	
	private Integer isDeleted;

	private String lastName;

	private String mobileNumber;

	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	private String password;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="driver")
	private List<Booking> bookings1;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="rider")
	private List<Booking> bookings2;

	//bi-directional many-to-one association to BookingHistory
	@OneToMany(mappedBy="driver")
	private List<BookingHistory> bookingHistories1;

	//bi-directional many-to-one association to BookingHistory
	@OneToMany(mappedBy="rider")
	private List<BookingHistory> bookingHistories2;

	//bi-directional many-to-one association to BookingRequest
	@OneToMany(mappedBy="user")
	private List<BookingRequest> bookingRequests;

	//bi-directional many-to-one association to Commission
	@OneToMany(mappedBy="user")
	private List<Commission> commissions;

	//bi-directional many-to-one association to DriverVehicleAssociation
	@OneToMany(mappedBy="user")
	private List<DriverVehicleAssociation> driverVehicleAssociations;

	//bi-directional many-to-one association to UserType
	@ManyToOne
	@JoinColumn(name="userType")
	private UserType userType;

	//bi-directional many-to-one association to UserPreferedLocation
	@OneToMany(mappedBy="user")
	private List<UserPreferedLocation> userPreferedLocations;

	//bi-directional many-to-one association to VehicleDetail
	@OneToMany(mappedBy="user")
	private List<VehicleDetail> vehicleDetails;

	//bi-directional one-to-one association to DriverProfile
	@OneToOne(mappedBy="user")
	private DriverProfile driverProfile;

	private Integer cancellationCount;

	private String deviceToken;
	
	@Transient 
	private int isOnline;
		
	public User() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAlternateNumber() {
		return this.alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
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

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Booking> getBookings1() {
		return this.bookings1;
	}

	public void setBookings1(List<Booking> bookings1) {
		this.bookings1 = bookings1;
	}

	public Booking addBookings1(Booking bookings1) {
		getBookings1().add(bookings1);
		bookings1.setDriver(this);

		return bookings1;
	}

	public Booking removeBookings1(Booking bookings1) {
		getBookings1().remove(bookings1);
		bookings1.setDriver(null);

		return bookings1;
	}

	public List<Booking> getBookings2() {
		return this.bookings2;
	}

	public void setBookings2(List<Booking> bookings2) {
		this.bookings2 = bookings2;
	}

	public Booking addBookings2(Booking bookings2) {
		getBookings2().add(bookings2);
		bookings2.setRider(this);

		return bookings2;
	}

	public Booking removeBookings2(Booking bookings2) {
		getBookings2().remove(bookings2);
		bookings2.setRider(null);

		return bookings2;
	}

	public List<BookingHistory> getBookingHistories1() {
		return this.bookingHistories1;
	}

	public void setBookingHistories1(List<BookingHistory> bookingHistories1) {
		this.bookingHistories1 = bookingHistories1;
	}

	public BookingHistory addBookingHistories1(BookingHistory bookingHistories1) {
		getBookingHistories1().add(bookingHistories1);
		bookingHistories1.setDriver(this);

		return bookingHistories1;
	}

	public BookingHistory removeBookingHistories1(BookingHistory bookingHistories1) {
		getBookingHistories1().remove(bookingHistories1);
		bookingHistories1.setDriver(null);

		return bookingHistories1;
	}

	public List<BookingHistory> getBookingHistories2() {
		return this.bookingHistories2;
	}

	public void setBookingHistories2(List<BookingHistory> bookingHistories2) {
		this.bookingHistories2 = bookingHistories2;
	}

	public BookingHistory addBookingHistories2(BookingHistory bookingHistories2) {
		getBookingHistories2().add(bookingHistories2);
		bookingHistories2.setRider(this);

		return bookingHistories2;
	}

	public BookingHistory removeBookingHistories2(BookingHistory bookingHistories2) {
		getBookingHistories2().remove(bookingHistories2);
		bookingHistories2.setRider(null);

		return bookingHistories2;
	}

	public List<BookingRequest> getBookingRequests() {
		return this.bookingRequests;
	}

	public void setBookingRequests(List<BookingRequest> bookingRequests) {
		this.bookingRequests = bookingRequests;
	}

	public BookingRequest addBookingRequest(BookingRequest bookingRequest) {
		getBookingRequests().add(bookingRequest);
		bookingRequest.setUser(this);

		return bookingRequest;
	}

	public BookingRequest removeBookingRequest(BookingRequest bookingRequest) {
		getBookingRequests().remove(bookingRequest);
		bookingRequest.setUser(null);

		return bookingRequest;
	}

	public List<Commission> getCommissions() {
		return this.commissions;
	}

	public void setCommissions(List<Commission> commissions) {
		this.commissions = commissions;
	}

	public Commission addCommission(Commission commission) {
		getCommissions().add(commission);
		commission.setUser(this);

		return commission;
	}

	public Commission removeCommission(Commission commission) {
		getCommissions().remove(commission);
		commission.setUser(null);

		return commission;
	}

	public List<DriverVehicleAssociation> getDriverVehicleAssociations() {
		return this.driverVehicleAssociations;
	}

	public void setDriverVehicleAssociations(List<DriverVehicleAssociation> driverVehicleAssociations) {
		this.driverVehicleAssociations = driverVehicleAssociations;
	}

	public DriverVehicleAssociation addDriverVehicleAssociation(DriverVehicleAssociation driverVehicleAssociation) {
		getDriverVehicleAssociations().add(driverVehicleAssociation);
		driverVehicleAssociation.setUser(this);

		return driverVehicleAssociation;
	}

	public DriverVehicleAssociation removeDriverVehicleAssociation(DriverVehicleAssociation driverVehicleAssociation) {
		getDriverVehicleAssociations().remove(driverVehicleAssociation);
		driverVehicleAssociation.setUser(null);

		return driverVehicleAssociation;
	}

	public UserType getUserType() {
		return this.userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public List<UserPreferedLocation> getUserPreferedLocations() {
		return this.userPreferedLocations;
	}

	public void setUserPreferedLocations(List<UserPreferedLocation> userPreferedLocations) {
		this.userPreferedLocations = userPreferedLocations;
	}

	public UserPreferedLocation addUserPreferedLocation(UserPreferedLocation userPreferedLocation) {
		getUserPreferedLocations().add(userPreferedLocation);
		userPreferedLocation.setUser(this);

		return userPreferedLocation;
	}

	public UserPreferedLocation removeUserPreferedLocation(UserPreferedLocation userPreferedLocation) {
		getUserPreferedLocations().remove(userPreferedLocation);
		userPreferedLocation.setUser(null);

		return userPreferedLocation;
	}

	public List<VehicleDetail> getVehicleDetails() {
		return this.vehicleDetails;
	}

	public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetail addVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().add(vehicleDetail);
		vehicleDetail.setUser(this);

		return vehicleDetail;
	}

	public VehicleDetail removeVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().remove(vehicleDetail);
		vehicleDetail.setUser(null);

		return vehicleDetail;
	}

	public DriverProfile getDriverProfile() {
		return this.driverProfile;
	}

	public void setDriverProfile(DriverProfile driverProfile) {
		this.driverProfile = driverProfile;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getCancellationCount() {
		return cancellationCount;
	}

	public void setCancellationCount(Integer cancellationCount) {
		this.cancellationCount = cancellationCount;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}
}