package com.trivecta.zipryde.model.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.dao.BookingDAO;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.LostItem;
import com.trivecta.zipryde.model.entity.Payment;

@Service("bookingService")
public class BookingServiceImpl implements BookingService{

	@Autowired
	BookingDAO bookingDAO;
	
	@Transactional
	public Booking createBooking(Booking booking) {
		return bookingDAO.createBooking(booking);
	}
	
	@Transactional
	public Booking updateBookingDriverStatus(Booking booking) throws UserValidationException {
		return bookingDAO.updateBookingDriverStatus(booking);
	}

	@Transactional
	public Booking updateBookingStatus(Booking booking,Payment payment) throws UserValidationException {
		return bookingDAO.updateBookingStatus(booking,payment);
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
	public List<Booking> getBookingByBookingStatusAndDriverId(String status,int driverId){
		return bookingDAO.getBookingByBookingStatusAndDriverId(status, driverId);
	}
	
	@Transactional
	public List<Booking> getBookingByBookingStatusAndUserId(String status,int customerId){
		return bookingDAO.getBookingByBookingStatusAndUserId(status, customerId);
	}
	
	@Transactional
	public List<Booking> getBookingByDriverId(int driverId,int paginationNo) {
		return bookingDAO.getBookingByDriverId(driverId,paginationNo);
	}

	@Transactional
	public List<Booking> getBookingByuserId(int customerId) {
		return bookingDAO.getBookingByuserId(customerId);
	}
	
	@Transactional
	public Integer getBookingCountByDate(Date bookingDate) {
		return bookingDAO.getBookingCountByDate(bookingDate);
	}
	
	@Transactional
	public List<Booking> getBookingRequestedByDriverId(int driverId) {
		return bookingDAO.getBookingRequestedByDriverId(driverId);
	}
	
	@Transactional
	public List<Booking> getFutureBookingRequestedByDriverId(int driverId) {
		return bookingDAO.getFutureBookingRequestedByDriverId(driverId);
	}
	
	@Transactional
	public Integer getBookingCountByDateNotInRequested(Date bookingDate){
		return bookingDAO.getBookingCountByDateNotInRequested(bookingDate);
	}
	
	@Transactional
	public List<Booking> getBookingByDateNotInRequested(Date bookingDate){
		return bookingDAO.getBookingByDateNotInRequested(bookingDate);
	}
	
	@Transactional
	public Integer getBookingCountByDateAndDriverId(Date bookingDate,Integer driverId){
		return bookingDAO.getBookingCountByDateAndDriverId(bookingDate, driverId);
	}
	
	@Transactional
	public void updateBookinStatusUnAnswered() {
		bookingDAO.updateBookinStatusUnAnswered();
	}
	
	@Transactional
	public LostItem saveLostItem(LostItem newLostItem) throws UserValidationException {
		return bookingDAO.saveLostItem(newLostItem);
	}
	
	@Transactional
	public LostItem getLostItemByBookingId(int bookingId){
		return bookingDAO.getLostItemByBookingId(bookingId);
	}
	
	@Transactional
	public List<LostItem> getAllLostItem(){
		return bookingDAO.getAllLostItem();
	}
}
