package com.nsdl.signing.crypto;

public interface ISubscribeEncryptDecrypt {

    String encrypt(String clientPublicKey, String proteanPrivateKey, String value);
    String decrypt(String clientPrivateKey, String proteanPublicKey, String value);
}
