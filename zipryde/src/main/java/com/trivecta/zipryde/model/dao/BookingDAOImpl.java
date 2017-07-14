package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.BookingRequest;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.PricingMstr;
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
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Booking saveBooking(Booking booking) {
		if(booking.getId() != 0) {
			//Update Booking
		}
		else {
			//Create Booking
		}
		return booking;
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
		
		BigDecimal suggestedPrice = 
				pricingDAO.calculatePricingByTypeDistanceAndPerson(
						booking.getDistanceInMiles(),booking.getCabType().getId(),booking.getNoOfPassengers());
		booking.setSuggestedPrice(suggestedPrice);
		
		session.save(booking);
		createBookingRequest(booking);
		return booking;		
	}
	
	@Async
	private BookingRequest createBookingRequest(Booking booking) {
		
		//List<> = mongoDbClient.getNearByActiveDrivers(longitude, latitude);
		BookingRequest bookingRequest = new BookingRequest();
		
		return bookingRequest;
	}
	
	
	
	
}
