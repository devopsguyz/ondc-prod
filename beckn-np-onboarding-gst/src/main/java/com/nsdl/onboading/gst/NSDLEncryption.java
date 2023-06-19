package com.nsdl.onboading.gst;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.json.JSONException;
import org.json.JSONObject;

public class NSDLEncryption {

	public static final String appKey = "GRbBNi4QXETc/dtKeTB7WjCITT2t4RgDRuwG515FQ5o=";
	public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	public static final String AES_ALGORITHM = "AES";
	public static final int ENC_BITS = 256;
	public static final String CHARACTER_ENCODING = "UTF-8";

	private static Cipher ENCRYPT_CIPHER;
	private static Cipher DECRYPT_CIPHER;
	private static KeyGenerator KEYGEN;

	static {
		try {
			ENCRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			DECRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			KEYGEN = KeyGenerator.getInstance(AES_ALGORITHM);
			KEYGEN.init(ENC_BITS);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to encode bytes[] to base64 string.
	 * 
	 * @param bytes : Bytes to encode
	 * @return : Encoded Base64 String
	 */
	public static String encodeBase64String(byte[] bytes) {
		return new String(java.util.Base64.getEncoder().encode(bytes));
	}

	/**
	 * This method is used to decode the base64 encoded string to byte[]
	 * 
	 * @param stringData : String to decode
	 * @return : decoded String
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] decodeBase64StringTOByte(String stringData) throws Exception {
		return java.util.Base64.getDecoder().decode(stringData.getBytes(CHARACTER_ENCODING));
	}

	/**
	 * This method is used to generate the base64 encoded secure AES 256 key *
	 * 
	 * @return : base64 encoded secure Key
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String generateSecureKey() throws Exception {
		SecretKey secretKey = KEYGEN.generateKey();
		return encodeBase64String(secretKey.getEncoded());
	}

	/**
	 * This method is used to encrypt the string which is passed to it as byte[] and
	 * return base64 encoded encrypted String
	 * 
	 * @param plainText : byte[]
	 * @param secret    : Key using for encrypt
	 * @return : base64 encoded of encrypted string.
	 * 
	 */

	public static String encryptEK(byte[] plainText, byte[] secret) {
		try {

			SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
			ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sk);
			return Base64.getEncoder().encodeToString(ENCRYPT_CIPHER.doFinal(plainText));

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String base64EncryptEk(String plainText, byte[] secret) {
		try {
			SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
			String encodedString = Base64.getEncoder().encodeToString(plainText.getBytes());
			ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sk);
			return  Base64.getEncoder().encodeToString(ENCRYPT_CIPHER.doFinal(encodedString.getBytes()));

		} catch (Exception ex) {
			System.out.println(" Error : ");
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * This method is used to decrypt base64 encoded string using an AES 256 bit
	 * key.
	 * 
	 * @param plainText : plain text to decrypt
	 * @param secret    : key to decrypt
	 * @return : Decrypted String
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] decrypt(String plainText, byte[] secret)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, Exception {
		SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
		DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, sk);
		return DECRYPT_CIPHER.doFinal( Base64.getDecoder().decode(plainText));
	}

	public static String encryptOTP(String otp) {
		String encryptedOtp = null;
		try {

			encryptedOtp = encryptEK(otp.getBytes(), decodeBase64StringTOByte(appKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedOtp;
	}

	public static byte[] generatePaddedSek(String sek, String decAppKey) throws Exception {
		byte[] decSek = decrypt(sek, decodeBase64StringTOByte(decAppKey));
		return decSek;
	}

	public static String BCHmac(byte[] data, byte[] Ek) {
		HMac hmac = new HMac(new SHA256Digest());

		byte[] resBuf = new byte[hmac.getMacSize()];
		hmac.init(new KeyParameter(Ek));
		hmac.update(data, 0, data.length);
		hmac.doFinal(resBuf, 0);

		return encodeBase64String(resBuf);
	}

	public static byte[] getJsonBase64Payload(String str) {

		JSONObject obj = null;
		try {
			obj = new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Base64.encodeBase64(obj.toString());
		// String en64 =
		// encodeBase64String(str.toString().getBytes(StandardCharsets.UTF_8));

		// return en64.getBytes(StandardCharsets.UTF_8);
		return Base64.getEncoder().encode(obj.toString().getBytes());
	}

	public static byte[] genDecryptedREK(String rek, byte[] ek)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception {
		byte[] encRek = decrypt(rek, ek);
		System.out.println("Decrypted Rek : " + encodeBase64String(encRek));

		return encRek;
	}

	public static String decryptGstrData(String gstrResp, byte[] encRek)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception {
		String originalData = new String(decodeBase64StringTOByte((new String((decrypt(gstrResp, encRek))))));
		return originalData;
	}

	public static void main(String[] args) throws JSONException {
		// OTPREQUEST();

		getAuthToken("575757");

	}

	public static void getAuthToken(String otp) {
		// String otp = "356023";
		String encOtp = NSDLEncryption.encryptOTP(otp);
		System.out.println("encOTP : " + encOtp);

//			String token = Authentication.AuthToken(encOtp);
//			System.out.println("token:" + token);
	}
}