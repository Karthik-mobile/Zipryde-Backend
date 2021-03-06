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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.PAYMENT;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.constants.ZipRydeConstants.USERTYPE;
import com.trivecta.zipryde.controller.ZiprydeController;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.LostItem;
import com.trivecta.zipryde.model.entity.Payment;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.VehicleDetail;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.model.service.PaymentService;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.utility.DistanceCalculator;
import com.trivecta.zipryde.utility.Utility;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.CallMaskingRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.LostItemRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CallMaskingResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.LostItemResponse;

@Component
public class BookingTransformer {

	@Autowired
	BookingService bookingService;
	
	@Autowired
	MongoDbClient mongoDbClient;

	private org.apache.log4j.Logger logger = Logger.getLogger(ZiprydeController.class);
	
	public BookingResponse createBooking(BookingRequest bookingRequest) throws ParseException, MandatoryValidationException, UserValidationException {
		
		StringBuffer errorMsg = new StringBuffer();
		
		if(!ValidationUtil.isValidString(bookingRequest.getFrom())) {
			errorMsg = errorMsg.append(ErrorMessages.PICKUP_LOC_REQUIRED+"\n");
		}
		if(!ValidationUtil.isValidString(bookingRequest.getTo())) {
			errorMsg = errorMsg.append(ErrorMessages.DROP_LOC_REQUIRED+"\n");
		}
		if( bookingRequest.getGeoLocationRequest() == null) {
			errorMsg = errorMsg.append(ErrorMessages.LAT_LON_REQUIRED+"\n");
		}
		
		if(bookingRequest.getCabTypeId() == null) {
			errorMsg = errorMsg.append(ErrorMessages.CAB_REQUIRED+"\n");
		}
		if(bookingRequest.getCustomerId() == null) {
			errorMsg = errorMsg.append(ErrorMessages.USER_ID_REQUIRED+"\n");
		}
		if(bookingRequest.getSuggestedPrice() == null) {
			errorMsg = errorMsg.append(ErrorMessages.SUGGESTED_PRICE_REQUIRED+"\n");
		}
		if(bookingRequest.getOfferedPrice() == null) {
			errorMsg = errorMsg.append(ErrorMessages.OFFERED_PRICE_REQUIRED+"\n");
		}
		
		/*if(bookingRequest.getGeoLocationRequest()!= null){
			Double distance = DistanceCalculator.distance(
					Double.valueOf(bookingRequest.getGeoLocationRequest().getFromLatitude()), 
					Double.valueOf(bookingRequest.getGeoLocationRequest().getFromLongitude()), 
					Double.valueOf(bookingRequest.getGeoLocationRequest().getToLatitude()), 
					Double.valueOf(bookingRequest.getGeoLocationRequest().getToLongitude()));
			distance= (double) Math.round(distance * 100) / 100;
			if(distance.compareTo(Double.valueOf("100.00")) > 0) {
				errorMsg = errorMsg.append(ErrorMessages.FROM_TO_LOC_NOT_IN_LIMIT+"\n");
			}
			boolean isFromInLimit  = 
					mongoDbClient.checkDistance(Double.valueOf(bookingRequest.getGeoLocationRequest().getFromLongitude()),
					Double.valueOf(bookingRequest.getGeoLocationRequest().getFromLatitude()));
			
			boolean isToInLimit  = 
					mongoDbClient.checkDistance(Double.valueOf(bookingRequest.getGeoLocationRequest().getToLongitude()),
					Double.valueOf(bookingRequest.getGeoLocationRequest().getToLatitude()));
			
			if(!isFromInLimit) {
				errorMsg = errorMsg.append(ErrorMessages.FROM_LOC_NOT_IN_LIMIT+"\n");
			}
			if(!isToInLimit) {
				errorMsg = errorMsg.append(ErrorMessages.TO_LOC_NOT_IN_LIMIT+"\n");
			}
		}*/
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
			booking.setDistanceInMiles(new BigDecimal(geoLocationRequest.getDistanceInMiles()));
			
			User customer  = new User();
			customer.setId(bookingRequest.getCustomerId().intValue());
			booking.setRider(customer);
			
			DateFormat startDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			
			if(bookingRequest.getStartDateTime() != null) {
				booking.setStartDateTime(startDate.parse(bookingRequest.getStartDateTime()));
			}
			
			if(bookingRequest.getBookingDateTime() != null) {
				booking.setBookingDateTime(startDate.parse(bookingRequest.getBookingDateTime()));
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
			BookingResponse bookingResponse = setBookingResponseFromBooking(newBooking,false,false);		
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
			
			//Enter TOLL Amount by DRIVER when ZIPRYDE Completes
			if(bookingRequest.getTollAmount() != null)
				booking.setTollAmount(new BigDecimal(bookingRequest.getTollAmount()));
			
			Booking updatedBooking = bookingService.updateBookingDriverStatus(booking);
			return setBookingResponseFromBooking(updatedBooking,false,false);
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
			
			//Enter Tip Amount  by User after ZIPRYDE Completes
			if(bookingRequest.getTipAmount() != null)
				booking.setTipAmount(new BigDecimal(bookingRequest.getTipAmount()));
			
			Booking updatedBooking = bookingService.updateBookingStatus(booking,null);
			return setBookingResponseFromBooking(updatedBooking,false,false);
		}
	}
		
