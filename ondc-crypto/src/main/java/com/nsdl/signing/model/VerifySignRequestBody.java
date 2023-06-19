package com.nsdl.signing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VerifySignRequestBody {
	@JsonProperty("signed_text")
	String signedText;
	@JsonProperty("public_key")
	String publicKey;
	@JsonProperty("text_to_sign")
	String textToSign;
}
