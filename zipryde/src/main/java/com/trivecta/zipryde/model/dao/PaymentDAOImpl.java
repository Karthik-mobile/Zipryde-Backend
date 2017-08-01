package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.NoResultException;

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
	
	@Autowired
	FCMNotificationDAO fCMNotificationDAO;

	@Override
	public void savePayment(Payment payment) {		
		Session session = this.sessionFactory.getCurrentSession();
		Booking origBooking = session.find(Booking.class, payment.getBooking().getId());
		payment.setBooking(origBooking);
		session.saveOrUpdate(payment);
		fCMNotificationDAO.sendBookingStatusNotification(origBooking);
	}

	@Override
	public Payment getPayment(Integer paymentId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Payment.class, paymentId);
	}

	@Override
	public Double getPaymentAmountByDate(Date date) {
		Session session = this.sessionFactory.getCurrentSession();
		BigDecimal amount = null ;
		try {
			amount = (BigDecimal) session.getNamedQuery("Payment.revenueAmountByDate").
						 setParameter("date", date).getSingleResult();
		}
		catch(NoResultException e) {
			//No Result found
		}
		if(amount == null) {
			amount  = BigDecimal.ZERO;
		}
		return amount.doubleValue();
	}
	
	@Override
	public Double getPaymentAmountByDateAndDriverId(Date date,Integer driverId) {
		Session session = this.sessionFactory.getCurrentSession();
		BigDecimal amount = null ;
		try {
			amount = (BigDecimal) session.getNamedQuery("Payment.revenueAmountByDateAndDriverId").
				 setParameter("date", date).setParameter("driverId", driverId).getSingleResult();
		}
		catch(NoResultException e) {
			//No Result found
		}
		if(amount == null) {
			amount  = BigDecimal.ZERO;
		}
		return amount.doubleValue();
	}
}
