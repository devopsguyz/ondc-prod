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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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
    /**
     * This method generating blakeHash by String. 
     * @param req It is a String 
     * @return Its returns blakeHash.
     * @throws Exception Thrown exception for blakeHash
     */
	public static String generateBlakeHash(String req) throws Exception {
		// String stringToHash = "Beckn!";
		// String stringToHash =
		// "{\"context\":{\"country\":\"IND\",\"domain\":\"nic2004:60212\",\"transaction_id\":\"f01fc03d-8add-4d6b-8595-4e6cb74a7283\",\"action\":\"on_search\",\"message_id\":\"481b1f20-acea-41f1-9117-3a68df59eb72\",\"city\":\"\",\"bap_uri\":\"https://beckn.free.beeceptor.com\",\"timestamp\":\"2021-10-28T12:42:18.150954734Z\",\"core_version\":\"0.9.1\",\"bap_id\":\"https://api.sandbox.beckn.juspay.in/dev/bap/cab/v1\",\"bpp_id\":\"api.sandbox.beckn.juspay.in/bpp/metro/v1\",\"bpp_uri\":\"https://metro-bpp.com/\"},\"message\":{\"catalog\":{\"bpp/descriptor\":{\"name\":\"BPP\"},\"bpp/providers\":[{\"id\":\"KMRL\",\"descriptor\":{\"name\":\"Kochi
		// Metro Rail
		// Limited\"},\"locations\":[{\"id\":\"CCUV\",\"descriptor\":{\"name\":\"Cusat\"},\"station_code\":\"CCUV\",\"gps\":\"10.0467,76.3182\"},{\"id\":\"KLMT\",\"descriptor\":{\"name\":\"Kalamassery\"},\"station_code\":\"KLMT\",\"gps\":\"10.0586,76.322\"},{\"id\":\"PDPM\",\"descriptor\":{\"name\":\"Pathadipalam\"},\"station_code\":\"PDPM\",\"gps\":\"10.0361,76.3144\"}],\"items\":[{\"id\":\"CCUV_TO_KLMT\",\"descriptor\":{\"name\":\"Cusat
		// to
		// Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:23:00.000Z\",\"2021-10-30T02:35:00.000Z\",\"2021-10-30T03:48:40.000Z\",\"2021-10-30T04:58:40.000Z\",\"2021-10-30T06:08:40.000Z\",\"2021-10-30T07:18:40.000Z\",\"2021-10-30T08:28:40.000Z\",\"2021-10-30T09:38:40.000Z\",\"2021-10-30T10:48:40.000Z\",\"2021-10-30T11:58:40.000Z\",\"2021-10-30T13:08:40.000Z\",\"2021-10-30T14:17:42.000Z\",\"2021-10-30T00:47:59.000Z\",\"2021-10-30T01:58:16.000Z\",\"2021-10-30T03:10:25.000Z\",\"2021-10-30T04:23:39.000Z\",\"2021-10-30T05:33:39.000Z\",\"2021-10-30T06:43:39.000Z\",\"2021-10-30T07:53:39.000Z\",\"2021-10-30T09:03:39.000Z\",\"2021-10-30T10:13:39.000Z\",\"2021-10-30T11:23:39.000Z\",\"2021-10-30T12:33:39.000Z\",\"2021-10-30T13:43:39.000Z\",\"2021-10-30T14:52:41.000Z\",\"2021-10-30T00:28:20.000Z\",\"2021-10-30T01:38:42.000Z\",\"2021-10-30T02:46:49.000Z\",\"2021-10-30T03:57:14.000Z\",\"2021-10-30T05:07:14.000Z\",\"2021-10-30T06:17:14.000Z\",\"2021-10-30T07:27:14.000Z\",\"2021-10-30T08:37:14.000Z\",\"2021-10-30T09:47:14.000Z\",\"2021-10-30T10:57:14.000Z\",\"2021-10-30T12:07:14.000Z\",\"2021-10-30T13:17:14.000Z\",\"2021-10-30T14:25:44.000Z\",\"2021-10-30T15:31:18.000Z\",\"2021-10-30T16:38:09.000Z\",\"2021-10-30T01:05:12.000Z\",\"2021-10-30T02:16:13.000Z\",\"2021-10-30T03:29:23.000Z\",\"2021-10-30T04:40:52.000Z\",\"2021-10-30T05:50:52.000Z\",\"2021-10-30T07:00:52.000Z\",\"2021-10-30T08:10:52.000Z\",\"2021-10-30T09:20:52.000Z\",\"2021-10-30T10:30:52.000Z\",\"2021-10-30T11:40:52.000Z\",\"2021-10-30T12:50:52.000Z\",\"2021-10-30T14:00:52.000Z\",\"2021-10-30T15:12:38.000Z\",\"2021-10-30T16:24:02.000Z\",\"2021-10-30T02:56:40.000Z\",\"2021-10-30T04:06:10.000Z\",\"2021-10-30T05:16:10.000Z\",\"2021-10-30T06:26:10.000Z\",\"2021-10-30T07:36:10.000Z\",\"2021-10-30T08:46:10.000Z\",\"2021-10-30T09:56:10.000Z\",\"2021-10-30T11:06:10.000Z\",\"2021-10-30T12:16:10.000Z\",\"2021-10-30T13:26:10.000Z\",\"2021-10-30T14:36:10.000Z\",\"2021-10-30T15:44:26.000Z\",\"2021-10-30T16:46:29.000Z\",\"2021-10-30T03:19:59.000Z\",\"2021-10-30T04:32:25.000Z\",\"2021-10-30T05:42:25.000Z\",\"2021-10-30T06:52:25.000Z\",\"2021-10-30T08:02:25.000Z\",\"2021-10-30T09:12:25.000Z\",\"2021-10-30T10:22:25.000Z\",\"2021-10-30T11:32:25.000Z\",\"2021-10-30T12:42:25.000Z\",\"2021-10-30T13:52:25.000Z\",\"2021-10-30T15:02:01.000Z\",\"2021-10-30T16:12:07.000Z\",\"2021-10-30T03:39:11.000Z\",\"2021-10-30T04:49:55.000Z\",\"2021-10-30T05:59:55.000Z\",\"2021-10-30T07:09:55.000Z\",\"2021-10-30T08:19:55.000Z\",\"2021-10-30T09:29:55.000Z\",\"2021-10-30T10:39:55.000Z\",\"2021-10-30T11:49:55.000Z\",\"2021-10-30T12:59:55.000Z\",\"2021-10-30T14:10:43.000Z\",\"2021-10-30T15:21:20.000Z\",\"2021-10-30T04:14:55.000Z\",\"2021-10-30T05:24:55.000Z\",\"2021-10-30T06:34:55.000Z\",\"2021-10-30T07:44:55.000Z\",\"2021-10-30T08:54:55.000Z\",\"2021-10-30T10:04:55.000Z\",\"2021-10-30T11:14:55.000Z\",\"2021-10-30T12:24:55.000Z\",\"2021-10-30T13:34:55.000Z\",\"2021-10-30T14:45:56.000Z\",\"2021-10-30T15:57:50.000Z\"]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:25:18.000Z\",\"2021-10-30T02:37:50.000Z\",\"2021-10-30T03:50:58.000Z\",\"2021-10-30T05:00:58.000Z\",\"2021-10-30T06:10:58.000Z\",\"2021-10-30T07:20:58.000Z\",\"2021-10-30T08:30:58.000Z\",\"2021-10-30T09:40:58.000Z\",\"2021-10-30T10:50:58.000Z\",\"2021-10-30T12:00:58.000Z\",\"2021-10-30T13:10:58.000Z\",\"2021-10-30T14:19:52.000Z\",\"2021-10-30T00:50:17.000Z\",\"2021-10-30T02:00:35.000Z\",\"2021-10-30T03:13:11.000Z\",\"2021-10-30T04:25:57.000Z\",\"2021-10-30T05:35:57.000Z\",\"2021-10-30T06:45:57.000Z\",\"2021-10-30T07:55:57.000Z\",\"2021-10-30T09:05:57.000Z\",\"2021-10-30T10:15:57.000Z\",\"2021-10-30T11:25:57.000Z\",\"2021-10-30T12:35:57.000Z\",\"2021-10-30T13:45:57.000Z\",\"2021-10-30T14:54:51.000Z\",\"2021-10-30T00:31:23.000Z\",\"2021-10-30T01:40:37.000Z\",\"2021-10-30T02:49:07.000Z\",\"2021-10-30T03:59:32.000Z\",\"2021-10-30T05:09:32.000Z\",\"2021-10-30T06:19:32.000Z\",\"2021-10-30T07:29:32.000Z\",\"2021-10-30T08:39:32.000Z\",\"2021-10-30T09:49:32.000Z\",\"2021-10-30T10:59:32.000Z\",\"2021-10-30T12:09:32.000Z\",\"2021-10-30T13:19:32.000Z\",\"2021-10-30T14:27:50.000Z\",\"2021-10-30T15:33:08.000Z\",\"2021-10-30T16:40:15.000Z\",\"2021-10-30T01:07:30.000Z\",\"2021-10-30T02:18:45.000Z\",\"2021-10-30T03:31:55.000Z\",\"2021-10-30T04:43:10.000Z\",\"2021-10-30T05:53:10.000Z\",\"2021-10-30T07:03:10.000Z\",\"2021-10-30T08:13:10.000Z\",\"2021-10-30T09:23:10.000Z\",\"2021-10-30T10:33:10.000Z\",\"2021-10-30T11:43:10.000Z\",\"2021-10-30T12:53:10.000Z\",\"2021-10-30T14:03:10.000Z\",\"2021-10-30T15:15:24.000Z\",\"2021-10-30T16:26:08.000Z\",\"2021-10-30T02:58:40.000Z\",\"2021-10-30T04:08:28.000Z\",\"2021-10-30T05:18:28.000Z\",\"2021-10-30T06:28:28.000Z\",\"2021-10-30T07:38:28.000Z\",\"2021-10-30T08:48:28.000Z\",\"2021-10-30T09:58:28.000Z\",\"2021-10-30T11:08:28.000Z\",\"2021-10-30T12:18:28.000Z\",\"2021-10-30T13:28:28.000Z\",\"2021-10-30T14:38:28.000Z\",\"2021-10-30T15:46:32.000Z\",\"2021-10-30T16:48:35.000Z\",\"2021-10-30T03:22:36.000Z\",\"2021-10-30T04:34:43.000Z\",\"2021-10-30T05:44:43.000Z\",\"2021-10-30T06:54:43.000Z\",\"2021-10-30T08:04:43.000Z\",\"2021-10-30T09:14:43.000Z\",\"2021-10-30T10:24:43.000Z\",\"2021-10-30T11:34:43.000Z\",\"2021-10-30T12:44:43.000Z\",\"2021-10-30T13:54:43.000Z\",\"2021-10-30T15:04:15.000Z\",\"2021-10-30T16:14:26.000Z\",\"2021-10-30T03:41:34.000Z\",\"2021-10-30T04:52:13.000Z\",\"2021-10-30T06:02:13.000Z\",\"2021-10-30T07:12:13.000Z\",\"2021-10-30T08:22:13.000Z\",\"2021-10-30T09:32:13.000Z\",\"2021-10-30T10:42:13.000Z\",\"2021-10-30T11:52:13.000Z\",\"2021-10-30T13:02:13.000Z\",\"2021-10-30T14:13:11.000Z\",\"2021-10-30T15:23:26.000Z\",\"2021-10-30T04:17:13.000Z\",\"2021-10-30T05:27:13.000Z\",\"2021-10-30T06:37:13.000Z\",\"2021-10-30T07:47:13.000Z\",\"2021-10-30T08:57:13.000Z\",\"2021-10-30T10:07:13.000Z\",\"2021-10-30T11:17:13.000Z\",\"2021-10-30T12:27:13.000Z\",\"2021-10-30T13:37:13.000Z\",\"2021-10-30T14:48:28.000Z\",\"2021-10-30T16:00:08.000Z\"]}}}],\"location_id\":\"CCUV\",\"matched\":true},{\"id\":\"PDPM_TO_KLMT\",\"descriptor\":{\"name\":\"Pathadipalam
		// to
		// Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"20\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true},{\"id\":\"PDPM_TO_CCUV\",\"descriptor\":{\"name\":\"Pathadipalam
		// to
		// Cusat\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true}]}]}}}";

		MessageDigest digest = MessageDigest.getInstance("BLAKE2B-512", BouncyCastleProvider.PROVIDER_NAME);
		digest.reset();
		digest.update(req.getBytes(StandardCharsets.UTF_8));
		byte[] hash = digest.digest();
		String bs64 = Base64.getEncoder().encodeToString(hash);
		System.out.println(bs64);
		return bs64;

		// System.out.println(toHex(hash)); // We could use bs64 or hex of the hash (Not
		// sure why we need to hex and then base64 it!! Seems overkill.
		// https://8gwifi.org/MessageDigest.jsp

	}
    /**
     * This method verifying sign by sign, request data and public key. 
     * @param sign  It is a unique sign.
     * @param requestData It is a String.
     * @param dbPublicKey It is a unique key.
     * @return Its returns isVerifies boolean.
     */
	public static boolean verifySignaturePK(String sign, String requestData, String dbPublicKey) {
		boolean isVerified = false;
		try {
			System.out.println("Sign : " + sign + " requestData : " + requestData + " PublicKey : " + dbPublicKey);
			// Ed25519PublicKeyParameters publicKey = new
			// Ed25519PublicKeyParameters(Hex.decode(dbPublicKey), 0);
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
    /**
     * This method generating signature by String req and public key.
     * @param req It is a String.
     * @param pk It is a unique key.
     * @return Its returns generated signature.
     */
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
    /**
     * This method generates signature plan by String req and public key.
     * @param req It is a String.
     * @param pk It is a public key.
     * @return Its returns generated signature.
     */
	public static String generateSignaturePlan(String req, String pk) {
		String signature = null;
		try {

			long testTimestamp = System.currentTimeMillis() / 1000L;

			String blakeValue;

			blakeValue = generateBlakeHash(req);

			String signedReq = generateSignaturePK(blakeValue, pk);

			signature = signedReq;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}
    /**
     * This method verify signature by sign, String request data and public key.
     * @param sign It is a unique sign.
     * @param requestData It is a String.
     * @param dbPublicKey It is a unique key.
     * @return Its returns isVerified boolean value.
     */
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
				log.info(e.getMessage());
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
				log.info("Signature is Verified.");
				return "Is Sign Verified : " + isVerified;

			} else {
				log.info("Signature is expired.");
				return "Signature is expired.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
    /**
     * This method verify signature plan by sign, String request data and public key.
     * @param sign It is a unique key.
     * @param requestData It is String request data.
     * @param dbPublicKey It is a unique key.
     * @return Its returns isVerified boolean value.
     */
	public static String verifySignaturePlan(String sign, String requestData, String dbPublicKey) {
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
    /**
     * This method generate key.
     * @return Its returns generated key.
     */
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
