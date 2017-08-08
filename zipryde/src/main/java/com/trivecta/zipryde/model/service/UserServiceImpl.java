package com.trivecta.zipryde.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.dao.UserDAO;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	UserDAO userDAO;
	
	@Transactional
	public OtpVerification generateAndSaveOTP(OtpVerification otpVerification) {
		OtpVerification otpResponse = userDAO.generateAndSaveOTP(otpVerification);
		return otpResponse;
	}
	
	@Transactional
	public String verifyOTP(OtpVerification otpVerification) {
		String verificationMsg = userDAO.verifyOTP(otpVerification);
		return verificationMsg;
	}

	@Transactional 
	public User saveUser(User user) throws NoResultEntityException, UserValidationException {
		if(user.getId() != null && user.getId() != 0) {
			return userDAO.updateUser(user);
		}
		else {
			return userDAO.createUser(user);
		}
	}
	
	@Transactional
	public List<User> getAllUserByUserType(String userType) {
		return userDAO.getAllUserByUserType(userType);
	}
	
	@Transactional
	public User verifyLogInUser(User user) throws NoResultEntityException, UserValidationException {
		return userDAO.verifyLogInUser(user);
	}
	
	@Transactional
	public User getUserByUserId(int userId){
		return userDAO.getUserByUserId(userId);
	}

	@Transactional
	public Integer getDriverCountBySatus(String status) {
		return userDAO.getDriverCountBySatus(status);
	}
	
	@Transactional
	public DriverVehicleAssociation unassignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle) throws UserValidationException {
		return userDAO.unassignDriverVehicleAssociation(driverVehicle);
	}
	
	@Transactional
	public DriverVehicleAssociation assignDriverVehicleAssociation(DriverVehicleAssociation driverVehicle) throws UserValidationException {
		return userDAO.assignDriverVehicleAssociation(driverVehicle);
	}
	
	@Transactional
	public UserSession saveUserSession(UserSession userSession) throws UserValidationException {
		return userDAO.saveUserSession(userSession);
	}
	
	@Transactional
	public DriverVehicleAssociation getActiveDriverVehicleAssociationByDriverId(int userId){
		return userDAO.getActiveDriverVehicleAssociationByDriverId(userId);
	}
	
	@Transactional
	public List<DriverVehicleAssociation> getAllDriverVehicleAssociationByDriverId(int userId){
		return userDAO.getAllDriverVehicleAssociationByDriverId(userId);
	}
	
	@Transactional
	public Integer getDriverCountByOnline() {
		return userDAO.getDriverCountByOnline();
	}
	
	@Transactional
	public User updatePasswordByUserAndType(User user) throws NoResultEntityException {
		return userDAO.updatePasswordByUserAndType(user);
	}

	@Transactional
	public void deleteUser(User user) throws NoResultEntityException , UserValidationException{
		userDAO.deleteUser(user);		
	}

	@Transactional
	public List<User> getDriversByStatus(String status) {
		return userDAO.getDriversByStatus(status);
	}
	
	@Transactional
	public List<User> getDriversByOnline() {
		return userDAO.getDriversByOnline();
	}
	
	@Transactional
	public List<DriverVehicleAssociation> getDriverVehcileAssociationByDriverIds(List<Integer> userIdList) {
		return userDAO.getDriverVehcileAssociationByDriverIds(userIdList);
	}
	
	@Transactional
	public void updateIdleDriverToOffline() {
		userDAO.updateIdleDriverToOffline();
	}
}
