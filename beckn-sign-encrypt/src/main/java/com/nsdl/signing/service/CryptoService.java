package com.nsdl.signing.service;

import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestData;
import com.nsdl.signing.model.RequestEncDecryptData;
import com.nsdl.signing.model.Web;

public interface CryptoService {

	public String generateSignature(RequestData request);
	public String verifySignature(RequestData request);
	public String verifySignaturePlan(RequestData request);
	public String encryptText(RequestEncDecryptData request) ;
	public String decryptText(RequestEncDecryptData request);
	public KeyData generateSignKey();
	public KeyData generateEncrypDecryptKey();
	public String checkOCSP(Web domain);
	public String generateSignaturePlan(RequestData request);
	
}
