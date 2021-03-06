package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the booking database table.
 * 
 */
@Entity
@Table(name="BOOKING")
@NamedQueries({
	@NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b"),
	@NamedQuery(name="Booking.findByBookingStatus", query="SELECT b FROM Booking b where b.bookingStatus.status = :status ORDER BY b.id DESC"),
	//@NamedQuery(name="Booking.findByBookingStartDate", query="SELECT b FROM Booking b where b.bookingStatus.status != 'REQUESTED' AND DATE(b.bookingDateTime) = :bookingDate")
	@NamedQuery(name="Booking.findByBookingDate", query="SELECT b FROM Booking b where DATE(b.bookingDateTime) = :bookingDate ORDER BY b.id DESC"),
	@NamedQuery(name="Booking.countByBookingDate", query="SELECT count(b) FROM Booking b where DATE(b.bookingDateTime) = :bookingDate"),
	@NamedQuery(name="Booking.countByBookingDateAndDriverId", query="SELECT count(b) FROM Booking b where DATE(b.bookingDateTime) = :bookingDate and b.bookingStatus.status != 'CANCELLED' and b.driver.id=:driverId"),
	@NamedQuery(name="Booking.findAllBookingNotInRequested", query="SELECT b FROM Booking b where b.bookingStatus.status NOT IN ('REQUESTED','UNANSWERED') ORDER BY b.bookingDateTime DESC"),
	@NamedQuery(name="Booking.findByBookingDateNotInRequested", query="SELECT b FROM Booking b where b.bookingStatus.status NOT IN ('REQUESTED','UNANSWERED') and DATE(b.bookingDateTime) = :bookingDate ORDER BY b.id DESC"),
	@NamedQuery(name="Booking.countByBookingDateNotInRequested", query="SELECT count(b) FROM Booking b where b.bookingStatus.status NOT IN ('REQUESTED','UNANSWERED') and DATE(b.bookingDateTime) = :bookingDate"),
	@NamedQuery(name="Booking.findByDriverId", query="SELECT b FROM Booking b where b.driver.id=:driverId and b.bookingStatus.status != 'ACCEPTED' ORDER BY b.bookingDateTime DESC"),
	@NamedQuery(name="Booking.findByRiderId", query="SELECT b FROM Booking b where b.rider.id=:riderId and b.bookingStatus.status NOT IN ('ACCEPTED','UNANSWERED') ORDER BY b.bookingDateTime DESC"),
	@NamedQuery(name="Booking.countByDriverId", query="SELECT count(b) FROM Booking b where b.driver.id=:driverId and b.bookingStatus.status != 'ACCEPTED' ORDER BY b.bookingDateTime DESC"),
	@NamedQuery(name="Booking.countByRiderId", query="SELECT count(b) FROM Booking b where b.rider.id=:riderId and b.bookingStatus.status NOT IN ('ACCEPTED','UNANSWERED') ORDER BY b.bookingDateTime DESC"),
	@NamedQuery(name="Booking.findByBookingStatusAndDriverIds", query="SELECT b FROM Booking b where b.bookingStatus.status = :status and b.driver.id in :driverIds ORDER BY b.id DESC"),
	@NamedQuery(name="Booking.findUnAssignedDriverIds", query="SELECT b.driver.id FROM Booking b where b.bookingStatus.status not in :status and b.driver.id in :driverIds "),
	@NamedQuery(name="Booking.findByBookingStatusAndDriverId", query="SELECT b FROM Booking b where b.bookingStatus.status in :status and b.driver.id = :driverId ORDER BY b.id DESC "),
	@NamedQuery(name="Booking.findByBookingStatusAndUserId", query="SELECT b FROM Booking b where b.bookingStatus.status in :status and b.rider.id = :riderId ORDER BY b.id DESC ")

})
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptedDateTime;

	private BigDecimal acceptedPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date bookingDateTime;

	private String crnNumber;

	private BigDecimal distanceInMiles;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateTime;

	@Column(name = "`from`")
	private String from;

	private BigDecimal fromLatitude;

	private BigDecimal fromLongitude;

	private Integer noOfPassengers;

	private BigDecimal offeredPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateTime;

	private BigDecimal suggestedPrice;

	@Column(name = "`to`")
	private String to;

	private BigDecimal toLatitude;

	private BigDecimal toLongitude;

	//bi-directional many-to-one association to CabType
	@ManyToOne
	@JoinColumn(name="cabTypeId")
	private CabType cabType;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="driverId")
	private User driver;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="customerId")
	private User rider;

	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="bookingStatusId")
	private Status bookingStatus;

	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="driverStatusId")
	private Status driverStatus;

	//bi-directional many-to-one association to BookingRequest
	@OneToMany(mappedBy="booking")
	private List<BookingRequest> bookingRequests;

	//bi-directional many-to-one association to Payment
	@OneToMany(mappedBy="booking")
	private List<Payment> payments;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	private BigDecimal tollAmount;
	
	private BigDecimal tipAmount;
	
	@Transient
	private Integer offeredPricePercentage;
	
	/* Used For Call Masking */
	@Transient
	private String mobileNumber;
	
	@Transient
	private String userType;
	
	public Booking() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAcceptedDateTime() {
		return this.acceptedDateTime;
	}

	public void setAcceptedDateTime(Date acceptedDateTime) {
		this.acceptedDateTime = acceptedDateTime;
	}

	public BigDecimal getAcceptedPrice() {
		return this.acceptedPrice;
	}

	public void setAcceptedPrice(BigDecimal acceptedPrice) {
		this.acceptedPrice = acceptedPrice;
	}

	public Date getBookingDateTime() {
		return this.bookingDateTime;
	}

	public void setBookingDateTime(Date bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public String getCrnNumber() {
		return this.crnNumber;
	}

	public void setCrnNumber(String crnNumber) {
		this.crnNumber = crnNumber;
	}

	public BigDecimal getDistanceInMiles() {
		return this.distanceInMiles;
	}

	public void setDistanceInMiles(BigDecimal distanceInMiles) {
		this.distanceInMiles = distanceInMiles;
	}

	public Date getEndDateTime() {
		return this.endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public BigDecimal getFromLatitude() {
		return this.fromLatitude;
	}

	public void setFromLatitude(BigDecimal fromLatitude) {
		this.fromLatitude = fromLatitude;
	}

	public BigDecimal getFromLongitude() {
		return this.fromLongitude;
	}

	public void setFromLongitude(BigDecimal fromLongitude) {
		this.fromLongitude = fromLongitude;
	}

	public Integer getNoOfPassengers() {
		return this.noOfPassengers;
	}

	public void setNoOfPassengers(Integer noOfPassengers) {
		this.noOfPassengers = noOfPassengers;
	}

	public BigDecimal getOfferedPrice() {
		return this.offeredPrice;
	}

	public void setOfferedPrice(BigDecimal offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public Date getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public BigDecimal getSuggestedPrice() {
		return this.suggestedPrice;
	}

	public void setSuggestedPrice(BigDecimal suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getToLatitude() {
		return this.toLatitude;
	}

	public void setToLatitude(BigDecimal toLatitude) {
		this.toLatitude = toLatitude;
	}

	public BigDecimal getToLongitude() {
		return this.toLongitude;
	}

	public void setToLongitude(BigDecimal toLongitude) {
		this.toLongitude = toLongitude;
	}

	public CabType getCabType() {
		return this.cabType;
	}

	public void setCabType(CabType cabType) {
		this.cabType = cabType;
	}

	public User getDriver() {
		return this.driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public User getRider() {
		return this.rider;
	}

	public void setRider(User rider) {
		this.rider = rider;
	}

	public Status getBookingStatus() {
		return this.bookingStatus;
	}

	public void setBookingStatus(Status bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Status getDriverStatus() {
		return this.driverStatus;
	}

	public void setDriverStatus(Status driverStatus) {
		this.driverStatus = driverStatus;
	}

	public List<BookingRequest> getBookingRequests() {
		return this.bookingRequests;
	}

	public void setBookingRequests(List<BookingRequest> bookingRequests) {
		this.bookingRequests = bookingRequests;
	}

	public BookingRequest addBookingRequest(BookingRequest bookingRequest) {
		getBookingRequests().add(bookingRequest);
		bookingRequest.setBooking(this);

		return bookingRequest;
	}

	public BookingRequest removeBookingRequest(BookingRequest bookingRequest) {
		getBookingRequests().remove(bookingRequest);
		bookingRequest.setBooking(null);

		return bookingRequest;
	}

	public List<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Payment addPayment(Payment payment) {
		getPayments().add(payment);
		payment.setBooking(this);

		return payment;
	}

	public Payment removePayment(Payment payment) {
		getPayments().remove(payment);
		payment.setBooking(null);

		return payment;
	}

	public Integer getOfferedPricePercentage() {
		return offeredPricePercentage;
	}

	public void setOfferedPricePercentage(Integer offeredPricePercentage) {
		this.offeredPricePercentage = offeredPricePercentage;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public BigDecimal getTollAmount() {
		return tollAmount;
	}

	public void setTollAmount(BigDecimal tollAmount) {
		this.tollAmount = tollAmount;
	}

	public BigDecimal getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(BigDecimal tipAmount) {
		this.tipAmount = tipAmount;
	}

}