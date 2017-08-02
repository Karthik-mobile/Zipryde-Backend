package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name="STATUS")
@NamedQueries ({
	@NamedQuery(name="Status.findAll", query="SELECT s FROM Status s") ,
	@NamedQuery(name="Status.findByStatus", query="SELECT s FROM Status s where s.status = :status")
})
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String status;
	
	private String statusValue;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="bookingStatus")
	private List<Booking> bookings1;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="driverStatus")
	private List<Booking> bookings2;

	//bi-directional many-to-one association to BookingHistory
	@OneToMany(mappedBy="bookingStatus")
	private List<BookingHistory> bookingHistories1;

	//bi-directional many-to-one association to BookingHistory
	@OneToMany(mappedBy="driverStatus")
	private List<BookingHistory> bookingHistories2;

	//bi-directional many-to-one association to VehicleDetail
	@OneToMany(mappedBy="status")
	private List<VehicleDetail> vehicleDetails;

	@OneToMany(mappedBy="status")
	private List<DriverProfile> driverProfiles;

	
	public Status() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Booking> getBookings1() {
		return this.bookings1;
	}

	public void setBookings1(List<Booking> bookings1) {
		this.bookings1 = bookings1;
	}

	public Booking addBookings1(Booking bookings1) {
		getBookings1().add(bookings1);
		bookings1.setBookingStatus(this);

		return bookings1;
	}

	public Booking removeBookings1(Booking bookings1) {
		getBookings1().remove(bookings1);
		bookings1.setBookingStatus(null);

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
		bookings2.setDriverStatus(this);

		return bookings2;
	}

	public Booking removeBookings2(Booking bookings2) {
		getBookings2().remove(bookings2);
		bookings2.setDriverStatus(null);

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
		bookingHistories1.setBookingStatus(this);

		return bookingHistories1;
	}

	public BookingHistory removeBookingHistories1(BookingHistory bookingHistories1) {
		getBookingHistories1().remove(bookingHistories1);
		bookingHistories1.setBookingStatus(null);

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
		bookingHistories2.setDriverStatus(this);

		return bookingHistories2;
	}

	public BookingHistory removeBookingHistories2(BookingHistory bookingHistories2) {
		getBookingHistories2().remove(bookingHistories2);
		bookingHistories2.setDriverStatus(null);

		return bookingHistories2;
	}

	public List<VehicleDetail> getVehicleDetails() {
		return this.vehicleDetails;
	}

	public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetail addVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().add(vehicleDetail);
		vehicleDetail.setStatus(this);

		return vehicleDetail;
	}

	public VehicleDetail removeVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().remove(vehicleDetail);
		vehicleDetail.setStatus(null);

		return vehicleDetail;
	}

	public List<DriverProfile> getDriverProfiles() {
		return driverProfiles;
	}

	public void setDriverProfiles(List<DriverProfile> driverProfiles) {
		this.driverProfiles = driverProfiles;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

}