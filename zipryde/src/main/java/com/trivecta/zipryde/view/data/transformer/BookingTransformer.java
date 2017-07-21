package com.trivecta.zipryde.view.data.transformer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.PAYMENT;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Payment;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.model.service.PaymentService;
import com.trivecta.zipryde.utility.Utility;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;

@Component
public class BookingTransformer {

	@Autowired
	BookingService bookingService;
	
    @Autowired
    PaymentService paymentService;

	
	/*
	 * Step 1 - Create Booking with Status - Requested
	 * Step 2 - get logged in near by Driver ( Max Count for now - 10) with Customer Geo location
	 * Step 3 - get Driver from Step 2 and Filter with Cab Type and Requested Percentage value by Customer
	 * Step 4 - Send Notification to Driver (Async Call)
	 * Step 5 - Return the Booking Id
	 * 
	 */
	
	/*
	 * Step1 - Driver Accepts Booking
	 * Step2 - Update Booking with Status - Scheduled
	 * Step3 - Send Notification
	 */
	
	public BookingResponse createBooking(BookingRequest bookingRequest) throws ParseException, MandatoryValidationException {
		
		StringBuffer errorMsg = new StringBuffer();
		
		if(!ValidationUtil.isValidString(bookingRequest.getFrom())) {
			errorMsg = errorMsg.append(ErrorMessages.PICKUP_LOC_REQUIRED);
		}
		if(!ValidationUtil.isValidString(bookingRequest.getTo())) {
			errorMsg = errorMsg.append(ErrorMessages.DROP_LOC_REQUIRED);
		}
		if( bookingRequest.getGeoLocationRequest() == null) {
			errorMsg = errorMsg.append(ErrorMessages.LAT_LON_REQUIRED);
		}
		
		if(bookingRequest.getCabTypeId() == null) {
			errorMsg = errorMsg.append(ErrorMessages.CAB_REQUIRED);
		}
		if(bookingRequest.getCustomerId() == null) {
			errorMsg = errorMsg.append(ErrorMessages.USER_ID_REQUIRED);
		}
		if(bookingRequest.getSuggestedPrice() == null) {
			errorMsg = errorMsg.append(ErrorMessages.SUGGESTED_PRICE_REQUIRED);
		}
		if(bookingRequest.getOfferedPrice() == null) {
			errorMsg = errorMsg.append(ErrorMessages.OFFERED_PRICE_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			Booking booking = new Booking();
			
			booking.setFrom(bookingRequest.getFrom());
			booking.setTo(bookingRequest.getTo());
			
			CabType cabType = new  CabType();
			cabType.setId(bookingRequest.getCabTypeId().intValue());
			booking.setCabType(cabType);
			
			GeoLocationRequest geoLocationRequest = bookingRequest.getGeoLocationRequest(); 
			
			booking.setFromLatitude(new BigDecimal(geoLocationRequest.getFromLatitude()));
			booking.setFromLongitude(new BigDecimal(geoLocationRequest.getFromLongitude()));
			booking.setToLatitude(new BigDecimal(geoLocationRequest.getToLatitude()));
			booking.setToLongitude(new BigDecimal(geoLocationRequest.getToLongitude()));
			booking.setDistanceInMiles(geoLocationRequest.getDistanceInMiles().intValue());
			
			User customer  = new User();
			customer.setId(bookingRequest.getCustomerId().intValue());
			booking.setRider(customer);
			
			DateFormat startDate = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
			
			if(bookingRequest.getStartDateTime() != null) {
				booking.setStartDateTime(startDate.parse(bookingRequest.getStartDateTime()));
			}
				
			booking.setSuggestedPrice(new BigDecimal(bookingRequest.getSuggestedPrice()));
			booking.setOfferedPrice(new BigDecimal(bookingRequest.getOfferedPrice()));
			if(bookingRequest.getOfferedPricePercentage() != null) {
				booking.setOfferedPricePercentage(bookingRequest.getOfferedPricePercentage().intValue());
			}
			
			Status status = new Status();
			status.setStatus(STATUS.REQUESTED);
			booking.setBookingStatus(status);
			if(bookingRequest.getNoOfPassengers() != null) {
				booking.setNoOfPassengers(bookingRequest.getNoOfPassengers().intValue());
			}
			else {
				booking.setNoOfPassengers(1);
			}
			Booking newBooking = bookingService.createBooking(booking);		
			BookingResponse bookingResponse = setBookingResponseFromBooking(newBooking,false);		
			return bookingResponse;
		}	
	}
	
	public BookingResponse updateBookingDriverStatus(BookingRequest bookingRequest) throws MandatoryValidationException {
		StringBuffer errorMsg = new StringBuffer();
		
		if(bookingRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		if(!ValidationUtil.isValidString(bookingRequest.getDriverStatus())) {
			errorMsg.append(ErrorMessages.DRIVER_STATUS_REQUIRED);					
		}
		if(bookingRequest.getDriverId() == null) {
			errorMsg.append(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {		
			Booking booking = new Booking();
			booking.setId(bookingRequest.getBookingId().intValue());
			
			User driver = new User();
			driver.setId(bookingRequest.getDriverId().intValue());
			booking.setDriver(driver);
			
			Status status = new Status();
			status.setStatus(bookingRequest.getDriverStatus());
			booking.setDriverStatus(status);
			
			Booking updatedBooking = bookingService.updateBookingDriverStatus(booking);
			return setBookingResponseFromBooking(updatedBooking,false);
		}
	}
	
	public BookingResponse updateBookingStatus(BookingRequest bookingRequest) {
		Booking booking = new Booking();
		booking.setId(bookingRequest.getBookingId().intValue());
		
		Status status = new Status();
		status.setStatus(bookingRequest.getBookingStatus());
		booking.setBookingStatus(status);
		
		Booking updatedBooking = bookingService.updateBookingStatus(booking);
		return setBookingResponseFromBooking(updatedBooking,false);
	}
	
	
	public BookingResponse updateBooking(BookingRequest bookingRequest) throws ParseException {
		
		Booking booking = new Booking();		
		booking.setId(bookingRequest.getBookingId().intValue());
		booking.setTo(bookingRequest.getTo());
				
		GeoLocationRequest geoLocationRequest = bookingRequest.getGeoLocationRequest();
		booking.setToLatitude(new BigDecimal(geoLocationRequest.getToLatitude()));
		booking.setToLongitude(new BigDecimal(geoLocationRequest.getToLongitude()));
		booking.setDistanceInMiles(geoLocationRequest.getDistanceInMiles().intValue());
		
		DateFormat endDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		booking.setEndDateTime(endDate.parse(bookingRequest.getStartDateTime()));
		
		BookingResponse bookingResponse = new BookingResponse();
		
		return bookingResponse;
	}
	
	public BookingResponse getBookingByBookingId(BookingRequest bookingRequest) throws MandatoryValidationException {
		
		if(bookingRequest.getBookingId() == null ){
			throw new MandatoryValidationException(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		else {
			Booking booking = bookingService.getBookingById(bookingRequest.getBookingId().intValue());
			return setBookingResponseFromBooking(booking,true);
		}
	}
	
	public List<BookingResponse> getBookingByDate(BookingRequest bookingRequest) throws ParseException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		Date searchDate = new Date();
		if(ValidationUtil.isValidString(bookingRequest.getStartDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			searchDate = dateFormat.parse(bookingRequest.getStartDateTime());
		}
		
		List<Booking> bookingList = bookingService.getBookingByDate(searchDate);
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingByBookingStatus(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getBookingStatus() == null) {
			throw new MandatoryValidationException(ErrorMessages.BOOKING_STATUS_REQUIRED);
		}
		
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingByBookingStatus(bookingRequest.getBookingStatus());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingByDriverId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingByDriverId(bookingRequest.getDriverId().intValue());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingByuserId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getCustomerId() == null) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingByuserId(bookingRequest.getCustomerId().intValue());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,true));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingRequestedByDriverId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
				
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingRequestedByDriverId(bookingRequest.getDriverId().intValue());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false));
			}
		}
		return bookingResponseList;		
	}
	
	public CommonResponse getBookingCountByDate(BookingRequest bookingRequest) throws ParseException {
		Date searchDate = new Date();
		if(ValidationUtil.isValidString(bookingRequest.getStartDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			searchDate = dateFormat.parse(bookingRequest.getStartDateTime());
		}
		
		Integer bookingCount = bookingService.getBookingCountByDate(searchDate);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setCount(bookingCount);
		return commonResponse;
	}
	
	public CommonResponse getBookingCountByDateNotInRequested(BookingRequest bookingRequest) throws ParseException {
		Date searchDate = new Date();
		if(ValidationUtil.isValidString(bookingRequest.getStartDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			searchDate = dateFormat.parse(bookingRequest.getStartDateTime());
		}
		
		Integer bookingCount = bookingService.getBookingCountByDateNotInRequested(searchDate);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setCount(bookingCount);
		return commonResponse;
	}
	
	public List<BookingResponse> getBookingByDateNotInRequested(BookingRequest bookingRequest) throws ParseException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		Date searchDate = new Date();
		if(ValidationUtil.isValidString(bookingRequest.getStartDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			searchDate = dateFormat.parse(bookingRequest.getStartDateTime());
		}
		
		List<Booking> bookingList = bookingService.getBookingByDateNotInRequested(searchDate);
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false));
			}
		}
		return bookingResponseList;		
	}
	
	public void savePayment(PaymentRequest paymentRequest) throws MandatoryValidationException {
		StringBuffer errorMsg = new StringBuffer();
		if(paymentRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		if(paymentRequest.getAmountPaid() == null) {
			errorMsg.append(ErrorMessages.AMOUNT_TO_PAY_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {			
	        Payment payment = new Payment();
	        if(paymentRequest.getPaymentId() != null)
	               payment.setId(paymentRequest.getPaymentId().intValue());
	        Booking booking = new Booking();
	        booking.setId(paymentRequest.getBookingId().intValue());
	        payment.setBooking(booking);
	        payment.setAmountPaid(BigDecimal.valueOf(paymentRequest.getAmountPaid()));
	        payment.setPaymentType(paymentRequest.getPaymentType() != null ? paymentRequest.getPaymentType() : PAYMENT.CASH);
	        payment.setPaidDateTime(new Date());
	        paymentService.savePayment(payment);
		}
	}

	
	private BookingResponse setBookingResponseFromBooking(Booking booking,boolean loadImages) {
		BookingResponse bookingResponse = new BookingResponse();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		bookingResponse.setBookingId(booking.getId());
		bookingResponse.setCustomerId(booking.getRider().getId());
		bookingResponse.setCustomerName(booking.getRider().getFirstName()+" "+booking.getRider().getLastName());
	
		if(booking.getDriver() != null) {
			bookingResponse.setDriverId(booking.getDriver().getId());
			bookingResponse.setDriverName(booking.getDriver().getFirstName()+" "+booking.getDriver().getLastName());
			bookingResponse.setVehicleNumber(booking.getDriver().getDriverProfile().getVehicleNumber());
			if(booking.getDriver().getDriverProfile().getDriverProfileImage() != null)
				bookingResponse.setDriverImage(DatatypeConverter.printBase64Binary(booking.getDriver().getDriverProfile().getDriverProfileImage()));
		}
		
		if(booking.getAcceptedDateTime() != null) {
			bookingResponse.setAcceptedDateTime(dateFormat.format(booking.getAcceptedDateTime()));
			if(booking.getAcceptedPrice() != null){
				bookingResponse.setAcceptedPrice(booking.getAcceptedPrice().doubleValue());
			}
		}
		
		if(booking.getBookingStatus() != null) {
			bookingResponse.setBookingStatus(booking.getBookingStatus().getStatus());
		}
		
		if(booking.getCabType() != null) {
			bookingResponse.setCabTypeId(booking.getCabType().getId());
			bookingResponse.setCabType(booking.getCabType().getType());
		}
		
		if(booking.getDriverStatus() != null)
			bookingResponse.setDriverStatus(booking.getDriverStatus().getStatus());
		
		if(booking.getBookingDateTime() != null) 
			bookingResponse.setBookingDateTime(dateFormat.format(booking.getBookingDateTime()));
		
		if(booking.getStartDateTime() != null)
			bookingResponse.setStartDateTime(dateFormat.format(booking.getStartDateTime()));
		
		if(booking.getEndDateTime() != null)
			bookingResponse.setEndDateTime(dateFormat.format(booking.getEndDateTime()));
		
		bookingResponse.setFrom(booking.getFrom());
		bookingResponse.setTo(booking.getTo());
		bookingResponse.setCrnNumber(booking.getCrnNumber());
		
		GeoLocationResponse geoLocationResponse = new GeoLocationResponse();
		if(booking.getFromLatitude() != null && booking.getFromLongitude() != null) {
			geoLocationResponse.setFromLatitude(booking.getFromLatitude().toString());
			geoLocationResponse.setFromLongitude(booking.getFromLongitude().toString());
		}
		if(booking.getToLatitude() != null && booking.getToLongitude() != null) {
			geoLocationResponse.setToLatitude(booking.getToLatitude().toString());
			geoLocationResponse.setToLongitude(booking.getToLongitude().toString());
		}
		geoLocationResponse.setDistanceInMiles(booking.getDistanceInMiles());
		
		bookingResponse.setGeoLocationResponse(geoLocationResponse);
		
		bookingResponse.setNoOfPassengers(booking.getNoOfPassengers());
		
		if(booking.getSuggestedPrice() != null)
			bookingResponse.setSuggestedPrice(booking.getSuggestedPrice().doubleValue());
		
		if(booking.getOfferedPrice() != null)
			bookingResponse.setOfferedPrice(booking.getOfferedPrice().doubleValue());
		
		return bookingResponse;
	}
	
	public CommonResponse getRevenueByDate(PaymentRequest paymentRequest) throws ParseException {	
		Date date = new Date();
		if(ValidationUtil.isValidString(paymentRequest.getPaidDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			date = dateFormat.parse(paymentRequest.getPaidDateTime());
		}
		
		Double revenueAmount = paymentService.getPaymentAmountByDate(date);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setRevenueAmount(revenueAmount);
		return commonResponse;
	}
	
}
