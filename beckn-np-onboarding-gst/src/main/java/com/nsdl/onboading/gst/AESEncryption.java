package com.nsdl.onboading.gst; 
/*******************************************************************************
 * DISCLAIMER: The sample code or utility or tool described herein
 *    is provided on an "as is" basis, without warranty of any kind.
 *    GSTN does not warrant or guarantee the individual success
 *    developers may have in implementing the sample code on their
 *    environment. 
 *    
 *    GSTN  does not warrant, guarantee or make any representations
 *    of any kind with respect to the sample code and does not make
 *    any representations or warranties regarding the use, results
 *    of use, accuracy, timeliness or completeness of any data or
 *    information relating to the sample code. UIDAI disclaims all
 *    warranties, express or implied, and in particular, disclaims
 *    all warranties of merchantability, fitness for a particular
 *    purpose, and warranties related to the code, or any service
 *    or software related thereto. 
 *    
 *   GSTN  is not responsible for and shall not be liable directly
 *    or indirectly for any direct, indirect damages or costs of any
 *    type arising out of use or any action taken by you or others
 *    related to the sample code.
 *    
 *    THIS IS NOT A SUPPORTED SOFTWARE.
 ******************************************************************************/

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

public class AESEncryption 
{
	public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	public static final String AES_ALGORITHM = "AES";
	public static final int ENC_BITS = 256;
	public static final String CHARACTER_ENCODING = "UTF-8";
	
	private static Cipher ENCRYPT_CIPHER;
	private static Cipher DECRYPT_CIPHER;
	private static KeyGenerator KEYGEN;	
	
	static
	{
		try
		{
			ENCRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			DECRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			KEYGEN = KeyGenerator.getInstance(AES_ALGORITHM);
			KEYGEN.init(ENC_BITS);
		}
		catch(NoSuchAlgorithmException | NoSuchPaddingException e) 
		{
			e.printStackTrace();
		}
	}

	/**
     * This method is used to encode bytes[] to base64 string.
     * 
     * @param bytes
     *            : Bytes to encode
     * @return : Encoded Base64 String
     */
	
   private static String encodeBase64String(byte[] bytes) 
   {
         return new String(java.util.Base64.getEncoder().encode(bytes));
   }
   /**
    * This method is used to decode the base64 encoded string to byte[]
    * 
    * @param stringData
    *            : String to decode
    * @return : decoded String
    * @throws UnsupportedEncodingException
    */
   public static byte[] decodeBase64StringTOByte(String stringData) throws Exception 
   {
		return java.util.Base64.getDecoder().decode(stringData.getBytes(CHARACTER_ENCODING));
   } 
   
    /**
    * This method is used to encrypt the string which is passed to it as byte[] and return base64 encoded
    * encrypted String
    * @param plainText
    *            : byte[]
    * @param secret
    *            : Key using for encrypt
    * @return : base64 encoded of encrypted string.
    * 
    */
	
	private static String encryptEK(byte[] plainText, byte[] secret)
	{
		try
		{
			SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
			ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sk);
			return Base64.getEncoder().encodeToString(ENCRYPT_CIPHER.doFinal(plainText));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	/**
    * This method is used to decrypt base64 encoded string using an AES 256 bit key.
    * 
     * @param plainText
    *            : plain text to decrypt
    * @param secret
    *            : key to decrypt
    * @return : Decrypted String
    * @throws IOException
    * @throws InvalidKeyException
    * @throws BadPaddingException
    * @throws IllegalBlockSizeException
    */
    
	public static byte[] decrypt(String plainText, byte[] secret)
                throws InvalidKeyException, IOException, IllegalBlockSizeException,
                BadPaddingException,Exception 
	{
		SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
		DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, sk);		
        return DECRYPT_CIPHER.doFinal(Base64.getDecoder().decode(plainText));
    }
    
    /**
     * This method is used to generate the base64 encoded secure AES 256 key     * 
     * @return : base64 encoded secure Key
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
 	
	private static String generateSecureKey() throws Exception
	{
 		SecretKey secretKey = KEYGEN.generateKey();
 		return encodeBase64String(secretKey.getEncoded());
 	}
 	   
 	//This is generate enc_app_key which is encrypted by public GSTN Certificate
	private static void produceSampleData() 
	{
		try 
		{
			System.out.println("@@inside produceSampleData..");
			
			String appkey = generateSecureKey();
			System.out.println("App key in encoded : " + appkey);
		
			String encryptedAppkey = EncryptionUtil.generateEncAppkey(decodeBase64StringTOByte(appkey));
			System.out.println("Encrypted App Key : " + encryptedAppkey);
			
			//Password for common API only
			String pass = "Api@706com";
			String passEnc = EncryptionUtil.generateEncAppkey(pass.getBytes());
			System.out.println("passEnc : " + passEnc);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
    
	public static void main(String args[])throws Exception
	{
		produceSampleData();
	}
}
