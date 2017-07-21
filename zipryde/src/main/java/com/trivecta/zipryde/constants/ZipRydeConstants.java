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
	
	
}
