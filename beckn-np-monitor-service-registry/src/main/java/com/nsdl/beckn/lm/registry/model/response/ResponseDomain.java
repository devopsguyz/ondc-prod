package com.nsdl.beckn.lm.registry.model.response;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDomain {

	String uuid;
	String domain;
	boolean allow;
	OffsetDateTime createdDate;
	OffsetDateTime updatedDate;
}
