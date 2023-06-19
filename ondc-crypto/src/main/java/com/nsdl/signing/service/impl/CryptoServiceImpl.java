package com.nsdl.signing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsdl.signing.crypto.CryptoUtl;
import com.nsdl.signing.model.Keypair;
import com.nsdl.signing.model.SignRequestBody;
import com.nsdl.signing.model.SignResponse;
import com.nsdl.signing.model.VerifySignRequestBody;
import com.nsdl.signing.model.VerifySignResponse;
import com.nsdl.signing.service.CryptoService;

@Service
public class CryptoServiceImpl implements CryptoService {

	@Autowired
	CryptoUtl cryptoUtl;

	@Override
	public Keypair generateKey() { // generate ed25519 keys

		return this.cryptoUtl.generate_sign_keys();

	}

	@Override
	public SignResponse sign(SignRequestBody body) {
		SignResponse sign = new SignResponse();
		try {
			sign.setSignedText(this.cryptoUtl.sign(body.getPrivateKey(), body.getTextToSign()));

			sign.setBlakeValue(this.cryptoUtl.generateBlakeHash(body.getTextToSign()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sign.setSignedText(e.getMessage());
		}

		return sign;
	}

	@Override
	public VerifySignResponse verifySign(VerifySignRequestBody body) {
		return this.cryptoUtl.verify_sign(body.getTextToSign(), body.getSignedText(), body.getPublicKey());

	}
}
