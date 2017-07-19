package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.trivecta.zipryde.model.entity.Booking;

public interface BookingDAO {
	
	public Booking createBooking(Booking booking);
	
	public Booking updateBookingDriverStatus(Booking booking);
	
	public Booking updateBookingStatus(Booking booking);
	
	public Booking getBookingById(int bookingId);
	
	public List<Booking> getBookingByDate(Date bookingDate);
	
	public List<Booking> getBookingByBookingStatus(String status);
	
	public List<Booking> getBookingByDriverId(int driverId);
	
	public List<Booking> getBookingByCustomerId(int customerId);
	
	public Integer getBookingCountByDate(Date bookingDate);
}
