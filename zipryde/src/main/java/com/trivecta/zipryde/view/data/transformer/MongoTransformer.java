package com.trivecta.zipryde.view.data.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.mongodb.MongoDbClient;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.UserGeoSpatialResponse;

@Component
public class MongoTransformer {

	@Autowired
	MongoDbClient mongoDbClient;
	
	public void insertDriverSession(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
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
			mongoDbClient.insertDriverSession(String.valueOf(geoLocationRequest.getUserId()), 
					Double.valueOf(geoLocationRequest.getFromLongitude()), 
					Double.valueOf(geoLocationRequest.getFromLatitude()));
		}
	}
	
	public void updateDriverSession(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
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
		}
	}
	
	public void updateDriverStatus(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
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
			mongoDbClient.updateDriverStatus(String.valueOf(geoLocationRequest.getUserId()), 
					geoLocationRequest.getIsOnline().intValue());
		}
	}
	
	public List<UserGeoSpatialResponse> getNearByActiveDrivers(GeoLocationRequest geoLocationRequest) throws MandatoryValidationException{
		if(geoLocationRequest.getFromLatitude() == null || geoLocationRequest.getFromLongitude() == null) {
			throw new MandatoryValidationException(ErrorMessages.LAT_LON_REQUIRED);
		}
		else {
			List<com.trivecta.zipryde.mongodb.UserGeoSpatialResponse> 
			mongoUserRespList =
				mongoDbClient.getNearByActiveDrivers(Double.valueOf(geoLocationRequest.getFromLongitude()),
						Double.valueOf(geoLocationRequest.getFromLatitude()));
		
		return mongoUserRespList.stream()
				.map(obj -> new UserGeoSpatialResponse(Integer.valueOf(obj.getUserId()), obj.getLatitude(),obj.getLongitude()))
				.collect(Collectors.toList());	
		}			
	}
}
