package com.trivecta.zipryde.model.dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_CONFIG_TYPE;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_MESSAGE;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_TITLE;
import com.trivecta.zipryde.constants.ZipRydeConstants.NOTIFICATION_TYPE;
import com.trivecta.zipryde.framework.helper.Notification;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;

@Repository
public class FCMNotificationDAOImpl implements FCMNotificationDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ZiprydeConfigurationDAO ziprydeConfigurationDAO;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Async
	public void sendBookingRequestNotification(Booking booking,User user) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBody(NOTIFICATION_MESSAGE.BOOKING_DRIVER_REQUEST+booking.getId());
			notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_DRIVER_REQUEST);
			notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_DRIVER_REQUEST);
			notifcation.setBookingId(booking.getId());
			notifcation.setDriver(true);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_DRIVER);
			sendFCMNotification(user.getDeviceToken(),notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block			
		}
	}
	
	@Async
	public void sendBookingConfirmationNotification(Booking booking) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBody(NOTIFICATION_MESSAGE.BOOKING_USER_CONFIRMATION+booking.getId());
			notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_USER_CONFIRMATION);
			notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_USER_CONFIRMATION);
			notifcation.setBookingId(booking.getId());
			notifcation.setDriver(false);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_RIDER);
			sendFCMNotification(booking.getRider().getDeviceToken(),notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block		
		}
	}
	
	@Async
	public void sendBookingStatusNotification(Booking booking,boolean toDriver) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBookingId(booking.getId());
			notifcation.setDriver(false);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_RIDER);
			notifcation.setBody(NOTIFICATION_MESSAGE.BOOKING_STATUS_CHANGE+booking.getId());
			
			String deviceToken = booking.getRider().getDeviceToken();
			if(ZipRydeConstants.STATUS.ON_SITE.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_DRIVER_ONSITE);
				notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_DRIVER_ONSITE);
			}
			else if(ZipRydeConstants.STATUS.ON_TRIP.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_DRIVER_ONTRIP);
				notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_DRIVER_ONTRIP);
			}
			else if(ZipRydeConstants.STATUS.COMPLETED.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_DRIVER_COMPLETED);
				notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_DRIVER_COMPLETED);
			}
			else if(ZipRydeConstants.STATUS.CANCELLED.equalsIgnoreCase(booking.getBookingStatus().getStatus())) {
				notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_CANCELLED);
				notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_CANCELLED);
				notifcation.setBody(NOTIFICATION_MESSAGE.BOOKING_CANCELLED);
				
				if(toDriver){
					notifcation.setDriver(true);
					notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_DRIVER);
					deviceToken = booking.getDriver().getDeviceToken();
				}
			}
			sendFCMNotification(deviceToken,notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block		
		}
	}
	
	@Async
	public void sendPaymentSuccessNotification(Booking booking) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBody(NOTIFICATION_MESSAGE.BOOKING_PAYMENT_SUCCESS+booking.getId());
			notifcation.setTitle(NOTIFICATION_TITLE.BOOKING_PAYMENT_SUCCESS);
			notifcation.setNotificationType(NOTIFICATION_TYPE.BOOKING_PAYMENT_SUCCESS);
			notifcation.setBookingId(booking.getId());
			notifcation.setDriver(false);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_RIDER);
			sendFCMNotification(booking.getRider().getDeviceToken(),notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block		
		}
	}
	
	@Async
	public void sendCommissionPendingNotification(String driverDeviceToken,Integer commissionId) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBody(NOTIFICATION_MESSAGE.COMMISSION_PENDING);
			notifcation.setTitle(NOTIFICATION_TITLE.COMMISSION_PENDING);
			notifcation.setNotificationType(NOTIFICATION_TYPE.COMMISSION_PENDING);
			notifcation.setDriver(true);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_DRIVER);
			notifcation.setCommissionId(commissionId);
			sendFCMNotification(driverDeviceToken,notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block		
		}
	}
	
	@Async
	public void sendCommissionPaidNotification(String driverDeviceToken,Integer commissionId) {
		try {
			Notification notifcation = new Notification();
			notifcation.setBody(NOTIFICATION_MESSAGE.COMMISSION_PAID);
			notifcation.setTitle(NOTIFICATION_TITLE.COMMISSION_PAID);
			notifcation.setNotificationType(NOTIFICATION_TYPE.COMMISSION_PAID);
			notifcation.setDriver(true);
			notifcation.setZiprydeConfigType(NOTIFICATION_CONFIG_TYPE.NOTIFICATION_DRIVER);
			notifcation.setCommissionId(commissionId);
			sendFCMNotification(driverDeviceToken,notifcation);
		} catch (IOException e) {
			// TODO Auto-generated catch block		
		}
	}
	
	private void sendFCMNotification(String userDeviceToken,Notification notification) throws IOException {
		String authKey = null;
		String FMCurl = "https://fcm.googleapis.com/fcm/send";
		/*if(isDriver) {
			type = "NOTIFICATION_DRIVER";
		}
		else {
			type = NOTIFICATION_CONFIG_TYPE.NOTIFICATION_RIDER;
		}*/
		ZiprydeConfiguration ziprydeConfiguration = ziprydeConfigurationDAO.getZiprydeConfigurationByType(notification.getZiprydeConfigType());
		if(ziprydeConfiguration != null) {
			authKey = ziprydeConfiguration.getAccessKey();
			FMCurl = ziprydeConfiguration.getUrl();
		}
		/*ObjectMapper mapperObj = new ObjectMapper();
		String notificationJsonStr = mapperObj.writeValueAsString(notification);
*/
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
			if(notification.isDriver()) {
				json.put("time_to_live", 180);//in seconds
			}			
			JSONObject info = new JSONObject();
			info.put("title", notification.getTitle());   // Notification title
			info.put("body", notification.getBody()); // Notification body			
			info.put("click_action", "OPEN_ACTIVITY_1");			
			json.put("notification", info);
			
			JSONObject data = new JSONObject();
			data.put("title", notification.getTitle());   // Notification title
			data.put("body", notification.getBody());
			data.put("notificationType", notification.getNotificationType());
			if(notification.getBookingId() != null)
				data.put("bookingId", notification.getBookingId());
			if(notification.getCommissionId() != null)
				data.put("commissionId", notification.getCommissionId());
			json.put("data", data);
			
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
