package com.trivecta.zipryde.model.service;

import java.util.Date;
import java.util.List;

import com.trivecta.zipryde.model.entity.Booking;

public interface BookingService {

	public Booking createBooking(Booking booking);
	
	public Booking updateBookingDriverStatus(Booking booking);
	
	public Booking updateBookingStatus(Booking booking);
	
	public Booking getBookingById(int bookingId);
	
	public List<Booking> getBookingByDate(Date bookingDate);
	
	public List<Booking> getBookingByBookingStatus(String status);
	
	public List<Booking> getBookingByDriverId(int driverId);
	
	public List<Booking> getBookingByuserId(int customerId);
	
	public Integer getBookingCountByDate(Date bookingDate);
	
	public List<Booking> getBookingRequestedByDriverId(int driverId);
	
	public Integer getBookingCountByDateNotInRequested(Date bookingDate);
	
	public List<Booking> getBookingByDateNotInRequested(Date bookingDate);
	
	public Integer getBookingCountByDateAndDriverId(Date bookingDate,Integer driverId);

}
