package com.nsdl.signing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VerifySignResponse {
	@JsonProperty("status")
	String status;
	@JsonProperty("details")
	String details;
}
