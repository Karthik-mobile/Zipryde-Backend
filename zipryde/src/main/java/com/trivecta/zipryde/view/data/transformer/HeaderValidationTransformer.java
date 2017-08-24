package com.trivecta.zipryde.view.data.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trivecta.zipryde.framework.exception.SessionExpiredException;
import com.trivecta.zipryde.model.service.HeaderValidationService;
import com.trivecta.zipryde.utility.Utility;

@Component
public class HeaderValidationTransformer {
	
	@Autowired
	HeaderValidationService headerValidationService;
	
	public Boolean validHeaderAccessToken(String accessToken) throws SessionExpiredException {
		return headerValidationService.validateHeaderAccessToken(Utility.encryptWithMD5(accessToken));
	}	
}
