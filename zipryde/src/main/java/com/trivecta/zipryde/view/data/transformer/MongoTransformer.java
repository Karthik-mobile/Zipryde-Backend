package com.trivecta.zipryde.view.data.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.DriverVehicleAssociation;
import com.trivecta.zipryde.model.entity.UserSession;
import com.trivecta.zipryde.model.service.UserService;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.response.UserGeoSpatialResponse;

@Component
public class MongoTransformer {

	@Autowired
	MongoDbClient mongoDbClient;
	
	@Autowired
	UserService userService;
	
	public void insertDriverSession(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		if(geoLocationRequest.getUserId() == null) {
			errorMsg.append(ErrorMessages.USER_ID_REQUIRED);
		}
		if(!ValidationUtil.isValidString(geoLocationRequest.getFromLatitude()) || 
				!ValidationUtil.isValidString(geoLocationRequest.getFromLongitude())){
			errorMsg.append(ErrorMessages.LAT_LON_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {			
			
			mongoDbClient.updateDriverSession(String.valueOf(geoLocationRequest.getUserId()), 
					Double.valueOf(geoLocationRequest.getFromLongitude()), 
					Double.valueOf(geoLocationRequest.getFromLatitude()));
			
			saveUserSession(geoLocationRequest.getUserId().intValue(),1);
		}
	}
	
	public void updateDriverSession(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		if(geoLocationRequest.getUserId() == null) {
			errorMsg.append(ErrorMessages.USER_ID_REQUIRED);
		}
		if(!ValidationUtil.isValidString(geoLocationRequest.getFromLatitude()) || 
				!ValidationUtil.isValidString(geoLocationRequest.getFromLongitude())){
			errorMsg.append(ErrorMessages.LAT_LON_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {			
			mongoDbClient.updateDriverSession(String.valueOf(geoLocationRequest.getUserId()), 
					Double.valueOf(geoLocationRequest.getFromLongitude()), 
					Double.valueOf(geoLocationRequest.getFromLatitude()));
			saveUserSession(geoLocationRequest.getUserId().intValue(),1);
		}
	}
	
	public void updateDriverOnlineStatus(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		if(geoLocationRequest.getUserId() == null) {
			errorMsg.append(ErrorMessages.USER_ID_REQUIRED);
		}
		if(geoLocationRequest.getIsOnline() == null) {
			errorMsg.append(ErrorMessages.ONLINE_STATUS_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {			
			mongoDbClient.updateDriverOnlineStatus(String.valueOf(geoLocationRequest.getUserId()), 
					geoLocationRequest.getIsOnline().intValue());
			
			saveUserSession(geoLocationRequest.getUserId().intValue(),geoLocationRequest.getIsOnline().intValue());
		}
	}
	
	public void saveUserSession(int userId,int isOnline) throws UserValidationException {
		//Mysql change online Status
		UserSession userSession = new UserSession();
		userSession.setIsActive(isOnline);
		/*if(geoLocationRequest.getIsOnline() != null) {
			userSession.setIsActive(geoLocationRequest.getIsOnline().intValue());
		}
		else {
			userSession.setIsActive(1);
		}*/
		userSession.setUserId(userId);
		userService.saveUserSession(userSession);
	}
	
	public List<UserGeoSpatialResponse> getNearByActiveDrivers(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException
	{
		if(geoLocationRequest.getFromLatitude() == null || geoLocationRequest.getFromLongitude() == null) {
			throw new MandatoryValidationException(ErrorMessages.LAT_LON_REQUIRED);
		}
		else 
		{
			List<UserGeoSpatialResponse> userGeoRespList = new ArrayList<UserGeoSpatialResponse>();
			List<com.trivecta.zipryde.mongodb.UserGeoSpatialResponse> 
			mongoUserRespList =
				mongoDbClient.getNearByActiveDrivers(Double.valueOf(geoLocationRequest.getFromLongitude()),
						Double.valueOf(geoLocationRequest.getFromLatitude()));
		
			if(mongoUserRespList != null && mongoUserRespList.size() > 0) 
			{
				List<Integer> userIdList = mongoUserRespList.stream()
				                .map(com.trivecta.zipryde.mongodb.UserGeoSpatialResponse::getUserId)
				                .collect(Collectors.toList()); 
						
				List<DriverVehicleAssociation> associationList = userService.getDriverVehcileAssociationByDriverIds(userIdList);
				
				if(associationList != null && associationList.size() > 0) {
					 userGeoRespList = mongoUserRespList.stream()
								.map(obj -> new UserGeoSpatialResponse(obj.getUserId(),obj.getLongitude(),obj.getLatitude()))
								.collect(Collectors.toList());	
					for(UserGeoSpatialResponse userGeoResponse : userGeoRespList ) {
						for(DriverVehicleAssociation driverVehicleAssociation : associationList) {
							if(userGeoResponse.getUserId().intValue() == driverVehicleAssociation.getUser().getId().intValue()) {
								userGeoResponse.setCabType(driverVehicleAssociation.getVehicleDetail().getCabType().getType());
								userGeoResponse.setCabTypeId(driverVehicleAssociation.getVehicleDetail().getCabType().getId());
							}
						}
					}
				}
				
				if(userGeoRespList != null && userGeoRespList.size() > 0) {
					Iterator<UserGeoSpatialResponse> iter = userGeoRespList.iterator();
					while (iter.hasNext()) {
					    if (!ValidationUtil.isValidString(iter.next().getCabType())) {
					        iter.remove();
					}
				}
			}	
		}
		return userGeoRespList;					
		}
	}
	
	public UserGeoSpatialResponse getGeoLocationByDriverId(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException{
		if(geoLocationRequest.getUserId() == null) {
			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
		}
		else {
			com.trivecta.zipryde.mongodb.UserGeoSpatialResponse 
			mongoUserResp =
				mongoDbClient.getGeoLocationByDriverId(geoLocationRequest.getUserId().toString());
		
			UserGeoSpatialResponse userResp = new 
					UserGeoSpatialResponse(mongoUserResp.getUserId(), mongoUserResp.getLongitude(), mongoUserResp.getLatitude());
			return userResp;
		}			
	}
}
