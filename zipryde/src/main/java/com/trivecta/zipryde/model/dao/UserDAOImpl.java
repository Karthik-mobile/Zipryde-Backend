package com.trivecta.zipryde.model.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.constants.ZipRydeConstants.USERTYPE;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserAlreadyLoggedInException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.DriverProfile;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;
import com.trivecta.zipryde.model.entity.UserType;
import com.trivecta.zipryde.model.entity.VehicleDetail;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.utility.TwilioSMS;
import com.trivecta.zipryde.utility.Utility;
import com.twilio.sdk.TwilioRestException;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	AdminDAO adminDAO;
	
	@Autowired
	MongoDbClient mongoDbClient;
	
	@Autowired
	BookingDAO bookingDAO;
	
	@Autowired
	FCMNotificationDAO fCMNotificationDAO;
	
	@Autowired
	TwilioSMSDAO twilioSMSDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private String generateUniqueOTP() {
		String alphaNumerics = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		String otp = "";
		for (int i = 0; i < 6; i++) {
			otp += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
		}
		return otp;		
	}
	
	private OtpVerification getByMobileNo(String mobileNo) {
		OtpVerification otpVerification = null;
		Session session = this.sessionFactory.getCurrentSession();
		Query qry = session.createNamedQuery("OtpVerification.findByMobile").setParameter("mobileNumber", mobileNo);
		try {
			otpVerification = (OtpVerification) qry.getSingleResult();
		}
		catch(Exception e) {
			//OTP Record not found against this given mobile number
		}
		return otpVerification;
	}
	
	private boolean isUserExistsByMobileNoAndType(String mobileNumber,String userType) {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> user = session.getNamedQuery("User.findByMobileNoAndUserType").
				setParameter("mobileNumber", mobileNumber).setParameter("userType", userType).
				getResultList();
		if(user != null && user.size() >0) {
			return true;
		}
		return false;
	}
	
	public OtpVerification generateAndSaveOTP(OtpVerification otpVerification) {
		OtpVerification origOTPVerification = getByMobileNo(otpVerification.getMobileNumber());
		Session session = this.sessionFactory.getCurrentSession();

		otpVerification.setOtp(generateUniqueOTP());
		
		Calendar validTime = Calendar.getInstance();
		validTime.add(Calendar.MINUTE, 2);
		otpVerification.setValidUntil(validTime.getTime());
		
		if(origOTPVerification == null) {
			session.save(otpVerification);
		}
		else {
			otpVerification.setId(origOTPVerification.getId());
			session.merge(otpVerification);
		}			
		try {
			String message = "Your OTP is "+otpVerification.getOtp() +" for ZipRyde registration ";
			twilioSMSDAO.sendSMS(otpVerification.getMobileNumber(), message);
		} catch (TwilioRestException e) {
			//nothing to do
		}		
		return otpVerification;		
	}
	
	public String verifyOTP(OtpVerification otpVerification) {
		Session session = this.sessionFactory.getCurrentSession();
		Query qry = session.createNamedQuery("OtpVerification.verifyByMobile")
				.setParameter("mobileNumber", otpVerification.getMobileNumber())
				.setParameter("otp", otpVerification.getOtp())
				.setParameter("validUntil", new Date());
		try {
			otpVerification = (OtpVerification) qry.getSingleResult();
			return ZipRydeConstants.OTP_VERIFIED;
		}
		catch(Exception e) {
			//OTP EXPIRED
			return ZipRydeConstants.OTP_EXPIRED;
		}
	}	
	
	public User updatePasswordByUserAndType(User user) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		User origUser = null;
		if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(user.getUserType().getType())) {
			origUser = getUserByEmailIdAndUserType(user.getEmailId(),user.getUserType().getType());
		}
		else {
			origUser = getUserByMobileNoAndType(user.getMobileNumber(),user.getUserType().getType());
		}
		origUser.setPassword(user.getPassword());
		User newUser = (User)session.merge(origUser);		
		return newUser;
	}
	
	public User verifyLogInUser(User user) throws NoResultEntityException, UserValidationException, UserAlreadyLoggedInException {
		User newUser = null;
		if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(user.getUserType().getType())) {
			newUser = getUserByEmailIdPsswdAndUserType(user.getEmailId(),user.getUserType().getType(),user.getPassword());
			if(user.getIsOverride() == 0) {
				validateSessionToken(newUser.getId());
			}
		}
		else {
			Session session = this.sessionFactory.getCurrentSession();
			newUser =  getUserByMobileNoPsswdAndUSerType(user.getMobileNumber(),user.getUserType().getType(),user.getPassword());
			
			if(user.getIsOverride() == 0) {
				validateSessionToken(newUser.getId());
			}
			
			if(newUser.getIsEnable() == 0 ) {
				throw new UserValidationException(ErrorMessages.ACCOUNT_DEACTIVATED);				
			}
			if (USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType()) && 
					(STATUS.REQUESTED.equalsIgnoreCase(newUser.getDriverProfile().getStatus().getStatus()))) {
					throw new UserValidationException(ErrorMessages.DIVER_NOT_APPROVED);								
			}			
			newUser.setDeviceToken(user.getDeviceToken());
			newUser.setModifiedDate(new Date());
			session.merge(newUser);			
		}	
		String otp = saveUserSession(newUser.getId(),1,true);
		fetchLazyInitialisation(newUser);
		newUser.setAccessToken(otp);
		return newUser;
	}
	

	public User logOutUser(int userId) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		User origUser =  null;		
		try {
			origUser = session.find(User.class, userId);
		}
		catch(Exception e){
			throw new UserValidationException(ErrorMessages.NO_USER_FOUND);
		}
		UserSession userSession = new UserSession();
		userSession.setIsActive(0);
		userSession.setSessionToken(null);
		userSession.setValidUntil(null);
		userSession.setUserId(userId);
		userSession.setIsOverride(1);
		saveUserSession(userSession);
		
		origUser.setDeviceToken(null);
		session.merge(origUser);
		return origUser;
		
	}

	private void validateSessionToken(int userId) throws UserAlreadyLoggedInException {
		UserSession userSession = getUserSessionByUserId(userId);
		if(userSession != null && userSession.getSessionToken() != null) {
			throw new UserAlreadyLoggedInException(ErrorMessages.USER_LOGGED_IN_ALREADY);
		}		
	}	
	
	public void updateDeviceToken(String accessToken,String deviceToken){
		Session session = this.sessionFactory.getCurrentSession();		
		Integer userId = null;
		try {
			userId = (Integer) session.getNamedQuery("UserSession.findBySessionToken")
					.setParameter("sessionToken", accessToken).getSingleResult();			
		}
		catch(Exception e){
			//No result
		}
		if(userId != null) {
			User user = getUserByUserId(userId);
			user.setDeviceToken(deviceToken);
			user.setModifiedDate(new Date());
			session.merge(user);
		}
	}
	
	private User getUserByMobileNoAndType(String mobileNumber,String userType) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = (User) session.getNamedQuery("User.findByMobileNoAndUserType").
					setParameter("mobileNumber", mobileNumber).setParameter("userType", userType).getSingleResult();
			return user;
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.NO_USER_FOUND);
		}			
	}
	
	private User getUserByEmailIdAndUserType(String emailId,String userType) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = (User) session.getNamedQuery("User.findByEmailIdAndUserType").
					setParameter("emailId", emailId).setParameter("userType", userType).getSingleResult();
					
			return user;
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.EMAIL_ID_NOT_EXISTS);
		}
	}
	
	private User getUserByEmailIdPsswdAndUserType(String emailId,String userType,String password) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = (User) session.getNamedQuery("User.findByEmailIdPsswdAndUserType").
					setParameter("emailId", emailId).setParameter("userType", userType).setParameter("password", password).getSingleResult();
					
			return user;
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.LOGGIN_FAILED);
		}
	}
	
	private User getUserByMobileNoPsswdAndUSerType(String mobileNumber,String userType,String password) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = (User) session.getNamedQuery("User.findByMobileNoPsswdAndUserType").
					setParameter("mobileNumber", mobileNumber).setParameter("userType", userType).setParameter("password", password).getSingleResult();
					
			return user;
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.LOGGIN_FAILED);
		}
	}

	public User createUser(User user) throws UserValidationException {
		
		if(!USERTYPE.WEB_ADMIN.equalsIgnoreCase(user.getUserType().getType()) && 
				isUserExistsByMobileNoAndType(user.getMobileNumber(),user.getUserType().getType())) {
			throw new UserValidationException(ErrorMessages.MOBILE_NO_EXISTS_ALREADY);
		}
		else if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(user.getUserType().getType())) {
			User emailUser = null;
			try {
				emailUser = getUserByEmailIdAndUserType(user.getEmailId(),user.getUserType().getType());
			} catch (NoResultEntityException e) {
				//No Result
			}
			if(emailUser != null) {
				throw new UserValidationException(ErrorMessages.MOBILE_NO_EXISTS_ALREADY);
			}
		}
		else {
			Session session = this.sessionFactory.getCurrentSession();
			user.setCreationDate(new Date());
			
			/*
			 * By Default DRIVER ,On Creation it will be Disable. 
			 * Once Web Admin Approved, It will get Enable
			 */
			if(USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())){
				if(user.getDriverProfile() != null && user.getDriverProfile().getStatus() != null &&
						STATUS.APPROVED.equalsIgnoreCase(user.getDriverProfile().getStatus().getStatus())) {
					user.setIsEnable(1);	
				}	
				else{
					user.setIsEnable(0);
				}
			}
			else {
				user.setIsEnable(1);
			}
						
			UserType userType = (UserType) session.getNamedQuery("UserType.findByType").
					setParameter("userType", user.getUserType().getType()).getSingleResult();
			user.setUserType(userType);
			
			user.setIsDeleted(0);
			session.save(user);
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())) {
				user.getDriverProfile().setUser(user);				
				Status status = null;				
				if(user.getDriverProfile().getStatus() != null) {
					status = adminDAO.findByStatus(user.getDriverProfile().getStatus().getStatus());
				}
				else {
					status = adminDAO.findByStatus(STATUS.REQUESTED);
				}
				user.getDriverProfile().setStatus(status);				
				session.save(user.getDriverProfile());
				user.getDriverProfile();
			}			
			else if(USERTYPE.RIDER.equalsIgnoreCase(user.getUserType().getType())){
				saveUserSession(user.getId(),1,false);
				//user.setAccessToken(otp);
			}			
			return user;
		}
		return user;		
	}
	
	private String saveUserSession(int userId,int isActive,boolean generateSessionToken) throws UserValidationException {
		UserSession userSession = new UserSession();
		userSession.setUserId(userId);
		userSession.setIsActive(isActive);
		String otp = null;
		if(generateSessionToken){
			otp = generateUniqueOTP() + userId;
			userSession.setSessionToken(Utility.encryptWithMD5(otp));
			Calendar validTime = Calendar.getInstance();
			validTime.add(Calendar.HOUR, 24);
			userSession.setValidUntil(validTime.getTime());
			userSession.setIsOverride(1);
		}		
		saveUserSession(userSession);
		return otp;
	}
	
	public User updateUser(User user) throws NoResultEntityException, UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		User origUser = null;
		try {
			origUser = session.find(User.class, user.getId());
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.NO_USER_FOUND);
		}	
		if (!USERTYPE.WEB_ADMIN.equalsIgnoreCase(origUser.getUserType().getType())
				&& !origUser.getMobileNumber().equalsIgnoreCase(user.getMobileNumber())) {
			throw new UserValidationException(ErrorMessages.MOBILE_NO_CANNOT_UPDATE);
		}

		origUser.setFirstName(user.getFirstName());
		origUser.setLastName(user.getLastName());
		origUser.setAlternateNumber(user.getAlternateNumber());
		origUser.setMobileNumber(user.getMobileNumber());
		origUser.setEmailId(user.getEmailId());
		origUser.setDeviceToken(user.getDeviceToken());
		if (user.getIsEnable() == null) {
			origUser.setIsEnable(0);
		} else {
			origUser.setIsEnable(user.getIsEnable());
		}

		origUser.setModifiedDate(new Date());
		origUser.setModifiedBy(user.getModifiedBy());

		DriverProfile origDriverProfile = null;
		Status status = null;

		if (USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())) {
			origDriverProfile = session.find(DriverProfile.class, origUser.getDriverProfile().getId());

			origDriverProfile.setComments(user.getDriverProfile().getComments());
			origDriverProfile.setLicenseNo(user.getDriverProfile().getLicenseNo());
			origDriverProfile.setLicenseIssuedOn(user.getDriverProfile().getLicenseIssuedOn());
			origDriverProfile.setLicenseValidUntil(user.getDriverProfile().getLicenseValidUntil());
			origDriverProfile.setRestrictions(user.getDriverProfile().getRestrictions());
			
			origDriverProfile.setDriverProfileImage(user.getDriverProfile().getDriverProfileImage());
			origDriverProfile.setLicenseBackImage( user.getDriverProfile().getLicenseBackImage());
			origDriverProfile.setLicenseFrontImage( user.getDriverProfile().getLicenseFrontImage());
			
			if (user.getDriverProfile() != null && user.getDriverProfile().getStatus() != null) {
				if (!user.getDriverProfile().getStatus().getStatus()
						.equalsIgnoreCase(origUser.getDriverProfile().getStatus().getStatus())) {
					String statusStr = user.getDriverProfile().getStatus().getStatus().toUpperCase();
					/*
					 * status = (Status)
					 * session.getNamedQuery("Status.findByStatus").
					 * setParameter("status", statusStr).getSingleResult();
					 */
					status = adminDAO.findByStatus(statusStr);
					origDriverProfile.setStatus(status);
				}
			}
			session.merge(origDriverProfile);
		}

		User newUser = (User) session.merge(origUser);
		return newUser;		
	}
		
	public void deleteUser(User user) throws NoResultEntityException, UserValidationException{
		Session session = this.sessionFactory.getCurrentSession();
		User origUser = null;
		try {
			origUser = session.find(User.class, user.getId());
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.NO_USER_FOUND);
		}	
		if (USERTYPE.DRIVER.equalsIgnoreCase(origUser.getUserType().getType()) && origUser.getDriverProfile() != null && 
				STATUS.ASSIGNED.equalsIgnoreCase(origUser.getDriverProfile().getStatus().getStatus())){
			throw new UserValidationException(ErrorMessages.DRIVER_ASSOCIATED_VEHICLE);
		}
		
		origUser.setIsDeleted(1);
		session.merge(origUser);
	}
	
	public List<User> getAllUserByUserType(String userType) {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> userList = session.getNamedQuery("User.findByUserType").
				setParameter("userType", userType).getResultList();
		for(User user : userList) {
			fetchLazyInitialisation(user);
		}
		return userList;
	}
	
	public User getUserByUserId(int userId){
		Session session = this.sessionFactory.getCurrentSession();
		User user = session.find(User.class, userId);
		fetchLazyInitialisation(user);
		return user;
	}

	public Integer getDriverCountBySatus(String status) {
		if(STATUS.UNASSIGNED.equalsIgnoreCase(status)) {
			return getDriverUnAssignedCount();
		}
		else {
			Session session = this.sessionFactory.getCurrentSession();
			Long userCount= (Long) session.createNamedQuery("User.countByTypeAndStatus").
					setParameter("userType",USERTYPE.DRIVER).setParameter("status", status).getSingleResult();
			if (userCount == null) {
				userCount = 0L;
			}
			return userCount.intValue();
		}
	}
	
	public List<User> getDriversByStatus(String status) {
		if(STATUS.UNASSIGNED.equalsIgnoreCase(status)) {
			return getDriversUnAssigned();
		}
		else {
			Session session = this.sessionFactory.getCurrentSession();
			List<User> userList = session.createNamedQuery("User.findByTypeAndStatus").
					setParameter("userType",USERTYPE.DRIVER).setParameter("status", status).getResultList();
			if (userList != null && userList.size() > 0) {
				for(User usr:userList){
					fetchLazyInitialisation(usr);
				}
			}
			return userList;
		}
	}
	
	private Integer getDriverUnAssignedCount() {
		Session session = this.sessionFactory.getCurrentSession();
		List<String> status = new ArrayList<String>();
		status.add(STATUS.APPROVED);
		status.add(STATUS.UNASSIGNED);
		Long userCount= (Long) session.createNamedQuery("User.countByUnAssignedDriver").
				setParameter("userType",USERTYPE.DRIVER).setParameter("status", status).getSingleResult();
		if (userCount == null) {
			userCount = 0L;
		}
		return userCount.intValue();
	}
	
	private List<User> getDriversUnAssigned() {
		Session session = this.sessionFactory.getCurrentSession();
		List<String> status = new ArrayList<String>();
		status.add(STATUS.APPROVED);
		status.add(STATUS.UNASSIGNED);
		List<User> userList = session.createNamedQuery("User.findUnAssignedDriver").
				setParameter("userType",USERTYPE.DRIVER).setParameter("status", status).getResultList();
		if (userList != null && userList.size() > 0) {
			for(User usr:userList){
				fetchLazyInitialisation(usr);
			}
		}
		return userList;
	}
	
	public Integer getDriverCountByOnline() {
		Session session = this.sessionFactory.getCurrentSession();
		Long userCount= (Long) session.createNamedQuery("User.countByOnline").
				setParameter("userType", USERTYPE.DRIVER).getSingleResult();
		if (userCount == null) {
			userCount = 0L;
		}
		return userCount.intValue();
	}
	
	public List<User> getDriversByOnline() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> userList = session.createNamedQuery("User.findByOnline").
				setParameter("userType", USERTYPE.DRIVER).getResultList();
		if (userList != null && userList.size() > 0) {
			for(User usr:userList){
				fetchLazyInitialisation(usr);
			}
		}
		return userList;
	}
