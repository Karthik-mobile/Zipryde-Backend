
package com.trivecta.zipryde.model.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.STATUS;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabPermit;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.VehicleDetail;

@Repository
public class VehicleDAOImpl implements VehicleDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private VehicleDetail getVehicleDetailByVIN(String vin) {
		Session session = this.sessionFactory.getCurrentSession();
		
		VehicleDetail vehicleDetail = null;
		try {
			vehicleDetail = (VehicleDetail) session.getNamedQuery("VehicleDetail.findByVIN").
					setParameter("vin", vin).getSingleResult();
		}
		catch(NoResultException e){
			//No VIN Exists
		}
		return vehicleDetail;
	}
	
	private VehicleDetail getVehicleDetailByLicensePlateNumber(String licensePlateNo) {
		Session session = this.sessionFactory.getCurrentSession();
		
		VehicleDetail vehicleDetail = null;
		try {
			vehicleDetail = (VehicleDetail) session.getNamedQuery("VehicleDetail.findByLicensePlateNumber").
					setParameter("licensePlateNo", licensePlateNo).getSingleResult();
		}
		catch(NoResultException e){
			//No VIN Exists
		}
		return vehicleDetail;
	}
	
	public VehicleDetail createVehicle(VehicleDetail vehicleDetail,CabPermit cabPermit) throws UserValidationException {
		
		VehicleDetail vinVehicle = getVehicleDetailByVIN(vehicleDetail.getVin());
		
		VehicleDetail licenSePlateVehicle = getVehicleDetailByLicensePlateNumber(vehicleDetail.getLicensePlateNo());
		
		StringBuffer errorMsg = new StringBuffer();
		if(vinVehicle != null) {
			errorMsg.append(ErrorMessages.VIN_EXISTS_ALREADY);
		}
		if(licenSePlateVehicle != null) {
			errorMsg.append(ErrorMessages.LICENSE_PLATE_EXISTS_ALREADY);
		}
		
		if(errorMsg.toString() != null && !"".equalsIgnoreCase(errorMsg.toString())) {
			throw new UserValidationException(errorMsg.toString());
		}
		else {
			Session session = this.sessionFactory.getCurrentSession();
						
			CabType cabType = session.find(CabType.class, vehicleDetail.getCabType().getId());
			vehicleDetail.setCabType(cabType);
		
			Model model = session.find(Model.class, vehicleDetail.getModel().getId());
			vehicleDetail.setModel(model);
			
			Status status = null;
			
			if(vehicleDetail.getStatus() != null) {
				status = (Status)
						session.getNamedQuery("Status.findByStatus").
						setParameter("status", vehicleDetail.getStatus().getStatus()).getSingleResult();			
			}
			else {
				status = (Status)
						session.getNamedQuery("Status.findByStatus").
						setParameter("status", STATUS.REQUESTED).getSingleResult();			
			}
			vehicleDetail.setStatus(status);
			
			if(vehicleDetail.getIsEnable() == null) {
				vehicleDetail.setIsEnable(0);
			}
			
			vehicleDetail.setCreationDate(new Date());
			vehicleDetail.setModifiedDate(new Date());
			session.save(vehicleDetail);
			
			if(cabPermit != null) {
				cabPermit.setVehicleDetail(vehicleDetail);
				session.save(cabPermit);
			}			
			return getVehicleDetailById(vehicleDetail.getId());	
		}		
	}
	
	public VehicleDetail updateVehicle(VehicleDetail vehicleDetail,CabPermit cabPermit) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		
		VehicleDetail origVehicle = null;
		
		try {
			origVehicle = session.find(VehicleDetail.class, vehicleDetail.getId());
		}
		catch(NoResultException e) {
			throw new UserValidationException(ErrorMessages.NO_CAB_FOUND_BY_ID);
		}
		
		VehicleDetail vinVehicle = getVehicleDetailByVIN(vehicleDetail.getVin());
		
		VehicleDetail licenSePlateVehicle = getVehicleDetailByLicensePlateNumber(vehicleDetail.getLicensePlateNo());
		
		StringBuffer errorMsg = new StringBuffer();
				
		if(vinVehicle!=null && origVehicle.getId() != vinVehicle.getId()) {
			errorMsg.append(ErrorMessages.VIN_EXISTS_ALREADY);
		}
		if(licenSePlateVehicle != null && origVehicle.getId() != licenSePlateVehicle.getId()) {
			errorMsg.append(ErrorMessages.LICENSE_PLATE_EXISTS_ALREADY);
		}
		
		if(errorMsg.toString() != null && !"".equalsIgnoreCase(errorMsg.toString())) {
			throw new UserValidationException(errorMsg.toString());
		}
		else {
			CabType cabType = session.find(CabType.class, vehicleDetail.getCabType().getId());
			origVehicle.setCabType(cabType);
		
			Model model = session.find(Model.class, vehicleDetail.getModel().getId());
			origVehicle.setModel(model);
			
			if(vehicleDetail.getStatus() != null) {
				Status status = null;
				status = (Status)
						session.getNamedQuery("Status.findByStatus").
						setParameter("status", vehicleDetail.getStatus().getStatus()).getSingleResult();	
				
				origVehicle.setStatus(status);		
			}
			
			origVehicle.setAccessories(vehicleDetail.getAccessories());
			origVehicle.setColor(vehicleDetail.getColor());
			origVehicle.setComments(vehicleDetail.getComments());
			origVehicle.setInsuranceCompany(vehicleDetail.getInsuranceCompany());
			origVehicle.setInsuranceNo(vehicleDetail.getInsuranceNo());
			origVehicle.setInsuranceValidUntil(vehicleDetail.getInsuranceValidUntil());
			origVehicle.setIsEnable(vehicleDetail.getIsEnable());
			origVehicle.setSeatingCapacity(vehicleDetail.getSeatingCapacity());
			origVehicle.setVin(vehicleDetail.getVin());
			origVehicle.setManufacturedYear(vehicleDetail.getManufacturedYear());
			origVehicle.setLicensePlateNo(vehicleDetail.getLicensePlateNo());
			origVehicle.setVehicleNumber(vehicleDetail.getVehicleNumber());
			origVehicle.setModifiedDate(new Date());
			session.merge(origVehicle);
			
			if(cabPermit != null && cabPermit.getId() != null) {
				CabPermit origCabPermit = session.find(CabPermit.class,cabPermit.getId());
				origCabPermit.setPermitNumber(cabPermit.getPermitNumber());
				origCabPermit.setPermitValidUntil(cabPermit.getPermitValidUntil());
				origCabPermit.setVehicleDetail(vehicleDetail);
				session.merge(origCabPermit);			
			}		
			return getVehicleDetailById(origVehicle.getId());	
		}	
	}
	
	public List<VehicleDetail> getAllVehicles() {
		Session session = this.sessionFactory.getCurrentSession();		
		List<VehicleDetail>  vehicleDetailsList =				
				session.getNamedQuery("VehicleDetail.findAll").getResultList();		
		for(VehicleDetail vehicleDetail : vehicleDetailsList) {
			fetchLazyInitialisation(vehicleDetail);
		}		
		return vehicleDetailsList;		
	}
	
	public VehicleDetail getVehicleDetailById(int vehicleId) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		VehicleDetail vehicleDetail = null;
		try {
			vehicleDetail = session.find(VehicleDetail.class, vehicleId);
			fetchLazyInitialisation(vehicleDetail);
			return vehicleDetail;
		}
		catch(NoResultException e) {
			throw new UserValidationException(ErrorMessages.NO_CAB_FOUND_BY_ID);
		}
		
	}
	
	public List<VehicleDetail> getAllAvailableVehicles() {
		Session session = this.sessionFactory.getCurrentSession();	
		List<VehicleDetail>  vehicleDetailsList =				
				session.getNamedQuery("VehicleDetail.findAvailableVehicle").getResultList();		
		for(VehicleDetail vehicleDetail : vehicleDetailsList) {
			fetchLazyInitialisation(vehicleDetail);
		}		
		return vehicleDetailsList;		
	}
	
	
	private void fetchLazyInitialisation(VehicleDetail vehicleDetail) {
		vehicleDetail.getCabType();
		vehicleDetail.getModel();		
		if(vehicleDetail.getCabPermits() != null)
			vehicleDetail.getCabPermits().size();
	}
}
