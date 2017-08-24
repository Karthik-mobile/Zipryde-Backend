package com.trivecta.zipryde.model.service;

import org.springframework.stereotype.Service;

import com.trivecta.zipryde.framework.exception.SessionExpiredException;


public interface HeaderValidationService {

	public  Boolean validateHeaderAccessToken(String accessToken) throws SessionExpiredException;

}
