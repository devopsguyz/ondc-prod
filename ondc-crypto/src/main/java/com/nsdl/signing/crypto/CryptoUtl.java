package com.nsdl.signing.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
import org.springframework.stereotype.Component;

import com.nsdl.signing.model.Keypair;
import com.nsdl.signing.model.VerifySignResponse;

import lombok.Data;

@Component
@Data
public class CryptoUtl {

	public CryptoUtl() {
		setup();
	}

	public void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
			System.out.println(Security.addProvider(new BouncyCastleProvider()));
		}
	}

	public Keypair generate_sign_keys() {
		// generate ed25519 keys
		SecureRandom RANDOM = new SecureRandom();
		Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
		keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM));
		AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
		Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
		Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();
		Keypair key = new Keypair();
		key.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
		key.setPublicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

		return key;

	}

	public String sign(String private_key, String text_to_sign) {

		return generateSignature(text_to_sign, private_key);
	}

	public VerifySignResponse verify_sign(String text_to_sign, String signed_text, String public_key) {
		VerifySignResponse response = new VerifySignResponse();
		try {

			response.setStatus(verifySignature(signed_text, text_to_sign, public_key));
		} catch (Exception e) {
			response.setStatus("error");
			response.setStatus(e.getMessage());

			// TODO: handle exception
		}
		return response;
	}

	String generateSignature(String req, String pk) {
		String signature = null;
		try {

			// long testTimestamp = System.currentTimeMillis() / 1000L;

			String blakeValue;

			blakeValue = generateBlakeHash(req);

			String signedReq = generateSignaturePK(blakeValue, pk);

			signature = signedReq;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	String generateSignaturePK(String req, String pk) {
		String signature = null;
		try {
			Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(
					Base64.getDecoder().decode(pk.getBytes()), 0);
			Signer sig = new Ed25519Signer();
			sig.init(true, privateKey);
			sig.update(req.getBytes(), 0, req.length());
			byte[] s1 = sig.generateSignature();
			signature = Base64.getEncoder().encodeToString(s1);
		} catch (DataLengthException | CryptoException e) {
			e.printStackTrace();
		}
		return signature;
	}

	public String generateBlakeHash(String req) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("BLAKE2B-512", BouncyCastleProvider.PROVIDER_NAME);
		digest.reset();
		digest.update(req.getBytes(StandardCharsets.UTF_8));
		byte[] hash = digest.digest();
		String bs64 = Base64.getEncoder().encodeToString(hash);
		System.out.println(bs64);
		return bs64;

	}

	String verifySignature(String sign, String requestData, String dbPublicKey) {
		boolean isVerified = false;
		try {

			String blakeValue = generateBlakeHash(requestData);

			isVerified = verifySignaturePK(sign, blakeValue, dbPublicKey);// sv.verifySignature(decodedSign);
			return "Is Sign Verified : " + isVerified;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	boolean verifySignaturePK(String sign, String requestData, String dbPublicKey) {
		boolean isVerified = false;
		try {
			System.out.println("Sign : " + sign + " requestData : " + requestData + " PublicKey : " + dbPublicKey);
			Ed25519PublicKeyParameters publicKey = new Ed25519PublicKeyParameters(
					Base64.getDecoder().decode(dbPublicKey), 0);
			Signer sv = new Ed25519Signer();
			sv.init(false, publicKey);
			sv.update(requestData.getBytes(), 0, requestData.length());

			byte[] decodedSign = Base64.getDecoder().decode(sign);
			isVerified = sv.verifySignature(decodedSign);
			System.out.println("Is Sign Verified : " + isVerified);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isVerified;
	}
}
