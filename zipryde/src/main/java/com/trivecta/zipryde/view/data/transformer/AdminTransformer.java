package com.trivecta.zipryde.view.data.transformer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_CONFIG_TYPE;
import com.trivecta.zipryde.framework.exception.MandatoryValidationException;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.trivecta.zipryde.model.service.AdminService;
import com.trivecta.zipryde.view.request.CommonRequest;
import com.trivecta.zipryde.view.request.ConfigurationRequest;
import com.trivecta.zipryde.view.request.PricingMstrRequest;
import com.trivecta.zipryde.view.response.CabTypeResponse;
import com.trivecta.zipryde.view.response.ConfigurationResponse;
import com.trivecta.zipryde.view.response.MakeModelResponse;
import com.trivecta.zipryde.view.response.NYOPResponse;
import com.trivecta.zipryde.view.response.PricingMstrResponse;
import com.trivecta.zipryde.view.response.PricingTypeResponse;

@Component
public class AdminTransformer {

	@Autowired
	AdminService adminService;
	
	public List<CabTypeResponse> getAllCabTypes() {
		List<CabTypeResponse> cabTypeResponseList = 
				new ArrayList<CabTypeResponse>();
		
		List<CabType> cabTypes = adminService.getAllCabTypes();
		
		if(cabTypes != null && cabTypes.size() > 0) {
			for(CabType cabType : cabTypes) {
				CabTypeResponse cabTypeResponse = 
						new CabTypeResponse();
				
				cabTypeResponse.setCabTypeId(cabType.getId());
				cabTypeResponse.setEngineType(cabType.getEngineType());
				cabTypeResponse.setIsEnable(cabType.getIsEnable());
				cabTypeResponse.setLevel(cabType.getLevel());
				cabTypeResponse.setType(cabType.getType());
				
				cabTypeResponse.setSeatingCapacity(cabType.getSeatingCapacity());
				
				if(cabType.getPricingMstrs() != null && cabType.getPricingMstrs().size() > 0 ) {
					if(cabType.getPricingMstrs().get(0).getPricePerUnit()  != null) {
						Double pricePerUnit = 
								cabType.getPricingMstrs().get(0).getPricePerUnit().doubleValue();				
						cabTypeResponse.setPricePerUnit(pricePerUnit);
					}
				}
				
				cabTypeResponseList.add(cabTypeResponse);
			}
		}
		return cabTypeResponseList;
	}
	
	public List<MakeModelResponse> getAllMake() {
		List<MakeModelResponse> makeModelResponseList = 
				new ArrayList<MakeModelResponse>();
		
		List<Make> makeList = adminService.getAllMake();
		
		if(makeList != null && makeList.size() > 0) {
			for(Make make : makeList) {
				MakeModelResponse makeModelResponse = 
						new MakeModelResponse();
				makeModelResponse.setMake(make.getMake());
				makeModelResponse.setMakeModelId(make.getId());
				
				makeModelResponseList.add(makeModelResponse);
			}
		}
		return makeModelResponseList;
	}
	
	public List<MakeModelResponse> getAllModelByMakeId(CommonRequest commonRequest) {
		List<MakeModelResponse> makeModelResponseList = 
				new ArrayList<MakeModelResponse>();
		
		List<Model> modelList = adminService.getAllModelByMakeId(commonRequest.getMakeId().intValue());
		
		if(modelList != null && modelList.size() > 0) {
			for(Model model : modelList) {
				MakeModelResponse makeModelResponse = 
						new MakeModelResponse();
				makeModelResponse.setModel(model.getModel());
				makeModelResponse.setMakeModelId(model.getId());
				
				makeModelResponseList.add(makeModelResponse);
			}
		}
		return makeModelResponseList;
	}
	
	public  List<NYOPResponse> getAllNYOPList() {
		List<NYOPResponse> nyopRespList = new ArrayList<NYOPResponse>();
		
		List<Nyop> nyopList = adminService.getAllNyopList();
		
		if(nyopList != null && nyopList.size() > 0){
			for(Nyop nyop : nyopList) {
				NYOPResponse nyopResponse = new NYOPResponse();
				nyopResponse.setPercentage(nyop.getPercentage());
				nyopRespList.add(nyopResponse);
			}
		}
		return nyopRespList;
	}
	
