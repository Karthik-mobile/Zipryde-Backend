package com.trivecta.zipryde.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import org.apache.http.NameValuePair;

@Component
public class TwilioSMS {
	
	public static final String ACCOUNT_SID = "ACf55fc14a8dc715e9a8268d120cce66a3";
    public static final String AUTH_TOKEN = "b1bcafe871461cba7d4df45f642c87fe";
    public static final String TWILIO_NUMBER = "+12149976818";
        
	public static String[] sendSMS(String to, String message, String mediaUrl) throws TwilioRestException {
		String[] sms = new String[3];
		 
		try {
	        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);	 
	        // Build a filter for the MessageList
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("Body", message));
	        params.add(new BasicNameValuePair("To", to)); //Add real number here
	        params.add(new BasicNameValuePair("From", TWILIO_NUMBER));
	        //params.add(new BasicNameValuePair("MediaUrl", mediaUrl));	        
	        MessageFactory messageFactory = client.getAccount().getMessageFactory();
	        Message twilioMessage = messageFactory.create(params);
	      
	        System.out.println(twilioMessage.getSid());
	        System.out.println(twilioMessage.getStatus());
	        System.out.println(twilioMessage.getErrorCode());
	        
	        //Check Status
	        Message statusMessage = client.getAccount().getMessage(twilioMessage.getSid());
	        
	        sms[0] = twilioMessage.getSid();
	        sms[1] = statusMessage.getStatus();
	        sms[2] = statusMessage.getErrorCode().toString();	
	        
	        return sms;
	    } 
	    catch (TwilioRestException e) {
	    	throw new TwilioRestException(e.getMessage(), e.getErrorCode());
	    }
	}
	
	/*public static void main(String args[]) {
		try {
			String[] sid = sendSMS("+16605287309","Hi, Welcome",null);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}