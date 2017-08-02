package com.trivecta.zipryde.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

public interface CommissionService {
	
	public Commission payCommission(Commission commission) throws NoResultEntityException;
	public Commission getCommission(int commissionId);
	public List getAllCommission();
	public CommissionMstr saveCommissionMaster(CommissionMstr commissionMstr);
	public CommissionMstr getCommissionMstr();
	public BigDecimal getCommissionByStatus(String status);
}
