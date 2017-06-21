package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the booking_history database table.
 * 
 */
@Entity
@Table(name="BOOKING_HISTORY")
@NamedQuery(name="BookingHistory.findAll", query="SELECT b FROM BookingHistory b")
public class BookingHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptedDateTime;

	private BigDecimal acceptedPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date bookingDateTime;

	private Integer crnNumber;

	private Integer distanceInMiles;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateTime;

	private String from;

	private BigDecimal fromLatitude;

	private BigDecimal fromLongitude;

	private Integer noOfPassengers;

	private BigDecimal offeredPrice;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateTime;

	private BigDecimal suggestedPrice;

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

	//bi-directional many-to-one association to Payment
	@OneToMany(mappedBy="bookingHistory")
	private List<Payment> payments;

	public BookingHistory() {
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

	public Integer getCrnNumber() {
		return this.crnNumber;
	}

	public void setCrnNumber(Integer crnNumber) {
		this.crnNumber = crnNumber;
	}

	public Integer getDistanceInMiles() {
		return this.distanceInMiles;
	}

	public void setDistanceInMiles(Integer distanceInMiles) {
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

	public List<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Payment addPayment(Payment payment) {
		getPayments().add(payment);
		payment.setBookingHistory(this);

		return payment;
	}

	public Payment removePayment(Payment payment) {
		getPayments().remove(payment);
		payment.setBookingHistory(null);

		return payment;
	}

}