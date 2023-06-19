package com.nsdl.beckn.np.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Logs {
	 
	private String requestId;
	String request;
	// logs,gateway,adaptor
	String type;
	// high,low,medium
	String priority;
	String exception;
	String javaStackTrace;
	String serverName;
}