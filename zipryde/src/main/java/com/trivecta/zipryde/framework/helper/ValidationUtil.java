package com.trivecta.zipryde.framework.helper;

public class ValidationUtil {

	public static boolean isValidString(String str) {
		if(str!= null && !"".equalsIgnoreCase(str.trim())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String getFullName(String firstName,String lastName) {
		String name = "";
		if(isValidString(firstName)) {
			name =  name + firstName +" ";
		}
		if(isValidString(lastName)) {
			name =  name + lastName;
		}
		return name;
	}
}
