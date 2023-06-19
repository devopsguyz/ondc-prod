package com.nsdl.signing.service.impl;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsdl.signing.crypto.EncryptDecrypt;
import com.nsdl.signing.crypto.GeneratePayload;
import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.RequestData;
import com.nsdl.signing.model.RequestEncDecryptData;
import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.CryptoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CryptoServiceImpl implements CryptoService {

	@Autowired
	GeneratePayload generatePayload;

	@Autowired
	EncryptDecrypt encryptDecrypt;

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
		// TODO Auto-generated method stub

		return this.encryptDecrypt.encrypt(request);
	}

	@Override
	public String decryptText(RequestEncDecryptData request) {
		// TODO Auto-generated method stub

		return this.encryptDecrypt.decrypt(request);
	}

	@Override
	public KeyData generateSignKey() {
		return this.generatePayload.generateKey();
	}

	@Override
	public KeyData generateEncrypDecryptKey() {
		return this.encryptDecrypt.generateKey();
	}

	@Override
	public String checkOCSP(Web web, Map<String, String> error) {
		URL destinationURL;
		try {
			if (web.getDomain().indexOf("http") == -1) {
				web.setDomain("https://" + web.getDomain());
			}

//			javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> {
//				return hostname.equals("localhost");
//			});

			destinationURL = new URL(web.getDomain());
			HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
			conn.setConnectTimeout(60 * 1000);
			conn.connect();
			Certificate[] certs = conn.getServerCertificates();
			for (Certificate cert : certs) {

				// System.out.println("Certificate is: " + cert);
				if (cert instanceof X509Certificate) {
					X509Certificate x = (X509Certificate) cert;
					try {
						if (x != null) {
							error.put("OSCP_CertificateDtl:", x.toString());
						}
						x.checkValidity();

						System.out.println(x.getIssuerDN());
						log.info("OSCP Valid : " + x.getIssuerDN());
						return "Valid";
					} catch (CertificateExpiredException e) {
						e.printStackTrace();
						error.put("OSCP_CertificateExpiredException:", e.getMessage());
						log.info("OSCP Invalid : " + e.getMessage());
						return "CertificateExpired";

						// TODO: handle exception
					} catch (Exception e) {
						// TODO: handle exception
						error.put("OSCP_CertificateNotYetValidException:", e.getMessage());

						log.info("OSCP Invalid : " + e.getMessage());
						e.printStackTrace();
						return "CertificateNotYetValid";

					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			error.put("OSCP_Exception:", e.getMessage());

			log.info("OSCP Invalid : " + e.getMessage());
			e.printStackTrace();
		}
		log.info("OSCP : Fail");
		error.put("OSCP:", "Not Valid");
		return "Not Valid";
	}

	public static void main(String[] args) {
		Web web = new Web();
		if ((args != null) && (args.length >= 1)) {
			web.setDomain(args[0]);
		} else {
			web.setDomain("https://b-ondc-seller-bpp.nlincs.io");
		}
		System.out.println(new CryptoServiceImpl().checkOCSP(web, new HashMap<>()));
	}
}
