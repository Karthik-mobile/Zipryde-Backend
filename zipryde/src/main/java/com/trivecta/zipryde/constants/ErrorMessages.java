package com.trivecta.zipryde.constants;

public interface ErrorMessages {

	static String DRIVER_ID_REQUIRED = "Driver Id Required";
	
	static String USER_ID_REQUIRED = "User Id Required";
	
	static String VEHICLE_ID_REQUIRED = "Vehicle Id Required";
	
	static String DRIVER_VEHICLE_ASSOCIATION_REQUIRED = "Driver Vehicle Association Id Required";
	
	static String LAT_LON_REQUIRED = "Latitude and Longitude Required";
	
	static String ONLINE_STATUS_REQUIRED = "Online Status Required";
	
	static String PICKUP_LOC_REQUIRED = "Pick up location Required";
	
	static String DROP_LOC_REQUIRED = "Drop location Required";
	
	static String CAB_REQUIRED = "Cab Required";
	
	static String CAB_TYPE_REQUIRED = "Cab Type Required";
	
	static String SUGGESTED_PRICE_REQUIRED = "Suggested Price Required";
		
	static String OFFERED_PRICE_REQUIRED = "Offered Price Required";
	
	static String FROM_DATE_REQUIRED = "From date is Required";
	
	static String VIN_REQUIRED = "VIN Required";
	
	static String LICENSE_PLATE_REQUIRED = "License Plate Number Required";
	
	static String BOOKING_ID_REQUIRED = "Booking Id Required";
	
	static String BOOKING_STATUS_REQUIRED = "Booking Status Required";
			
	static String DRIVER_STATUS_REQUIRED = "Driver Status  Required";
	
	static String AMOUNT_TO_PAY_REQUIRED = "Amount to Pay Required";
	
	static String MOBILE_MANDATORY = "Mobile No is Mandatory ";
	
	static String OTP_MANDATORY = "OTP is Mandatory";
	
	static String LAST_NAME_MANDATORY = "Last Name is Mandatory";
	
	static String PASSWORD_MANDATORY = "Password is Mandatory";
	
	static String EMAIL_MANDATORY = "Email is Mandatory" ;

	static String SEATING_CAPACITY_MANDATORY = "Seating Capacity is Mandatory";
	
	static String USER_TYPE_MANDATORY = "User Type is Mandatory ";
	
	static String DISTANCE_CAB_TYPE_PERSON_MANDATORY = "Distance,Cab Type,No Of Passengers are Mandatory for Price Calculation";
	
	static String LICENSE_VALID_UNTIL_MANDATORY  = "License Valid Until is Mandatory";
	
	static String MOBILE_NO_EXISTS_ALREADY = "Mobile Number exists already" ;
	
	static String MOBILE_NO_CANNOT_UPDATE = "Mobile No cannot be Updated";
	
	static String NO_USER_FOUND = "No User found";	
	
	static String LICENSE_ISSUED_GREATER_CURRENT = "License Issued on must be less than Current Date";
	
	static String LICENSE_VALID_UNTIL_LESSER = "License Valid Until must be greater than Current Date";
	
	static String LOGGIN_FAILED = "UserName or  Password does not match";
	
	static String VIN_EXISTS_ALREADY = "Vehicle with VIN Exists already";
	
	static String LICENSE_PLATE_EXISTS_ALREADY = "Vehicle with License Plate Exists already";
	
	static String NO_CAB_FOUND_BY_ID = "No Cab found for the given Cab Id";
	
	static String DIVER_NOT_APPROVED = "Driver Request is Pending for Approval";
	
	static String ACCOUNT_DEACTIVATED = "Account Deactivated";
	
	static String USER_NOT_DRIVER = "Given User for Vehicle Association is not a Driver";
	
	static String DRIVER_ASSOCIATED_VEHICLE = "Driver Associated with Vehicle. Unassign the Vehicle and delete";
	
	static String COMMISSION_ID_EMPTY = "Commission Id is Mandatory";
	
	static String COMMISSION_ID_INVALID = "Invalid Commission Id";

	static String COMMISSION_MSTR_REQUEST_EXCEPTION_MESSAGE = "Send Either No of Miles or No of Trips in request";

	static String STATUS_REQUERIED = "Status Required";
	
	static String BOOKING_ACCEPTED_ALREADY = "Zipryde has been Accepted Already";
	
	static String BOOKING_CANCELLED = "Your ZipRyde has been cancelled";
	
	static String BOOKING_CANNOT_CANCEL = "You cannot cancel this Zipryde";
	
	static String DRIVER_ACTIVE_BOOKING ="You have Active Zipryde and you cannot go offline";
	
	static String CONFIGURATION_TYPE_ALREADY_EXISTS = "Configuration Type Already Exists";
	
	static String  NO_BOOKING_FOUND = "No Zipryde found for this Id";
	
	static String LOST_RAISED_COMPLETED = "You can raise the Lost Item for Completed or Paid Zip Rydes only";
	
	static String USER_LOGGED_IN_ALREADY = "You have logged in using another device.Do you want to Override?";
	
	static String SESSION_EXPIRED = "Your session has expired. Please login again !";
}
