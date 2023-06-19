package com.nsdl.signing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestData;
import com.nsdl.signing.model.RequestEncDecryptData;
import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.impl.CryptoServiceImpl;

@RestController()
public class SpringControllerApi {

	@Autowired
	CryptoServiceImpl cryptoServiceImpl;
	
	@GetMapping("/hello")
	public ResponseEntity<String> welcome() {
		return ResponseEntity.ok("Welcome");
	}
	
	@PostMapping("/signature/key")
	public ResponseEntity<KeyData> key() {
		return ResponseEntity.ok(cryptoServiceImpl.generateSignKey());
	}
	

	@PostMapping("/signature/generate")
	public ResponseEntity<String> signature(@RequestBody RequestData request) {
		return ResponseEntity.ok(cryptoServiceImpl.generateSignature(request));
	}
	
	@PostMapping("/signature/generate/onboarding")
	public ResponseEntity<String> signaturePlan(@RequestBody RequestData request) {
		return ResponseEntity.ok(cryptoServiceImpl.generateSignaturePlan(request));
	}

	@PostMapping("/signature/verify")
	public ResponseEntity<String> signatureVerify(@RequestBody RequestData request) {
		return ResponseEntity.ok(cryptoServiceImpl.verifySignature(request));
	}
	
	@PostMapping("/signature/verify/onboarding")
	public ResponseEntity<String> signatureVerifyPlan(@RequestBody RequestData request) {
		return ResponseEntity.ok(cryptoServiceImpl.verifySignaturePlan(request));
	}
	
	@PostMapping("/encrypt/text")
	public ResponseEntity<String> encryptText(@RequestBody RequestEncDecryptData request) {
		return ResponseEntity.ok(cryptoServiceImpl.encryptText(request));
	}
	
	@PostMapping("/decrypt/text")
	public ResponseEntity<String> decryptText(@RequestBody RequestEncDecryptData request) {
		return ResponseEntity.ok(cryptoServiceImpl.decryptText(request));
	}
	
	@PostMapping("/encrypt/decrypt/key")
	public ResponseEntity<KeyData> keyEncDecrypt() {
		return ResponseEntity.ok(cryptoServiceImpl.generateEncrypDecryptKey());
	}
	
	@PostMapping("/ocsp/verify")
	public ResponseEntity<String> ocspVerify(@RequestBody Web web) {
		return ResponseEntity.ok(cryptoServiceImpl.checkOCSP(web));
	}
}
