package com.nsdl.beckn.np.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RequestText {

	@JsonProperty("client_public_key")
	String clientPublicKey;

	@JsonProperty("challenge")
	String challenge;

	@JsonProperty("client_private_key")
	String clientPrivateKey;
}
