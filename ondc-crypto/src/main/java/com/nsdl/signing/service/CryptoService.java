package com.nsdl.signing.service;

import com.nsdl.signing.model.Keypair;
import com.nsdl.signing.model.SignRequestBody;
import com.nsdl.signing.model.SignResponse;
import com.nsdl.signing.model.VerifySignRequestBody;
import com.nsdl.signing.model.VerifySignResponse;

public interface CryptoService {
	Keypair generateKey();

	SignResponse sign(SignRequestBody body);

	VerifySignResponse verifySign(VerifySignRequestBody body);
}
