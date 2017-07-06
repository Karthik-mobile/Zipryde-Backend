package com.trivecta.zipryde.view.data.transformer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.USERTYPE;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.DriverProfile;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.OtpVerification;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserSession;
import com.trivecta.zipryde.model.entity.UserType;
import com.trivecta.zipryde.model.entity.VehicleDetail;
import com.trivecta.zipryde.model.service.UserService;
import com.trivecta.zipryde.utility.Utility;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.DriverVehicleAssociationRequest;
import com.trivecta.zipryde.view.request.OTPRequest;
import com.trivecta.zipryde.view.request.UserRequest;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.DriverVehicleAssociationResponse;
import com.trivecta.zipryde.view.response.OTPResponse;
import com.trivecta.zipryde.view.response.UserResponse;

@Component
public class UserTransformer {

	@Autowired
	UserService userService;
	
	private OTPResponse setOTPResponse(OtpVerification otpVerification) {
		if(otpVerification != null) {
			OTPResponse response = new OTPResponse();
			
			response.setMobileNumber(String.valueOf(otpVerification.getMobileNumber()));
			response.setOtp(otpVerification.getOtp());
			
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");		
			response.setValidity(dateFormat.format(otpVerification.getValidUntil()));
			
			return response;
		}
		else {
			return null;
		}	
	}
	
	private OtpVerification createOtpVerificationFromRequest(OTPRequest otpRequest) {
		OtpVerification newOtpVerification = new OtpVerification();
		
		newOtpVerification.setMobileNumber(otpRequest.getMobileNumber());
		newOtpVerification.setOtp(otpRequest.getOtp());
		
		return newOtpVerification;
	}
	
	public OTPResponse getOTPByMobile(OTPRequest otpRequest) throws MandatoryValidationException {
		if(ValidationUtil.isValidString(otpRequest.getMobileNumber())) {
			OtpVerification otpVerification = 
					userService.generateAndSaveOTP(createOtpVerificationFromRequest(otpRequest));
			//TODO: Send OTP in SMS
			return setOTPResponse(otpVerification);
		}
		else {
			throw new MandatoryValidationException(ErrorMessages.MOBILE_MANDATORY);			
		}		
	}
	
	public OTPResponse verifyOTPByMobile(OTPRequest otpRequest) throws MandatoryValidationException {
		String errorMessage = "";
		if(!ValidationUtil.isValidString(otpRequest.getMobileNumber())) {
			errorMessage = errorMessage + ErrorMessages.MOBILE_MANDATORY+"\n";
		}
		if(!ValidationUtil.isValidString(otpRequest.getOtp())) {
			errorMessage = errorMessage + ErrorMessages.OTP_MANDATORY;
		}		
		if(errorMessage != "") {
			throw new MandatoryValidationException(errorMessage);
		}
		else {
			OtpVerification otpVerification =
					createOtpVerificationFromRequest(otpRequest);
			
			String verificationMessage = userService.verifyOTP(otpVerification);
			OTPResponse otpResponse = new OTPResponse();
			otpResponse.setOtpStatus(verificationMessage);
			return otpResponse;
		}		
	}
	
	
	public UserResponse saveUser(UserRequest userRequest) throws ParseException, NoResultEntityException, MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer("");
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date licenseValidUntil = null;
		Date licenseIssuedOn = null;
		
		if(!ValidationUtil.isValidString(userRequest.getUserType())) {
			errorMsg = errorMsg.append(ErrorMessages.USER_TYPE_MANDATORY);
		}
		
