package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the payment database table.
 * 
 */
@Entity
@Table(name="PAYMENT")
@NamedQueries({
	@NamedQuery(name="Payment.findAll", query="SELECT p FROM Payment p"),
	@NamedQuery(name="Payment.revenueAmountByDate", query="SELECT SUM(amountPaid) FROM Payment p where DATE(p.paidDateTime) = :date"),
	@NamedQuery(name="Payment.revenueAmountByDateAndDriverId", query="SELECT SUM(amountPaid) FROM Payment p where DATE(p.paidDateTime) = :date and p.booking.driver.id=:driverId")
})

public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private BigDecimal amountPaid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date paidDateTime;

	private String paymentType;

	//bi-directional many-to-one association to Booking
	@ManyToOne
	@JoinColumn(name="bookingId")
	private Booking booking;

	//bi-directional many-to-one association to BookingHistory
	@ManyToOne
	@JoinColumn(name="bookingHistoryId")
	private BookingHistory bookingHistory;

	public Payment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getPaidDateTime() {
		return this.paidDateTime;
	}

	public void setPaidDateTime(Date paidDateTime) {
		this.paidDateTime = paidDateTime;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Booking getBooking() {
		return this.booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public BookingHistory getBookingHistory() {
		return this.bookingHistory;
	}

	public void setBookingHistory(BookingHistory bookingHistory) {
		this.bookingHistory = bookingHistory;
	}

}