/*	public List<User> getAllApprovedEnabledDrivers() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> userList = session.getNamedQuery("User.findByApprovedAndEnabled").
				setParameter("userType", USERTYPE.DRIVER).
				setParameter("status", STATUS.APPROVED).
				setParameter("isEnable", 1).getResultList();
		for(User user : userList) {
			fetchLazyInitialisation(user);
		}
		return userList;
	}
*/	
	
	private void updateDriverProfileStatus(int driverProfileId,String statusStr,String vehicleNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		Status status = adminDAO.findByStatus(statusStr);
		DriverProfile driverProfile = session.find(DriverProfile.class, driverProfileId);
		driverProfile.setStatus(status);
		driverProfile.setVehicleNumber(vehicleNumber);
		session.merge(driverProfile);
	}
	
	public DriverVehicleAssociation assignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		driverVehicle.setCreationDate(new Date());
			
		User user = getUserByUserId(driverVehicle.getUser().getId());
		if (USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())) {
			driverVehicle.setUser(user);
		} else {
			throw new UserValidationException(ErrorMessages.USER_NOT_DRIVER);
		}
		VehicleDetail vehicle = session.find(VehicleDetail.class, driverVehicle.getVehicleDetail().getId());
		driverVehicle.setVehicleDetail(vehicle);

		driverVehicle.setFromDate(new Date());

		if (vehicle.getInsuranceValidUntil() != null) {
			if (vehicle.getCabPermits() != null && vehicle.getCabPermits().size() > 0 && vehicle.getCabPermits().get(0)
					.getPermitValidUntil().compareTo(vehicle.getInsuranceValidUntil()) > 0) {
				driverVehicle.setToDate(vehicle.getInsuranceValidUntil());
			} else {
				driverVehicle.setToDate(vehicle.getCabPermits().get(0).getPermitValidUntil());

			}
		}
		session.save(driverVehicle);
		updateDriverProfileStatus(driverVehicle.getUser().getDriverProfile().getId(),STATUS.ASSIGNED,vehicle.getVehicleNumber());
		return driverVehicle;		
	}
	
	public DriverVehicleAssociation unassignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		DriverVehicleAssociation origDriverVehicle =
					session.find(DriverVehicleAssociation.class, driverVehicle.getId());
		origDriverVehicle.setToDate(driverVehicle.getToDate());
		origDriverVehicle.setModifiedDate(new Date());
		origDriverVehicle.setModifiedBy(driverVehicle.getModifiedBy());
		session.merge(origDriverVehicle);
		updateDriverProfileStatus(origDriverVehicle.getUser().getDriverProfile().getId(),STATUS.UNASSIGNED,null);
		return origDriverVehicle;		
	}
	
	public DriverVehicleAssociation getActiveDriverVehicleAssociationByDriverId(int userId) {
		Session session = this.sessionFactory.getCurrentSession();
		DriverVehicleAssociation driverAssociation = null;
		try {
			driverAssociation = (DriverVehicleAssociation)
					session.getNamedQuery("DriverVehicleAssociation.findActiveAssociationByUserId").setParameter("userId", userId).getSingleResult();
		}
		catch(Exception e){
			//No Active Association exists
		}
		return driverAssociation;
	}
	
	
	public List<DriverVehicleAssociation> getAllDriverVehicleAssociationByDriverId(int userId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<DriverVehicleAssociation> driverAssociationList =
					session.getNamedQuery("DriverVehicleAssociation.findAllByUserId").setParameter("userId", userId).getResultList();
		return driverAssociationList;
	}
	
	
	public UserSession saveUserSession(UserSession userSession) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		if(userSession.getIsActive() == 0) {
			try {
				Integer userId =  (Integer) session.getNamedQuery("UserSession.findByUserIdAndStatusNotNull")
						.setParameter("userId", userSession.getUserId()).setMaxResults(1).getSingleResult();
				if(userId != null)
					throw new UserValidationException(ErrorMessages.DRIVER_ACTIVE_BOOKING);
			}
			catch(NoResultException e){
				//Nothing to do
			}
		}
		
		UserSession origUserSession = getUserSessionByUserId(userSession.getUserId());
		
		if(origUserSession != null) {
			origUserSession.setIsActive(userSession.getIsActive());	
			if(userSession.getIsOverride() == 1) {
				origUserSession.setSessionToken(userSession.getSessionToken());
				origUserSession.setValidUntil(userSession.getValidUntil());
			}
			session.merge(origUserSession);
			return origUserSession;
		}
		else {
			userSession.setLogInDateTime(new Date());
			session.save(userSession);
			return userSession;
		}
	}
	
	private UserSession getUserSessionByUserId(int userId) {
		Session session = this.sessionFactory.getCurrentSession();
		try{
			UserSession userSession = (UserSession) 
					session.getNamedQuery("UserSession.findByUserId").setParameter("userId", userId).getSingleResult();
			return userSession;
		}
		catch(Exception e){
			//No Result
		}
		return null;
	}
	
	private void fetchLazyInitialisation(User user){
		user.getBookingHistories1();
		user.getCommissions();
		
		UserSession userSession = getUserSessionByUserId(user.getId());
		if(userSession  != null) {
			user.setIsOnline(userSession.getIsActive());
			if(userSession.getBookingId() != null) {
					user.setBookingId(userSession.getBookingId());
			}
		}
		
		if(USERTYPE.RIDER.equalsIgnoreCase(user.getUserType().getType())){
			if(user.getUserPreferedLocations() != null && user.getUserPreferedLocations().size() > 0) {
				user.getUserPreferedLocations().size();
			}
		}
		else if(USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())){
			user.getDriverProfile();
			
			if(user.getDriverVehicleAssociations() != null && 
					user.getDriverVehicleAssociations().size() > 0){
				user.getDriverVehicleAssociations().size();
			}
			
			if(user.getCommissions() != null && user.getCommissions().size() > 0) {
				user.getCommissions().size();
			}			
		}		
	}
	
	public List<DriverVehicleAssociation> getDriverVehcileAssociationByDriverIds(List<Integer> userIdList) {
		Session session = this.sessionFactory.getCurrentSession();
		List<DriverVehicleAssociation> driverVehicleAssociationList = 
				session.getNamedQuery("DriverVehicleAssociation.findActiveAssociationByUserIds").setParameter("userIds", userIdList).getResultList();
		return driverVehicleAssociationList;
	}
	
	@Async
	public void updateIdleDriverToOffline() {
		try {
			mongoDbClient.updateIdleDriverToOffline();
			List<Integer> driverIds = mongoDbClient.findDriversByActive(0);
			if(driverIds != null && driverIds.size() > 0) {
				Session session = this.sessionFactory.getCurrentSession();
				List<UserSession> userSessions = session.getNamedQuery("UserSession.findByActiveUserIds").setParameter("userIds", driverIds).getResultList();
				for(UserSession userSession : userSessions){
					userSession.setIsActive(0);
					session.merge(userSession);
				}
				//bookingDAO.cancelBookingByDriversInOffline(driverIds);				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	
	public void updateDriverSession(Integer userId,Double longitude,Double latitude) throws UserValidationException {
		mongoDbClient.updateDriverSession(String.valueOf(userId), longitude,latitude);
		UserSession userSession  = new UserSession();
		userSession.setUserId(userId);
		userSession.setIsActive(1);
		saveUserSession(userSession);
		
		/**
		 * If the Driver Location is Near By Booking Location, Then Auto Update the Status to Onsite
		 */		
	/*	UserSession origUserSession = getUserSessionByUserId(userId);
		if(origUserSession != null && origUserSession.getBookingId() != null && origUserSession.getBookingId() != 0) {
			Booking booking = bookingDAO.getBookingById(origUserSession.getBookingId());
			if(booking != null &&  STATUS.SCHEDULED.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				Integer driverUserId = mongoDbClient.checkDriverNearByBookingLocation(
						String.valueOf(userId), booking.getFromLongitude().doubleValue(), booking.getFromLatitude().doubleValue());
				if(driverUserId != null){
					Booking updateBooking = new Booking();
					updateBooking.setId(booking.getId());
					
					Status status = new Status();
					status.setStatus(STATUS.ON_SITE);
					updateBooking.setDriverStatus(status);					
					bookingDAO.updateBookingDriverStatus(updateBooking);
				}				
			}
		}*/		
	}
}
