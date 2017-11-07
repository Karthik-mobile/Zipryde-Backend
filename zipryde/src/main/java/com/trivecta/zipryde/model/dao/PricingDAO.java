package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;

public interface PricingDAO {

	public List<PricingMstr> getAllPricingMstr() ;
	//public BigDecimal calculatePricingByTypeAndDistance(int NoOfMiles, int cabTypeId);
	public BigDecimal calculatePricingByTypeDistanceAndPerson(BigDecimal NoOfMiles, int cabTypeId,int noOfPerson)  throws UserValidationException;
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeDistanceAndPerson(BigDecimal NoOfMiles, int cabTypeId,int noOfPerson)  throws UserValidationException;
	public List<PricingType> getAllEnabledPricingType();
	public List<PricingMstr> getAllPricingMstrByCabType(int cabTypeId) ;
	public List<PricingMstr> savePricingMstrs(List<PricingMstr> pricingMstrList);
	public PricingMstr savePricingMstr(PricingMstr pricingMstr);
}
