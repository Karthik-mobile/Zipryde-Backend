package com.trivecta.zipryde.model.service;

import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

public interface CommissionService {
	
	public void payCommission(Commission commission) throws NoResultEntityException;
	public Commission getCommission(int commissionId);
	public List getAllCommission();
	public void saveCommissionMaster(CommissionMstr commissionMstr);
}
