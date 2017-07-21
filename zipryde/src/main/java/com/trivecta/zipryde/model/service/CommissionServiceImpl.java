package com.trivecta.zipryde.model.service;

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
	public void payCommission(Commission commission) throws NoResultEntityException {
		commissionDAO.payCommission(commission);
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
	public void saveCommissionMaster(CommissionMstr commissionMstr) {
		commissionDAO.saveCommissionMaster(commissionMstr);
		
	}

}
