package com.trivecta.zipryde.model.dao;

import java.io.IOException;
import java.util.List;

import com.trivecta.zipryde.framework.helper.Notification;
import com.trivecta.zipryde.model.entity.Booking;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.User;
import com.trivecta.zipryde.model.entity.UserType;

public interface FCMNotificationDAO {

	public void sendBookingRequestNotification(Booking booking,User user);
	
	public void sendBookingConfirmationNotification(Booking booking);
	
	public void sendBookingStatusNotification(Booking booking,boolean toDriver);
	
	//public void sendDriverOnlineStatusNotification();

}
