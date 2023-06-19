package com.nsdl.signing.test;

import com.nsdl.signing.crypto.EncryptDecrypt;
import com.nsdl.signing.model.RequestEncDecryptData;

public class SampleUseCaseTest {
 
	 

	public static void main(String[] args) {
	 
		try {
			EncryptDecrypt test = new EncryptDecrypt();
			// test.testEncryption();
			RequestEncDecryptData request = new RequestEncDecryptData();
			request.setValue("Fossgen is awesome!");
			request.setClientPrivateKey(
					"MFECAQEwBQYDK2VuBCIEIKgV2X9E18BedNLxfIt1bv4evnRu+7d/qJ9HJr33nPlrgSEA5Z40wuZrZZlM32btk5mRquamESf5B+BGRVM8x/qTAFU=");
			request.setClientPublicKey("MCowBQYDK2VuAyEA5Z40wuZrZZlM32btk5mRquamESf5B+BGRVM8x/qTAFU=");
			request.setProteanPublicKey("MCowBQYDK2VuAyEA13ZQjiRLAA5YG6prELnmQwboQlpj0MzI94XF/kG4UmY=");
			request.setProteanPrivateKey("MFECAQEwBQYDK2VuBCIEIJDIsJi4nLGZ7BKaJkkIzJxubIndEOvT5hx0MKgoGYFvgSEA13ZQjiRLAA5YG6prELnmQwboQlpj0MzI94XF/kG4UmY=");

			String enc = test.encrypt(request);
			System.out.println(request.getValue());

			System.out.println(enc);
			request.setValue(enc);

			System.out.println(test.decrypt(request));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
