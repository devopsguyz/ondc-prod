package com.nsdl.signing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Keypair {
	@JsonProperty("private_key")
	String privateKey;
	@JsonProperty("public_key")
	String publicKey;
}
