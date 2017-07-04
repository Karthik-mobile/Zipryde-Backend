
package com.trivecta.zipryde.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.view.data.transformer.AdminTransformer;
import com.trivecta.zipryde.view.data.transformer.BookingTransformer;
import com.trivecta.zipryde.view.data.transformer.MongoTransformer;
import com.trivecta.zipryde.view.data.transformer.UserTransformer;
import com.trivecta.zipryde.view.data.transformer.VehicleTransformer;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.CabRequest;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.OTPRequest;
import com.trivecta.zipryde.view.request.UserRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CabResponse;
import com.trivecta.zipryde.view.response.CabTypeResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.MakeModelResponse;
import com.trivecta.zipryde.view.response.NYOPResponse;
import com.trivecta.zipryde.view.response.OTPResponse;
import com.trivecta.zipryde.view.response.UserGeoSpatialResponse;
import com.trivecta.zipryde.view.response.UserResponse;

@RestController
public class ZiprydeController {

	@Autowired
	UserTransformer userTransformer;
	
	@Autowired
	AdminTransformer adminTransformer;
	
	@Autowired
	BookingTransformer bookingTranssformer;
	
	@Autowired
	VehicleTransformer vehicleTransformer;
	
	@Autowired
	MongoTransformer mongoTransfomer;
	
	@RequestMapping(value="/getOTPByMobile")
	public @ResponseBody OTPResponse getOTPByMobile(@RequestBody OTPRequest otpRequest) throws MandatoryValidationException{
		return userTransformer.getOTPByMobile(otpRequest);
	}
	
	@RequestMapping(value="/verifyOTPByMobile")
	public @ResponseBody OTPResponse verifyOTPByMobile(@RequestBody OTPRequest otpRequest) throws MandatoryValidationException{
		return userTransformer.verifyOTPByMobile(otpRequest);
	}
	
	@RequestMapping(value = "/saveUser")
	public @ResponseBody UserResponse saveUser(@RequestBody UserRequest user) 
			throws ParseException, NoResultEntityException, MandatoryValidationException, UserValidationException {
		return userTransformer.saveUser(user);		
	}
	
	@RequestMapping(value = "/saveVehicle")
	public @ResponseBody CabResponse saveVehicle(@RequestBody CabRequest cabRequest) throws ParseException, MandatoryValidationException, UserValidationException{
		return vehicleTransformer.saveVehicle(cabRequest);
	}
	
	@RequestMapping(value = "/getAllUserByUserType")
	public @ResponseBody List<UserResponse> getAllUserByUserType(@RequestBody CommonRequest commonRequest) {
		return userTransformer.getAllUserByUserType(commonRequest);
	}
	
	@RequestMapping(value = "/getUserByUserId")
	public @ResponseBody UserResponse getUserByUserId(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException {
		return userTransformer.getUserByUserId(commonRequest);
	}
	
	@RequestMapping(value = "/getVehicleByVehicleId")
	public @ResponseBody CabResponse getVehicleByVehicleId(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, UserValidationException {
		return vehicleTransformer.getVehicleByVehicleId(commonRequest);
	}
	
	@RequestMapping(value = "/getAllCabTypes")
	public @ResponseBody List<CabTypeResponse> getAllCabTypes() {
		return adminTransformer.getAllCabTypes();
	}
	
	@RequestMapping(value = "/getAllMake")
	public @ResponseBody List<MakeModelResponse> getAllMake() {
		return adminTransformer.getAllMake();
	}
	
	@RequestMapping(value = "/getAllModelByMakeId")
	public @ResponseBody List<MakeModelResponse> getAllModelByMakeId(@RequestBody CommonRequest commonRequest) {
		return adminTransformer.getAllModelByMakeId(commonRequest);
	}
	
	@RequestMapping(value = "/getAllNYOPList")
	public @ResponseBody List<NYOPResponse> getAllNYOPList() {
		return adminTransformer.getAllNYOPList();
	}
	
	@RequestMapping(value = "/getAllVehicle")
	public @ResponseBody List<CabResponse> getAllVehicle() {
		return vehicleTransformer.getAllVehicle();
	}
	
	@RequestMapping(value = "/getAllNYOPByCabTypeDistAndNoOfPassenger")
	public @ResponseBody List<NYOPResponse> getAllNYOPByCabTypeDistAndNoOfPassenger(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException {
		return adminTransformer.getAllNYOPByCabTypeDistAndNoOfPassenger(commonRequest);
	}
	
	@RequestMapping(value = "/verifyLogInUser")
	public @ResponseBody UserResponse verifyLogInUser(@RequestBody UserRequest userRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException {
		return userTransformer.verifyLogInUser(userRequest);
	}
	
	@RequestMapping(value = "/requestBooking")
	public @ResponseBody BookingResponse requestBooking(@RequestBody BookingRequest bookingRequest) throws ParseException {
		return bookingTranssformer.createBooking(bookingRequest);
	}
	
	@RequestMapping(value = "/getDriverCountBySatus")
	public CommonResponse getDriverCountBySatus(@RequestBody CommonRequest commonRequest) {
		return userTransformer.getDriverCountBySatus(commonRequest);
	}
	

	//TODO
	//1. DRIVER APPROVE / REJECT BOOKING
	//2. GET BOOKINGS BY USER TYPE AND USER ID
	
	/** --------- MONGO DB SERVICE -------------------- **/
	
	@RequestMapping(value = "/getNearByActiveDrivers")
	public List<UserGeoSpatialResponse> getNearByActiveDrivers(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		return mongoTransfomer.getNearByActiveDrivers(geoLocationRequest);
	}
	
	@RequestMapping(value = "/insertDriverSession")
	public void insertDriverSession(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		mongoTransfomer.insertDriverSession(geoLocationRequest);
	}
	
	@RequestMapping(value = "/updateDriverSession")
	public void updateDriverSession(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		mongoTransfomer.updateDriverSession(geoLocationRequest);
	}
	
	@RequestMapping(value = "/updateDriverStatus")
	public void updateDriverStatus(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		mongoTransfomer.updateDriverStatus(geoLocationRequest);
	}

	
}
