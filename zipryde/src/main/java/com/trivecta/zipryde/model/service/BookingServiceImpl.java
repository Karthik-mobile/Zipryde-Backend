package com.trivecta.zipryde.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.model.dao.BookingDAO;
import com.trivecta.zipryde.model.entity.Booking;

@Service("bookingService")
public class BookingServiceImpl implements BookingService{

	@Autowired
	BookingDAO bookingDAO;
	
	@Transactional
	public Booking createBooking(Booking booking) {
		return bookingDAO.createBooking(booking);
	}
	
	@Transactional
	public Booking updateBookingDriverStatus(Booking booking) {
		return bookingDAO.updateBookingDriverStatus(booking);
	}

	@Transactional
	public Booking updateBookingStatus(Booking booking) {
		return bookingDAO.updateBookingStatus(booking);
	}
	
	
}
