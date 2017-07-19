package com.trivecta.zipryde.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Payment;

@Repository
public class PaymentDAOImpl implements PaymentDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void savePayment(Payment payment) {		
		Session session = this.sessionFactory.getCurrentSession();
		
		Booking origBooking = session.find(Booking.class, payment.getBooking().getId());
		payment.setBooking(origBooking);
		session.saveOrUpdate(payment);
	}

	@Override
	public Payment getPayment(Integer paymentId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Payment.class, paymentId);
	}
}
