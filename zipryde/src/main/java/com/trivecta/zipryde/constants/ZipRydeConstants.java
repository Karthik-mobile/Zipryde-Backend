package com.trivecta.zipryde.constants;

public class ZipRydeConstants {

	public static String OTP_VERIFIED = "VERIFIED";
	public static String OTP_EXPIRED = "EXPIRED";
	
	public interface USERTYPE {
		static String DRIVER = "DRIVER";
		static String RIDER = "RIDER";
		static String CAB_OWNER = "CAB_OWNER";
		static String WEB_ADMIN ="WEB_ADMIN";
	}
	
	public interface STATUS {
		//DRIVER PROFILE / CAB STATUS
		
		static String APPROVED = "APPROVED";
		static String REJECTED = "REJECTED";
		static String ASSIGNED = "ASSIGNED";
		static String UNASSIGNED = "UNASSIGNED";
		
		static String REQUESTED = "REQUESTED";
		static String ON_TRIP =  "ON_TRIP";
		static String SCHEDULED = "SCHEDULED";
		static String COMPLETED = "COMPLETED";
		static String CANCELLED = "CANCELLED";
		static String ACCEPTED = "ACCEPTED";
	}
	
	
	public interface PRICINGTYPE {
		static String STANDARD = "STANDARD";
		static String PERSON = "PERSON";
		static String DISTANCE = "DISTANCE";
	}
	
	public interface PAYMENT {
		static String CASH = "CASH";
		static String PAID = "PAID";
		static String PENDING = "PENDING";
	}
	
	public interface CONFIGURATION {
		static String FCM = "FCM";
	}
		
	public interface NOTIFICATION_TITLE {
		static String BOOKING_DRIVER_REQUEST = "ZIPRYDE DRIVER REQUEST";
		static String BOOKING_USER_CONFIRMATION = "ZIPRYDE USER CONFIRMATION";
		static String BOOKING_DRIVER_ONTRIP = "ZIPRYDE BOOKING ON TRIP";
		static String BOOKING_DRIVER_COMPLETED = "ZIPRYDE BOOKING COMPLETED";
		static String BOOKING_PAYMENT_SUCCESS = "ZIPRYDE PAYMENT SUCCESS";
		static String BOOKING_CANCELLED = "ZIPRYDE BOOKING CANCELLED";
	}
	
	public interface NOTIFICATION_MESSAGE {
		static String BOOKING_DRIVER_REQUEST = "You have a Booking Request with Id ";
		static String BOOKING_USER_CONFIRMATION = "Your Booking has been Confirmed. Your Booking Id is ";
		static String BOOKING_STATUS_CHANGE = "Booking status changed for Id is ";
		static String BOOKING_PAYMENT_SUCCESS="Payment Success for Booking Id ";
		static String BOOKING_CANCELLED = "Your Booking has been Cancelled";
	}
}
