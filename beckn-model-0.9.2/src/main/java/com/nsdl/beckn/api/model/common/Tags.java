package com.nsdl.beckn.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Tags {
	@JsonProperty("additionalProp1")
	private String additionalProp1;

	@JsonProperty("additionalProp2")
	private String additionalProp2;

	@JsonProperty("additionalProp3")
	private String additionalProp3;
}
