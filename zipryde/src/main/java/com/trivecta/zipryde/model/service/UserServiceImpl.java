package com.trivecta.zipryde.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.dao.UserDAO;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.User;

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
	public Integer getUserCountByTypeAndStatus(String userType, String status) {
		return userDAO.getUserCountByTypeAndStatus(userType, status);
	}
}
