package com.trivecta.zipryde.view.data.transformer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.framework.helper.ValidationUtil;
import com.trivecta.zipryde.model.entity.CabPermit;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.VehicleDetail;
import com.trivecta.zipryde.model.service.VehicleService;
import com.trivecta.zipryde.view.request.CabRequest;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.response.CabPermitResponse;
import com.trivecta.zipryde.view.response.CabResponse;

@Component
public class VehicleTransformer {

	@Autowired
	VehicleService vehicleService;
	
	public CabResponse saveVehicle(CabRequest cabRequest) throws ParseException, MandatoryValidationException, UserValidationException {
		
		StringBuffer errorMsg = new StringBuffer();
		
		VehicleDetail vehicleDetail = new VehicleDetail();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		if(cabRequest.getLicensePlateNo() == null) {
			errorMsg.append(ErrorMessages.LICENSE_PLATE_REQUIRED);
		}		
		if(cabRequest.getVin() == null) {
			errorMsg.append(ErrorMessages.VIN_REQUIRED);
		}
		
		if(cabRequest.getSeatingCapacity() == null || cabRequest.getSeatingCapacity().intValue() == 0) {
			errorMsg.append(ErrorMessages.SEATING_CAPACITY_MANDATORY);
		}
				
		if(ValidationUtil.isValidString(errorMsg.toString())){
			// Throw error
			throw new MandatoryValidationException(errorMsg.toString());
		}
		else {
		
			if(cabRequest.getCabId() != null && cabRequest.getCabId().intValue() != 0) {
				vehicleDetail.setId(cabRequest.getCabId().intValue());
			}
			else {
				vehicleDetail.setId(0);
			}
			
			CabType cabType = new CabType();
			cabType.setId(cabRequest.getCabTypeId().intValue());		
			vehicleDetail.setCabType(cabType);
			
			Model model  = new Model();
			model.setId(cabRequest.getModelId().intValue());
			vehicleDetail.setModel(model);
			
			if(cabRequest.getStatus() != null) {
				Status status = new Status();
				status.setStatus(cabRequest.getStatus());
				vehicleDetail.setStatus(status);
			}
			
			if(cabRequest.getEnableCab() != null) {
				vehicleDetail.setIsEnable(cabRequest.getEnableCab().intValue());
			}
			else {
				vehicleDetail.setIsEnable(0);
			}
			
			vehicleDetail.setSeatingCapacity(cabRequest.getSeatingCapacity().intValue());
			vehicleDetail.setManufacturedYear(dateFormat.parse(cabRequest.getYearOfManufactured()));
			vehicleDetail.setLicensePlateNo(cabRequest.getLicensePlateNo());
			vehicleDetail.setColor(cabRequest.getColor());
			vehicleDetail.setAccessories(cabRequest.getAccessories());
			vehicleDetail.setInsuranceCompany(cabRequest.getInsuranceCompanyName());
			vehicleDetail.setInsuranceNo(cabRequest.getInsuranceNumber());			
			vehicleDetail.setInsuranceValidUntil(dateFormat.parse(cabRequest.getInsuranceValidUntil()));
					
			CabPermit cabPermit = new CabPermit();
			if(cabRequest.getCabPermitRequest().getCabPermitId() != null) {
				cabPermit.setId(cabRequest.getCabPermitRequest().getCabPermitId().intValue());
			}
			cabPermit.setPermitNumber(cabRequest.getCabPermitRequest().getCabPermitNumber());
			cabPermit.setPermitValidUntil(dateFormat.parse(cabRequest.getCabPermitRequest().getCabPermitValidUntil()));
			vehicleDetail.setVin(cabRequest.getVin());
			
			VehicleDetail newVehicleDetail = vehicleService.saveVehicle(vehicleDetail, cabPermit);
			
			return setCabResponseFromVehicleDetail(newVehicleDetail);
		}
	}
	
	public List<CabResponse> getAllVehicle() {
		 List<CabResponse> cabList = new ArrayList<CabResponse>();
		 
		 List<VehicleDetail> vehicleList = vehicleService.getAllVehicles();
		 
		 if(vehicleList != null && vehicleList.size() > 0) {
			 for(VehicleDetail vehicleDetail : vehicleList) {
				 cabList.add(setCabResponseFromVehicleDetail(vehicleDetail));
			 }
		 }
		 return cabList;
	}
	
	public List<CabResponse> getAllAvailableVehicles() {
		 List<CabResponse> cabList = new ArrayList<CabResponse>();
		 
		 List<VehicleDetail> vehicleList = vehicleService.getAllAvailableVehicles();
		 
		 if(vehicleList != null && vehicleList.size() > 0) {
			 for(VehicleDetail vehicleDetail : vehicleList) {
				 cabList.add(setCabResponseFromVehicleDetail(vehicleDetail));
			 }
		 }
		 return cabList;
	}
	
	public CabResponse getVehicleByVehicleId(CommonRequest commonRequest) throws UserValidationException, MandatoryValidationException {
		if(commonRequest.getCabId() == null) {
			throw new MandatoryValidationException(ErrorMessages.VEHICLE_ID_REQUIRED);
		}
		else {
			VehicleDetail vehicleDetail = vehicleService.getVehicleDetailById(commonRequest.getCabId().intValue());
			return setCabResponseFromVehicleDetail(vehicleDetail);
		}	
	}
	
	private CabResponse setCabResponseFromVehicleDetail(VehicleDetail vehicleDetail) {
		CabResponse cabResponse = new CabResponse();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		
		cabResponse.setAccessories(vehicleDetail.getAccessories());
		cabResponse.setCabId(vehicleDetail.getId());

		if(vehicleDetail.getCabType() != null) {
			cabResponse.setCabTypeId(vehicleDetail.getCabType().getId());
			cabResponse.setCabType(vehicleDetail.getCabType().getType());
		}
		
		if(vehicleDetail.getCabPermits() != null && vehicleDetail.getCabPermits().size() >0) {
			CabPermitResponse cabPermitResponse = new CabPermitResponse();
			cabPermitResponse.setCabPermitId(vehicleDetail.getCabPermits().get(0).getId());
			cabPermitResponse.setCabPermitNumber(vehicleDetail.getCabPermits().get(0).getPermitNumber());
			cabPermitResponse.setCabPermitValidUntil(
					dateFormat.format(vehicleDetail.getCabPermits().get(0).getPermitValidUntil()));
			cabResponse.setCabPermitResponse(cabPermitResponse);
		}
		
		
		cabResponse.setColor(vehicleDetail.getColor());
		cabResponse.setEnableCab(vehicleDetail.getIsEnable());
		cabResponse.setInsuranceCompanyName(vehicleDetail.getInsuranceCompany());
		cabResponse.setInsuranceNumber(vehicleDetail.getInsuranceNo());
		cabResponse.setInsuranceValidUntil(dateFormat.format(vehicleDetail.getInsuranceValidUntil()));
		cabResponse.setLicensePlateNo(vehicleDetail.getLicensePlateNo());
		
		if(vehicleDetail.getModel()!= null) {
			cabResponse.setMakeId(vehicleDetail.getModel().getMake().getId());
			cabResponse.setModelId(vehicleDetail.getModel().getId());
		}
		
		cabResponse.setSeatingCapacity(vehicleDetail.getSeatingCapacity());
		cabResponse.setVin(vehicleDetail.getVin());
		cabResponse.setYearOfManufactured(dateFormat.format(vehicleDetail.getManufacturedYear()));
		cabResponse.setStatus(vehicleDetail.getStatus().getStatus());
		cabResponse.setComments(vehicleDetail.getComments());
		return cabResponse;

	}
}
