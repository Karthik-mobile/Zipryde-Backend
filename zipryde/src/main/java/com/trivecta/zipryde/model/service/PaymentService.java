package com.trivecta.zipryde.model.service;

import java.util.Date;

import com.trivecta.zipryde.model.entity.Payment;


public interface PaymentService {
	
	public void savePayment(Payment payment);
	public Payment getPayment(Integer paymentId);
	public Double getPaymentAmountByDate(Date searchDate);
}
