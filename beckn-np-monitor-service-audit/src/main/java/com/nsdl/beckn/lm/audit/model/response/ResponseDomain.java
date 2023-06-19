package com.nsdl.beckn.lm.audit.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDomain {

	String uuid;
	String domain;
	boolean allow;
	String createdDate;
	String updatedDate;
}
