package com.nsdl.signing.service;

import java.util.Map;

import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestData;
import com.nsdl.signing.model.RequestEncDecryptData;
import com.nsdl.signing.model.Web;

public interface CryptoService {
    /**
     * This method generate signature by sign,requestData,clientPublicKey,clientPrivateKey.
     * @param request It is a RequestData.
     * @return Its returns generated signature.
     */
	String generateSignature(RequestData request);
	
    /**
     * This method verify signature by sign,requestData,clientPublicKey,clientPrivateKey.
     * @param request It is a RequestData.
     * @return Its returns verify signature.
     */
	String verifySignature(RequestData request);
	
    /**
     * This method verify signaturePlan by sign,requestData,clientPublicKey,clientPrivateKey.
     * @param request It is a RequestData.
     * @return Its returns verify signaturePlan.
     */
	String verifySignaturePlan(RequestData request);
	
    /**
     * This method encrypt text by clientPublicKey,clientPrivateKey,proteanPrivateKey,proteanPublicKey,value.
     * @param request It is a RequestEncDecryptData.
     * @return Its returns encrypted text.
     */
	String encryptText(RequestEncDecryptData request);
    /**
     * This method decrypt text by clientPublicKey,clientPrivateKey,proteanPrivateKey,proteanPublicKey,value.
     * @param request It is a RequestEncDecryptData. 
     * @return Its returns decrypted text.
     */
	String decryptText(RequestEncDecryptData request);
    /**
     * Its generate signature key.
     * @return Its returns generate signature key.
     */
	KeyData generateSignKey();
    /**
     * Its generate EncryptDecryotkey.
     * @return Its return EncryptDecryotkey.
     */
	KeyData generateEncrypDecryptKey();
    /**
     * Its checks OCSP certificate by domain and map error.
     * @param domain It is domain name.
     * @param error It is a map interface.
     * @return  Its return OCSP certificate validation,
     */
	String checkOCSP(Web domain, Map<String, String> error);
    /**
     * Its generate signature plan by clientPublicKey,clientPrivateKey,proteanPrivateKey,proteanPublicKey,value.
     * @param request It is a request data.
     * @return Its returns generated signature Plan.
     */
	String generateSignaturePlan(RequestData request);

}
