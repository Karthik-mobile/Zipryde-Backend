package com.trivecta.zipryde.model.dao;

import java.util.List;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabPermit;
import com.trivecta.zipryde.model.entity.VehicleDetail;

public interface VehicleDAO {

	public VehicleDetail createVehicle(VehicleDetail vehicleDetail,CabPermit cabPermit) throws UserValidationException;
	
	public VehicleDetail updateVehicle(VehicleDetail vehicleDetail,CabPermit cabPermit) throws UserValidationException;
	
	public List<VehicleDetail> getAllVehicles();
	
	public VehicleDetail getVehicleDetailById(int vehicleId) throws UserValidationException;
}
