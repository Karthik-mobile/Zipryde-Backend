package com.trivecta.zipryde.view.data.transformer;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.LostItem;
import com.trivecta.zipryde.model.entity.Payment;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.model.service.PaymentService;
import com.trivecta.zipryde.utility.Utility;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.LostItemRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.LostItemResponse;

@Component
public class BookingTransformer {

	@Autowired
	BookingService bookingService;
	
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
	
	public BookingResponse updateBookingDriverStatus(BookingRequest bookingRequest) throws MandatoryValidationException, UserValidationException {
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
	
	public BookingResponse updateBookingStatus(BookingRequest bookingRequest) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		
		if(bookingRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		if(!ValidationUtil.isValidString(bookingRequest.getBookingStatus())) {
			errorMsg.append(ErrorMessages.BOOKING_STATUS_REQUIRED);					
		}
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			Booking booking = new Booking();
			booking.setId(bookingRequest.getBookingId().intValue());
			
			Status status = new Status();
			status.setStatus(bookingRequest.getBookingStatus());
			booking.setBookingStatus(status);			
			Booking updatedBooking = bookingService.updateBookingStatus(booking,null);
			return setBookingResponseFromBooking(updatedBooking,false);
		}
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
	
	public CommonResponse getBookingCountByDateAndDriverId(BookingRequest bookingRequest) throws ParseException, MandatoryValidationException {
		if(bookingRequest.getDriverId() == null) {
  			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
  		}
  		else {
  			Date searchDate = new Date();
  			if(ValidationUtil.isValidString(bookingRequest.getStartDateTime())) {
  				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  				searchDate = dateFormat.parse(bookingRequest.getStartDateTime());
  			}
  			
  			Integer bookingCount = bookingService.getBookingCountByDateAndDriverId(searchDate,bookingRequest.getDriverId().intValue());
  			CommonResponse commonResponse = new CommonResponse();
  			commonResponse.setCount(bookingCount);
  			return commonResponse;
  		}
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

	
	public BookingResponse setBookingResponseFromBooking(Booking booking,boolean loadImages) {
		BookingResponse bookingResponse = new BookingResponse();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		bookingResponse.setBookingId(booking.getId());
		bookingResponse.setCustomerId(booking.getRider().getId());
		bookingResponse.setCustomerName(booking.getRider().getFirstName()+" "+booking.getRider().getLastName());
		bookingResponse.setCustomerMobileNumber(booking.getRider().getMobileNumber());
		if(booking.getDriver() != null) {
			bookingResponse.setDriverId(booking.getDriver().getId());
			bookingResponse.setDriverName(booking.getDriver().getFirstName()+" "+booking.getDriver().getLastName());
			bookingResponse.setDriverMobileNumber(booking.getDriver().getMobileNumber());
			if(booking.getDriver().getDriverProfile() != null) {
				bookingResponse.setVehicleNumber(booking.getDriver().getDriverProfile().getVehicleNumber());
				if(booking.getDriver().getDriverProfile().getDriverProfileImage() != null)
					bookingResponse.setDriverImage(DatatypeConverter.printBase64Binary(booking.getDriver().getDriverProfile().getDriverProfileImage()));
			}
		}
		
		if(booking.getAcceptedDateTime() != null) {
			bookingResponse.setAcceptedDateTime(dateFormat.format(booking.getAcceptedDateTime()));
			if(booking.getAcceptedPrice() != null){
				bookingResponse.setAcceptedPrice(booking.getAcceptedPrice().setScale(2,RoundingMode.CEILING).doubleValue());
			}
		}
		
		if(booking.getBookingStatus() != null) {
			bookingResponse.setBookingStatusCode(booking.getBookingStatus().getStatus());
			bookingResponse.setBookingStatus(booking.getBookingStatus().getStatusValue());
		}
		
		if(booking.getCabType() != null) {
			bookingResponse.setCabTypeId(booking.getCabType().getId());
			bookingResponse.setCabType(booking.getCabType().getType());
		}
		
		if(booking.getDriverStatus() != null){
			bookingResponse.setDriverStatusCode(booking.getDriverStatus().getStatus());
			bookingResponse.setDriverStatus(booking.getDriverStatus().getStatusValue());
		}
		
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
			bookingResponse.setSuggestedPrice(booking.getSuggestedPrice().setScale(2,RoundingMode.CEILING).doubleValue());
		
		if(booking.getOfferedPrice() != null)
			bookingResponse.setOfferedPrice(booking.getOfferedPrice().setScale(2,RoundingMode.CEILING).doubleValue());
		
		return bookingResponse;
	}
	
	
	/** -------------------- LOST ITEM ---- ------------*/
	
	public LostItemResponse saveLostItem(LostItemRequest lostItemRequest) throws UserValidationException, MandatoryValidationException {
		if(lostItemRequest.getBookingId() == null) {
			throw new MandatoryValidationException(ErrorMessages.BOOKING_ID_REQUIRED);
		}
			
		LostItem lostItem = new LostItem();
		lostItem.setBookingId(lostItemRequest.getBookingId());
		lostItem.setComments(lostItemRequest.getComments());
		
		LostItem newLostItem = bookingService.saveLostItem(lostItem);
		return setLostItemResponse(newLostItem);
	}

	public LostItemResponse getLostItemByBookingId(LostItemRequest lostItemRequest) throws MandatoryValidationException {
		if(lostItemRequest.getBookingId() == null) {
			throw new MandatoryValidationException(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		LostItem newLostItem = bookingService.getLostItemByBookingId(lostItemRequest.getBookingId());
		return setLostItemResponse(newLostItem);
	}
	
	public List<LostItemResponse> getAllLostItems() {
		List<LostItemResponse> lostItemResponseList = new ArrayList<LostItemResponse>();
		List<LostItem> lostItems = bookingService.getAllLostItem();
		if(lostItems != null && lostItems.size() > 0) {
			for(LostItem lostItem : lostItems) {
				lostItemResponseList.add(setLostItemResponse(lostItem));
			}
		}
		return lostItemResponseList;
	}
	
	private LostItemResponse setLostItemResponse(LostItem lostItem){
		LostItemResponse lostItemResponse  = new LostItemResponse();
		if(lostItem != null) {
			lostItemResponse.setBookingId(lostItem.getBookingId());
			lostItemResponse.setComments(lostItem.getComments());
			lostItemResponse.setCrnNumber(lostItem.getCrnNumber());
			lostItemResponse.setDriverMobileNumber(lostItem.getDriverMobileNumber());
			lostItemResponse.setLostItemId(lostItem.getId());
			lostItemResponse.setUserMobileNumber(lostItem.getUserMobileNumber());
		}
		return lostItemResponse;
	}
}

