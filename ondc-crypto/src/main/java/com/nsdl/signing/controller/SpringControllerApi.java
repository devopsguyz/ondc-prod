package com.nsdl.signing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.signing.model.Keypair;
import com.nsdl.signing.model.SignRequestBody;
import com.nsdl.signing.model.SignResponse;
import com.nsdl.signing.model.VerifySignRequestBody;
import com.nsdl.signing.model.VerifySignResponse;
import com.nsdl.signing.service.impl.CryptoServiceImpl;

@RestController()
public class SpringControllerApi {

	@Autowired
	CryptoServiceImpl cryptoServiceImpl;

	@GetMapping("/generate-sign-keys")
	public ResponseEntity<Keypair> generateKey() {
		return ResponseEntity.ok(this.cryptoServiceImpl.generateKey());
	}

	@PostMapping("/sign")
	public ResponseEntity<SignResponse> sign(@RequestBody SignRequestBody body) {
		return ResponseEntity.ok(this.cryptoServiceImpl.sign(body));
	}

	@PostMapping("/verify-sign")
	public ResponseEntity<VerifySignResponse> verifySign(@RequestBody VerifySignRequestBody body) {
		return ResponseEntity.ok(this.cryptoServiceImpl.verifySign(body));
	}

}
