package com.trivecta.zipryde.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

public class Utility {
	Logger log = Logger.getLogger(this.getClass());
	
	private static MessageDigest md;
		
	public static String encryptWithMD5(String password){
		if(password !=null && password.length() > 0) {
		    try {
		        md = MessageDigest.getInstance("MD5");
		        byte[] passBytes = password.getBytes();
		        md.reset();
		        byte[] digested = md.digest(passBytes);
		        StringBuffer sb = new StringBuffer();
		        for(int i=0;i<digested.length;i++){
		            sb.append(Integer.toHexString(0xff & digested[i]));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException ex) {
		        
		    }
		}
	    return null;
	}
	
	public static void main(String args[]) {
		System.out.println(encryptWithMD5("test123"));
	}
}
