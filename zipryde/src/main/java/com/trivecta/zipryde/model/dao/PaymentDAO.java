package com.trivecta.zipryde.model.dao;

import com.trivecta.zipryde.model.entity.Payment;

public interface PaymentDAO {
	
	public void savePayment(Payment payment);
	public Payment getPayment(Integer paymentId);	
}
