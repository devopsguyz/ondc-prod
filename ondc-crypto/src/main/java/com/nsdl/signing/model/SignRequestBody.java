package com.nsdl.signing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SignRequestBody {
	@JsonProperty("private_key")
	String privateKey;
	@JsonProperty("text_to_sign")
	String textToSign;
}
