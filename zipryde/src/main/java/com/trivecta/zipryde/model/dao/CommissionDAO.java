package com.trivecta.zipryde.model.dao;


import java.math.BigDecimal;
import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

public interface CommissionDAO {

	public Commission payCommission(Commission commission) throws NoResultEntityException;
	public CommissionMstr saveCommissionMaster(CommissionMstr commissionMstr);
	public Commission getCommission(int commissionId);
	public List getAllCommissions();
	public void updateCommision(Booking booking);
	public CommissionMstr getCommissionMstr();
	public BigDecimal getCommissionByStatus(String status);
	public List<Commission> getCommissionByDriverIdAndStatus(int driverId,String status);
}
