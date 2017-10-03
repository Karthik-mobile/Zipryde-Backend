package com.trivecta.zipryde.view.data.transformer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.USERTYPE;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserAlreadyLoggedInException;
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
import com.trivecta.zipryde.utility.TwilioSMS;
import com.trivecta.zipryde.utility.Utility;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.DriverVehicleAssociationRequest;
import com.trivecta.zipryde.view.request.OTPRequest;
import com.trivecta.zipryde.view.request.UserRequest;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.DriverVehicleAssociationResponse;
import com.trivecta.zipryde.view.response.OTPResponse;
import com.trivecta.zipryde.view.response.UserResponse;
import com.twilio.sdk.TwilioRestException;

@Component
public class UserTransformer {

	@Autowired
	UserService userService;
	
	private OTPResponse setOTPResponse(OtpVerification otpVerification) {
		if(otpVerification != null) {
			OTPResponse response = new OTPResponse();
			
			response.setMobileNumber(String.valueOf(otpVerification.getMobileNumber()));
			//response.setOtp(otpVerification.getOtp());
			response.setOtp("");
			
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
	
	public void deleteUser(UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException , UserValidationException {
		if(userRequest.getUserId() == null || userRequest.getUserId().intValue() == 0) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		else {
			User user = new User();
			user.setId(userRequest.getUserId().intValue());
			userService.deleteUser(user);
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
			user.setDeviceToken(userRequest.getDeviceToken());		
			 
			if(userRequest.getIsEnable() != null) {
				user.setIsEnable(userRequest.getIsEnable().intValue());
			}
			
			if(USERTYPE.DRIVER.equalsIgnoreCase(userRequest.getUserType())) {
				
				DriverProfile driverProfile = new DriverProfile();
				
				if(userRequest.getDriverProfileId() != null) {
					driverProfile.setId(userRequest.getDriverProfileId().intValue());
				}
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
				
				try {
					if(userRequest.getUserImage() != null)
						driverProfile.setDriverProfileImage(userRequest.getUserImage().getBytes());
					if(userRequest.getLicenseFrontImage() != null)
						driverProfile.setLicenseFrontImage(userRequest.getLicenseFrontImage().getBytes());
					if(userRequest.getLicenseBackImage() != null)
						driverProfile.setLicenseBackImage(userRequest.getLicenseBackImage().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     			     
				user.setDriverProfile(driverProfile);			
			}
			User savedUser = userService.saveUser(user);
			return setUserResponse(savedUser,true,false);
		}		
	}
	
	public List<UserResponse> getAllUserByUserType(CommonRequest commonRequest) {
		List<UserResponse> userResponseList = new ArrayList<UserResponse>();
		List<User> userList = userService.getAllUserByUserType(commonRequest.getUserType());
		
		if(userList != null && userList.size() > 0) {
			for(User user : userList) {
				userResponseList.add(setUserResponse(user,false,false));
			}
		}
		return userResponseList;
	}
		
	public UserResponse getUserByUserId(CommonRequest commonRequest) throws MandatoryValidationException {
		if(commonRequest.getUserId() == null) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		User user = userService.getUserByUserId(commonRequest.getUserId().intValue());
		return setUserResponse(user,true,false);
	}
	
	
	public UserResponse verifyLogInUser(UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException, UserValidationException, UserAlreadyLoggedInException {
		
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
			user.setDeviceToken(userRequest.getDeviceToken());
			UserType userType = new UserType();
			userType.setType(userRequest.getUserType());
			user.setUserType(userType);
			user.setIsOverride(userRequest.getOverrideSessionToken());
			User newUser = userService.verifyLogInUser(user);
			return setUserResponse(newUser,false,true);
		}		
	}
	
	public void logoutUser(CommonRequest commonRequest) throws MandatoryValidationException, UserValidationException {
		if(commonRequest.getUserId() == null) {
			throw new MandatoryValidationException(ErrorMessages.USER_ID_REQUIRED);
		}
		userService.logOutUser(commonRequest.getUserId());
	}
	
	public UserResponse updatePasswordByUserAndType(UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException {
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
			User newUser = userService.updatePasswordByUserAndType(user);
			return setUserResponse(newUser,false,false);
		}
	}
	
	public CommonResponse getDriverCountBySatus(CommonRequest commonRequest) {
		Integer driverCount = userService.getDriverCountBySatus(commonRequest.getStatus());
		
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setCount(driverCount);
		return commonResponse;
	}
	
	public CommonResponse getDriverCountByOnline() {
		Integer driverCount = userService.getDriverCountByOnline();		
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setCount(driverCount);
		return commonResponse;
	}
	
	public List<UserResponse> getDriversByStatus(CommonRequest commonRequest) {
		List<UserResponse> userResponseList = new ArrayList<UserResponse>();
		List<User> userList = userService.getDriversByStatus(commonRequest.getStatus());
		
		if(userList != null && userList.size() > 0) {
			for(User user : userList) {
				userResponseList.add(setUserResponse(user,false,false));
			}
		}
		return userResponseList;
	}
	
	public List<UserResponse> getDriversByOnline() {
		List<UserResponse> userResponseList = new ArrayList<UserResponse>();
		List<User> userList = userService.getDriversByOnline();
		
		if(userList != null && userList.size() > 0) {
			for(User user : userList) {
				userResponseList.add(setUserResponse(user,false,false));
			}
		}
		return userResponseList;
	}
	
	public DriverVehicleAssociationResponse assignDriverVehicleAssociation(DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException, ParseException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();		
		
		if(driverVehicleRequest.getDriverId() == null) {
			errorMsg.append(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		if(driverVehicleRequest.getCabId() == null) {
			errorMsg.append(ErrorMessages.VEHICLE_ID_REQUIRED);			
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
			DriverVehicleAssociation driverVehicle = new DriverVehicleAssociation();
					
			User user = new User();
			user.setId(driverVehicleRequest.getDriverId().intValue());
			driverVehicle.setUser(user);
			
			VehicleDetail vehicleDetail = new VehicleDetail();
			vehicleDetail.setId(driverVehicleRequest.getCabId().intValue());
			driverVehicle.setVehicleDetail(vehicleDetail);
			
			DriverVehicleAssociation newAssociation = 
					userService.assignDriverVehicleAssociation(driverVehicle);		
			return setDriverVehicleResponse(newAssociation);			
		}		
	}

	public DriverVehicleAssociationResponse unassignDriverVehicleAssociation(DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException, UserValidationException {
		if(driverVehicleRequest.getDriverVehicleId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_VEHICLE_ASSOCIATION_REQUIRED);
		}
		else {
			DriverVehicleAssociation driverVehicle = new DriverVehicleAssociation();
		
			if(driverVehicleRequest.getDriverVehicleId() != null) {
				driverVehicle.setId(driverVehicleRequest.getDriverVehicleId().intValue());
			}
			driverVehicle.setToDate(new Date());
						
			DriverVehicleAssociation newAssociation = 
					userService.unassignDriverVehicleAssociation(driverVehicle);		
			return setDriverVehicleResponse(newAssociation);			
		}		
	}	
	
	public DriverVehicleAssociationResponse getActiveDriverVehicleAssociationByDriverId
					(DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException {
		if(driverVehicleRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		else {
			DriverVehicleAssociation driverAssociation = 
					userService.getActiveDriverVehicleAssociationByDriverId(driverVehicleRequest.getDriverId().intValue());
			return setDriverVehicleResponse(driverAssociation);
		}
	}
	
	public List<DriverVehicleAssociationResponse> getAllDriverVehicleAssociationByDriverId
					(DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException {
		if(driverVehicleRequest.getDriverId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		else {
			List<DriverVehicleAssociationResponse> driverVehicleResponseList = 
					new ArrayList<DriverVehicleAssociationResponse>();
			List<DriverVehicleAssociation> driverAssociationList = 
				userService.getAllDriverVehicleAssociationByDriverId(driverVehicleRequest.getDriverId().intValue());
			
			if(driverAssociationList != null && driverAssociationList.size() >0) {
				for(DriverVehicleAssociation driverAssociation : driverAssociationList) {
					driverVehicleResponseList.add(setDriverVehicleResponse(driverAssociation));
				}
			}	
			return driverVehicleResponseList;
		}
	}
	
	public void updateDeviceToken(String accessToken,CommonRequest commonRequest) {
		if(ValidationUtil.isValidString(accessToken) && ValidationUtil.isValidString(commonRequest.getDeviceToken())) {
			userService.updateDeviceToken(Utility.encryptWithMD5(accessToken), commonRequest.getDeviceToken());
		}
	}
		
	private DriverVehicleAssociationResponse setDriverVehicleResponse(DriverVehicleAssociation newAssociation) {
		DriverVehicleAssociationResponse driverVehicleResp = new DriverVehicleAssociationResponse();
		if(newAssociation != null) {		
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			
			driverVehicleResp.setDriverVehicleId(newAssociation.getId());
			driverVehicleResp.setCabId(newAssociation.getVehicleDetail().getId());
			driverVehicleResp.setCabType(newAssociation.getVehicleDetail().getCabType().getType());
			driverVehicleResp.setCabSeatingCapacity(newAssociation.getVehicleDetail().getSeatingCapacity());
			driverVehicleResp.setVin(newAssociation.getVehicleDetail().getVin());
			driverVehicleResp.setVehicleNumber(newAssociation.getVehicleDetail().getVehicleNumber());
			driverVehicleResp.setDriverId(newAssociation.getUser().getId());
			driverVehicleResp.setDriverName(
					ValidationUtil.getFullName(newAssociation.getUser().getFirstName(),newAssociation.getUser().getLastName()));
			
			driverVehicleResp.setFromDate(dateFormat.format(newAssociation.getFromDate()));
			
			if(newAssociation.getToDate() != null) {
				driverVehicleResp.setToDate(dateFormat.format(newAssociation.getToDate()));
			}	
		}
		return driverVehicleResp;
	}
	
	private UserResponse setUserResponse(User user,boolean loadImage,boolean setAccessToken) {
		UserResponse userResponse = new UserResponse();
		
		userResponse.setUserId(user.getId());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setEmailId(user.getEmailId());
		userResponse.setMobileNumber(String.valueOf(user.getMobileNumber()));
		userResponse.setIsEnable(user.getIsEnable());
		userResponse.setCancellationCount(user.getCancellationCount());
		userResponse.setUserType(user.getUserType().getType());
		
		userResponse.setIsOnline(user.getIsOnline());
		if(setAccessToken) {
			System.out.println(" User : "+user.getId() + " Access Token :"+user.getAccessToken());
			userResponse.setAccessToken(user.getAccessToken());
		}
		userResponse.setBookingId(user.getBookingId());
		
		if(USERTYPE.DRIVER.equalsIgnoreCase(userResponse.getUserType()) && user.getDriverProfile() != null ) {
			userResponse.setDriverProfileId(user.getDriverProfile().getId());
			userResponse.setLicenseNo(user.getDriverProfile().getLicenseNo());
			userResponse.setVehicleNumber(user.getDriverProfile().getVehicleNumber());		
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
			
			userResponse.setDefaultPercentageAccepted(user.getDriverProfile().getDefaultPercentage());
				
			userResponse.setStatusCode(user.getDriverProfile().getStatus().getStatus());
			userResponse.setStatus(user.getDriverProfile().getStatus().getStatusValue());
			userResponse.setComments(user.getDriverProfile().getComments());
			
			if(loadImage) {
				if(user.getDriverProfile().getLicenseFrontImage() != null) {
					userResponse.setLicenseFrontImage(DatatypeConverter.printBase64Binary(user.getDriverProfile().getLicenseFrontImage()));
				}
				if(user.getDriverProfile().getLicenseBackImage() != null) {
					userResponse.setLicenseBackImage(DatatypeConverter.printBase64Binary(user.getDriverProfile().getLicenseBackImage()));
				}
				if(user.getDriverProfile().getDriverProfileImage() != null) {
					userResponse.setUserImage(DatatypeConverter.printBase64Binary(user.getDriverProfile().getDriverProfileImage()));
				}
			}
		}			
		return userResponse;
	}
}
