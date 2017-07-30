package com.trivecta.zipryde.model.dao;

import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;

public interface ZiprydeConfigurationDAO {
	public ZiprydeConfiguration getZiprydeConfigurationByType(String type);
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration);
}
