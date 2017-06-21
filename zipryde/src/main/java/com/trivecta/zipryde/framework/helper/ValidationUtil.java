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
}
