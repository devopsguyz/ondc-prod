package com.nsdl.signing.test;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

public class OCSPTest {

	public static void main(String[] args) {
		URL destinationURL;
		try {
			destinationURL = new URL("https://www.google.com/");

			HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
			conn.connect();
			Certificate[] certs = conn.getServerCertificates();
			for (Certificate cert : certs) {

			//	System.out.println("Certificate is: " + cert);
				if (cert instanceof X509Certificate) {
					X509Certificate x = (X509Certificate) cert;
					try {

						x.checkValidity();
						System.out.println("Validate");
					} catch (CertificateExpiredException e) {
						e.printStackTrace();
						// TODO: handle exception
					} catch (CertificateNotYetValidException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					System.out.println(x.getIssuerDN());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