		if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(userRequest.getUserType())){ 
			if(!ValidationUtil.isValidString(userRequest.getEmailId())) {
				errorMsg = errorMsg.append(ErrorMessages.EMAIL_MANDATORY);
			}
		}
		else if(USERTYPE.DRIVER.equalsIgnoreCase(userRequest.getUserType())) {
			if(userRequest.getLicenseIssuedOn() != null) {
				licenseIssuedOn = 
						dateFormat.parse(userRequest.getLicenseIssuedOn());
				
				if(licenseIssuedOn.compareTo(new Date()) > 0) {
					errorMsg = errorMsg.append(ErrorMessages.LICENSE_ISSUED_GREATER_CURRENT);
				}
			}
			
			if(userRequest.getLicenseValidUntil() != null){
				licenseValidUntil = 
						dateFormat.parse(userRequest.getLicenseValidUntil());
				
				if(licenseValidUntil.compareTo(new Date()) <= 0) {
					errorMsg = errorMsg.append(ErrorMessages.LICENSE_VALID_UNTIL_LESSER);
				}
			}
			else {
				errorMsg = errorMsg.append(ErrorMessages.LICENSE_VALID_UNTIL_MANDATORY);
			}
		}
		
		if(!ValidationUtil.isValidString(userRequest.getMobileNumber())) {
			errorMsg = errorMsg.append(ErrorMessages.MOBILE_MANDATORY);
		}
		
		if(userRequest.getUserId() != null && userRequest.getUserId().intValue() != 0) {
			
		}
		else {
			if(!ValidationUtil.isValidString(userRequest.getLastName())) {
				errorMsg = errorMsg.append(ErrorMessages.LAST_NAME_MANDATORY);
			}			
			if(!ValidationUtil.isValidString(userRequest.getPassword())) {
				errorMsg = errorMsg.append(ErrorMessages.PASSWORD_MANDATORY);
			}
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())){
			// Throw error
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			User user = new User();
			
			if(userRequest.getUserId() == null || userRequest.getUserId().intValue() == 0) {
				user.setPassword(Utility.encryptWithMD5(userRequest.getPassword()));				
			}
			else {
				user.setId(userRequest.getUserId().intValue());
			}
			
			user.setMobileNumber(userRequest.getMobileNumber());
			UserType userType = new UserType();
			userType.setType(userRequest.getUserType());
			user.setUserType(userType);
			
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setEmailId(userRequest.getEmailId());
			user.setAlternateNumber(userRequest.getAlternateNumber());
				
			if(userRequest.getIsEnable() != null) {
				user.setIsEnable(userRequest.getIsEnable().intValue());
			}	
			else if(!USERTYPE.DRIVER.equalsIgnoreCase(userRequest.getUserType())){
				user.setIsEnable(1);
			}
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(userRequest.getUserType())) {
				
				DriverProfile driverProfile = new DriverProfile();
				
				if(userRequest.getDriverProfileId() != null) {
					driverProfile.setId(userRequest.getDriverProfileId().intValue());
				}
				driverProfile.setVehicleNumber(userRequest.getVehicleNumber());
				driverProfile.setLicenseNo(userRequest.getLicenseNo());	
				driverProfile.setLicenseIssuedOn(licenseIssuedOn);
				driverProfile.setLicenseValidUntil(licenseValidUntil);	
				driverProfile.setRestrictions(userRequest.getRestriction());
				
				if(userRequest.getDefaultPercentageAccepted() != null) {
					driverProfile.setDefaultPercentage(
						Integer.parseInt(userRequest.getDefaultPercentageAccepted().toString()));	
				}
				
				Status status = new Status();
				status.setStatus(userRequest.getStatus());
				
				driverProfile.setStatus(status);
				driverProfile.setComments(userRequest.getComments());
				
		/*		try {
					//List<MultipartFile> files = userRequest.getLicenseImages();
					//MultipartFile[] files = userRequest.getLicenseImages();
					if(files != null && files.length > 0 ) {
						System.out.println(" FIles Size : "+files.length);
						
						System.out.println(" FIles Name : "+files[0].getName());
						
						driverProfile.setLicenseFrontImage(files[0].getBytes());
						if(files.length>1){
							driverProfile.setLicenseBackImage(files[1].getBytes());
						}
					}
				} catch (IOException e) {
					//Images have not set. 
					System.out.println(" File Upload Exceptionn "+e);
				}	*/		
				user.setDriverProfile(driverProfile);			
			}
			User savedUser = userService.saveUser(user);
			return setUserResponse(savedUser);
		}		
	}
	
	public List<UserResponse> getAllUserByUserType(CommonRequest commonRequest) {
		List<UserResponse> userResponseList = new ArrayList<UserResponse>();
		List<User> userList = userService.getAllUserByUserType(commonRequest.getUserType());
		
		if(userList != null && userList.size() > 0) {
			for(User user : userList) {
				userResponseList.add(setUserResponse(user));
			}
		}
		return userResponseList;
	}
	
	public List<UserResponse> getAllApprovedEnabledDrivers() {
		List<UserResponse> userResponseList = new ArrayList<UserResponse>();
		List<User> userList = userService.getAllApprovedEnabledDrivers();
		
		if(userList != null && userList.size() > 0) {
			for(User user : userList) {
				userResponseList.add(setUserResponse(user));
			}
		}
		return userResponseList;
	}
	
	public UserResponse getUserByUserId(CommonRequest commonRequest) throws MandatoryValidationException {
		if(commonRequest.getUserId() == null) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		User user = userService.getUserByUserId(commonRequest.getUserId().intValue());
		return setUserResponse(user);
	}
	
	
	public UserResponse verifyLogInUser(UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException, UserValidationException {
		
		StringBuffer errorMsg = new StringBuffer("");
		
		if(!ValidationUtil.isValidString(userRequest.getUserType())) {
			errorMsg = errorMsg.append(ErrorMessages.USER_TYPE_MANDATORY);
		}
				
		if(USERTYPE.WEB_ADMIN.equalsIgnoreCase(userRequest.getUserType())){ 
			if(!ValidationUtil.isValidString(userRequest.getEmailId())) {
				errorMsg = errorMsg.append(ErrorMessages.EMAIL_MANDATORY);
			}
		}		
		else if(!ValidationUtil.isValidString(userRequest.getMobileNumber())) {
			errorMsg = errorMsg.append(ErrorMessages.MOBILE_MANDATORY);
		}		
		
		if(!ValidationUtil.isValidString(userRequest.getPassword())) {
			errorMsg = errorMsg.append(ErrorMessages.PASSWORD_MANDATORY);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())){
			// Throw error
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			User user = new User();
			user.setPassword(Utility.encryptWithMD5(userRequest.getPassword()));
			user.setMobileNumber(userRequest.getMobileNumber());
			user.setEmailId(userRequest.getEmailId());
			UserType userType = new UserType();
			userType.setType(userRequest.getUserType());
			user.setUserType(userType);
			User newUser = userService.verifyLogInUser(user);
			return setUserResponse(newUser);
		}		
	}
	
	public CommonResponse getDriverCountBySatus(CommonRequest commonRequest) {
		Integer driverCount = userService.getUserCountByTypeAndStatus(USERTYPE.DRIVER, commonRequest.getStatus());
		
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setCount(driverCount);
		return commonResponse;
	}
	
	public DriverVehicleAssociationResponse saveDriverVehicleAssociation(DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException, ParseException {
		StringBuffer errorMsg = new StringBuffer();		
		
		if(driverVehicleRequest.getDriverId() == null) {
			errorMsg.append(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		if(driverVehicleRequest.getCabId() == null) {
			errorMsg.append(ErrorMessages.VEHICLE_ID_REQUIRED);			
		}
		if(driverVehicleRequest.getFromDate() == null) {
			errorMsg.append(ErrorMessages.FROM_DATE_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			DriverVehicleAssociationResponse driverVehicleResp = new DriverVehicleAssociationResponse();
			DriverVehicleAssociation driverVehicle = new DriverVehicleAssociation();
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			if(driverVehicleRequest.getDriverVehicleId() != null) {
				driverVehicle.setId(driverVehicleRequest.getDriverVehicleId().intValue());
			}
			driverVehicle.setFromDate(dateFormat.parse(driverVehicleRequest.getFromDate()));
			if(driverVehicleRequest.getToDate() != null) {
				driverVehicle.setToDate(dateFormat.parse(driverVehicleRequest.getToDate()));
			}
			User user = new User();
			user.setId(driverVehicleRequest.getDriverId().intValue());
			driverVehicle.setUser(user);
			
			VehicleDetail vehicleDetail = new VehicleDetail();
			vehicleDetail.setId(driverVehicleRequest.getCabId().intValue());
			driverVehicle.setVehicleDetail(vehicleDetail);
			
			DriverVehicleAssociation newAssociation = 
					userService.saveDriverVehicleAssociation(driverVehicle);
		
			driverVehicleResp.setDriverVehicleId(newAssociation.getId());
			driverVehicleResp.setCabId(newAssociation.getVehicleDetail().getId());
			driverVehicleResp.setVin(newAssociation.getVehicleDetail().getVin());
			driverVehicleResp.setLicensePlateNumber(newAssociation.getVehicleDetail().getLicensePlateNo());
			driverVehicleResp.setDriverId(newAssociation.getUser().getId());
			driverVehicleResp.setDriverName(
					ValidationUtil.getFullName(newAssociation.getUser().getFirstName(),newAssociation.getUser().getLastName()));
			
			driverVehicleResp.setFromDate(dateFormat.format(newAssociation.getFromDate()));
			
			if(newAssociation.getToDate() != null) {
				driverVehicleResp.setToDate(dateFormat.format(newAssociation.getToDate()));
			}
			return driverVehicleResp;			
		}		
	}

	public void saveUserSession(UserSession userSession) {
		userService.saveUserSession(userSession);
	}
	
	private UserResponse setUserResponse(User user) {
		UserResponse userResponse = new UserResponse();
		
		userResponse.setUserId(user.getId());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setEmailId(user.getEmailId());
		userResponse.setMobileNumber(String.valueOf(user.getMobileNumber()));
		userResponse.setIsEnable(user.getIsEnable());
		
		if(user.getUserType() != null) {
			userResponse.setUserType(user.getUserType().getType());
		}
			
		if(USERTYPE.DRIVER.equalsIgnoreCase(userResponse.getUserType()) && user.getDriverProfile() != null ) {
			userResponse.setDriverProfileId(user.getDriverProfile().getId());
			userResponse.setVehicleNumber(user.getDriverProfile().getVehicleNumber());
			userResponse.setLicenseNo(user.getDriverProfile().getLicenseNo());
				
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				
			if(user.getDriverProfile().getLicenseIssuedOn() != null) {
				String licenseIssuedOn = 
						dateFormat.format(user.getDriverProfile().getLicenseIssuedOn());
					
				userResponse.setLicenseIssuedOn(licenseIssuedOn);
			}
				
			String licenseValidUntil = 
				dateFormat.format(user.getDriverProfile().getLicenseValidUntil());
					
			userResponse.setLicenseValidUntil(licenseValidUntil);
			userResponse.setRestriction(user.getDriverProfile().getRestrictions());

			// Need to get From User Session
			userResponse.setIsLoggedIn(0);
			userResponse.setDefaultPercentageAccepted(user.getDriverProfile().getDefaultPercentage());
				
			userResponse.setStatus(user.getDriverProfile().getStatus().getStatus());
			userResponse.setComments(user.getDriverProfile().getComments());
		}			
		return userResponse;
	}
}
