package com.trivecta.zipryde.model.dao;

import java.util.List;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;

public interface ZiprydeConfigurationDAO {
	public ZiprydeConfiguration getZiprydeConfigurationByType(String type);
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration)throws UserValidationException ;
	public List<ZiprydeConfiguration> getAllZiprydeConfigurations();
}
