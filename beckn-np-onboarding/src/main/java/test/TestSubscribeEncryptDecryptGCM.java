package test;

import com.nsdl.signing.crypto.SubscribeEncryptDecryptGCM;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import java.security.Security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestSubscribeEncryptDecryptGCM {

    private static final String PLAIN_TEXT = "6db65041-de15-484a-ba74-1086c2ab15c5";

    private static final String CLIENT_PRIVATE_KEY = "MC4CAQAwBQYDK2VuBCIEIPNxJhWiObr8T1WgN53gWjKULs6wKBJnVxX3aYtrK8JS";
    private static final String CLIENT_PUBLIC_KEY = "MCowBQYDK2VuAyEABxp9W+BXIBcAXIqdW48NkNXcfn1jyjXhowwreql3Pk0=";

    private static final String PROTEAN_PUBLIC_KEY = "MCowBQYDK2VuAyEAjVFpmmVk5QB61NmPrmG2sQl4aUzuRud0wOH69YrVBmI=";
    private static final String PROTEAN_PRIVATE_KEY = "MFECAQEwBQYDK2VuBCIEILjmcSrhn/fF1VuyBfnxOVHiec0lv5Slw9NvAcbL7oxTgSEAjVFpmmVk5QB61NmPrmG2sQl4aUzuRud0wOH69YrVBmI=";

    private static final String WRONG_TEST_KEY = "This is a wrong key for failing the test";

    private static final SubscribeEncryptDecryptGCM SUBSCRIBE_ENCRYPT_DECRYPT_GCM = new SubscribeEncryptDecryptGCM();

    @Before
    public void setup() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Test
    public void testSuccessDecryption() {
        String encryptedText = SUBSCRIBE_ENCRYPT_DECRYPT_GCM.encrypt(CLIENT_PUBLIC_KEY, PROTEAN_PRIVATE_KEY, PLAIN_TEXT);
        assertEquals(PLAIN_TEXT, SUBSCRIBE_ENCRYPT_DECRYPT_GCM.decrypt(CLIENT_PRIVATE_KEY, PROTEAN_PUBLIC_KEY, encryptedText));
    }

    @Test
    public void testFailDecryption() {
        String encryptedText = SUBSCRIBE_ENCRYPT_DECRYPT_GCM.encrypt(CLIENT_PUBLIC_KEY, PROTEAN_PRIVATE_KEY, PLAIN_TEXT);
        assertNotEquals(PLAIN_TEXT, SUBSCRIBE_ENCRYPT_DECRYPT_GCM.decrypt(WRONG_TEST_KEY, PROTEAN_PUBLIC_KEY, encryptedText));
    }
}
