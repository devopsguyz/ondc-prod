package com.nsdl.beckn.lm.audit.model.request;

import lombok.Data;

@Data
public class RequestDomain {

	String uuid;
	String domain;
	boolean allow;

}
