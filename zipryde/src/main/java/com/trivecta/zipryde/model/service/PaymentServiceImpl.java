package com.trivecta.zipryde.model.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.model.dao.PaymentDAO;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Payment;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentDAO paymentDAO;
	
	@Override
	@Transactional
	public void savePayment(Payment payment){
		paymentDAO.savePayment(payment);
	}

	@Override
	@Transactional
	public Payment getPayment(Integer paymentId) {
		return paymentDAO.getPayment(paymentId);
	}
}
