package com.trivecta.zipryde.model.service;

import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;

public interface UserService {

	public OtpVerification generateAndSaveOTP(OtpVerification otpVerification);

	public String verifyOTP(OtpVerification otpVerification);
	
	public User saveUser(User user) throws NoResultEntityException ,UserValidationException;
	
	public List<User> getAllUserByUserType(String userType);
	
	public User verifyLogInUser(User user) throws NoResultEntityException, UserValidationException;
	
	public User getUserByUserId(int userId);
	
	public Integer getUserCountByTypeAndStatus(String userType,String status);
	
	public List<User> getAllApprovedEnabledDrivers();
	
	public DriverVehicleAssociation saveDriverVehicleAssociation(DriverVehicleAssociation driverVehicle)  throws UserValidationException;
	
	public UserSession saveUserSession(UserSession userSession);
	
	public DriverVehicleAssociation getActiveDriverVehicleAssociationByDriverId(int userId) ;
	
	public List<DriverVehicleAssociation> getAllDriverVehicleAssociationByDriverId(int userId);
	
	public Integer getDriverCountByOnline();
	
	public User updatePasswordByUserAndType(User user) throws NoResultEntityException ;
}
