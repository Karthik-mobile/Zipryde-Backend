package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cab_type database table.
 * 
 */
@Entity
@Table(name="CAB_TYPE")
@NamedQuery(name="CabType.findAll", query="SELECT c FROM CabType c")
public class CabType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String engineType;

	private Integer isEnable;

	private Integer level;

	private String type;
	
	private Integer  seatingCapacity;

	//bi-directional many-to-one association to Booking
	@OneToMany(mappedBy="cabType")
	private List<Booking> bookings;

	//bi-directional many-to-one association to BookingHistory
	@OneToMany(mappedBy="cabType")
	private List<BookingHistory> bookingHistories;

	//bi-directional many-to-one association to PricingMstr
	@OneToMany(mappedBy="cabType")
	private List<PricingMstr> pricingMstrs;

	//bi-directional many-to-one association to VehicleDetail
	@OneToMany(mappedBy="cabType")
	private List<VehicleDetail> vehicleDetails;

	public CabType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEngineType() {
		return this.engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Booking addBooking(Booking booking) {
		getBookings().add(booking);
		booking.setCabType(this);

		return booking;
	}

	public Booking removeBooking(Booking booking) {
		getBookings().remove(booking);
		booking.setCabType(null);

		return booking;
	}

	public List<BookingHistory> getBookingHistories() {
		return this.bookingHistories;
	}

	public void setBookingHistories(List<BookingHistory> bookingHistories) {
		this.bookingHistories = bookingHistories;
	}

	public BookingHistory addBookingHistory(BookingHistory bookingHistory) {
		getBookingHistories().add(bookingHistory);
		bookingHistory.setCabType(this);

		return bookingHistory;
	}

	public BookingHistory removeBookingHistory(BookingHistory bookingHistory) {
		getBookingHistories().remove(bookingHistory);
		bookingHistory.setCabType(null);

		return bookingHistory;
	}

	public List<PricingMstr> getPricingMstrs() {
		return this.pricingMstrs;
	}

	public void setPricingMstrs(List<PricingMstr> pricingMstrs) {
		this.pricingMstrs = pricingMstrs;
	}

	public PricingMstr addPricingMstr(PricingMstr pricingMstr) {
		getPricingMstrs().add(pricingMstr);
		pricingMstr.setCabType(this);

		return pricingMstr;
	}

	public PricingMstr removePricingMstr(PricingMstr pricingMstr) {
		getPricingMstrs().remove(pricingMstr);
		pricingMstr.setCabType(null);

		return pricingMstr;
	}

	public List<VehicleDetail> getVehicleDetails() {
		return this.vehicleDetails;
	}

	public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetail addVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().add(vehicleDetail);
		vehicleDetail.setCabType(this);

		return vehicleDetail;
	}

	public VehicleDetail removeVehicleDetail(VehicleDetail vehicleDetail) {
		getVehicleDetails().remove(vehicleDetail);
		vehicleDetail.setCabType(null);

		return vehicleDetail;
	}

	public Integer getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(Integer seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

}