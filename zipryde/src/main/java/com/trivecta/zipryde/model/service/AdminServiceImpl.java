package com.trivecta.zipryde.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.model.dao.AdminDAO;
import com.trivecta.zipryde.model.dao.PricingDAO;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;
import com.trivecta.zipryde.model.entity.UserType;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDAO adminDAO;
	
	@Autowired
	PricingDAO pricingDAO;
	
	@Transactional
	public List<Make> getAllMake() {
		return adminDAO.getAllMake();
	}
	
	@Transactional
	public List<Model> getAllModelByMakeId(int makeId) {
		return adminDAO.getAllModelByMakeId(makeId);
	}

	@Transactional
	public List<CabType> getAllCabTypes() {
		return adminDAO.getAllCabTypes();
	}

	@Transactional
	public List<UserType> getAllUserTypes() {
		return adminDAO.getAllUserTypes();
	}

	@Transactional
	public List<Nyop> getAllNyopList() {
		return adminDAO.getAllNyopList();
	}
	
	@Transactional
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeDistanceAndPerson(int NoOfMiles, int cabTypeId,int noOfPerson) {
		return pricingDAO.getAllNYOPByCabTypeDistanceAndPerson(NoOfMiles, cabTypeId,noOfPerson);
	}
	
	@Transactional
	public List<PricingType> getAllEnabledPricingType() {
		return pricingDAO.getAllEnabledPricingType();
	}
	
	@Transactional
	public List<PricingMstr> getAllPricingMstrByCabType(int cabTypeId) {
		return pricingDAO.getAllPricingMstrByCabType(cabTypeId);
	}
	
	@Transactional
	public void savePricingMstrs(List<PricingMstr> pricingMstrList) {
		pricingDAO.savePricingMstrs(pricingMstrList);
	}
	
	@Transactional
	public List<PricingMstr> getAllPricingMstr() {
		return pricingDAO.getAllPricingMstr();
	}
}
