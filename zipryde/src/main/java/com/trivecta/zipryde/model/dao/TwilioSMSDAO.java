package com.trivecta.zipryde.model.dao;

import com.twilio.sdk.TwilioRestException;

public interface TwilioSMSDAO {
	public void sendSMS(String to, String message) throws TwilioRestException;
}
