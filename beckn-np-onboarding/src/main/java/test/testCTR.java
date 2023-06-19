package test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class testCTR {
	private static final String initVector = "encryptionIntVec";

	 
	public static IvParameterSpec getIV( ) {
		try {
			return new IvParameterSpec(initVector.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			SecureRandom secureRandom = new SecureRandom();
	 		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			// Then generate the key. Can be 128, 192 or 256 bit
			byte[] key = new byte[256 / 8];
			secureRandom.nextBytes(key);
			// Now generate a nonce. You can also use an ever-increasing counter, which is
			// even more secure. NEVER REUSE A NONCE!

			Key keySpec = new SecretKeySpec(key, "AES");
			IvParameterSpec ivSpec = getIV();

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			byte[] plaintext = "Hello World CTR".getBytes(StandardCharsets.UTF_8);
			byte[] ciphertext = cipher.doFinal(plaintext);
			System.out.println("ENCR:" + Base64.getEncoder().encodeToString(ciphertext));
			ivSpec = getIV();
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] plaintext1 = cipher.doFinal(ciphertext);
			String plaintextString = new String(plaintext1, StandardCharsets.UTF_8);
			System.out.println("DECR:" + plaintextString);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
