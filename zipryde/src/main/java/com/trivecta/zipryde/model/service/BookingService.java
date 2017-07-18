package com.trivecta.zipryde.model.service;

import com.trivecta.zipryde.model.entity.Booking;

public interface BookingService {

	public Booking createBooking(Booking booking);
	
	public Booking updateBookingDriverStatus(Booking booking);
	
	public Booking updateBookingStatus(Booking booking);
	
}
