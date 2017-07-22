package com.trivecta.zipryde.model.dao;


import java.util.List;

import com.trivecta.zipryde.framework.exception.NoResultEntityException;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.Commission;
import com.trivecta.zipryde.model.entity.CommissionMstr;

public interface CommissionDAO {

	public void payCommission(Commission commission) throws NoResultEntityException;
	public void saveCommissionMaster(CommissionMstr commissionMstr);
	public Commission getCommission(int commissionId);
	public List getAllCommissions();
	public void updateCommision(Booking booking);
	public CommissionMstr getCommissionMstr();
}
