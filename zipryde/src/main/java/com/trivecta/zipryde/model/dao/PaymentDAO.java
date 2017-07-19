package com.trivecta.zipryde.model.dao;

import java.util.Date;

import com.trivecta.zipryde.model.entity.Payment;

public interface PaymentDAO {
	
	public void savePayment(Payment payment);
	public Payment getPayment(Integer paymentId);
	public Double getPaymentAmountByDate(Date date);	
}
