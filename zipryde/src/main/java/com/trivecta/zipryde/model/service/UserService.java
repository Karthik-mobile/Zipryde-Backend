package com.trivecta.zipryde.model.service;

import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserAlreadyLoggedInException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.AppVersion;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;

public interface UserService {

	public OtpVerification generateAndSaveOTP(OtpVerification otpVerification);

	public String verifyOTP(OtpVerification otpVerification);
	
	public User saveUser(User user) throws NoResultEntityException ,UserValidationException;
	
	public List<User> getAllUserByUserType(String userType);
	
	public User verifyLogInUser(User user,AppVersion newAppVersion) throws NoResultEntityException, UserValidationException, UserAlreadyLoggedInException;
	
	public void logOutUser(int userId) throws UserValidationException;
	
	public void updateDeviceToken(String accessToken,String deviceToken);
	
	public User getUserByUserId(int userId);
	
	public Integer getDriverCountBySatus(String status);
	
	public DriverVehicleAssociation assignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle)  throws UserValidationException;
	
	public DriverVehicleAssociation unassignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle)  throws UserValidationException;
	
	public UserSession saveUserSession(UserSession userSession)  throws UserValidationException ; 
	
	public DriverVehicleAssociation getActiveDriverVehicleAssociationByDriverId(int userId) ;
	
	public List<DriverVehicleAssociation> getAllDriverVehicleAssociationByDriverId(int userId);
	
	public Integer getDriverCountByOnline();
	
	public User updatePasswordByUserAndType(User user) throws NoResultEntityException ;
	
	public void deleteUser(User user) throws NoResultEntityException , UserValidationException;
	
	public List<User> getDriversByStatus(String status);
	
	public List<User> getDriversByOnline();

	public List<DriverVehicleAssociation> getDriverVehcileAssociationByDriverIds(List<Integer> userIdList);
	
	public void updateIdleDriverToOffline();
	
	public void updateDriverSession(Integer userId,Double longitude,Double latitude) throws UserValidationException;
}
