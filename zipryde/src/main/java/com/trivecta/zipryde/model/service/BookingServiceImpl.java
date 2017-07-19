package com.trivecta.zipryde.model.service;

import java.util.Date;
import java.util.List;

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

	@Transactional
	public Booking getBookingById(int bookingId) {
		return bookingDAO.getBookingById(bookingId);
	}

	@Transactional
	public List<Booking> getBookingByDate(Date bookingDate) {
		return bookingDAO.getBookingByDate(bookingDate);
	}

	@Transactional
	public List<Booking> getBookingByBookingStatus(String status) {
		return bookingDAO.getBookingByBookingStatus(status);
	}

	@Transactional
	public List<Booking> getBookingByDriverId(int driverId) {
		return bookingDAO.getBookingByDriverId(driverId);
	}

	@Transactional
	public List<Booking> getBookingByCustomerId(int customerId) {
		return bookingDAO.getBookingByCustomerId(customerId);
	}
	
	
}
