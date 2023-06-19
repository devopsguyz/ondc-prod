package com.nsdl.signing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SignResponse {
	@JsonProperty("signed_text")
	String signedText;

	@JsonProperty("blake_value")
	String blakeValue;

}
