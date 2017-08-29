package com.trivecta.zipryde.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.dao.AdminDAO;
import com.trivecta.zipryde.model.dao.PricingDAO;
import com.trivecta.zipryde.model.dao.ZiprydeConfigurationDAO;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;
import com.trivecta.zipryde.model.entity.UserType;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.trivecta.zipryde.model.entity.ZiprydeMstr;

@Service("ziprydeConfigService")
public class ZiprydeConfigServiceImpl implements ZiprydeConfigService {

	@Autowired
	ZiprydeConfigurationDAO ziprydeConfigurationDAO;
	
	@Transactional
	public ZiprydeConfiguration getZiprydeConfigurationByType(String type) {
		return ziprydeConfigurationDAO.getZiprydeConfigurationByType(type);
	}
	
	@Transactional
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration) throws UserValidationException {
		return ziprydeConfigurationDAO.saveZiprydeConfiguration(ziprydeConfiguration);
	}
	
	@Transactional
	public List<ZiprydeConfiguration> getAllZiprydeConfigurations() {
		return ziprydeConfigurationDAO.getAllZiprydeConfigurations();
	}
	
	@Transactional
	public ZiprydeMstr getZiprydeMstrByType(String type){
		return ziprydeConfigurationDAO.getZiprydeMstrByType(type);
	}
	
	@Transactional
	public String getZiprydeMstrValueByType(String type){
		return ziprydeConfigurationDAO.getZiprydeMstrValueByType(type);
	}
}
