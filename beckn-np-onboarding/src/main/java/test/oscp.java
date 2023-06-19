package test;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.impl.CryptoServiceImpl;

public class oscp {
	public String checkOCSP(Web web) {
		URL destinationURL;
		try {
			if (web.getDomain().indexOf("http") == -1) {
				web.setDomain("https://" + web.getDomain());
			}
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

						x.checkValidity();

						System.out.println(x.getIssuerDN());
						// log.info("OSCP Valid : " + x.getIssuerDN());
						return "Valid";
					} catch (CertificateExpiredException e) {
						e.printStackTrace();
						// log.info("OSCP Invalid : " + e.getMessage());
						return "CertificateExpired";

						// TODO: handle exception
					} catch (CertificateNotYetValidException e) {
						// TODO: handle exception
						// log.info("OSCP Invalid : " + e.getMessage());
						e.printStackTrace();
						return "CertificateNotYetValid";

					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// log.info("OSCP Invalid : " + e.getMessage());
			e.printStackTrace();
		}
		// log.info("OSCP : Fail");
		return "Not Valid";
	}

	public static void main(String[] args) {
		Web web = new Web();
		if ((args != null) && (args.length >= 1)) {
			web.setDomain(args[0]);
		} else {
			web.setDomain("https://b-ondc-seller-bpp.nlincs.io");
		}
		System.out.println(new CryptoServiceImpl().checkOCSP(web, new HashMap<String, String>()));
	}
}
