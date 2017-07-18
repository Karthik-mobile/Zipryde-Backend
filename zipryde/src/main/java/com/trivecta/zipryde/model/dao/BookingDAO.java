package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;

import com.trivecta.zipryde.model.entity.Booking;

public interface BookingDAO {
	
	public Booking createBooking(Booking booking);
	
	public Booking updateBookingDriverStatus(Booking booking);
	
	public Booking updateBookingStatus(Booking booking);
	
}
