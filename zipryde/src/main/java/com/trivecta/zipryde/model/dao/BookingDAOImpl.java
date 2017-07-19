package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.BookingRequest;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.mongodb.UserGeoSpatialResponse;

@Repository
public class BookingDAOImpl implements BookingDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	PricingDAO pricingDAO;
	
	@Autowired
	MongoDbClient mongoDbClient;
	
	@Autowired
	AdminDAO adminDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
		
	private String generateUniqueCRN() {
		String alphaNumerics = "1234567890";
		String crn = "";
		for (int i = 0; i < 8; i++) {
			crn += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
		}
		return "CRN-"+crn;		
	}
	
	public Booking createBooking(Booking booking){
		Session session = this.sessionFactory.getCurrentSession();
		
		booking.setBookingDateTime(new Date());
		
		CabType cabType = session.find(CabType.class, booking.getCabType().getId());
		booking.setCabType(cabType);
		
		User customer = session.find(User.class, booking.getRider().getId());
		booking.setRider(customer);
		
		if(booking.getDriver() != null) {
			User driver = session.find(User.class, booking.getDriver().getId());
			booking.setDriver(driver);
		}
	
		//booking.setCrnNumber(generateUniqueCRN());
		Status bookingStatus = adminDAO.findByStatus(STATUS.REQUESTED);
		booking.setBookingStatus(bookingStatus);
		
		booking.setBookingDateTime(new Date());
		
		BigDecimal suggestedPrice = 
				pricingDAO.calculatePricingByTypeDistanceAndPerson(
						booking.getDistanceInMiles(),booking.getCabType().getId(),booking.getNoOfPassengers());
		booking.setSuggestedPrice(suggestedPrice);
		
		session.save(booking);
		createBookingRequest(booking);
		return booking;		
	}
	
	
	/*
	 * DRIVER STATUS - ACCEPTED / REJECTED / ON-TRIP / COMPLETED
	 */
	public Booking updateBookingDriverStatus(Booking booking){
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, booking.getId());
		Status driverStatus = adminDAO.findByStatus(booking.getDriverStatus().getStatus());
		origBooking.setDriverStatus(driverStatus);
		
		User driver = session.find(User.class, booking.getDriver().getId());
		origBooking.setDriver(driver);
		
		if(STATUS.ACCEPTED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
			Status bookingStatus = adminDAO.findByStatus(STATUS.SCHEDULED);
			origBooking.setBookingStatus(bookingStatus);
			origBooking.setCrnNumber(generateUniqueCRN());
		}
		else if(STATUS.COMPLETED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
			origBooking.setBookingStatus(driverStatus);
		}
		
		session.merge(origBooking);
		return origBooking;
	}
	
	/*
	 * Booking Status - SCHEDULED / REJECTED / COMPLETED
	 */
	
	public Booking updateBookingStatus(Booking booking) {
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, booking.getId());
		Status bookingStatus = 	adminDAO.findByStatus(booking.getBookingStatus().getStatus());
		origBooking.setBookingStatus(bookingStatus);
		session.merge(origBooking);
		return origBooking;
	}
	
	
	public Booking getBookingById(int bookingId) {
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, bookingId);
		fetchLazyInitialisation(origBooking);
		return origBooking;
	}
	
	public List<Booking> getBookingByDate(Date bookingDate) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingStartDate").
				 setParameter("bookingDate", bookingDate).getResultList();
		 
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByBookingStatus(String status) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingStatus").setParameter("status", status).getResultList();
		 
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByDriverId(int driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByDriverId").setParameter("driverId", driverId).getResultList();
		 
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByCustomerId(int customerId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByRiderId").setParameter("riderId", customerId).getResultList();
		 
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	private void fetchLazyInitialisation(Booking booking) {
		if(booking.getBookingRequests() != null) {
			booking.getBookingRequests().size();
		}
		if(booking.getPayments() != null) {
			booking.getPayments().size();
		}
		booking.getBookingStatus();
		booking.getCabType();
		booking.getDriverStatus();	
	}
	
	
	private void assignBookingToNearBYDrivers(Booking booking) {
		List<UserGeoSpatialResponse> nearByDriversList = mongoDbClient.getNearByActiveDrivers(booking.getFromLongitude().doubleValue(), booking.getFromLatitude().doubleValue());
		
	}
	
	@Async
	private BookingRequest createBookingRequest(Booking booking) {
		
		//List<> = mongoDbClient.getNearByActiveDrivers(longitude, latitude);
		BookingRequest bookingRequest = new BookingRequest();
		
		return bookingRequest;
	}
	
	
	
	
}
