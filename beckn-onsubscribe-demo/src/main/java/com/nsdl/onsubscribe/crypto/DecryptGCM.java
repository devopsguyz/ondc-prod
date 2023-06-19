package com.nsdl.onsubscribe.crypto;

import com.nsdl.onsubscribe.utl.EncryptionUtil;
import com.nsdl.onsubscribe.utl.KeyUtil;
import org.springframework.stereotype.Component;

@Component
public class DecryptGCM {

    public static String clientPrivateKey = "MFECAQEwBQYDK2VuBCIEIGDMqcp4yHbFFcH2eTqPK7pOUerhubc0Xggqfps9H8ZDgSEAHH8kACAM75hrDMdSjX3sfulqVdEE0f3ZOpy0EtVR3wg=";
    public static String proteanPublicKey = "MCowBQYDK2VuAyEALtPj74XkIrkyxTqyssjtYJ3KRND5FnzK5MDrwlK3kC8=";


    /**
     * This method decrypts String value by the use of clientPrivateKey and proteanPublicKey.
     * First it generates sharedKey using generateSharedKey method of KeyUtil. And then decrypt
     * the value using decryptData method of EncryptionUtil.
     *
     * @param clientPrivateKey It is a unique key.
     * @param proteanPublicKey It is a unique key.
     * @param value            It is a String.
     * @return Its returns decrypted String based on the
     *         clientPrivateKey,proteanPublicKey value.
     */
    public String decrypt(String clientPrivateKey, String proteanPublicKey, String value) {
        String decryptedData = null;

        try {
            String sharedKey = KeyUtil.generateSharedKey(clientPrivateKey, proteanPublicKey);
            decryptedData =  EncryptionUtil.decryptData(sharedKey, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedData != null ? decryptedData : "";
    }
}
