package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.PAYMENT;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

@Repository
public class CommissionDAOImpl implements CommissionDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void payCommission(Commission commission) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();
		
		Commission commissionOrg = session.find(Commission.class, commission.getId());
		if(commissionOrg == null)
			throw new NoResultEntityException(ErrorMessages.COMMISSION_ID_INVALID);
		commissionOrg.setStatus(PAYMENT.PAID);
		commissionOrg.setPaidDate(new Date());
		session.merge(commissionOrg);
	}

	@Override
	public void saveCommissionMaster(CommissionMstr commissionMstr) {
		Session session = this.sessionFactory.getCurrentSession();
		CommissionMstr orgCommissionMstr = getCommissionMstr();
		if(orgCommissionMstr == null) {
			commissionMstr.setFromDate(new Date());
			session.save(commissionMstr);
		}else {
			orgCommissionMstr.setCommisionPercentage(commissionMstr.getCommisionPercentage());
			orgCommissionMstr.setNoOfMiles(commissionMstr.getNoOfMiles());
			orgCommissionMstr.setNoOfTrips(commissionMstr.getNoOfTrips());
			session.merge(orgCommissionMstr);
			commissionMstr = orgCommissionMstr;
		}		
	}

	@Override
	public Commission getCommission(int commissionId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Commission.class, commissionId);
	}

	@Override
	public List getAllCommissions() {
		Session session = this.sessionFactory.getCurrentSession();
		return session.getNamedQuery("Commission.findAll").getResultList();
	}
	
	@Override
	@Async
	public void updateCommision(Booking booking){
		
		Session session = this.sessionFactory.getCurrentSession();
		CommissionMstr commissionMstr = (CommissionMstr)session.getNamedQuery("CommissionMstr.getCommissionMstrForDate")
				.getSingleResult();
		Commission commission = null;
		try{
			commission = (Commission)session.getNamedQuery("Commission.getLatest")
					.setParameter("driverId",booking.getDriver().getId()).getSingleResult();
		}catch(NoResultException nre) {
			System.out.println("New Commission rowto be created");
		}
				
		
		if(commission == null) {
			commission = getNewCommissionObject();
		}
		
		commission.setUser(booking.getDriver());
		commission.setNoOfMiles(commission.getNoOfMiles() + booking.getDistanceInMiles());
		commission.setNoOfTrips(commission.getNoOfTrips() + 1);
		Double commissionAmount = calculateCommissionAmount(booking.getAcceptedPrice(),commissionMstr.getCommisionPercentage());
		commission.setCommisionAmount(BigDecimal.valueOf(commission.getCommisionAmount().doubleValue() + commissionAmount));
		if(commission.getNoOfMiles() >= commissionMstr.getNoOfMiles() || commission.getNoOfTrips() >= commissionMstr.getNoOfTrips()) {
			commission.setCalculatedDate(new Date());
			commission.setStatus(PAYMENT.PENDING);
		}
		
		session.saveOrUpdate(commission);		
	}

	private Commission getNewCommissionObject() {
		Commission commission = new Commission();
		commission.setCommisionAmount(BigDecimal.ZERO);
		commission.setNoOfMiles(0);
		commission.setNoOfTrips(0);
		return commission;
	}
	
	private Double calculateCommissionAmount(BigDecimal acceptedPrice, BigDecimal commisionPercentage) {
		return (acceptedPrice.doubleValue() * commisionPercentage.doubleValue()) / 100 ;
	}

	@Override
	public CommissionMstr getCommissionMstr(){
		Session session = this.sessionFactory.getCurrentSession();
		CommissionMstr commissionMstr = null;
		try {
			commissionMstr = (CommissionMstr)session.getNamedQuery("CommissionMstr.getCommissionMstrForDate")
					.getSingleResult();
		}catch(NoResultException nre) {
			nre.printStackTrace();
		}
		return commissionMstr;
		
	}

	@Override
	public BigDecimal getCommissionByStatus(String status) {
		Session session = this.sessionFactory.getCurrentSession();
		BigDecimal commissionAmount = null;
		try {
			commissionAmount = (BigDecimal)session.getNamedQuery("Commission.getCommissionAmountByStatus").setParameter("status", status)
			.getSingleResult();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		if(commissionAmount == null)
			commissionAmount = BigDecimal.ZERO;
		return commissionAmount;
	}
	
	
}
