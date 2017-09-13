package com.trivecta.zipryde.view.response;

public class ConfigurationResponse {
	
	private Integer id;
	
	private String url;
	
	private String accessKey;
		
	private String type;

	private String accountSID;
	
	private String twilioNo;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountSID() {
		return accountSID;
	}

	public void setAccountSID(String accountSID) {
		this.accountSID = accountSID;
	}

	public String getTwilioNo() {
		return twilioNo;
	}

	public void setTwilioNo(String twilioNo) {
		this.twilioNo = twilioNo;
	}

	


}