	public List<NYOPResponse> getAllNYOPByCabTypeDistAndNoOfPassenger(CommonRequest commonRequest) throws MandatoryValidationException{
		
		if(commonRequest.getDistanceInMiles() != null && commonRequest.getCabTypeId() != null && 
				commonRequest.getNoOfPassengers() != null) {
			List<NYOPResponse> nyopResponseList = 
					new ArrayList<NYOPResponse>();
			
			
			Map<Integer,BigDecimal> nyopPricingList =
					adminService.getAllNYOPByCabTypeDistanceAndPerson(
							commonRequest.getDistanceInMiles().intValue(), 
							commonRequest.getCabTypeId().intValue() ,
							commonRequest.getNoOfPassengers().intValue());
			
			
			Iterator<Map.Entry<Integer, BigDecimal>> entries = nyopPricingList.entrySet().iterator();
			while (entries.hasNext()) {
			    Map.Entry<Integer, BigDecimal> entry = entries.next();
			    
			    NYOPResponse nyopResponse = new NYOPResponse();
			    nyopResponse.setPercentage(entry.getKey());
			    nyopResponse.setPrice(String.valueOf(entry.getValue()));
			    nyopResponse.setStatus(true);
			    nyopResponse.setErrorMessage("");
			    nyopResponseList.add(nyopResponse);
			}
			
			return nyopResponseList;
		}
		else {
			throw new MandatoryValidationException(ErrorMessages.DISTANCE_CAB_TYPE_PERSON_MANDATORY);
		}	
	}
	
	public List<PricingTypeResponse> getAllEnabledPricingType() {
		List<PricingTypeResponse> pricingTypeResponseList = new ArrayList<PricingTypeResponse>();
		List<PricingType> pricingTypeList = adminService.getAllEnabledPricingType();
		if(pricingTypeList != null && pricingTypeList.size() > 0) {
			for(PricingType pricingType : pricingTypeList) {
				PricingTypeResponse pricingTypeResp = new PricingTypeResponse();
				pricingTypeResp.setPricingTypeId(pricingType.getId());
				pricingTypeResp.setPricingType(pricingType.getType());
				pricingTypeResponseList.add(pricingTypeResp);
			}
		}
		return pricingTypeResponseList;		
	}
	
	public List<PricingMstrResponse> getAllPricingMstrByCabType(CommonRequest commonRequest) throws MandatoryValidationException {
		if(commonRequest.getCabTypeId() == null) {
			throw new MandatoryValidationException(ErrorMessages.CAB_TYPE_REQUIRED);
		}
		List<PricingMstr> pricingMstrList = adminService.getAllPricingMstrByCabType(commonRequest.getCabTypeId().intValue());
		List<PricingMstrResponse> pricingMstrRespList = setPricingMstrResponseFromList(pricingMstrList);
		return pricingMstrRespList;				
	}
	
	public List<PricingMstrResponse> getAllPricingMstr() throws MandatoryValidationException {
		
		List<PricingMstr> pricingMstrList = adminService.getAllPricingMstr();
		List<PricingMstrResponse> pricingMstrRespList = setPricingMstrResponseFromList(pricingMstrList);
		return pricingMstrRespList;				
	}
	
	public List<PricingMstrResponse> savePricingMstrs(List<PricingMstrRequest> pricingMstrReqList) {
		List<PricingMstrResponse> pricingMstrRespList = new ArrayList<PricingMstrResponse>();		
		List<PricingMstr> pricingMstrs = setPricingMstrListFromRequest(pricingMstrReqList);
		List<PricingMstr> newPricingMstrList = adminService.savePricingMstrs(pricingMstrs);
		if(newPricingMstrList != null && newPricingMstrList.size() >0 ) {
			for(PricingMstr pricingMstr : newPricingMstrList) {			
				pricingMstrRespList.add(setPricingMstrResponse(pricingMstr));
			}
		}
		return pricingMstrRespList;
	}
	
	public PricingMstrResponse savePricingMstr(PricingMstrRequest pricingMstrReq) {
		PricingMstr pricingMstr = setPricingMstrFromRequest(pricingMstrReq);
		pricingMstr  = adminService.savePricingMstr(pricingMstr);
		return setPricingMstrResponse(pricingMstr);
	}
	
	public ConfigurationResponse getZiprydeConfigurationByType(ConfigurationRequest configurationRequest){
		ZiprydeConfiguration ziprydeConfiguration = adminService.getZiprydeConfigurationByType(configurationRequest.getType());
		return setConfigurationResponse(ziprydeConfiguration);
	}
	
	public ConfigurationResponse saveZiprydeConfiguration(ConfigurationRequest configurationRequest) throws UserValidationException {
		ZiprydeConfiguration ziprydeConfiguration = new ZiprydeConfiguration();
		ziprydeConfiguration.setType(configurationRequest.getType());
		ziprydeConfiguration.setAccessKey(configurationRequest.getAccessKey());
		ziprydeConfiguration.setUrl(configurationRequest.getUrl());
		ziprydeConfiguration.setId(configurationRequest.getId());
		ZiprydeConfiguration newZipRydeConfiguration = adminService.saveZiprydeConfiguration(ziprydeConfiguration);
		return setConfigurationResponse(newZipRydeConfiguration);		
	}
	
