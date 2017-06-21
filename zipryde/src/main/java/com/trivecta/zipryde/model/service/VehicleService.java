package com.trivecta.zipryde.model.service;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabPermit;
import com.trivecta.zipryde.model.entity.VehicleDetail;

public interface VehicleService {

	public VehicleDetail saveVehicle(VehicleDetail vehicleDetail,CabPermit cabPermit) throws UserValidationException;
	
	
}
