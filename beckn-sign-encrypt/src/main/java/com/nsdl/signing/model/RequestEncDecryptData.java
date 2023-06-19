package com.nsdl.signing.model;

import lombok.Data;

@Data
public class RequestEncDecryptData {
	String clientPublicKey;
	String clientPrivateKey;
	String proteanPrivateKey;
	String proteanPublicKey;
	String value;

	 
}
