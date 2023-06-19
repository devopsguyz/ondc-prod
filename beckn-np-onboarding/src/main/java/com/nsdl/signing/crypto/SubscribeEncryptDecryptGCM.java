package com.nsdl.signing.crypto;

import java.security.Security;

import com.nsdl.signing.utl.GCMEncryptionUtil;
import com.nsdl.signing.utl.GCMKeyUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SubscribeEncryptDecryptGCM implements ISubscribeEncryptDecrypt{

    public static void setup() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * This method encrypts clientPublicKey,proteanPrivateKey and value.
     * It takes the clientPublicKey and proteanPrivateKey and generates a shared key.
     * This shared key combines the value and returns an encrypted string.
     * Algorithm used is AES/GCM/NoPadding.
     *
     * @param clientPublicKey   It is unique key.
     * @param proteanPrivateKey It is unique key.
     * @param value             It is a String.
     * @return encrypted value  It is a String.
     */
    @Override
    public String encrypt(String clientPublicKey, String proteanPrivateKey, String value) {
        String encryptedData = null;

        try {
            String sharedKey = GCMKeyUtil.generateSharedKey(proteanPrivateKey, clientPublicKey);
            encryptedData = GCMEncryptionUtil.encryptData(sharedKey, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedData != null ? encryptedData : "";
    }

    /**
     * This method decrypts String value by the use of clientPrivateKey and proteanPublicKey.
     * First it generates sharedKey using generateSharedKey method of GCMKeyUtil. And then decrypt
     * the value using decryptData method of GCMEncryptionUtil.
     *
     * @param clientPrivateKey It is a unique key.
     * @param proteanPublicKey It is a unique key.
     * @param value            It is a String.
     * @return Its returns decrypted String based on the
     *         clientPrivateKey,proteanPublicKey value.
     */
    @Override
    public String decrypt(String clientPrivateKey, String proteanPublicKey, String value) {
        String decryptedData = null;

        try {
            String sharedKey = GCMKeyUtil.generateSharedKey(clientPrivateKey, proteanPublicKey);
            decryptedData =  GCMEncryptionUtil.decryptData(sharedKey, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedData != null ? decryptedData : "";
    }
}
