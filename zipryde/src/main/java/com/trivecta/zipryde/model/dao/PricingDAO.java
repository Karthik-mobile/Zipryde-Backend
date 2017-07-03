package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trivecta.zipryde.model.entity.PricingMstr;

public interface PricingDAO {

	public List<PricingMstr> getAllPricingMstr() ;
	//public BigDecimal calculatePricingByTypeAndDistance(int NoOfMiles, int cabTypeId);
	public BigDecimal calculatePricingByTypeDistanceAndPerson(int NoOfMiles, int cabTypeId,int noOfPerson) ;
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeDistanceAndPerson(int NoOfMiles, int cabTypeId,int noOfPerson);
}
