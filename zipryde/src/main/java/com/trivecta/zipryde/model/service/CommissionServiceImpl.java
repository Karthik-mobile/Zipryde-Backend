package com.trivecta.zipryde.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.dao.CommissionDAO;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

@Service("commissionService")
public class CommissionServiceImpl implements CommissionService {
	
	@Autowired
	CommissionDAO commissionDAO;
		
	@Override
	@Transactional
	public Commission payCommission(Commission commission) throws NoResultEntityException {
		return commissionDAO.payCommission(commission);
	}

	@Override
	@Transactional
	public Commission getCommission(int commissionId) {
		return commissionDAO.getCommission(commissionId);		
	}

	@Override
	@Transactional
	public List getAllCommission() {
		return commissionDAO.getAllCommissions();
	}

	@Override
	@Transactional
	public CommissionMstr saveCommissionMaster(CommissionMstr commissionMstr) {
		return commissionDAO.saveCommissionMaster(commissionMstr);
		
	}

	@Override
	@Transactional
	public CommissionMstr getCommissionMstr() {
		return commissionDAO.getCommissionMstr();
	}

	@Override
	@Transactional
	public BigDecimal getCommissionByStatus(String status) {
		return commissionDAO.getCommissionByStatus(status);
	}
	
	@Transactional
	public List<Commission> getCommissionByDriverIdAndStatus(int driverId,String status) {
		return commissionDAO.getCommissionByDriverIdAndStatus(driverId, status);
	}

}
