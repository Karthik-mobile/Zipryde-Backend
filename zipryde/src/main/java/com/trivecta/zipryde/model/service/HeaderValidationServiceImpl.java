package com.trivecta.zipryde.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trivecta.zipryde.framework.exception.SessionExpiredException;
import com.trivecta.zipryde.model.dao.HeaderValidationDAO;

@Service
public class HeaderValidationServiceImpl implements HeaderValidationService{

	@Autowired
	HeaderValidationDAO headerValidationDAO;
	
	@Transactional
	public  Boolean validateHeaderAccessToken(String accessToken) throws SessionExpiredException {
		return headerValidationDAO.validateHeaderAccessToken(accessToken);
	}
}
