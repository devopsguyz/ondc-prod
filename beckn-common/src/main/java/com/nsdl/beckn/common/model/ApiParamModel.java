package com.nsdl.beckn.common.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApiParamModel implements Serializable {

	private static final long serialVersionUID = 7461538270839107324L;

	private String name;
	private boolean appendApiName; // to be checked
	private String httpEntityEndpoint;
	private int httpTimeout;
	private int httpRetryCount;
	private int headerValidity;
	private boolean headerAuthentication;
	private boolean setAuthorizationHeader;
}
