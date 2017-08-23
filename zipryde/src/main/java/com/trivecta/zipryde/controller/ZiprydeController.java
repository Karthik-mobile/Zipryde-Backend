package com.trivecta.zipryde.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.SessionExpiredException;
import com.trivecta.zipryde.framework.exception.UserAlreadyLoggedInException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.view.data.transformer.AdminTransformer;
import com.trivecta.zipryde.view.data.transformer.BookingTransformer;
import com.trivecta.zipryde.view.data.transformer.HeaderValidationTransformer;
import com.trivecta.zipryde.view.data.transformer.MongoTransformer;
import com.trivecta.zipryde.view.data.transformer.PaymentTransformer;
import com.trivecta.zipryde.view.data.transformer.UserTransformer;
import com.trivecta.zipryde.view.data.transformer.VehicleTransformer;
import com.trivecta.zipryde.view.request.BookingRequest;
import com.trivecta.zipryde.view.request.CabRequest;
import com.trivecta.zipryde.view.request.CommissionMasterRequest;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.ConfigurationRequest;
import com.trivecta.zipryde.view.request.DriverVehicleAssociationRequest;
import com.trivecta.zipryde.view.request.GeoLocationRequest;
import com.trivecta.zipryde.view.request.LostItemRequest;
import com.trivecta.zipryde.view.request.OTPRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.request.PricingMstrRequest;
import com.trivecta.zipryde.view.request.UserRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CabResponse;
import com.trivecta.zipryde.view.response.CabTypeResponse;
import com.trivecta.zipryde.view.response.CommissionMasterResponse;
import com.trivecta.zipryde.view.response.CommissionResponse;
import com.trivecta.zipryde.view.response.CommonResponse;
import com.trivecta.zipryde.view.response.ConfigurationResponse;
import com.trivecta.zipryde.view.response.DriverVehicleAssociationResponse;
import com.trivecta.zipryde.view.response.GeoLocationResponse;
import com.trivecta.zipryde.view.response.LostItemResponse;
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
	
	@Autowired
	PaymentTransformer paymentTransformer;
	
	@Autowired
	HeaderValidationTransformer headerValidationTransformer;
	

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
	
	@RequestMapping(value = "/verifyLogInUser")
	public @ResponseBody UserResponse verifyLogInUser(@RequestBody UserRequest userRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException, UserAlreadyLoggedInException {
		return userTransformer.verifyLogInUser(userRequest);
	}
	
	@RequestMapping(value = "/logoutUser")
	public void logoutUser(@RequestHeader(value="access-token") String token , @RequestBody CommonRequest commonRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException, UserAlreadyLoggedInException, SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userTransformer.logoutUser(commonRequest);
		}
	}
	
	@RequestMapping(value = "/saveUser")
	public @ResponseBody UserResponse saveUser(@ModelAttribute UserRequest user) 
			   throws ParseException, NoResultEntityException, MandatoryValidationException, UserValidationException {
			  return userTransformer.saveUser(user);  
	 }
	
	@RequestMapping(value = "/deleteUser")
	public void deleteUser(@RequestHeader(value="access-token") String token,@RequestBody UserRequest userRequest) throws MandatoryValidationException, NoResultEntityException,UserValidationException, SessionExpiredException{
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userTransformer.deleteUser(userRequest);
		}
	}
	
	@RequestMapping(value = "/getAllUserByUserType")
	public @ResponseBody List<UserResponse> getAllUserByUserType(@RequestHeader(value="access-token") String token,@RequestBody CommonRequest commonRequest) throws SessionExpiredException {
		List<UserResponse> userResponse = new ArrayList<UserResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userResponse = 	userTransformer.getAllUserByUserType(commonRequest);
		}
		return userResponse;
	}
	
	@RequestMapping(value = "/getUserByUserId")
	public @ResponseBody UserResponse getUserByUserId(@RequestHeader(value="access-token") String token,@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			return userTransformer.getUserByUserId(commonRequest);
		}
		return new UserResponse();
	}
		
	@RequestMapping(value = "/updatePasswordByUserAndType")
	public @ResponseBody UserResponse updatePasswordByUserAndType(@RequestHeader(value="access-token") String token,
			@RequestBody UserRequest userRequest)  throws MandatoryValidationException, NoResultEntityException, UserValidationException, SessionExpiredException {
		UserResponse userResponse = new UserResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userResponse = userTransformer.updatePasswordByUserAndType(userRequest);
		}
		return userResponse;
	}
	
	@RequestMapping(value = "/assignDriverVehicleAssociation")
	public @ResponseBody DriverVehicleAssociationResponse assignDriverVehicleAssociation(@RequestHeader(value="access-token") String token,
			@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) 
			throws MandatoryValidationException, ParseException, UserValidationException, SessionExpiredException {
		DriverVehicleAssociationResponse driverVehicleAss = new DriverVehicleAssociationResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			driverVehicleAss = userTransformer.assignDriverVehicleAssociation(driverVehicleRequest);;
		}
		return driverVehicleAss;
	}
	
	@RequestMapping(value = "/unassignDriverVehicleAssociation")
	public @ResponseBody DriverVehicleAssociationResponse unassignDriverVehicleAssociation(@RequestHeader(value="access-token") String token,
			@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) 
			throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		DriverVehicleAssociationResponse driverVehicleAss = new DriverVehicleAssociationResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			driverVehicleAss = userTransformer.unassignDriverVehicleAssociation(driverVehicleRequest);
		}
		return driverVehicleAss;
	}
	
	@RequestMapping(value = "/getActiveDriverVehicleAssociationByDriverId")
	public @ResponseBody DriverVehicleAssociationResponse getActiveDriverVehicleAssociationByDriverId
				(@RequestHeader(value="access-token") String token,
						@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException, SessionExpiredException {
		DriverVehicleAssociationResponse driverVehicleAss = new DriverVehicleAssociationResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			driverVehicleAss = userTransformer.getActiveDriverVehicleAssociationByDriverId(driverVehicleRequest);
		}
		return driverVehicleAss;
	}
	
	@RequestMapping(value = "/getAllDriverVehicleAssociationByDriverId")
	public @ResponseBody List<DriverVehicleAssociationResponse> getAllDriverVehicleAssociationByDriverId
						(@RequestHeader(value="access-token") String token,
								@RequestBody DriverVehicleAssociationRequest driverVehicleRequest) throws MandatoryValidationException, SessionExpiredException {
		List<DriverVehicleAssociationResponse>  driverAssocList = new ArrayList<DriverVehicleAssociationResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			driverAssocList = userTransformer.getAllDriverVehicleAssociationByDriverId(driverVehicleRequest);
		}
		return driverAssocList;
	}
	
	/** --------------- DASHBOARD API ----------------- 
	 * @throws SessionExpiredException */
	@RequestMapping(value = "/getDriverCountBySatus")
	public CommonResponse getDriverCountBySatus(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse = userTransformer.getDriverCountBySatus(commonRequest);
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getDriverCountByOnline")
	public CommonResponse getDriverCountByOnline(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse = userTransformer.getDriverCountByOnline();
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getDriversByStatus")
	public @ResponseBody List<UserResponse> getDriversByStatus(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws SessionExpiredException {
		List<UserResponse> userRespList = new ArrayList<UserResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userRespList = userTransformer.getDriversByStatus(commonRequest);
		}
		return userRespList;
	}
	
	@RequestMapping(value = "/getDriversByOnline")
	public @ResponseBody List<UserResponse> getDriversByOnline(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<UserResponse> userRespList = new ArrayList<UserResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userRespList = userTransformer.getDriversByOnline();
		}
		return userRespList;
	}
	
	@RequestMapping(value = "/getBookingCountByDate")
	public @ResponseBody CommonResponse getBookingCountByDate(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse = bookingTranssformer.getBookingCountByDate(bookingRequest);
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getBookingCountByDateNotInRequested")
	public @ResponseBody CommonResponse getBookingCountByDateNotInRequested(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse = bookingTranssformer.getBookingCountByDateNotInRequested(bookingRequest);
		}
		return commonResponse;
	}

	@RequestMapping(value = "/getRevenueByDate")
	public @ResponseBody CommonResponse getRevenueByDate(@RequestHeader(value="access-token") String token,
			@RequestBody PaymentRequest paymentRequest) throws ParseException, SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse =  paymentTransformer.getRevenueByDate(paymentRequest);
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getRevenueByDateAndDriverId")
	public @ResponseBody CommonResponse getRevenueByDateAndDriverId(@RequestHeader(value="access-token") String token,
			@RequestBody PaymentRequest paymentRequest) throws ParseException, MandatoryValidationException, SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse =  paymentTransformer.getRevenueByDateAndDriverId(paymentRequest);
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getBookingCountByDateAndDriverId")
	public @ResponseBody CommonResponse getBookingCountByDateAndDriverId(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, MandatoryValidationException, SessionExpiredException {
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse =  bookingTranssformer.getBookingCountByDateAndDriverId(bookingRequest);
		}
		return commonResponse;
	}
	
	@RequestMapping(value = "/getCommissionAmountByStatus")
	public @ResponseBody CommonResponse getCommissionAmountByStatus(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest)throws MandatoryValidationException, SessionExpiredException{
		CommonResponse commonResponse = new CommonResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commonResponse = paymentTransformer.getCommissionAmountByStatus(commonRequest);
		}
		return commonResponse;
	}
	
	/** ----------------- VEHICLE  --------------------- 
	 * @throws SessionExpiredException */
	
	@RequestMapping(value = "/saveVehicle")
	public @ResponseBody CabResponse saveVehicle(@RequestHeader(value="access-token") String token,
			@RequestBody CabRequest cabRequest) throws ParseException, MandatoryValidationException, UserValidationException, SessionExpiredException{
		CabResponse cabResponse = new CabResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			cabResponse = vehicleTransformer.saveVehicle(cabRequest);
		}		
		return cabResponse;
	}
	
	@RequestMapping(value = "/getAllVehicle")
	public @ResponseBody List<CabResponse> getAllVehicle(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<CabResponse> cabResponseList = new ArrayList<CabResponse>(); 
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			cabResponseList = vehicleTransformer.getAllVehicle();
		}
		return cabResponseList;
	}
	
	@RequestMapping(value = "/getAllAvailableVehicles")
	public @ResponseBody List<CabResponse> getAllAvailableVehicles(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<CabResponse> cabResponseList = new ArrayList<CabResponse>(); 
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			cabResponseList = vehicleTransformer.getAllAvailableVehicles();
		}
		return cabResponseList;
	}
		
	@RequestMapping(value = "/getVehicleByVehicleId")
	public @ResponseBody CabResponse getVehicleByVehicleId(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		CabResponse cabResponse = new CabResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			cabResponse = vehicleTransformer.getVehicleByVehicleId(commonRequest);
		}
		return cabResponse;
	}
	
	@RequestMapping(value = "/getAllCabTypes")
	public @ResponseBody List<CabTypeResponse> getAllCabTypes(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			return adminTransformer.getAllCabTypes();
		}
		return new ArrayList<CabTypeResponse>();
	}
	
	@RequestMapping(value = "/getAllMake")
	public @ResponseBody List<MakeModelResponse> getAllMake(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<MakeModelResponse> makeModelList = new ArrayList<MakeModelResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			makeModelList = adminTransformer.getAllMake();
		}
		return makeModelList;
	}
	
	@RequestMapping(value = "/getAllModelByMakeId")
	public @ResponseBody List<MakeModelResponse> getAllModelByMakeId(@RequestHeader(value="access-token") String token,@RequestBody CommonRequest commonRequest) throws SessionExpiredException {
		List<MakeModelResponse> makeModelList = new ArrayList<MakeModelResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			makeModelList =  adminTransformer.getAllModelByMakeId(commonRequest);
		}
		return makeModelList;
	}
	
	/** ------------------- NYOP & PRICING  ------------------------ 
	 * @throws SessionExpiredException */
	@RequestMapping(value = "/getAllNYOPList")
	public @ResponseBody List<NYOPResponse> getAllNYOPList(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<NYOPResponse> nyopList = new ArrayList<NYOPResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			nyopList= adminTransformer.getAllNYOPList();
		}	
		return nyopList;
	}
	
	@RequestMapping(value = "/getAllNYOPByCabTypeDistAndNoOfPassenger")
	public @ResponseBody List<NYOPResponse> getAllNYOPByCabTypeDistAndNoOfPassenger(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, SessionExpiredException {
		List<NYOPResponse> nyopList = new ArrayList<NYOPResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			nyopList=  adminTransformer.getAllNYOPByCabTypeDistAndNoOfPassenger(commonRequest);
		}	
		return nyopList;
	}
	
	@RequestMapping(value = "/getAllEnabledPricingType")
	public @ResponseBody List<PricingTypeResponse> getAllEnabledPricingType(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<PricingTypeResponse> pricingResponseList = new ArrayList<PricingTypeResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			pricingResponseList =adminTransformer.getAllEnabledPricingType(); 
		}
		return pricingResponseList;
	}
	
	@RequestMapping(value = "/getAllPricingMstrByCabType")
	public @ResponseBody List<PricingMstrResponse> getAllPricingMstrByCabType(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, SessionExpiredException {
		List<PricingMstrResponse> pricingResponseList = new ArrayList<PricingMstrResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			pricingResponseList = adminTransformer.getAllPricingMstrByCabType(commonRequest); 
		}
		return pricingResponseList;
	}
	
	@RequestMapping(value = "/getAllPricingMstr")
	public @ResponseBody List<PricingMstrResponse> getAllPricingMstr(@RequestHeader(value="access-token") String token) throws MandatoryValidationException, SessionExpiredException {
		List<PricingMstrResponse> pricingResponseList = new ArrayList<PricingMstrResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			pricingResponseList = adminTransformer.getAllPricingMstr();
		}
		return pricingResponseList;
	}
	
	@RequestMapping(value = "/savePricingMstrs")
	public List<PricingMstrResponse> savePricingMstrs(@RequestHeader(value="access-token") String token,
			@RequestBody List<PricingMstrRequest> pricingMstrReqList) throws SessionExpiredException {
		List<PricingMstrResponse> pricingResponseList = new ArrayList<PricingMstrResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			pricingResponseList =  adminTransformer.savePricingMstrs(pricingMstrReqList);
		}
		return pricingResponseList;
	}
	
	@RequestMapping(value = "/savePricingMstr")
	public PricingMstrResponse savePricingMstr(@RequestHeader(value="access-token") String token,
			@RequestBody PricingMstrRequest pricingMstrReqList) throws SessionExpiredException {
		PricingMstrResponse pricingMstrResp = new PricingMstrResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			pricingMstrResp = adminTransformer.savePricingMstr(pricingMstrReqList);
		}
		return pricingMstrResp;
	}
	
	/** ------------------- BOOKING  ------------------------ 
	 * @throws SessionExpiredException */
	@RequestMapping(value = "/requestBooking")
	public @ResponseBody BookingResponse requestBooking(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, MandatoryValidationException, SessionExpiredException {
		BookingResponse bookingResponse = new BookingResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponse =bookingTranssformer.createBooking(bookingRequest); 
		}
		return bookingResponse;
	}
	
	@RequestMapping(value = "/updateBookingDriverStatus")
	public @ResponseBody BookingResponse updateBookingDriverStatus(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException{
		BookingResponse bookingResponse = new BookingResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponse = bookingTranssformer.updateBookingDriverStatus(bookingRequest);
		}
		return bookingResponse;
	}	

	@RequestMapping(value = "/updateBookingStatus")
	public @ResponseBody BookingResponse updateBookingStatus(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		BookingResponse bookingResponse = new BookingResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponse = bookingTranssformer.updateBookingStatus(bookingRequest);
		}
		return bookingResponse;
	}	
	
	@RequestMapping(value = "/getBookingByBookingId")
	public @ResponseBody BookingResponse getBookingByBookingId(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, SessionExpiredException {
		BookingResponse bookingResponse = new BookingResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponse = bookingTranssformer.getBookingByBookingId(bookingRequest);
		}
		return bookingResponse;
	}
	
	@RequestMapping(value = "/getBookingByDate")
	public @ResponseBody List<BookingResponse> getBookingByDate(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingByDate(bookingRequest);
		}
		return bookingResponseList;
	}
	
	@RequestMapping(value = "/getBookingByDateNotInRequested")
	public @ResponseBody List<BookingResponse> getBookingByDateNotInRequested(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws ParseException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingByDateNotInRequested(bookingRequest);
		}
		return bookingResponseList;
	}
	
	@RequestMapping(value = "/getBookingByBookingStatus")
	public @ResponseBody List<BookingResponse> getBookingByBookingStatus(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingByBookingStatus(bookingRequest);
		}
		return bookingResponseList;
	}
	
	@RequestMapping(value = "/getBookingByDriverId")
	public @ResponseBody List<BookingResponse> getBookingByDriverId(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingByDriverId(bookingRequest);
		}
		return bookingResponseList;
	}
	
	@RequestMapping(value = "/getBookingByuserId")
	public @ResponseBody List<BookingResponse> getBookingByuserId(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingByuserId(bookingRequest);
		}
		return bookingResponseList;
	}
	
	@RequestMapping(value = "/getBookingRequestedByDriverId")
	public @ResponseBody List<BookingResponse> getBookingRequestedByDriverId(@RequestHeader(value="access-token") String token,
			@RequestBody BookingRequest bookingRequest) throws MandatoryValidationException, SessionExpiredException {
		List<BookingResponse> bookingResponseList = new ArrayList<BookingResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			bookingResponseList = bookingTranssformer.getBookingRequestedByDriverId(bookingRequest);
		}
		return bookingResponseList;
	}
	
	
	/** ----------- PAYMENT -------------------- 
	 * @throws SessionExpiredException */
	@RequestMapping(value = "/savePayment")  
    public @ResponseStatus(value = HttpStatus.OK)  void savePayment(@RequestHeader(value="access-token") String token,
    		@RequestBody PaymentRequest paymentRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException{
		if(headerValidationTransformer.validHeaderAccessToken(token)) {   
			paymentTransformer.savePayment(paymentRequest);
		}
    }
	
	/** -------- COMMISSION ----------------- 
	 * @throws SessionExpiredException */
	
	@RequestMapping(value = "/payCommission")  
    public @ResponseBody CommissionResponse payCommission(@RequestHeader(value="access-token") String token,
    		@RequestBody CommonRequest commonRequest) throws UserValidationException, NoResultEntityException, SessionExpiredException {
		CommissionResponse commissionResponse = new CommissionResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commissionResponse = paymentTransformer.saveCommission(commonRequest);
		}
		return commissionResponse;
    }
	
	@RequestMapping(value = "/getAllCommission")  
    public @ResponseBody List<CommissionResponse> getAllCommission(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<CommissionResponse> commissionRespList = new ArrayList<CommissionResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commissionRespList = paymentTransformer.getAllCommissionsAvailable();
		}
		return commissionRespList;
    }
	
	@RequestMapping(value = "/saveCommissionMaster")  
    public @ResponseBody CommissionMasterResponse saveCommissionMaster(@RequestHeader(value="access-token") String token,
    		@RequestBody CommissionMasterRequest commissionMasterRequest) throws MandatoryValidationException, SessionExpiredException{
		CommissionMasterResponse commissionMstrResponse = new CommissionMasterResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commissionMstrResponse = paymentTransformer.saveCommissionMstr(commissionMasterRequest);
		}
		return commissionMstrResponse;
    }
	
	@RequestMapping(value = "/getCommissionMaster")  
    public @ResponseBody CommissionMasterResponse getCommissionMaster(@RequestHeader(value="access-token") String token) throws MandatoryValidationException, SessionExpiredException{
		CommissionMasterResponse commissionMstrResponse = new CommissionMasterResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commissionMstrResponse = paymentTransformer.getCommissionMstr();
		}
		return commissionMstrResponse;
    }
		
	@RequestMapping(value = "/getCommissionByDriverIdAndStatus")  
	public @ResponseBody List<CommissionResponse> getCommissionByDriverIdAndStatus(@RequestHeader(value="access-token") String token,
			@RequestBody CommonRequest commonRequest) throws MandatoryValidationException, SessionExpiredException {
		List<CommissionResponse> commissionRespList = new ArrayList<CommissionResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			commissionRespList = paymentTransformer.getCommissionByDriverIdAndStatus(commonRequest);
		}
		return commissionRespList;
	}
	
	/** ------------ CONFIGURATIONS -----------------------*/
	
	@RequestMapping(value = "/getZiprydeConfigurationByType")  
	public @ResponseBody ConfigurationResponse getZiprydeConfigurationByType(@RequestHeader(value="access-token") String token,
			@RequestBody ConfigurationRequest configurationRequest) throws SessionExpiredException {
		ConfigurationResponse configurationResponse  = new ConfigurationResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			configurationResponse =  adminTransformer.getZiprydeConfigurationByType(configurationRequest);
		}
		return configurationResponse;
	}
	
	@RequestMapping(value = "/saveZiprydeConfiguration")  
	public @ResponseBody ConfigurationResponse saveZiprydeConfiguration(@RequestHeader(value="access-token") String token,
			@RequestBody ConfigurationRequest configurationRequest) throws UserValidationException, SessionExpiredException {
		ConfigurationResponse configurationResponse  = new ConfigurationResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			configurationResponse = adminTransformer.saveZiprydeConfiguration(configurationRequest);
		}
		return configurationResponse;
	}
	
	@RequestMapping(value = "/getAllZiprydeConfigurations")  
	public @ResponseBody List<ConfigurationResponse> getAllZiprydeConfigurations(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<ConfigurationResponse> configurationResList = new ArrayList<ConfigurationResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			configurationResList = adminTransformer.getAllZiprydeConfigurations();
		}
		return configurationResList;
	}
		
	/** ------------Lost Item ------------- 
	 * @throws SessionExpiredException */
	
	@RequestMapping(value = "/saveLostItem")  
	public @ResponseBody LostItemResponse saveLostItem(@RequestHeader(value="access-token") String token,
			@RequestBody LostItemRequest lostItemRequest) throws UserValidationException, MandatoryValidationException, SessionExpiredException {
		LostItemResponse lostItemResponse = new LostItemResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			lostItemResponse = bookingTranssformer.saveLostItem(lostItemRequest);
		}
		return lostItemResponse;
	}
	
	@RequestMapping(value = "/getLostItemByBookingId")  
	public @ResponseBody LostItemResponse getLostItemByBookingId(@RequestHeader(value="access-token") String token,
			@RequestBody LostItemRequest lostItemRequest) throws MandatoryValidationException, SessionExpiredException {
		LostItemResponse lostItemResponse = new LostItemResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			lostItemResponse = bookingTranssformer.getLostItemByBookingId(lostItemRequest);
		}
		return lostItemResponse;
	}

	@RequestMapping(value = "/getAllLostItems")  
	public @ResponseBody List<LostItemResponse> getAllLostItems(@RequestHeader(value="access-token") String token) throws SessionExpiredException {
		List<LostItemResponse> lostItemRespList = new ArrayList<LostItemResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			lostItemRespList = bookingTranssformer.getAllLostItems();
		}
		return lostItemRespList;
	}
	
	/** --------- MONGO DB SERVICE -------------------- 
	 * @throws SessionExpiredException */
	
	@RequestMapping(value = "/getGeoLocationByDriverId")
	public @ResponseBody UserGeoSpatialResponse getGeoLocationByDriverId(@RequestHeader(value="access-token") String token,
			@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, SessionExpiredException {
		UserGeoSpatialResponse userGeoResponse = new UserGeoSpatialResponse();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userGeoResponse = mongoTransfomer.getGeoLocationByDriverId(geoLocationRequest);
		}
		return userGeoResponse;
	}
	
	@RequestMapping(value = "/getNearByActiveDrivers")
	public @ResponseBody List<UserGeoSpatialResponse> getNearByActiveDrivers(@RequestHeader(value="access-token") String token,
			@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, SessionExpiredException {
		List<UserGeoSpatialResponse> userGeoResponseList = new ArrayList<UserGeoSpatialResponse>();
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			userGeoResponseList =	mongoTransfomer.getNearByActiveDrivers(geoLocationRequest);
		}
		return userGeoResponseList;
	}
	
	@RequestMapping(value = "/insertDriverSession")
	public void insertDriverSession(@RequestHeader(value="access-token") String token,
			@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			mongoTransfomer.updateDriverSession(geoLocationRequest);
		}
	}
	
	@RequestMapping(value = "/updateDriverSession")
	public void updateDriverSession(@RequestHeader(value="access-token") String token,
			@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			mongoTransfomer.updateDriverSession(geoLocationRequest);
		}
	}
	
	@RequestMapping(value = "/updateDriverStatus")
	public void updateDriverStatus(@RequestHeader(value="access-token") String token,
			@RequestBody GeoLocationRequest geoLocationRequest) throws MandatoryValidationException, UserValidationException, SessionExpiredException {
		if(headerValidationTransformer.validHeaderAccessToken(token)) {
			mongoTransfomer.updateDriverOnlineStatus(geoLocationRequest);
		}
	}	
}
