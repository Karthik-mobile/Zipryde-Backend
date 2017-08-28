package com.trivecta.zipryde.model.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_MESSAGE;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.Notification;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.BookingRequest;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.LostItem;
import com.trivecta.zipryde.model.entity.Payment;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.mongodb.UserGeoSpatialResponse;
import com.trivecta.zipryde.utility.TwilioSMS;
import com.twilio.sdk.TwilioRestException;

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
	
	@Autowired
	CommissionDAO commissionDAO;
	
	@Autowired
	PaymentDAO paymentDAO;
	
	@Autowired
	FCMNotificationDAO fCMNotificationDAO;
	
	@Autowired
	TwilioSMSDAO twilioSMSDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
		
	private String generateUniqueCRN() {
		String alphaNumerics = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		String crn = "";
		for (int i = 0; i < 8; i++) {
			crn += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
		}
		return "CRN-"+crn;		
	}
	
	public Booking createBooking(Booking booking){
		Session session = this.sessionFactory.getCurrentSession();
		Integer isFutureBooking = 0;
		
		if(booking.getBookingDateTime() == null){
			booking.setBookingDateTime(new Date());
			
			Status bookingStatus = adminDAO.findByStatus(STATUS.REQUESTED);
			booking.setBookingStatus(bookingStatus);
		}
		else {
			isFutureBooking = 1;
			Status bookingStatus = adminDAO.findByStatus(STATUS.FUTURE_REQUESTED);
			booking.setBookingStatus(bookingStatus);	
		}
		
		CabType cabType = session.find(CabType.class, booking.getCabType().getId());
		booking.setCabType(cabType);
		
		User customer = session.find(User.class, booking.getRider().getId());
		booking.setRider(customer);
		
		if(booking.getDriver() != null) {
			User driver = session.find(User.class, booking.getDriver().getId());
			booking.setDriver(driver);
		}
			
		BigDecimal suggestedPrice = 
				pricingDAO.calculatePricingByTypeDistanceAndPerson(
						booking.getDistanceInMiles(),booking.getCabType().getId(),booking.getNoOfPassengers());
		booking.setSuggestedPrice(suggestedPrice);
		booking.setCrnNumber(generateUniqueCRN());
		booking.setCreationDate(new Date());
		session.save(booking);
		updateUserSessionStatus(booking.getRider().getId(),STATUS.REQUESTED,booking.getId());
		assignBookingToNearBYDrivers(booking,isFutureBooking);
		return booking;		
	}
	
	
	/*
	 * DRIVER STATUS - ACCEPTED / REJECTED / ON-TRIP / COMPLETED
	 */
	public Booking updateBookingDriverStatus(Booking booking) throws UserValidationException{
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, booking.getId());
		
		if(STATUS.CANCELLED.equalsIgnoreCase(origBooking.getBookingStatus().getStatus())) {
			throw new UserValidationException(ErrorMessages.BOOKING_CANCELLED);
		}
		else {			
			boolean updateBooking = true;
			
			if(STATUS.ON_SITE.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
						!STATUS.ACCEPTED.equalsIgnoreCase(origBooking.getDriverStatus().getStatus())) {
					updateBooking = false;
			}
			else if(STATUS.ON_TRIP.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
					!STATUS.ON_SITE.equalsIgnoreCase(origBooking.getDriverStatus().getStatus())) {
				updateBooking = false;
			}
			else if(STATUS.COMPLETED.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
					!STATUS.ON_TRIP.equalsIgnoreCase(origBooking.getDriverStatus().getStatus())) {
				updateBooking = false;
			}
			else if(STATUS.PAID.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
					!STATUS.COMPLETED.equalsIgnoreCase(origBooking.getDriverStatus().getStatus())) {
				updateBooking = false;
			}
			
			if(updateBooking && STATUS.ON_SITE.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
				Integer driverId = mongoDbClient.checkDriverNearByBookingLocation(
						String.valueOf(origBooking.getDriver().getId()), origBooking.getFromLongitude().doubleValue(), origBooking.getFromLatitude().doubleValue());
				if(driverId == null) {
					updateBooking = false;
				}
				else {
					try {
						String vehicleNumber = "";
						if(origBooking.getDriver() != null && origBooking.getDriver().getDriverProfile()!= null) {
							vehicleNumber = origBooking.getDriver().getDriverProfile().getVehicleNumber();
						}
						String message = "Your Zipryde "+ vehicleNumber  +"is arriving";
						twilioSMSDAO.sendSMS(origBooking.getRider().getMobileNumber(), message);
					} catch (TwilioRestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
			
			if(updateBooking) {
				boolean isDriverAccepted = false;
				
				if((!STATUS.ACCEPTED.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
						!STATUS.SCHEDULED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) ||
						(origBooking.getDriverStatus() != null && 
						 STATUS.SCHEDULED.equalsIgnoreCase(origBooking.getDriverStatus().getStatus()) && 
								STATUS.ACCEPTED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) || 
						(STATUS.ACCEPTED.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && 
								origBooking.getDriver() == null) || 
						 (STATUS.SCHEDULED.equalsIgnoreCase(booking.getDriverStatus().getStatus()) && origBooking.getDriver() == null))
					{					
					Status driverStatus = adminDAO.findByStatus(booking.getDriverStatus().getStatus());
					origBooking.setDriverStatus(driverStatus);			
					
					if(STATUS.ACCEPTED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
						User driver = session.find(User.class, booking.getDriver().getId());
						origBooking.setDriver(driver);
						
						Status bookingStatus = adminDAO.findByStatus(STATUS.SCHEDULED);
						origBooking.setBookingStatus(bookingStatus);	
						origBooking.setAcceptedDateTime(new Date());
						origBooking.setAcceptedPrice(origBooking.getOfferedPrice());				
		
						User user = session.find(User.class,origBooking.getRider().getId());
						if(user.getCancellationCount() == null) {
							user.setCancellationCount(0);
						}
						else {
							user.setCancellationCount(user.getCancellationCount()+1);
						}
						session.merge(user);
						origBooking.setRider(user);						
						updateUserSessionStatus(origBooking.getDriver().getId(),STATUS.ACCEPTED,origBooking.getId());
						updateUserSessionStatus(origBooking.getRider().getId(),STATUS.SCHEDULED,origBooking.getId());
						isDriverAccepted = true;
					}
					else if(STATUS.SCHEDULED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
						User driver = session.find(User.class, booking.getDriver().getId());
						origBooking.setDriver(driver);
						
						Status bookingStatus = adminDAO.findByStatus(STATUS.ACCEPTED);
						origBooking.setBookingStatus(bookingStatus);	
						origBooking.setAcceptedDateTime(new Date());
						origBooking.setAcceptedPrice(origBooking.getOfferedPrice());				
		
						User user = session.find(User.class,origBooking.getRider().getId());
						if(user.getCancellationCount() == null) {
							user.setCancellationCount(0);
						}
						else {
							user.setCancellationCount(user.getCancellationCount()+1);
						}
						session.merge(user);
						origBooking.setRider(user);						
						isDriverAccepted = true;
					}
					else {
						origBooking.setBookingStatus(driverStatus);
						if(STATUS.ON_TRIP.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
							origBooking.setStartDateTime(new Date());
							updateUserSessionStatus(origBooking.getDriver().getId(),STATUS.ON_TRIP,origBooking.getId());
							updateUserSessionStatus(origBooking.getRider().getId(),STATUS.ON_TRIP,origBooking.getId());
						}
						else if(STATUS.COMPLETED.equalsIgnoreCase(booking.getDriverStatus().getStatus())) {
							origBooking.setEndDateTime(new Date());
							updateUserSessionStatus(origBooking.getDriver().getId(),STATUS.COMPLETED,origBooking.getId());
							updateUserSessionStatus(origBooking.getRider().getId(),STATUS.COMPLETED,origBooking.getId());
						//	commissionDAO.updateCommision(origBooking);
						}			
					}	
					origBooking.setModifiedDate(new Date());
					origBooking = (Booking)session.merge(origBooking);
					if(isDriverAccepted) {
						fCMNotificationDAO.sendBookingConfirmationNotification(origBooking);
						deleteAcceptedBookingRequest(origBooking.getId());
					}
					else {
						fCMNotificationDAO.sendBookingStatusNotification(origBooking,false);
					}
				}
				else {
					throw new UserValidationException(ErrorMessages.BOOKING_ACCEPTED_ALREADY);
				}
			}
		
			return origBooking;
		}
	}
	
	private void updateUserSessionStatus(int userId,String status,Integer bookingId) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			UserSession userSession = (UserSession)session.getNamedQuery("UserSession.findByUserId").
					setParameter("userId", userId).getSingleResult();
			userSession.setStatus(status);
			userSession.setBookingId(bookingId);
			session.merge(userSession);
		}
		catch(Exception e){
			//No result
		}
	}
	
	/*
	 * Booking Status -  CANCELLED / PAID
	 */	
	public Booking updateBookingStatus(Booking booking,Payment payment) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, booking.getId());
		boolean isDriver = true;
		boolean updateBooking = true;
		
		if(STATUS.CANCELLED.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {		
			if((STATUS.ON_TRIP.equalsIgnoreCase(origBooking.getBookingStatus().getStatus()) || 
				STATUS.COMPLETED.equalsIgnoreCase(origBooking.getBookingStatus().getStatus()))) {
				throw new UserValidationException(ErrorMessages.BOOKING_CANNOT_CANCEL);
			}
			else {
				User user = session.find(User.class,origBooking.getRider().getId());
				if(user.getCancellationCount() == null) {
					user.setCancellationCount(1);
				}
				else {
					user.setCancellationCount(user.getCancellationCount()+1);
				}
				session.merge(user);
				origBooking.setRider(user);	
				updateUserSessionStatus(origBooking.getRider().getId(),null,null);
				if(origBooking.getDriver() != null) {
					updateUserSessionStatus(origBooking.getDriver().getId(),null,null);
				}
				else {
					isDriver = false;
				}
				deleteAcceptedBookingRequest(origBooking.getId());
			}
		}
		if(STATUS.PAID.equalsIgnoreCase(booking.getBookingStatus().getStatus()) &&
				!STATUS.COMPLETED.equalsIgnoreCase(origBooking.getDriverStatus().getStatus())){
			updateBooking = false;
		}
		
		if(updateBooking) {
			Status bookingStatus = 	adminDAO.findByStatus(booking.getBookingStatus().getStatus());
			origBooking.setBookingStatus(bookingStatus);
			session.merge(origBooking);
			//IF PAID, THEN UPDATE USER SESSION STATUS AND SAVE PAYMENT
			if(STATUS.PAID.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				updateUserSessionStatus(origBooking.getDriver().getId(),null,null);
				updateUserSessionStatus(origBooking.getRider().getId(),null,null);
				commissionDAO.updateCommision(origBooking);			
				paymentDAO.savePayment(payment);
			}	
			else if(!STATUS.SCHEDULED.equalsIgnoreCase(origBooking.getBookingStatus().getStatus())) {
				fCMNotificationDAO.sendBookingStatusNotification(origBooking,isDriver);
			}		
		}
		
		return origBooking;
	}
	
	public void cancelBookingByDriversInOffline(List<Integer> driverIds) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Booking> bookings = session.getNamedQuery("Booking.findByBookingStatusAndDriverIds").
				setParameter("status", STATUS.SCHEDULED).setParameter("driverIds", driverIds).getResultList();
		if(bookings != null && bookings.size() > 0) {
			for(Booking booking : bookings) {
				Status bookingStatus = 	adminDAO.findByStatus(STATUS.CANCELLED);
				booking.setBookingStatus(bookingStatus);
				session.merge(booking);
				updateUserSessionStatus(booking.getDriver().getId(),null,null);	
				updateUserSessionStatus(booking.getRider().getId(),null,null);
				deleteAcceptedBookingRequest(booking.getId());
				fCMNotificationDAO.sendBookingStatusNotification(booking,false);
			}
		}		
	}
	
	public Integer getBookingCountByDate(Date bookingDate) {
		Session session = this.sessionFactory.getCurrentSession();
		Long bookingCount = null;
		try {
			bookingCount =  (Long) session.getNamedQuery("Booking.countByBookingDate").
				 setParameter("bookingDate", bookingDate).getSingleResult();
		}
		catch(NoResultException e){
			//No Count
		}
		if(bookingCount == null){
			bookingCount = 0L;
		}
		return bookingCount.intValue();
	}
	
	public Integer getBookingCountByDateAndDriverId(Date bookingDate,Integer driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		Long bookingCount = null;
		try {
			bookingCount = (Long) session.getNamedQuery("Booking.countByBookingDateAndDriverId")
					 .setParameter("bookingDate", bookingDate).setParameter("driverId", driverId).getSingleResult();
		}
		catch(NoResultException e){
			//No Count
		}
		if(bookingCount == null){
			bookingCount = 0L;
		}
		return bookingCount.intValue();
	}
	
	public Booking getBookingById(int bookingId) {
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking  = session.find(Booking.class, bookingId);
		fetchLazyInitialisation(origBooking);
		return origBooking;
	}
	
	public List<Booking> getBookingByDate(Date bookingDate) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingDate").
				 setParameter("bookingDate", bookingDate).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public Integer getBookingCountByDateNotInRequested(Date bookingDate) {
		Session session = this.sessionFactory.getCurrentSession();
		Long bookingCount = (Long) session.getNamedQuery("Booking.countByBookingDateNotInRequested").
				 setParameter("bookingDate", bookingDate).getSingleResult();
		if(bookingCount == null) {
		 bookingCount = 0L;
		}
		return bookingCount.intValue();
	}
	
	public List<Booking> getBookingByDateNotInRequested(Date bookingDate) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingDateNotInRequested").
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
	
	public List<Booking> getBookingByBookingStatusAndDriverId(String status,int driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingStatusAndDriverId")
				 .setParameter("status", status).setParameter("driverId", driverId).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByBookingStatusAndUserId(String status,int customerId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByBookingStatusAndUserId")
				 .setParameter("status", status).setParameter("riderId", customerId).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByDriverId(int driverId,int paginationNo) {
		Session session = this.sessionFactory.getCurrentSession();
		int maxResult = 10;
		int firstResult = maxResult - 9;
		if(paginationNo != 0) {
			maxResult = paginationNo * 10;
			firstResult = maxResult - 9;
		}
		List<Booking> bookingList = 
				 session.getNamedQuery("Booking.findByDriverId").
				 setParameter("driverId", driverId).setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingByuserId(int customerId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("Booking.findByRiderId").setParameter("riderId", customerId).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getBookingRequestedByDriverId(int driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("BookingRequest.findByDriverId").setParameter("userId", driverId).getResultList();
		 if(bookingList != null && bookingList.size() >0){
			 for(Booking booking : bookingList){
				 fetchLazyInitialisation(booking);
			 }
		 }
		 return bookingList;
	}
	
	public List<Booking> getFutureBookingRequestedByDriverId(int driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		 List<Booking> bookingList = session.getNamedQuery("BookingRequest.findByFutureBookingAndDriverId").setParameter("userId", driverId).getResultList();
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
	
	@Async
	private void assignBookingToNearBYDrivers(Booking booking,Integer isFutureBooking) {
		Session session = this.sessionFactory.getCurrentSession();
		List<UserGeoSpatialResponse> nearByDriversList = mongoDbClient.getNearByActiveDrivers(booking.getFromLongitude().doubleValue(), booking.getFromLatitude().doubleValue());
		
		if(nearByDriversList != null && nearByDriversList.size() > 0) {
			List<Integer> userIdList = nearByDriversList.stream()
	                .map(UserGeoSpatialResponse::getUserId)
	                .collect(Collectors.toList());
			
			List<Integer> driverIdList = session.getNamedQuery("UserSession.findByUserIdsAndNoStatus")
					.setParameter("userIds", userIdList).getResultList();			
			
			if(driverIdList != null && driverIdList.size() > 0) {
				List<DriverVehicleAssociation> driverVehicleAssociationList = 
						session.getNamedQuery("DriverVehicleAssociation.findByCabTypeAndUserIds")
						.setParameter("cabTypeId", booking.getCabType().getId())
						.setParameter("userIds", driverIdList).getResultList();
				for(DriverVehicleAssociation driverVehicleAssociation : driverVehicleAssociationList)	{
					createBookingRequest(booking,driverVehicleAssociation.getUser(),isFutureBooking);
				}
			}
		}
	}
	
	@Async
	private void deleteAcceptedBookingRequest(Integer bookingId){
		Session session = this.sessionFactory.getCurrentSession();
		try {
			session.getNamedQuery("BookingRequest.deleteByBookingId").setParameter("bookingId", bookingId).executeUpdate();
		}
		catch(Exception e){
			//Delete the Accepted Booking Request
		}
	}
	
	@Async
	private void createBookingRequest(Booking booking,User user,Integer isFutureBooking) {
		Session session = this.sessionFactory.getCurrentSession();
		//List<> = mongoDbClient.getNearByActiveDrivers(longitude, latitude);
		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setBooking(booking);
		bookingRequest.setUser(user);
		bookingRequest.setCreationDate(new Date());
		bookingRequest.setIsDeleted(0);
		bookingRequest.setIsFutureRequest(isFutureBooking);
		session.save(bookingRequest);
		fCMNotificationDAO.sendBookingRequestNotification(booking,user);
	}	
	
	public void updateBookinStatusUnAnswered(){
		Session session = this.sessionFactory.getCurrentSession();
		try {
			session.createStoredProcedureCall("UPDATE_BOOKING_EXCEEDS_TIME").execute();
		}
		catch(Exception e) {
			//No Data to Processed			
		}
	}
	
	
	/**----------- Lost Items ----------------- */
	
	public LostItem saveLostItem(LostItem newLostItem) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		LostItem lostItem = getLostItemByBookingId(newLostItem.getBookingId());
		
		if(lostItem == null) {
			Booking booking = getBookingById(newLostItem.getBookingId());
			if(booking != null) {
				if(STATUS.COMPLETED.equalsIgnoreCase(booking.getBookingStatus().getStatus()) || 
						STATUS.PAID.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
					newLostItem.setCrnNumber(booking.getCrnNumber());
					if(booking.getDriver() != null)
						newLostItem.setDriverMobileNumber(booking.getDriver().getMobileNumber());
					newLostItem.setUserMobileNumber(booking.getRider().getMobileNumber());
					session.save(newLostItem);
					return newLostItem;
				}
				else {
					throw new UserValidationException(ErrorMessages.LOST_RAISED_COMPLETED);
				}
				
			}
			else {
				throw new UserValidationException(ErrorMessages.NO_BOOKING_FOUND);
			}
		}
		else{
			lostItem.setComments(newLostItem.getComments());
			lostItem.setModifiedDate(new Date());
			session.merge(lostItem);
			return lostItem;
		}
	}
	
	public LostItem getLostItemByBookingId(int bookingId) {
		Session session = this.sessionFactory.getCurrentSession();
		LostItem lostItem = null;
		try {
			lostItem = (LostItem)session.getNamedQuery("LostItem.findByBookingId").
					setParameter("bookingId", bookingId).getSingleResult();
		}
		catch(NoResultException e){
			//no data
		}
		return lostItem;
	}
	
	public List<LostItem> getAllLostItem() {
		Session session = this.sessionFactory.getCurrentSession();
		List<LostItem> lostItems = session.getNamedQuery("LostItem.findAll").getResultList();
		return lostItems;
	}
}
