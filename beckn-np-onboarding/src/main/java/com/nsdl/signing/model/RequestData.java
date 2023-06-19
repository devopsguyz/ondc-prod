package com.nsdl.signing.model;

import lombok.Data;

@Data
public class RequestData {
	String sign;
	String requestData;
	String clientPublicKey;
	String clientPrivateKey;
 

}
