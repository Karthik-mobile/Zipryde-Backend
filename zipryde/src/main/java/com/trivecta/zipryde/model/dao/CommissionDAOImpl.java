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
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;
import com.trivecta.zipryde.model.entity.User;

@Repository
public class CommissionDAOImpl implements CommissionDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	FCMNotificationDAO fCMNotificationDAO;

	@Override
	public Commission payCommission(Commission commission) throws NoResultEntityException {
		Session session = this.sessionFactory.getCurrentSession();		
		Commission commissionOrg = null;
		try {
			commissionOrg = session.find(Commission.class, commission.getId());
		}
		catch(Exception e){
			throw new NoResultEntityException(ErrorMessages.COMMISSION_ID_INVALID);
		}
		commissionOrg.setStatus(STATUS.PAID);
		commissionOrg.setPaidDate(new Date());
		//Enable User if Commission Paid
		/*User user = (User)session.find(User.class, commissionOrg.getUser().getId());
		user.setIsEnable(1);
		session.merge(user);
		commissionOrg.setUser(user);*/
		session.merge(commissionOrg);
		fCMNotificationDAO.sendCommissionPaidNotification(commissionOrg.getUser().getDeviceToken(),commissionOrg.getId());
		return commissionOrg;
	}

	@Override
	public CommissionMstr saveCommissionMaster(CommissionMstr commissionMstr) {
		Session session = this.sessionFactory.getCurrentSession();
		CommissionMstr orgCommissionMstr = getCommissionMstr();
		if(orgCommissionMstr == null) {
			commissionMstr.setFromDate(new Date());
			commissionMstr = (CommissionMstr)session.save(commissionMstr);
			return commissionMstr;
		}else {
			orgCommissionMstr.setCommisionPercentage(commissionMstr.getCommisionPercentage());
			orgCommissionMstr.setNoOfMiles(commissionMstr.getNoOfMiles());
			orgCommissionMstr.setNoOfTrips(commissionMstr.getNoOfTrips());
			orgCommissionMstr = (CommissionMstr) session.merge(orgCommissionMstr);
			return orgCommissionMstr;
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
	public List<Commission> getCommissionByDriverIdAndStatus(int driverId,String status){
		Session session = this.sessionFactory.getCurrentSession();
		List<Commission> commissionList = session.getNamedQuery("Commission.getByDriverIdAndStatus").setParameter("driverId", driverId) .setParameter("status",status).getResultList();
		return commissionList;
	}
	
	@Override
	@Async
	public void updateCommision(Booking booking){		
		Session session = this.sessionFactory.getCurrentSession();
		CommissionMstr commissionMstr = null;
		Commission commission = null;
		try {
			commissionMstr = (CommissionMstr)session.getNamedQuery("CommissionMstr.getCommissionMstrForDate").getSingleResult();
		}
		catch(NoResultException nre) {
			//No Commiission Mstr
		}				
			
		if(commissionMstr != null) {
			try {
				commission = (Commission)session.getNamedQuery("Commission.getLatest")
						.setParameter("driverId",booking.getDriver().getId()).getSingleResult();
			}
			catch(NoResultException nre) {
				//No Commiission
			}	
			if(commission == null) {
				commission = getNewCommissionObject();
			}
			
			User user = (User)session.find(User.class, booking.getDriver().getId());
			commission.setNoOfMiles(commission.getNoOfMiles() + booking.getDistanceInMiles());
			commission.setNoOfTrips(commission.getNoOfTrips() + 1);
			Double commissionAmount = calculateCommissionAmount(booking.getAcceptedPrice(),commissionMstr.getCommisionPercentage());
			commission.setCommisionAmount(BigDecimal.valueOf(commission.getCommisionAmount().doubleValue() + commissionAmount));
			if(commission.getNoOfMiles() >= commissionMstr.getNoOfMiles() || commission.getNoOfTrips() >= commissionMstr.getNoOfTrips()) {
				commission.setCalculatedDate(new Date());
				commission.setStatus(PAYMENT.PENDING);
				//If Commission is Pending disable DRIVER
				/*user.setIsEnable(0);
				session.merge(user);*/
			}		
			commission.setUser(user);
			session.saveOrUpdate(commission);	
			if(PAYMENT.PENDING.equalsIgnoreCase(commission.getStatus())){
				fCMNotificationDAO.sendCommissionPendingNotification(booking.getDriver().getDeviceToken(),commission.getId());
			}
		}		
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
