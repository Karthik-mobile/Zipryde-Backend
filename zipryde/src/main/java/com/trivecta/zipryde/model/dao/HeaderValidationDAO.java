package com.trivecta.zipryde.model.dao;

import com.trivecta.zipryde.framework.exception.SessionExpiredException;

public interface HeaderValidationDAO {

	public  Boolean validateHeaderAccessToken(String accessToken) throws SessionExpiredException;
}
