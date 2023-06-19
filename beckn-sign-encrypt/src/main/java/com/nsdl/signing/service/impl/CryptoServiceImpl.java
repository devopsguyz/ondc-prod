package com.nsdl.signing.service.impl;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.signing.crypto.ISubscribeEncryptDecrypt;
import com.nsdl.signing.crypto.SubscribeEncryptDecryptGCM;
import com.nsdl.signing.utl.GCMKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nsdl.signing.crypto.EncryptDecrypt;
import com.nsdl.signing.crypto.GeneratePayload;
import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestData;
import com.nsdl.signing.model.RequestEncDecryptData;
import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.CryptoService;

@Service
public class CryptoServiceImpl implements CryptoService {

	@Autowired
	GeneratePayload generatePayload;

	@Autowired
	EncryptDecrypt encryptDecrypt;

	@Value("${enc.algo}")
	private String encAlgo;

	@Override
	public String generateSignature(RequestData request) {
		// TODO Auto-generated method stub

		return this.generatePayload.generateSignature(request.getRequestData(), request.getClientPrivateKey());
	}

	@Override
	public String generateSignaturePlan(RequestData request) {
		// TODO Auto-generated method stub

		return this.generatePayload.generateSignaturePlan(request.getRequestData(), request.getClientPrivateKey());
	}

	@Override
	public String verifySignature(RequestData request) {
		// TODO Auto-generated method stub
		return this.generatePayload.verifySignature(request.getSign(), request.getRequestData(),
				request.getClientPublicKey());
	}

	@Override
	public String verifySignaturePlan(RequestData request) {
		// TODO Auto-generated method stub
		return this.generatePayload.verifySignaturePlan(request.getSign(), request.getRequestData(),
				request.getClientPublicKey());
	}

	@Override
	public String encryptText(RequestEncDecryptData request) {
		ISubscribeEncryptDecrypt encDec;
		if ("GCM".equals(encAlgo)) {
			encDec = new SubscribeEncryptDecryptGCM();
			return encDec.encrypt(request.getProteanPublicKey(), request.getClientPrivateKey(), request.getValue());
		} else {
			return this.encryptDecrypt.encrypt(request);
		}
	}

	@Override
	public String decryptText(RequestEncDecryptData request) {
		ISubscribeEncryptDecrypt encDec;
		if ("GCM".equals(encAlgo)) {
			encDec = new SubscribeEncryptDecryptGCM();
			return encDec.decrypt(request.getProteanPrivateKey(), request.getClientPublicKey(), request.getValue());
		} else {
			return this.encryptDecrypt.decrypt(request);
		}
	}

	@Override
	public KeyData generateSignKey() {
		return this.generatePayload.generateKey();
	}

	@Override
	public KeyData generateEncrypDecryptKey() {
		if ("GCM".equals(encAlgo)) {
			return new ObjectMapper().convertValue(GCMKeyUtil.generateKeyPair(), KeyData.class);
		} else {
			return this.encryptDecrypt.generateKey();
		}
	}

	@Override
	public String checkOCSP(Web web) {
		URL destinationURL;
		try {
			if (web.getDomain().indexOf("http") == -1) {
				web.setDomain("https://" + web.getDomain());
			}
			destinationURL = new URL(web.getDomain());

			HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
			conn.connect();
			Certificate[] certs = conn.getServerCertificates();
			for (Certificate cert : certs) {

				// System.out.println("Certificate is: " + cert);
				if (cert instanceof X509Certificate) {
					X509Certificate x = (X509Certificate) cert;
					try {
						x.checkValidity();
						System.out.println(x.getIssuerDN());
						return "Valid";
					} catch (CertificateExpiredException e) {
						e.printStackTrace();
						return "CertificateExpired";

						// TODO: handle exception
					} catch (CertificateNotYetValidException e) {
						// TODO: handle exception
						e.printStackTrace();
						return "CertificateNotYetValid";

					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Not Valid";
	}
}
