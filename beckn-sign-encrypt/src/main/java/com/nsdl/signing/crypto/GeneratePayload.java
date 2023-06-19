package com.nsdl.signing.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;

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
import org.springframework.stereotype.Component;

import com.nsdl.signing.model.KeyData;

@Component
public class GeneratePayload {
	public static String privateKey = "K2qouM7hs57AStiEcKfCvXTgLtVFxMRhhCsbBUrWVaI=";
	public static String publicKey = "K0el1kYfJI222a8Zja9jOsU68zU+zqT6/AiTobEl66k=";
	public static String kid = "nsdl.co.in.ba|nsdl_bap1|ed25519";

	public static String[] headers = new String[] { "Signature keyId", "algorithm", "created", "expires", "headers",
			"signature" };

	public GeneratePayload() {
		// TODO Auto-generated constructor stub
		setup();
	}

	public static void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
			System.out.println(Security.addProvider(new BouncyCastleProvider()));
		}
	}

	public static String generateBlakeHash(String req) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("BLAKE2B-512", BouncyCastleProvider.PROVIDER_NAME);
		digest.reset();
		digest.update(req.getBytes(StandardCharsets.UTF_8));
		byte[] hash = digest.digest();
		String bs64 = Base64.getEncoder().encodeToString(hash);
		System.out.println(bs64);
		return bs64;

	}

	public static boolean verifySignaturePK(String sign, String requestData, String dbPublicKey) {
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

	public static String generateSignaturePK(String req, String pk) {
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

	public static String generateSignature(String req, String pk) {
		String signature = null;
		try {

			long testTimestamp = System.currentTimeMillis() / 1000L;

			String blakeValue;

			blakeValue = generateBlakeHash(req);

			String signingString = "(created): " + testTimestamp + "\n(expires): " + (testTimestamp + 60000)
					+ "\ndigest: BLAKE-512=" + blakeValue + "";

			String header = "(" + testTimestamp + ") (" + (testTimestamp + 60000) + ") BLAKE-512=" + blakeValue + "";

			String signedReq = generateSignaturePK(signingString, pk);

			signature = "Signature keyId=\"" + kid + "\",algorithm=\"ed25519\", created=\"" + testTimestamp
					+ "\", expires=\"" + (testTimestamp + 60000)
					+ "\", headers=\"(created) (expires) digest\", signature=\"" + signedReq + "\"";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	public static String generateSignaturePlan(String req, String pk) {
		String signature = null;
		try {

			// long testTimestamp = System.currentTimeMillis() / 1000L;

			String blakeValue;

			// blakeValue = generateBlakeHash(req);

			String signedReq = generateSignaturePK(req, pk);

			signature = signedReq;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	public static String verifySignature(String sign, String requestData, String dbPublicKey) {
		boolean isVerified = false;
		try {
			String signArray[] = sign.split(",");
			HashMap<String, String> hs = new HashMap<>();
			try {
				for (String data : signArray) {
					// String dataAr[]=data.trim().split("=");
					int index = data.indexOf("=");
					String key = data.substring(0, index).trim();
					if (headers[0].equalsIgnoreCase(key) || headers[1].equalsIgnoreCase(key)
							|| headers[2].equalsIgnoreCase(key) || headers[3].equalsIgnoreCase(key)
							|| headers[4].equalsIgnoreCase(key) || headers[5].equalsIgnoreCase(key)) {
						System.out.println(key + ":" + data.substring(index + 2, data.indexOf("\"", index + 2)).trim());
						hs.put(key.toLowerCase(), data.substring(index + 2, data.indexOf("\"", index + 2)).trim());
					} else {
						throw new Exception();
					}
				}
				for (String data : headers) {
					if (hs.get(data.toLowerCase()) == null) {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				return "Signature is not valid header format.";
			}

			long cTime = Long.parseLong(hs.get(headers[2].toLowerCase()));
			long eTime = Long.parseLong(hs.get(headers[3].toLowerCase()));
			long testTimestamp = System.currentTimeMillis() / 1000L;

			System.out.println(testTimestamp);
			System.out.println(cTime <= testTimestamp);
			System.out.println(testTimestamp <= eTime);

			if ((cTime <= testTimestamp) && (testTimestamp <= eTime)) {
				sign = hs.get(headers[5]);

				String blakeValue = generateBlakeHash(requestData);

				String signingString = "(created): " + cTime + "\n(expires): " + eTime + "\ndigest: BLAKE-512="
						+ blakeValue + "";

				isVerified = verifySignaturePK(sign, signingString, dbPublicKey);// sv.verifySignature(decodedSign);
				return "Is Sign Verified : " + isVerified;

			} else {
				return "Signature is expired.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String verifySignaturePlan(String sign, String requestData, String dbPublicKey) {
		boolean isVerified = false;
		try {

			// String blakeValue = generateBlakeHash(requestData);

			isVerified = verifySignaturePK(sign, requestData, dbPublicKey);// sv.verifySignature(decodedSign);
			return "Is Sign Verified : " + isVerified;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public KeyData generateKey() {
		// generate ed25519 keys
		SecureRandom RANDOM = new SecureRandom();
		Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
		keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM));
		AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
		Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
		Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();
		KeyData key = new KeyData();
		key.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
		key.setPublicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

		return key;

	}
}
