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
public class SubscribeEncryptDecrypt implements ISubscribeEncryptDecrypt{

	String clientPrivateKey = "MC4CAQAwBQYDK2VuBCIEIPNxJhWiObr8T1WgN53gWjKULs6wKBJnVxX3aYtrK8JS";
	String clientPublicKey = "MCowBQYDK2VuAyEABxp9W+BXIBcAXIqdW48NkNXcfn1jyjXhowwreql3Pk0=";

	String proteanPublicKey = "MCowBQYDK2VuAyEAjVFpmmVk5QB61NmPrmG2sQl4aUzuRud0wOH69YrVBmI=";
	String proteanPrivateKey = "MFECAQEwBQYDK2VuBCIEILjmcSrhn/fF1VuyBfnxOVHiec0lv5Slw9NvAcbL7oxTgSEAjVFpmmVk5QB61NmPrmG2sQl4aUzuRud0wOH69YrVBmI=";

	public static String secretKey = "TlsPremasterSecret";

	public static void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	/**
	 * This method decrypt String by clientPrivateKey,proteanPublicKey value.
	 * 
	 * @param clientPrivateKey It is a unique key.
	 * @param proteanPublicKey It is a unique key.
	 * @param value            It is a String.
	 * @return Its returns decrypted String based on the
	 *         clientPrivateKey,proteanPublicKey value.
	 */
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

	/**
	 * This method gets public key by String algo and byte jceBytes and throws
	 * exception.
	 * 
	 * @param algo     It is a String.
	 * @param jceBytes It is a byte.
	 * @return Its returns public key based on the String algo and byte jceBytes.
	 * @throws Exception Thrown for public key.
	 */
	public PublicKey getPublicKey(String algo, byte[] jceBytes) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(jceBytes);
		PublicKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePublic(x509EncodedKeySpec);
		return key;
	}

	/**
	 * This method gets private key by String algo and byte jceBytes and throws
	 * exception.
	 * 
	 * @param algo     It is a String.
	 * @param jceBytes It is a byte.
	 * @return Its returns private key based on the String algo and byte jceBytes
	 * @throws Exception Thrown for private key.
	 */
	public PrivateKey getPrivateKey(String algo, byte[] jceBytes) throws Exception {
		PrivateKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePrivate(new PKCS8EncodedKeySpec(jceBytes));
		return key;
	}

	/**
	 * This method String encrypt by clientPublicKey,proteanPrivateKey and value.
	 * 
	 * @param clientPublicKey   It is unique key.
	 * @param proteanPrivateKey It is unique key.
	 * @param value             It is a String.
	 * @return Its returns encrypted String based on the
	 *         clientPublicKey,proteanPrivateKey and value.
	 */
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
			SecretKey key1 = atServer1.generateSecret(secretKey); // //derive secret at server 1.
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
		String enc = decr.encrypt(decr.clientPublicKey,
				decr.proteanPrivateKey,
				"6db65041-de15-484a-ba74-1086c2ab15c5");
		System.out.println(enc);
		System.out.println(decr.decrypt(decr.clientPrivateKey, decr.proteanPublicKey, enc));

	}

}
