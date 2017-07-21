

package com.trivecta.zipryde.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.trivecta.zipryde.view.request.DriverVehicleAssociationRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.OTPRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.request.PricingMstrRequest;
import com.trivecta.zipryde.view.request.UserRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CabResponse;
import com.trivecta.zipryde.view.response.CabTypeResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.DriverVehicleAssociationResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.MakeModelResponse;
import com.trivecta.zipryde.view.response.NYOPResponse;
import com.trivecta.zipryde.view.response.OTPResponse;
import com.trivecta.zipryde.view.response.PricingMstrResponse;
import com.trivecta.zipryde.view.response.PricingTypeResponse;
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
	
	/** ------------ MOBILE REGISTRATION ------------- */
	
	@RequestMapping(value="/getOTPByMobile")
	public @ResponseBody OTPResponse getOTPByMobile(@RequestBody OTPRequest otpRequest) throws MandatoryValidationException{
		return userTransformer.getOTPByMobile(otpRequest);
	}
	
	@RequestMapping(value="/verifyOTPByMobile")
	public @ResponseBody OTPResponse verifyOTPByMobile(@RequestBody OTPRequest otpRequest) throws MandatoryValidationException{
		return userTransformer.verifyOTPByMobile(otpRequest);
	}
	
	/** ------------------- USER ------------------------ */
	
	@RequestMapping(value = "/saveUser")
	public @ResponseBody UserResponse saveUser(@ModelAttribute UserRequest user) 
			   throws ParseException, NoResultEntityException, MandatoryValidationException, UserValidationException {
			  return userTransformer.saveUser(user);  
	 }
	
	@RequestMapping(value = "/deleteUser")
	public void deleteUser(@RequestBody UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException,UserValidationException{
		userTransformer.deleteUser(userRequest);
	}
	
	@RequestMapping(value = "/getAllUserByUserType")
	public @ResponseBody List<UserResponse> getAllUserByUserType(@RequestBody CommonRequest commonRequest) {
		return userTransformer.getAllUserByUserType(commonRequest);
	}
	
	@RequestMapping(value = "/getUserByUserId")
	public @ResponseBody UserResponse getUserByUserId(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException {
		return userTransformer.getUserByUserId(commonRequest);
	}
	
	@RequestMapping(value = "/verifyLogInUser")
	public @ResponseBody UserResponse verifyLogInUser(@RequestBody UserRequest userRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException {
		return userTransformer.verifyLogInUser(userRequest);
	}
	
	@RequestMapping(value = "/updatePasswordByUserAndType")
	public @ResponseBody UserResponse updatePasswordByUserAndType(@RequestBody UserRequest userRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException {
		return userTransformer.updatePasswordByUserAndType(userRequest);
	}
	
	@RequestMapping(value = "/assignDriverVehicleAssociation")
	public @ResponseBody DriverVehicleAssociationResponse assignDriverVehicleAssociation(@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) 
			throws MandatoryValidationException, ParseException, UserValidationException {
		return userTransformer.assignDriverVehicleAssociation(driverVehicleRequest);
	}
	
	@RequestMapping(value = "/unassignDriverVehicleAssociation")
	public @ResponseBody DriverVehicleAssociationResponse unassignDriverVehicleAssociation(@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) 
			throws MandatoryValidationException, UserValidationException {
		return userTransformer.unassignDriverVehicleAssociation(driverVehicleRequest);
	}
	
	@RequestMapping(value = "/getActiveDriverVehicleAssociationByDriverId")
	public @ResponseBody DriverVehicleAssociationResponse getActiveDriverVehicleAssociationByDriverId
				(@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException {
		return userTransformer.getActiveDriverVehicleAssociationByDriverId(driverVehicleRequest);
	}
	
	@RequestMapping(value = "/getAllDriverVehicleAssociationByDriverId")
	public @ResponseBody List<DriverVehicleAssociationResponse> getAllDriverVehicleAssociationByDriverId
						(@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException {
		return userTransformer.getAllDriverVehicleAssociationByDriverId(driverVehicleRequest);
	}
	
	/** --------------- DASHBOARD API ----------------- */
	@RequestMapping(value = "/getDriverCountBySatus")
	public CommonResponse getDriverCountBySatus(@RequestBody CommonRequest commonRequest) {
		return userTransformer.getDriverCountBySatus(commonRequest);
	}
	
	@RequestMapping(value = "/getDriverCountByOnline")
	public CommonResponse getDriverCountByOnline() {
		return userTransformer.getDriverCountByOnline();
	}
	
	@RequestMapping(value = "/getDriversByStatus")
	public @ResponseBody List<UserResponse> getDriversByStatus(@RequestBody CommonRequest commonRequest) {
		return userTransformer.getDriversByStatus(commonRequest);
	}
	
	@RequestMapping(value = "/getDriversByOnline")
	public @ResponseBody List<UserResponse> getDriversByOnline() {
		return userTransformer.getDriversByOnline();
	}
	
	@RequestMapping(value = "/getBookingCountByDate")
	public @ResponseBody CommonResponse getBookingCountByDate(@RequestBody BookingRequest bookingRequest) throws ParseException {
		return bookingTranssformer.getBookingCountByDate(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingCountByDateNotInRequested")
	public @ResponseBody CommonResponse getBookingCountByDateNotInRequested(@RequestBody BookingRequest bookingRequest) throws ParseException {
		return bookingTranssformer.getBookingCountByDateNotInRequested(bookingRequest);
	}

	@RequestMapping(value = "/getRevenueByDate")
	public @ResponseBody CommonResponse getRevenueByDate(@RequestBody PaymentRequest paymentRequest) throws ParseException {
		return bookingTranssformer.getRevenueByDate(paymentRequest);
	}
	
	/** ----------------- VEHICLE  --------------------- */
	
	@RequestMapping(value = "/saveVehicle")
	public @ResponseBody CabResponse saveVehicle(@RequestBody CabRequest cabRequest) throws ParseException, MandatoryValidationException, UserValidationException{
		return vehicleTransformer.saveVehicle(cabRequest);
	}
	
	@RequestMapping(value = "/getAllVehicle")
	public @ResponseBody List<CabResponse> getAllVehicle() {
		return vehicleTransformer.getAllVehicle();
	}
	
	@RequestMapping(value = "/getAllAvailableVehicles")
	public @ResponseBody List<CabResponse> getAllAvailableVehicles() {
		return vehicleTransformer.getAllAvailableVehicles();
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
	
	/** ------------------- NYOP & PRICING  ------------------------ */
	@RequestMapping(value = "/getAllNYOPList")
	public @ResponseBody List<NYOPResponse> getAllNYOPList() {
		return adminTransformer.getAllNYOPList();
	}
	
	@RequestMapping(value = "/getAllNYOPByCabTypeDistAndNoOfPassenger")
	public @ResponseBody List<NYOPResponse> getAllNYOPByCabTypeDistAndNoOfPassenger(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException {
		return adminTransformer.getAllNYOPByCabTypeDistAndNoOfPassenger(commonRequest);
	}
	
	@RequestMapping(value = "/getAllEnabledPricingType")
	public @ResponseBody List<PricingTypeResponse> getAllEnabledPricingType() {
		return adminTransformer.getAllEnabledPricingType();
	}
	
	@RequestMapping(value = "/getAllPricingMstrByCabType")
	public @ResponseBody List<PricingMstrResponse> getAllPricingMstrByCabType(@RequestBody CommonRequest commonRequest) throws MandatoryValidationException {
		return adminTransformer.getAllPricingMstrByCabType(commonRequest);
	}
	
	@RequestMapping(value = "/getAllPricingMstr")
	public @ResponseBody List<PricingMstrResponse> getAllPricingMstr() throws MandatoryValidationException {
		return adminTransformer.getAllPricingMstr();
	}
	
	@RequestMapping(value = "/savePricingMstrs")
	public List<PricingMstrResponse> savePricingMstrs(@RequestBody List<PricingMstrRequest> pricingMstrReqList) {
		return adminTransformer.savePricingMstrs(pricingMstrReqList);
	}
	
	@RequestMapping(value = "/savePricingMstr")
	public PricingMstrResponse savePricingMstr(@RequestBody PricingMstrRequest pricingMstrReqList) {
		return adminTransformer.savePricingMstr(pricingMstrReqList);
	}
	
	/** ------------------- BOOKING  ------------------------ */
	@RequestMapping(value = "/requestBooking")
	public @ResponseBody BookingResponse requestBooking(@RequestBody BookingRequest bookingRequest) throws ParseException, MandatoryValidationException {
		return bookingTranssformer.createBooking(bookingRequest);
	}
	
	@RequestMapping(value = "/updateBookingDriverStatus")
	public @ResponseBody BookingResponse updateBookingDriverStatus(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException{
		return bookingTranssformer.updateBookingDriverStatus(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByBookingId")
	public @ResponseBody BookingResponse getBookingByBookingId(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException {
		return bookingTranssformer.getBookingByBookingId(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByDate")
	public @ResponseBody List<BookingResponse> getBookingByDate(@RequestBody BookingRequest bookingRequest) throws ParseException {
		return bookingTranssformer.getBookingByDate(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByDateNotInRequested")
	public @ResponseBody List<BookingResponse> getBookingByDateNotInRequested(@RequestBody BookingRequest bookingRequest) throws ParseException {
		return bookingTranssformer.getBookingByDateNotInRequested(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByBookingStatus")
	public @ResponseBody List<BookingResponse> getBookingByBookingStatus(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException {
		return bookingTranssformer.getBookingByBookingStatus(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByDriverId")
	public @ResponseBody List<BookingResponse> getBookingByDriverId(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException {
		return bookingTranssformer.getBookingByDriverId(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingByuserId")
	public @ResponseBody List<BookingResponse> getBookingByuserId(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException {
		return bookingTranssformer.getBookingByuserId(bookingRequest);
	}
	
	@RequestMapping(value = "/getBookingRequestedByDriverId")
	public @ResponseBody List<BookingResponse> getBookingRequestedByDriverId(@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException {
		return bookingTranssformer.getBookingRequestedByDriverId(bookingRequest);
	}
	
	//TODO
	//2. GET BOOKINGS BY USER TYPE AND USER ID
	
	/** ----------- PAYMENT -------------------- */
	@RequestMapping(value = "/savePayment")  
    public @ResponseStatus(value = HttpStatus.OK)  void savePayment(@RequestBody PaymentRequest paymentRequest) throws MandatoryValidationException{
           bookingTranssformer.savePayment(paymentRequest);
    }
		
	/** --------- MONGO DB SERVICE -------------------- */
	
	@RequestMapping(value = "/getGeoLocationByDriverId")
	public @ResponseBody UserGeoSpatialResponse getGeoLocationByDriverId(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		return mongoTransfomer.getGeoLocationByDriverId(geoLocationRequest);
	}
	
	@RequestMapping(value = "/getNearByActiveDrivers")
	public @ResponseBody List<UserGeoSpatialResponse> getNearByActiveDrivers(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
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
	public void updateDriverOnlineStatus(@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException {
		mongoTransfomer.updateDriverOnlineStatus(geoLocationRequest);
	}

	
}
