package com.trivecta.zipryde.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;
import com.trivecta.zipryde.model.entity.UserType;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;

public interface AdminService {

	public List<Make> getAllMake();
	
	public List<Make> getAllEnabledMake();
	
	public List<Model> getAllModelByMakeId(int makeId);
	
	public List<CabType> getAllCabTypes();
	
	public List<UserType> getAllUserTypes();
	
	public List<Nyop> getAllNyopList();
	
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeDistanceAndPerson(BigDecimal NoOfMiles, int cabTypeId,int noOfPerson) throws UserValidationException;
	
	public List<PricingType> getAllEnabledPricingType();
	
	public List<PricingMstr> getAllPricingMstrByCabType(int cabTypeId);
	
	public List<PricingMstr> savePricingMstrs(List<PricingMstr> pricingMstrList);
	
	public List<PricingMstr> getAllPricingMstr();
	
	public PricingMstr savePricingMstr(PricingMstr pricingMstr);
	
	public Make saveMake(Make make) throws UserValidationException;
	
	public Model saveModel(Model model) throws UserValidationException;
	
	public Make getMakeByMakeId(Integer makeId);
	
	public Model getModelByModelId(Integer modelId);
	
	public List<Model> getAllModel();
}
