package com.trivecta.zipryde.model.dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.framework.helper.Notification;
import com.trivecta.zipryde.model.dao.ZiprydeConfigurationDAO;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.trivecta.zipryde.model.service.AdminService;

@Repository
public class FCMNotificationDAOImpl implements FCMNotificationDAO{
	
	@Autowired
	ZiprydeConfigurationDAO ziprydeConfigurationDAO;

	// userDeviceIdKey is the device id you will query from your database     
	
	@Async
	public void pushNotification(String userDeviceToken,String title,Notification notification,boolean isDriver) throws IOException {
		sendFCMNotification(userDeviceToken,title,notification,isDriver);
	}
	
	@Async
	private void sendFCMNotification(String userDeviceToken,String title,Notification notification,boolean isDriver) throws IOException {
		//String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
		//String FMCurl = API_URL_FCM;  
		String authKey = null;
		String FMCurl = null;
		String type  = null;
		if(isDriver) {
			type = "NOTIFICATION-DRIVER";
		}
		else {
			type = "NOTIFICATION-RIDER";
		}
		ZiprydeConfiguration ziprydeConfiguration = ziprydeConfigurationDAO.getZiprydeConfigurationByType(type);
		if(ziprydeConfiguration != null) {
			authKey = ziprydeConfiguration.getAccessKey();
			FMCurl = ziprydeConfiguration.getUrl();
		}
		ObjectMapper mapperObj = new ObjectMapper();
		String notificationJsonStr = mapperObj.writeValueAsString(notification);

		try {
			URL url = new URL(FMCurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization","key="+authKey);
			conn.setRequestProperty("Content-Type","application/json");

			JSONObject json = new JSONObject();
			json.put("to",userDeviceToken.trim());
			JSONObject info = new JSONObject();
			info.put("title", title);   // Notification title
			info.put("body", notificationJsonStr); // Notification body
			json.put("notification", info);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			conn.getInputStream();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

}