	public BookingResponse updateBooking(BookingRequest bookingRequest) throws ParseException {
		
		Booking booking = new Booking();		
		booking.setId(bookingRequest.getBookingId().intValue());
		booking.setTo(bookingRequest.getTo());
				
		GeoLocationRequest geoLocationRequest = bookingRequest.getGeoLocationRequest();
		booking.setToLatitude(new BigDecimal(geoLocationRequest.getToLatitude()));
		booking.setToLongitude(new BigDecimal(geoLocationRequest.getToLongitude()));
		booking.setDistanceInMiles(new BigDecimal(geoLocationRequest.getDistanceInMiles()));
		
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
			return setBookingResponseFromBooking(booking,true,true);
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
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
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
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingByBookingStatusAndDriverId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		StringBuffer errorMsg = new StringBuffer();
		if(bookingRequest.getBookingStatus() == null) {
			errorMsg.append(ErrorMessages.BOOKING_STATUS_REQUIRED);
		}
		if(bookingRequest.getDriverId() == null) {
			errorMsg.append(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();			
			List<Booking> bookingList = 
					bookingService.getBookingByBookingStatusAndDriverId(bookingRequest.getBookingStatus(),bookingRequest.getDriverId().intValue());
			
			if(bookingList != null && bookingList.size() > 0) {
				for(Booking booking : bookingList) {
					bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
				}
			}
			return bookingResponseList;
		}				
	}
	
	public List<BookingResponse> getBookingByBookingStatusAndUserId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		StringBuffer errorMsg = new StringBuffer();
		if(bookingRequest.getBookingStatus() == null) {
			errorMsg.append(ErrorMessages.BOOKING_STATUS_REQUIRED);
		}
		if(bookingRequest.getCustomerId() == null) {
			errorMsg.append(ErrorMessages.USER_ID_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();		
			List<Booking> bookingList = 
					bookingService.getBookingByBookingStatusAndUserId(bookingRequest.getBookingStatus(),bookingRequest.getCustomerId().intValue());
			
			if(bookingList != null && bookingList.size() > 0) {
				for(Booking booking : bookingList) {
					bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
				}
			}
			return bookingResponseList;		
		}		
	}
	
	public List<BookingResponse> getBookingByDriverId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		
		if(bookingRequest.getPageNo() == null) {
			bookingRequest.setPageNo(1);
		}
		
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingByDriverId(bookingRequest.getDriverId(),bookingRequest.getPageNo());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				BookingResponse bookingResponse= setBookingResponseFromBooking(booking,true,false);
				bookingResponse.setPageNo(bookingRequest.getPageNo());
				bookingResponseList.add(bookingResponse);
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getBookingByuserId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getCustomerId() == null) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		
		if(bookingRequest.getPageNo() == null) {
			bookingRequest.setPageNo(1);
		}
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getBookingByuserId(bookingRequest.getCustomerId(),bookingRequest.getPageNo());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				BookingResponse bookingResponse= setBookingResponseFromBooking(booking,true,false);
				bookingResponse.setPageNo(bookingRequest.getPageNo());
				bookingResponseList.add(bookingResponse);
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
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getFutureBookingRequestedByDriverId(BookingRequest bookingRequest) throws MandatoryValidationException  {
		if(bookingRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
				
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		
		List<Booking> bookingList = bookingService.getFutureBookingRequestedByDriverId(bookingRequest.getDriverId().intValue());
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
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
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
			}
		}
		return bookingResponseList;		
	}
	
	public List<BookingResponse> getAllBookingNotInRequested() throws ParseException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
	
		List<Booking> bookingList = bookingService.getAllBookingNotInRequested();
		
		if(bookingList != null && bookingList.size() > 0) {
			for(Booking booking : bookingList) {
				bookingResponseList.add(setBookingResponseFromBooking(booking,false,false));
			}
		}
		return bookingResponseList;		
	}

	
	public CallMaskingResponse getCallMaskingNumber(CallMaskingRequest callMaskingRequest) throws MandatoryValidationException {
		StringBuffer errorMsg = new StringBuffer();
		CallMaskingResponse callMaskingResponse = new CallMaskingResponse();
		if(callMaskingRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		if(callMaskingRequest.getUserType() == null) {
			errorMsg.append(ErrorMessages.USER_TYPE_MANDATORY);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			logger.info("getCallMaskingNumber : booking Id : "+callMaskingRequest.getBookingId());
			logger.info("getCallMaskingNumber : User Type: "+callMaskingRequest.getUserType());
			Booking booking = bookingService.getBookingById(callMaskingRequest.getBookingId());
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(callMaskingRequest.getUserType())){
				callMaskingResponse.setBookingId(booking.getId());
				callMaskingResponse.setMobileNumber(booking.getRider().getMobileNumber());
				callMaskingResponse.setUserType(USERTYPE.RIDER);
			}
			else if(USERTYPE.RIDER.equalsIgnoreCase(callMaskingRequest.getUserType())) {
				callMaskingResponse.setBookingId(booking.getId());
				if(booking.getDriver() != null) {
					callMaskingResponse.setMobileNumber(booking.getDriver().getMobileNumber());
					callMaskingResponse.setUserType(USERTYPE.DRIVER);
				}
			}
		}
		return callMaskingResponse;		
	}
	
	public CallMaskingResponse getCallMaskingNumber(CallMaskingRequest callMaskingRequest,String accessToken) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		CallMaskingResponse callMaskingResponse = new CallMaskingResponse();
		if(callMaskingRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			logger.info("getCallMaskingNumber : booking Id : "+callMaskingRequest.getBookingId());
			logger.info("getCallMaskingNumber : accessToken : "+accessToken);
			Booking booking = bookingService.getCallMaskingNumberByBookingId(
					callMaskingRequest.getBookingId(),Utility.encryptWithMD5(accessToken));
			
			callMaskingResponse.setBookingId(booking.getId());
			callMaskingResponse.setMobileNumber(booking.getMobileNumber());
			callMaskingResponse.setUserType(booking.getUserType());
		}
		return callMaskingResponse;		
	}
	
	public BookingResponse setBookingResponseFromBooking(Booking booking,boolean loadImages,boolean loadVehicle) {
		BookingResponse bookingResponse = new BookingResponse();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		bookingResponse.setBookingId(booking.getId());
		bookingResponse.setCustomerId(booking.getRider().getId());
		bookingResponse.setCustomerName(booking.getRider().getFirstName()+" "+booking.getRider().getLastName());
		//bookingResponse.setCustomerMobileNumber(booking.getRider().getMobileNumber());
		if(booking.getDriver() != null) {
			bookingResponse.setDriverId(booking.getDriver().getId());
			/* MAIL Changes : ZipRyde App Changes to be compliant with TX State Requirements */
			//bookingResponse.setDriverName(booking.getDriver().getFirstName()+" "+booking.getDriver().getLastName());
			bookingResponse.setDriverName(booking.getDriver().getFirstName());
			//bookingResponse.setDriverMobileNumber(booking.getDriver().getMobileNumber());
			if(booking.getDriver().getDriverProfile() != null) {
				bookingResponse.setVehicleNumber(booking.getDriver().getDriverProfile().getVehicleNumber());
				if(booking.getDriver().getDriverProfile().getDriverProfileImage() != null)
					bookingResponse.setDriverImage(DatatypeConverter.printBase64Binary(booking.getDriver().getDriverProfile().getDriverProfileImage()));
			}
			/* MAIL Changes : ZipRyde App Changes to be compliant with TX State Requirements */
			if(loadVehicle) {
				if(booking.getDriver().getDriverVehicleAssociations() != null && booking.getDriver().getDriverVehicleAssociations().size() > 0) {
					VehicleDetail vehicleDetail = booking.getDriver().getDriverVehicleAssociations().get(0).getVehicleDetail();
					bookingResponse.setCabImage(DatatypeConverter.printBase64Binary(vehicleDetail.getProfileImage()));
					bookingResponse.setMake(vehicleDetail.getModel().getMake().getMake());
					bookingResponse.setModel(vehicleDetail.getModel().getModel());
					bookingResponse.setLicensePlateNumber(vehicleDetail.getLicensePlateNo());
				}				
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
		
		if(booking.getTipAmount() != null)
			bookingResponse.setTipAmount(booking.getTipAmount().setScale(2,RoundingMode.CEILING).doubleValue());
		
		if(booking.getTollAmount() != null)
			bookingResponse.setTollAmount(booking.getTollAmount().setScale(2,RoundingMode.CEILING).doubleValue());
		
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

