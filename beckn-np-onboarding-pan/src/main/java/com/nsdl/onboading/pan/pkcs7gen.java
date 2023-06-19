package com.nsdl.onboading.pan;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import java.util.Enumeration;
import java.util.ArrayList;
import org.bouncycastle.util.encoders.Base64;

import lombok.extern.slf4j.Slf4j;

import java.security.cert.*;

@Slf4j
public class pkcs7gen {

	public static String genSig(String jksFile, String pwd, String userid, String pan) throws Exception {

		KeyStore keystore = KeyStore.getInstance("jks");
		InputStream input = new FileInputStream(jksFile);
		try {
			char[] password = pwd.toCharArray();
			keystore.load(input, password);
		} catch (IOException e) {
			log.info("genSig:keystore load:"+e.getMessage());

		} 
		Enumeration e = keystore.aliases();
		String alias = "";

		if (e != null) {
			while (e.hasMoreElements()) {
				String n = (String) e.nextElement();
				if (keystore.isKeyEntry(n)) {
					alias = n;
				}
			}
		}
		PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, pwd.toCharArray());
		X509Certificate myPubCert = (X509Certificate) keystore.getCertificate(alias);
		byte[] dataToSign = (userid.trim()+"^^"+pan.trim()).getBytes();
		CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		sgen.addSigner(privateKey, myPubCert, CMSSignedDataGenerator.DIGEST_SHA1);
		Certificate[] certChain = keystore.getCertificateChain(alias);
		ArrayList certList = new ArrayList();
		CertStore certs = null;
		for (int i = 0; i < certChain.length; i++)
			certList.add(certChain[i]);
		sgen.addCertificatesAndCRLs(
				CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC"));
		CMSSignedData csd = sgen.generate(new CMSProcessableByteArray(dataToSign), true, "BC");
		byte[] signedData = csd.getEncoded();
		byte[] signedData64 = Base64.encode(signedData);
	 
	   return new String(signedData64);
	}
}
