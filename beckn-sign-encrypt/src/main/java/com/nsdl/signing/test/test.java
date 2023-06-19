package com.nsdl.signing.test;

import com.nsdl.signing.crypto.EncryptDecrypt;
import com.nsdl.signing.model.RequestEncDecryptData;

public class test {
public static void main(String[] args) {
	
//	{
//	    "value": "IVxQVGMfRgKHM6PJ467MlGaJqiXlthV8tPW3AO5EdITvlIe2beguqlu3w/Unb+yS",
//	    "clientPublicKey": "MCowBQYDK2VuAyEAAaNER83kVoRlWsZlR5OV00WSQfjopSTI6sX/sElmS10=",
//	    "proteanPrivateKey": "MFECAQEwBQYDK2VuBCIEIIAQGtwEmCtyJDBNvfyKZU9PO2F8blnM8ZrOVybZUPdjgSEAOKujVi6ItqfwMzYruW+7+/4aKQ6hyJ7ZBHYVBZg+g3Y="
//	}
	RequestEncDecryptData requestEncDecryptData = new RequestEncDecryptData();
	requestEncDecryptData.setValue("IVxQVGMfRgKHM6PJ467MlGaJqiXlthV8tPW3AO5EdITvlIe2beguqlu3w/Unb+yS");
	requestEncDecryptData.setClientPublicKey("MCowBQYDK2VuAyEAAaNER83kVoRlWsZlR5OV00WSQfjopSTI6sX/sElmS10=");
	requestEncDecryptData.setProteanPrivateKey("MFECAQEwBQYDK2VuBCIEIIAQGtwEmCtyJDBNvfyKZU9PO2F8blnM8ZrOVybZUPdjgSEAOKujVi6ItqfwMzYruW+7+/4aKQ6hyJ7ZBHYVBZg+g3Y=");
	
	EncryptDecrypt encr= new EncryptDecrypt();
	encr.setup();
	String decrStr = encr.decrypt(requestEncDecryptData);
	System.out.println(decrStr);
}
}
