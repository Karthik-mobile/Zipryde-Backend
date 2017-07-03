package com.trivecta.zipryde.model.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.constants.ZipRydeConstants.USERTYPE;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.DriverProfile;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserType;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
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
	
	public User verifyLogInUser(User user) throws NoResultEntityException {
		if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(user.getUserType().getType())) {
			return getUserByEmailIdPsswdAndUserType(user.getEmailId(),user.getUserType().getType(),user.getPassword());
		}
		else if (USERTYPE.RIDER.equalsIgnoreCase(user.getUserType().getType())){
			return getUserByMobileNoPsswdAndUSerType(user.getMobileNumber(),user.getUserType().getType(),user.getPassword());
		}
		else if (USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())){
			return getUserByMobileNoPsswdAndUSerTypeIsEnable(user.getMobileNumber(),user.getUserType().getType(),user.getPassword(),1);
		}
		return null;
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
	
	private User getUserByMobileNoPsswdAndUSerTypeIsEnable(String mobileNumber,String userType,String password,int isEnable) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			User user = (User) session.getNamedQuery("User.findByMobileNoPsswdUserTypeIsEnable").
					setParameter("mobileNumber", mobileNumber).setParameter("userType", userType).
					setParameter("password", password).setParameter("isEnable", isEnable).getSingleResult();
					
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
		
		if(isUserExistsByMobileNoAndType(user.getMobileNumber(),user.getUserType().getType())) {
			throw new UserValidationException(ErrorMessages.MOBILE_NO_EXISTS_ALREADY);
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
				user.setIsEnable(0);
			}
			
			UserType userType = (UserType) session.getNamedQuery("UserType.findByType").
					setParameter("userType", user.getUserType().getType()).getSingleResult();
			user.setUserType(userType);
			
			session.save(user);
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())) {
				user.getDriverProfile().setUser(user);
				
				Status status = null;
				
				if(user.getDriverProfile().getStatus() != null) {
					status = (Status)
							session.getNamedQuery("Status.findByStatus").
							setParameter("status", user.getDriverProfile().getStatus().getStatus()).getSingleResult();			
				}
				else {
					status = (Status)
							session.getNamedQuery("Status.findByStatus").
							setParameter("status", STATUS.REQUESTED).getSingleResult();			
				}
				user.getDriverProfile().setStatus(status);
				
				session.save(user.getDriverProfile());
			}
			
			user.getDriverProfile();
			return user;
		}		
	}
	
	public User updateUser(User user) throws NoResultEntityException, UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			
			User origUser = session.find(User.class, user.getId());
			
			if(!USERTYPE.WEB_ADMIN.equalsIgnoreCase(origUser.getUserType().getType()) && 
					!origUser.getMobileNumber().equalsIgnoreCase(user.getMobileNumber())) {
				throw new UserValidationException(ErrorMessages.MOBILE_NO_CANNOT_UPDATE);
			}
			
			origUser.setModifiedDate(new Date());
			
			DriverProfile origDriverProfile = null;
			
			Status status = null;
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(user.getUserType().getType())){
				origDriverProfile = session.find(DriverProfile.class, origUser.getDriverProfile().getId());
				
				origDriverProfile.setComments(user.getDriverProfile().getComments());
				
				if(user.getDriverProfile() != null && user.getDriverProfile().getStatus() != null)
				{					
					/*if(STATUS.APPROVED.equalsIgnoreCase(user.getDriverProfile().getStatus().getStatus())) {
						user.setIsEnable(1);	
					}*/
					
					if(user.getDriverProfile().getStatus() != null) {
						status = (Status)
								session.getNamedQuery("Status.findByStatus").
								setParameter("status", user.getDriverProfile().getStatus().getStatus()).getSingleResult();			
					}
					else {
						status = (Status)
								session.getNamedQuery("Status.findByStatus").
								setParameter("status", STATUS.REQUESTED).getSingleResult();			
					}
					
					origDriverProfile.setStatus(status);	
				}
			}
			User newUser = (User)session.merge(origUser);
			
			if(origDriverProfile != null) {
				session.merge(origDriverProfile);
			}
			return newUser;
		}
		catch(NoResultException e) {
			throw new NoResultEntityException(ErrorMessages.NO_USER_FOUND);
		}		
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

	private void fetchLazyInitialisation(User user){
		user.getBookingHistories1();
		user.getCommissions();
		
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
	

}
