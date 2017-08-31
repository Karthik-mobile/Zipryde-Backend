package com.trivecta.zipryde.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_CONFIG_TYPE;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Repository
public class TwilioSMSDAOImpl implements TwilioSMSDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ZiprydeConfigurationDAO ziprydeConfigurationDAO;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Async
	public void sendSMS(String to, String message) throws TwilioRestException {
	/*	String ACCOUNT_SID = "ACf55fc14a8dc715e9a8268d120cce66a3";
		String AUTH_TOKEN = "b1bcafe871461cba7d4df45f642c87fe";
		String TWILIO_NUMBER = "+12149976818";*/

		String ACCOUNT_SID = null;
		String AUTH_TOKEN = null;
		String TWILIO_NUMBER = null;
		
		ZiprydeConfiguration ziprydeConfiguration = ziprydeConfigurationDAO
				.getZiprydeConfigurationByType(NOTIFICATION_CONFIG_TYPE.TWILIO_SMS);
		if (ziprydeConfiguration != null) {
			ACCOUNT_SID = ziprydeConfiguration.getAccoutSID();
			AUTH_TOKEN = ziprydeConfiguration.getAccessKey();
			TWILIO_NUMBER = ziprydeConfiguration.getTwilioNo();
		}

		try {
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
			// Build a filter for the MessageList
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Body", message));
			params.add(new BasicNameValuePair("To", to)); // Add real number															// here
			params.add(new BasicNameValuePair("From", TWILIO_NUMBER));
			// params.add(new BasicNameValuePair("MediaUrl", mediaUrl));
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message twilioMessage = messageFactory.create(params);
			// Check Status
			/*
			 * Message statusMessage =
			 * client.getAccount().getMessage(twilioMessage.getSid()); sms[0] =
			 * twilioMessage.getSid(); sms[1] = statusMessage.getStatus();
			 * sms[2] = statusMessage.getErrorCode().toString();
			 */

			// return sms;
		} catch (TwilioRestException e) {
			throw new TwilioRestException(e.getMessage(), e.getErrorCode());
		}
	}
}
