
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
		static String FUTURE_REQUESTED = "FUTURE_REQUESTED";
		static String ON_TRIP =  "ON_TRIP";
		static String ON_SITE =  "ON_SITE";
		static String SCHEDULED = "SCHEDULED";
		static String COMPLETED = "COMPLETED";
		static String CANCELLED = "CANCELLED";
		static String ACCEPTED = "ACCEPTED";
		static String PAID = "PAID";
	}
	
	
	public interface PRICINGTYPE {
		static String STANDARD = "STANDARD";
		static String PERSON = "PERSON";
		static String DISTANCE = "DISTANCE";
	}
	
	public interface PAYMENT {
		static String CASH = "CASH";
		static String PENDING = "PENDING";
	}
	
	public interface CONFIGURATION {
		static String FCM = "FCM";
	}
		
	public interface NOTIFICATION_TYPE {
		static String BOOKING_DRIVER_REQUEST = "BOOKING_DRIVER_REQUEST";
		static String BOOKING_USER_CONFIRMATION = "BOOKING_USER_CONFIRMATION";
		static String BOOKING_DRIVER_ONTRIP = "BOOKING_DRIVER_ONTRIP";
		static String BOOKING_DRIVER_ONSITE = "BOOKING_DRIVER_ONSITE";
		static String BOOKING_DRIVER_COMPLETED = "BOOKING_DRIVER_COMPLETED";
		static String BOOKING_PAYMENT_SUCCESS = "BOOKING_PAYMENT_SUCCESS";
		static String BOOKING_CANCELLED = "BOOKING_CANCELLED";
		static String COMMISSION_PENDING = "COMMISSION_PENDING";
		static String COMMISSION_PAID = "COMMISSION_PAID";
	}
	
	public interface NOTIFICATION_TITLE {
		static String BOOKING_DRIVER_REQUEST = "ZIPRYDE DRIVER REQUEST";
		static String BOOKING_USER_CONFIRMATION = "ZIPRYDE USER CONFIRMATION";
		static String BOOKING_DRIVER_ONTRIP = "ZIPRYDE BOOKING ON TRIP";
		static String BOOKING_DRIVER_ONSITE = "ZIPRYDE DRIVER REACHED";
		static String BOOKING_DRIVER_COMPLETED = "ZIPRYDE BOOKING COMPLETED";
		static String BOOKING_PAYMENT_SUCCESS = "ZIPRYDE PAYMENT SUCCESS";
		static String BOOKING_CANCELLED = "ZIPRYDE BOOKING CANCELLED";
		static String COMMISSION_PENDING = "ZIPRYDE COMMISSION PENDING";
		static String COMMISSION_PAID = "ZIPRYDE COMMISSION PAID";
	}
	
	public interface NOTIFICATION_MESSAGE {
		static String BOOKING_DRIVER_REQUEST = "You have a Zipryde Request with Id ";
		static String BOOKING_USER_CONFIRMATION = "Your Zipryde has been Confirmed. Your Zipryde Id is ";
		static String BOOKING_STATUS_CHANGE = "Zipryde status changed for Id is ";
		static String BOOKING_PAYMENT_SUCCESS="Payment Success for Booking Id ";
		static String BOOKING_CANCELLED = "Your Zipryde has been Cancelled";
		static String COMMISSION_PENDING = "Your Zipryde Commission is Pending";
		static String COMMISSION_PAID = "Your Zipryde Commission Paid";
	}
		
	public interface NOTIFICATION_CONFIG_TYPE {
		static String NOTIFICATION_DRIVER = "NOTIFICATION_DRIVER";
		static String NOTIFICATION_RIDER = "NOTIFICATION_RIDER";
		static String TWILIO_SMS ="TWILIO_SMS";
	}
	
	public interface ZIPRYDE_CONFIGURATION {
		static String NO_OF_MILES_TO_SEARCH = "NO_OF_MILES_TO_SEARCH";
		static String NO_OF_METERS_DRIVER_ONSITE = "NO_OF_METERS_DRIVER_ONSITE";
		static String ENABLE_LOC_LIMIT = "ENABLE_LOC_LIMIT";
		static String GEO_DISTNACE_SEARCH_LIMIT = "GEO_DISTNACE_SEARCH_LIMIT";
		static String GEO_LATITUDE_LIMIT = "GEO_LATITUDE_LIMIT";
		static String GEO_LONGITUDE_LIMIT = "GEO_LONGITUDE_LIMIT";
	}	

	public static String YES = "Y";	
	public static String NO = "N";
}
