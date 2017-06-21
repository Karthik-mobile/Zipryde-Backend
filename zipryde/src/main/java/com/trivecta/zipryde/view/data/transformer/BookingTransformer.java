package com.trivecta.zipryde.view.data.transformer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;

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
	
	public BookingResponse createBooking(BookingRequest bookingRequest) throws ParseException {
		
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
		
		if(bookingRequest.getStartDateTime() != null) {
			DateFormat startDate = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
			booking.setStartDateTime(startDate.parse(bookingRequest.getStartDateTime()));
		}
				
		booking.setOfferedPrice(new BigDecimal(bookingRequest.getOfferedPrice()));
		booking.setOfferedPricePercentage(bookingRequest.getOfferedPricePercentage().intValue());
		
		Booking newBooking = bookingService.createBooking(booking);		
		BookingResponse bookingResponse = setBookingResponseFromBooking(newBooking);		
		return bookingResponse;
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
	
	
	private BookingResponse setBookingResponseFromBooking(Booking booking) {
		BookingResponse bookingResponse = new BookingResponse();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		bookingResponse.setBookingId(booking.getId());
		bookingResponse.setCustomerId(booking.getRider().getId());
		bookingResponse.setCustomerName(booking.getRider().getFirstName()+" "+booking.getRider().getLastName());
		bookingResponse.setDriverId(booking.getDriver().getId());
		
		if(booking.getAcceptedDateTime() != null) {
			bookingResponse.setAcceptedDateTime(dateFormat.format(booking.getAcceptedDateTime()));
			bookingResponse.setAcceptedPrice(booking.getAcceptedPrice().doubleValue());
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
		
		if(booking.getStartDateTime() != null)
			bookingResponse.setStartDateTime(dateFormat.format(booking.getStartDateTime()));
		
		if(booking.getEndDateTime() != null)
			bookingResponse.setEndDateTime(dateFormat.format(booking.getEndDateTime()));
		
		bookingResponse.setFrom(booking.getFrom());
		bookingResponse.setTo(booking.getTo());
		
		
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
		
		
		bookingResponse.setSuggestedPrice(booking.getSuggestedPrice().doubleValue());
		
		if(booking.getOfferedPrice() != null)
			bookingResponse.setOfferedPrice(booking.getOfferedPrice().doubleValue());
		
		
		return bookingResponse;
	}
}
