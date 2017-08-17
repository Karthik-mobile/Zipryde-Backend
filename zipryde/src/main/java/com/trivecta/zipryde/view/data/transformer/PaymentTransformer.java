package com.trivecta.zipryde.view.data.transformer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.PAYMENT;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;
import com.trivecta.zipryde.model.entity.Payment;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.service.BookingService;
import com.trivecta.zipryde.model.service.CommissionService;
import com.trivecta.zipryde.model.service.PaymentService;
import com.trivecta.zipryde.view.request.CommissionMasterRequest;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.PaymentRequest;
import com.trivecta.zipryde.view.response.BookingResponse;
import com.trivecta.zipryde.view.response.CommissionMasterResponse;
import com.trivecta.zipryde.view.response.CommissionResponse;
import com.trivecta.zipryde.view.response.CommonResponse;

@Component
public class PaymentTransformer {
    
	@Autowired
    PaymentService paymentService;
    
    @Autowired
    CommissionService commissionService;
    
    @Autowired
    BookingService bookingService;
    
    public void savePayment(PaymentRequest paymentRequest) throws MandatoryValidationException, UserValidationException {
		StringBuffer errorMsg = new StringBuffer();
		if(paymentRequest.getBookingId() == null) {
			errorMsg.append(ErrorMessages.BOOKING_ID_REQUIRED);
		}
		if(paymentRequest.getAmountPaid() == null) {
			errorMsg.append(ErrorMessages.AMOUNT_TO_PAY_REQUIRED);
		}
		
		if(ValidationUtil.isValidString(errorMsg.toString())) {
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {			
	        Payment payment = new Payment();
	        if(paymentRequest.getPaymentId() != null)
	               payment.setId(paymentRequest.getPaymentId().intValue());
	        Booking booking = new Booking();
	        booking.setId(paymentRequest.getBookingId().intValue());
	        payment.setBooking(booking);
	        payment.setAmountPaid(BigDecimal.valueOf(paymentRequest.getAmountPaid()));
	        payment.setPaymentType(paymentRequest.getPaymentType() != null ? paymentRequest.getPaymentType() : PAYMENT.CASH);
	        payment.setPaidDateTime(new Date());
	        
	        Status status = new Status();
			status.setStatus(STATUS.PAID);
			booking.setBookingStatus(status);			
			Booking updatedBooking = bookingService.updateBookingStatus(booking,payment);
			//return setBookingResponseFromBooking(updatedBooking,false);
	       // paymentService.savePayment(payment);
		}
	}
    
    public CommonResponse getRevenueByDate(PaymentRequest paymentRequest) throws ParseException {	
		Date date = new Date();
		if(ValidationUtil.isValidString(paymentRequest.getPaidDateTime())) {
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			date = dateFormat.parse(paymentRequest.getPaidDateTime());
		}
		
		Double revenueAmount = paymentService.getPaymentAmountByDate(date);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setRevenueAmount(revenueAmount);
		return commonResponse;
	}
    
    public CommonResponse getRevenueByDateAndDriverId(PaymentRequest paymentRequest) throws ParseException, MandatoryValidationException {	
  		if(paymentRequest.getDriverId() == null) {
  			throw new MandatoryValidationException(ErrorMessages.DRIVER_ID_REQUIRED);
  		}
  		else {
  			Date date = new Date(); 	  		
  	  		
  	  		if(ValidationUtil.isValidString(paymentRequest.getPaidDateTime())) {
  	  			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  	  			date = dateFormat.parse(paymentRequest.getPaidDateTime());
  	  		}
  	  		
  	  		Double revenueAmount = paymentService.getPaymentAmountByDateAndDriverId(date,paymentRequest.getDriverId().intValue());
  	  		CommonResponse commonResponse = new CommonResponse();
  	  		commonResponse.setRevenueAmount(revenueAmount);
  	  		return commonResponse;
  		}
  	}

	public CommissionResponse saveCommission(CommonRequest commonRequest) throws UserValidationException, NoResultEntityException {		
		if(commonRequest.getCommissionId() != null) {
			Commission commission = new Commission();
			commission.setId(commonRequest.getCommissionId().intValue());
			Commission newCommission = commissionService.payCommission(commission);
			return setCommissionResponse(newCommission);
		}
		else
			throw new UserValidationException(ErrorMessages.COMMISSION_ID_EMPTY);		
	}
	
	public List<CommissionResponse> getAllCommissionsAvailable() {		
		List<CommissionResponse> commissionList = new ArrayList<CommissionResponse>();
		List objectList = commissionService.getAllCommission();
		for(Object object : objectList) {
			Commission commission = (Commission)object;
			commissionList.add(setCommissionResponse(commission));
		}
		return commissionList;
	}
	
	public CommissionMasterResponse saveCommissionMstr(CommissionMasterRequest commissionMstrRequest) throws MandatoryValidationException{
		CommissionMstr commissionMstr = new CommissionMstr();
		if(commissionMstrRequest.getNoOfMiles() == null && commissionMstrRequest.getNoOfTrips() == null)
			throw new MandatoryValidationException(ErrorMessages.COMMISSION_MSTR_REQUEST_EXCEPTION_MESSAGE);
		
		commissionMstr.setNoOfMiles(commissionMstrRequest.getNoOfMiles());
		commissionMstr.setNoOfTrips(commissionMstrRequest.getNoOfTrips());
		commissionMstr.setCommisionPercentage(BigDecimal.valueOf(commissionMstrRequest.getCommissionPercentage()));		
		commissionMstr = commissionService.saveCommissionMaster(commissionMstr);
		return setCommissionMstrResponse(commissionMstr);
	}

	public CommissionMasterResponse getCommissionMstr() {
		CommissionMstr commissionMstr = commissionService.getCommissionMstr();		
		return setCommissionMstrResponse(commissionMstr);
	}
	
	public CommonResponse getCommissionAmountByStatus(CommonRequest commonRequest) throws MandatoryValidationException {
		if(commonRequest == null || StringUtils.isBlank(commonRequest.getStatus()))
			throw new MandatoryValidationException(ErrorMessages.STATUS_REQUERIED);
		Double amount = commissionService.getCommissionByStatus(commonRequest.getStatus().trim().toUpperCase()).doubleValue();
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setRevenueAmount(amount);
		return commonResponse;		
	}
	
	private CommissionMasterResponse setCommissionMstrResponse(CommissionMstr commissionMstr) {
		CommissionMasterResponse commissionMasterResponse = new CommissionMasterResponse();
		if(commissionMstr != null) {
			commissionMasterResponse.setCommissionPercentage(commissionMstr.getCommisionPercentage().doubleValue());
			commissionMasterResponse.setNoOfMiles(commissionMstr.getNoOfMiles());
			commissionMasterResponse.setNoOfTrips(commissionMstr.getNoOfTrips());
			commissionMasterResponse.setId(commissionMstr.getId());
		}
		return commissionMasterResponse;
	}
	private CommissionResponse setCommissionResponse(Commission commission) {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		CommissionResponse commissionResponse = new CommissionResponse();
		commissionResponse.setCommissionId(commission.getId());
		if(commission.getCalculatedDate() != null)
			commissionResponse.setCalcualtedDate(dateFormat.format(commission.getCalculatedDate()));
		if(commission.getPaidDate() != null)
			commissionResponse.setPaidDate(dateFormat.format(commission.getPaidDate()));
		commissionResponse.setCommissionAmount(commission.getCommisionAmount().doubleValue());
		commissionResponse.setDriverName(commission.getUser().getFirstName());
		commissionResponse.setStatus(commission.getStatus());
		commissionResponse.setNoOfMiles(commission.getNoOfMiles());
		commissionResponse.setNoOfTrips(commission.getNoOfTrips());
		return commissionResponse;
	}
}
