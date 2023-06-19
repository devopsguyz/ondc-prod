package com.nsdl.signing.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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

import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestEncDecryptData;

@Component
public class EncryptDecrypt {

	public static String serverKey = "MFECAQEwBQYDK2VuBCIEIJDIsJi4nLGZ7BKaJkkIzJxubIndEOvT5hx0MKgoGYFvgSEA13ZQjiRLAA5YG6prELnmQwboQlpj0MzI94XF/kG4UmY=";
	public static String secretKey = "TlsPremasterSecret";

	public static void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public String encrypt(RequestEncDecryptData request) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(request.getProteanPublicKey());
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(request.getClientPrivateKey());
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			// *Server1
			Cipher cipher1 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher1.init(Cipher.ENCRYPT_MODE, key1);
			byte[] encrypted1 = cipher1.doFinal(request.getValue().getBytes(StandardCharsets.UTF_8));
			String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);

			return b64Encryped1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String decrypt(RequestEncDecryptData request) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(request.getClientPublicKey());
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(request.getProteanPrivateKey());
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			Cipher cipher2 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher2.init(Cipher.DECRYPT_MODE, key1); // Same derived key in server 2same as key1
			byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(request.getValue())); // b64 decode the
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

	public void testEncryption() throws Exception {
		/*
		 * Two Parties generating key pair on their own servers.
		 */
		KeyPair keyPairParty1 = KeyPairGenerator.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME)
				.generateKeyPair();
		KeyPair keyPairParty2 = KeyPairGenerator.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME)
				.generateKeyPair();

		System.out.println(
				"Server Private" + Base64.getEncoder().encodeToString(keyPairParty1.getPrivate().getEncoded()));
		System.out
				.println("Server Public" + Base64.getEncoder().encodeToString(keyPairParty1.getPublic().getEncoded()));

		System.out
				.println("User Private" + Base64.getEncoder().encodeToString(keyPairParty2.getPrivate().getEncoded()));
		System.out.println("User Public" + Base64.getEncoder().encodeToString(keyPairParty2.getPublic().getEncoded()));

		KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
		atServer1.init(keyPairParty1.getPrivate()); // Server1 uses its private key to initialize the aggreement object
		atServer1.doPhase(keyPairParty2.getPublic(), true); // Uses Server2's ppublic Key
		SecretKey key1 = atServer1.generateSecret("TlsPremasterSecret"); // derive secret at server 1.
																			// "TlsPremasterSecret" is the algorithm for
																			// secret key. It is an aes key actually.

		// Let's see what happens at server 2
		KeyAgreement atServer2 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
		atServer2.init(keyPairParty2.getPrivate()); // Server2 uses its private key to initialize the aggreement object
		atServer2.doPhase(keyPairParty1.getPublic(), true); // Uses Server1's ppublic Key
		SecretKey key2 = atServer2.generateSecret("TlsPremasterSecret"); // derive secret at server 2.
																			// "TlsPremasterSecret" is the algorithm for
																			// secret key. It is an aes key actually.

		// Encrypt value, server Private key , user Public key
		// user own Privte key , server public key , decrypt

		// Assertions.assertArrayEquals(key1.getEncoded(), key2.getEncoded());
		// Same key was derived in both places and can be used for encrypted message.

		// *Server1
		Cipher cipher1 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
		cipher1.init(Cipher.ENCRYPT_MODE, key1);
		byte[] encrypted1 = cipher1.doFinal("Beckn is awesome!".getBytes(StandardCharsets.UTF_8));
		String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);

		// At Server2
		Cipher cipher2 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
		cipher2.init(Cipher.DECRYPT_MODE, key2); // Same derived key in server 2same as key1
		byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(b64Encryped1)); // b64 decode the message before
																						// decrypting the bytes

		// Assertions.assertEquals("Beckn is awesome!", new String(decrypted2)); //
		// Ensure that the same encrypted message
		// is received.!

	}

	public KeyData generateKey() {
		// generate ed25519 keys
//		SecureRandom RANDOM = new SecureRandom();

		KeyPair agreementKeyPair;
		try {
			agreementKeyPair = KeyPairGenerator.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME)
					.generateKeyPair();

			String publicKey = Base64.getEncoder().encodeToString(agreementKeyPair.getPublic().getEncoded());
			String privateKey = Base64.getEncoder().encodeToString(agreementKeyPair.getPrivate().getEncoded());

			KeyData key = new KeyData();
			key.setPrivateKey(privateKey);
			key.setPublicKey(publicKey);

			return key;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	public static void main(String[] args) {
//		setup();
//		try {
//			EncryptDecrypt test = new EncryptDecrypt();
//			// test.testEncryption();
//			RequestEncDecryptData request = new RequestEncDecryptData();
//			request.setValue("Fossgen is awesome!");
//			request.setPrivateKey(
//					"MFECAQEwBQYDK2VuBCIEIKgV2X9E18BedNLxfIt1bv4evnRu+7d/qJ9HJr33nPlrgSEA5Z40wuZrZZlM32btk5mRquamESf5B+BGRVM8x/qTAFU=");
//			request.setPublicKey("MCowBQYDK2VuAyEA5Z40wuZrZZlM32btk5mRquamESf5B+BGRVM8x/qTAFU=");
//			request.setServerPublicKey("MCowBQYDK2VuAyEA13ZQjiRLAA5YG6prELnmQwboQlpj0MzI94XF/kG4UmY=");
//
//			String enc = test.encrypt(request);
//			System.out.println(request.getValue());
//
//			System.out.println(enc);
//			request.setValue(enc);
//
//			System.out.println(test.decrypt(request));
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
