package com.trivecta.zipryde.model.dao;

import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.User;

public interface UserDAO {

	public OtpVerification generateAndSaveOTP(OtpVerification otpVerification);

	public String verifyOTP(OtpVerification otpVerification);
	
	public User createUser(User user) throws UserValidationException;
	
	public User updateUser(User user) throws NoResultEntityException,UserValidationException;
	
	public List<User> getAllUserByUserType(String userType);
	
	public User verifyLogInUser(User user) throws NoResultEntityException;
	
	public User getUserByUserId(int userId);
}
