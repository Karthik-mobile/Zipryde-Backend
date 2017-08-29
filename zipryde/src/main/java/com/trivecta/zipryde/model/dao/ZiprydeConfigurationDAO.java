package com.trivecta.zipryde.model.dao;

import java.util.List;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.trivecta.zipryde.model.entity.ZiprydeMstr;

public interface ZiprydeConfigurationDAO {
	public ZiprydeConfiguration getZiprydeConfigurationByType(String type);
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration)throws UserValidationException ;
	public List<ZiprydeConfiguration> getAllZiprydeConfigurations();
	public ZiprydeMstr getZiprydeMstrByType(String type);
	public String  getZiprydeMstrValueByType(String type);
}
