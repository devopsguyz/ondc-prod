package com.nsdl.beckn.klm;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.NoIvGenerator;

public class test {
	public static void main(String[] args) {
		System.setProperty("JASYPT_ENCRYPTOR_PASSWORD", "MY_SECRET");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("MY_SECRET");
		config.setAlgorithm("PBEWithMD5AndDES");

		config.setPoolSize("1");
		config.setIvGenerator(new NoIvGenerator());
		config.setProviderName("SunJCE");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		System.out.println(encryptor.encrypt("pwd@1234"));
		System.out.println("Server:" + encryptor.encrypt("Prote@nrgOndc123"));
		System.out.println(encryptor.decrypt("ogSNeQYWwyL8qTXXLRU3x6zm0Rc/NaiE"));
//	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//	textEncryptor.setPassword(myEncryptionPassword);
	}
}
