package com.nsdl.beckn.lm.registry.model.response;

import java.util.Map;

import com.nsdl.beckn.lm.registry.model.NPApiLogs;
import com.nsdl.beckn.lm.registry.utl.CommonUtl;

import lombok.Data;

@Data
public class LogsResponse {

	Map<String, Object> request;
	Map<String, Object> response;
	String createdOn;

	public LogsResponse(NPApiLogs obj) {
		this.request = obj.getJsonRequest();
		this.response = obj.getJsonResponse();

		this.createdOn = CommonUtl.convert24DatetoString(obj.getCreatedDate());
	}

}
