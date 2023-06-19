package com.nsdl.signing.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SubscribeEncryptDecrypt implements ISubscribeEncryptDecrypt {

	String clientPrivateKey = "MFECAQEwBQYDK2VuBCIEIChY69PwPeovw1zAh7TRU+E40LIEykBsbIBp3CanVvRegSEASfWOME2kQQ75i5iMHx0ZodBn0P9UTHcOkeczDmeOVkU=";
	String clientPublicKey = "MCowBQYDK2VuAyEASfWOME2kQQ75i5iMHx0ZodBn0P9UTHcOkeczDmeOVkU=";

	String proteanPublicKey = "MCowBQYDK2VuAyEALtPj74XkIrkyxTqyssjtYJ3KRND5FnzK5MDrwlK3kC8=";
	String proteanPrivateKey = "MFECAQEwBQYDK2VuBCIEIAj5U1DVAX5eGI1jIIcjmzWgPQlIg/T1Q6A3pZ0AIWp6gSEAJGnKRTAEcSvpgD0mw9gBHv94E3w8sTtmPlszuXIEAF0=";

	public static String secretKey = "TlsPremasterSecret";

	public static void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	@Override
	public String decrypt(String clientPrivateKey, String proteanPublicKey, String value) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(proteanPublicKey);
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(clientPrivateKey);
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			Cipher cipher2 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher2.init(Cipher.DECRYPT_MODE, key1); // Same derived key in server 2same as key1
			byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(value)); // b64 decode the
																					// message before

			return new String(decrypted2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public PublicKey getPublicKey(String algo, byte[] jceBytes) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(jceBytes);
		PublicKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePublic(x509EncodedKeySpec);
		return key;
	}

	public PrivateKey getPrivateKey(String algo, byte[] jceBytes) throws Exception {
		PrivateKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePrivate(new PKCS8EncodedKeySpec(jceBytes));
		return key;
	}

	@Override
	public String encrypt(String clientPublicKey, String proteanPrivateKey, String value) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(clientPublicKey);
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(proteanPrivateKey);
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			// *Server1
			Cipher cipher1 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher1.init(Cipher.ENCRYPT_MODE, key1);
			byte[] encrypted1 = cipher1.doFinal(value.getBytes(StandardCharsets.UTF_8));
			String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);

			return b64Encryped1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		SubscribeEncryptDecrypt decr = new SubscribeEncryptDecrypt();

		decr.setup();
		String enc = decr.encrypt(decr.clientPublicKey, decr.proteanPrivateKey, "abc sa,md ");
		System.out.println(enc);
		System.out.println(decr.decrypt(decr.clientPrivateKey, decr.proteanPublicKey, enc));

	}

}
