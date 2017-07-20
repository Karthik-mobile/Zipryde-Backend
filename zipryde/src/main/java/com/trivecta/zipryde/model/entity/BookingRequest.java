package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the booking_request database table.
 * 
 */
@Entity
@Table(name="BOOKING_REQUEST")
@NamedQueries({
	@NamedQuery(name="BookingRequest.findAll", query="SELECT b FROM BookingRequest b"),
	@NamedQuery(name="BookingRequest.findByDriverId", query="SELECT b.booking FROM BookingRequest b where b.user.id = :userId ORDER BY b.id DESC")
})
public class BookingRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	//bi-directional many-to-one association to Booking
	@ManyToOne
	@JoinColumn(name="bookingId")
	private Booking booking;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="driverId")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	public BookingRequest() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Booking getBooking() {
		return this.booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
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

}