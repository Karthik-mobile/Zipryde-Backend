
package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.LostItem;
import com.trivecta.zipryde.model.entity.Payment;

public interface BookingDAO {
	
	public Booking createBooking(Booking booking);
	
	public Booking updateBookingDriverStatus(Booking booking) throws UserValidationException;
	
	public Booking updateBookingStatus(Booking booking,Payment payment) throws UserValidationException;
	
	public Booking getBookingById(int bookingId);
	
	public List<Booking> getBookingByDate(Date bookingDate);
	
	public List<Booking> getBookingByBookingStatus(String status);
	
	public List<Booking> getBookingByBookingStatusAndDriverId(String status,int driverId);
	
	public List<Booking> getBookingByBookingStatusAndUserId(String status,int customerId);
	
	public List<Booking> getBookingByDriverId(int driverId,int paginationNo);
	
	public List<Booking> getBookingByuserId(int customerId,int paginationNo);
	
	public Integer getBookingCountByDate(Date bookingDate);
	
	public List<Booking> getBookingRequestedByDriverId(int driverId);
	
	public List<Booking> getFutureBookingRequestedByDriverId(int driverId);
	
	public Integer getBookingCountByDateNotInRequested(Date bookingDate);
	
	public List<Booking> getBookingByDateNotInRequested(Date bookingDate);
	
	public Integer getBookingCountByDateAndDriverId(Date bookingDate,Integer driverId);
	
	public void updateBookinStatusUnAnswered();
	
	public void cancelBookingByDriversInOffline(List<Integer> driverIds);
	
	public Booking getActiveBookingByDriverId(int driverId);
	
	public Booking getCallMaskingNumberByBookingId(Integer bookingId,String accessToken) throws UserValidationException;

	
	/**----------- Lost Items ----------------- */
	public LostItem saveLostItem(LostItem newLostItem) throws UserValidationException;
	public LostItem getLostItemByBookingId(int bookingId) ;
	public List<LostItem> getAllLostItem();
}
