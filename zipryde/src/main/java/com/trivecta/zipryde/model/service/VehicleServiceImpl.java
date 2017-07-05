package com.trivecta.zipryde.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.dao.VehicleDAO;
import com.trivecta.zipryde.model.entity.CabPermit;
import com.trivecta.zipryde.model.entity.VehicleDetail;

@Service("cabService")
public class VehicleServiceImpl implements VehicleService{

	@Autowired
	VehicleDAO vehicleDAO;
	
	@Transactional
	public VehicleDetail saveVehicle(VehicleDetail vehicleDetail, CabPermit cabPermit)throws UserValidationException {
		
		if(vehicleDetail.getId() == null || vehicleDetail.getId() == 0) {
			return vehicleDAO.createVehicle(vehicleDetail, cabPermit);	
		}
		else {
			return vehicleDAO.updateVehicle(vehicleDetail, cabPermit);
		}		
	}
	
	@Transactional
	public List<VehicleDetail> getAllVehicles(){
		return vehicleDAO.getAllVehicles();
	}
	
	@Transactional
	public VehicleDetail getVehicleDetailById(int vehicleId) throws UserValidationException {
		return vehicleDAO.getVehicleDetailById(vehicleId);
	}
	
	@Transactional
	public List<VehicleDetail> getAllAvailableVehicles() {
		return vehicleDAO.getAllAvailableVehicles();
	}


}
