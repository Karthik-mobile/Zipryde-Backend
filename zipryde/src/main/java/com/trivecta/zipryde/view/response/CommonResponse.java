package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

	private Number count;

	public Number getCount() {
		return count;
	}

	public void setCount(Number count) {
		this.count = count;
	}

	
}
