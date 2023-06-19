package com.nsdl.onsubscribe.controller;

import com.nsdl.onsubscribe.crypto.DecryptGCM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.onsubscribe.crypto.Decrypt;
import com.nsdl.onsubscribe.model.ReqKeyOnSubsribe;
import com.nsdl.onsubscribe.model.ReqOnSubsribe;
import com.nsdl.onsubscribe.model.ResponseOnSubsribe;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OnSubcribeController {

	@Autowired
	Decrypt decrypt;

	@Autowired
	DecryptGCM decryptGCM;

	@Value("${enc.algo}")
	String encAlgo;

	@PostMapping("/test/on_subscribe")
	public ResponseEntity<ResponseOnSubsribe> onsubscribe(@RequestBody ReqOnSubsribe reqOnSubsribe) {

		ResponseOnSubsribe res = new ResponseOnSubsribe();

		if ("GCM".equals(encAlgo)) {
			res.setAnswer(decryptGCM.decrypt(
					DecryptGCM.clientPrivateKey,
					DecryptGCM.proteanPublicKey,
					reqOnSubsribe.getChallenge()
			));
		} else {
			res.setAnswer(decrypt.decrypt(
					decrypt.clientPrivateKey,
					decrypt.proteanPublicKey,
					reqOnSubsribe.getChallenge()
			));
		}

		return ResponseEntity.ok(res);
	}

	@PostMapping("/test/key/subscribe")
	public ResponseEntity<String> onsubscribeKey(@RequestBody ReqKeyOnSubsribe reqOnSubsribe) {

		if ("GCM".equals(encAlgo)) {
			DecryptGCM.clientPrivateKey = reqOnSubsribe.getClientPrivateKey();
			DecryptGCM.proteanPublicKey = reqOnSubsribe.getProteanPublicKey();
		} else {
			decrypt.clientPrivateKey=reqOnSubsribe.getClientPrivateKey();
			decrypt.proteanPublicKey=reqOnSubsribe.getProteanPublicKey();
		}

		return ResponseEntity.ok("OK");
	}
}