	public  List<ConfigurationResponse> getAllZiprydeConfigurations() {
		List<ConfigurationResponse> configurationResponseList = new ArrayList<ConfigurationResponse>();
		List<ZiprydeConfiguration> zipRydeConfigList = adminService.getAllZiprydeConfigurations();
		if(zipRydeConfigList != null && zipRydeConfigList.size() > 0 ) {
			for(ZiprydeConfiguration ziprydeConfig : zipRydeConfigList) {
				configurationResponseList.add(setConfigurationResponse(ziprydeConfig));
			}
		}
		return configurationResponseList;
	}
	
	private ConfigurationResponse setConfigurationResponse(ZiprydeConfiguration ziprydeConfiguration) {
		ConfigurationResponse configurationResponse= new ConfigurationResponse();
		configurationResponse.setId(ziprydeConfiguration.getId());
		configurationResponse.setUrl(ziprydeConfiguration.getUrl());
		configurationResponse.setType(ziprydeConfiguration.getType());
		configurationResponse.setAccessKey(ziprydeConfiguration.getAccessKey());
		return configurationResponse;	
	}
	
	private List<PricingMstrResponse>  setPricingMstrResponseFromList(List<PricingMstr> pricingMstrList) {
		List<PricingMstrResponse> pricingMstrRespList = new ArrayList<PricingMstrResponse>();		
		if(pricingMstrList != null && pricingMstrList.size() >0 ) {
			for(PricingMstr pricingMstr : pricingMstrList) {			
				pricingMstrRespList.add(setPricingMstrResponse(pricingMstr));
			}
		}
		return pricingMstrRespList;
	}
	
	private PricingMstrResponse setPricingMstrResponse(PricingMstr pricingMstr) {
		PricingMstrResponse pricingMstrResponse  = new PricingMstrResponse();
		
		CabTypeResponse cabTypeResponse = new CabTypeResponse();
		cabTypeResponse.setCabTypeId(pricingMstr.getCabType().getId());
		cabTypeResponse.setType(pricingMstr.getCabType().getType());
		pricingMstrResponse.setCabTypeResponse(cabTypeResponse);
		
		PricingTypeResponse pricingTypeResponse = new PricingTypeResponse();
		pricingTypeResponse.setPricingTypeId(pricingMstr.getPricingType().getId());
		pricingTypeResponse.setPricingType(pricingMstr.getPricingType().getType());
		
		pricingMstrResponse.setPricingTypeResponse(pricingTypeResponse);

		pricingMstrResponse.setIsEnable(pricingMstr.getIsEnable());
		pricingMstrResponse.setPricingMstrId(pricingMstr.getId());
		
		if(pricingMstr.getPrice() != null) {
			pricingMstrResponse.setPrice(pricingMstr.getPrice().doubleValue());
		}
		else {
			pricingMstrResponse.setPrice(pricingMstr.getPricePerUnit().doubleValue());
		}
		return pricingMstrResponse;
	}
	
	private List<PricingMstr> setPricingMstrListFromRequest(List<PricingMstrRequest> pricingMstrReqList) {
		List<PricingMstr> pricingMstrList = null;
		
		if(pricingMstrReqList != null && pricingMstrReqList.size() > 0) {
			pricingMstrList = new ArrayList<PricingMstr>();
			
			for(PricingMstrRequest pricingMstrReq  : pricingMstrReqList) {				
				pricingMstrList.add(setPricingMstrFromRequest(pricingMstrReq));				
			}
		}
		return pricingMstrList;
	}
	
	private PricingMstr setPricingMstrFromRequest(PricingMstrRequest pricingMstrReq) {
		PricingMstr pricingMstr = new PricingMstr();
		CabType cabType = new CabType();
		cabType.setId(pricingMstrReq.getCabTypeId().intValue());
		pricingMstr.setCabType(cabType);
		
		PricingType pricingType = new PricingType();
		pricingType.setId(pricingMstrReq.getPricingTypeId().intValue());
		pricingMstr.setPricingType(pricingType);
		
		if(pricingMstrReq.getPricingMstrId() != null) {
			pricingMstr.setId(pricingMstrReq.getPricingMstrId().intValue());
		}
		pricingMstr.setIsEnable(pricingMstrReq.getIsEnable().intValue());
		pricingMstr.setPrice(new BigDecimal(pricingMstrReq.getPrice()).setScale(2,RoundingMode.CEILING));
		return pricingMstr;
	}
	
	
}
