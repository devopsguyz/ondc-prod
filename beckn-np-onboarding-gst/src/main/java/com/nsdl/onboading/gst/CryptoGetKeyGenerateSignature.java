package com.nsdl.onboading.gst;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Enumeration;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.util.Store;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;

@Slf4j
public class CryptoGetKeyGenerateSignature {

	private static char JKSPassword[];
	private static char PFXPassword[];
	private static KeyStore ks = null;
	private static String alias = null;
	private static X509Certificate UserCert = null;
	private static PrivateKey UserCertPrivKey = null;
	private static PublicKey UserCertPubKey = null;
	private static X509Certificate myPubCert = null;

	static {
		JKSPassword = "".toCharArray();
		PFXPassword = "tcs".toCharArray();
	}

	public CryptoGetKeyGenerateSignature() {

	}

	public static void getKeySignature(String aspId,String JKSPasswordArg, String PFXPasswordArg, String jksFilepath,
			Gst gst) {
		// String aspId ="10032017111549010101";
		String ts = "";

		if (JKSPasswordArg == null) {
			JKSPasswordArg = "";

		}
		if (PFXPasswordArg == null) {
			PFXPasswordArg = "";

		}
		JKSPassword = JKSPasswordArg.toCharArray();
		PFXPassword = PFXPasswordArg.toCharArray();

		ts = getCurrTs();
		gst.setTimestamp(ts);

		log.info("AspId : " + aspId);
		log.info("TimeStamp : " + ts);

		String aspData = aspId + ts;

		try {
			String sign = generateSignature(aspData, jksFilepath, gst);
			log.info("sign:" + sign);
			gst.setSign(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String generateSignature(String data, String jksFilepath, Gst gst) throws Exception {

		log.info("@@inside generateSignature: " + data);

		String signature;

		try {
			// Adding Security Provider for PKCS 12
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			// Setting password for the e-Token

			// logging into token
			ks = KeyStore.getInstance("jks");

			FileInputStream fileInputStream = new FileInputStream(jksFilepath);

			// Loading Keystore
			// log.info("loading keystore");
			ks.load(fileInputStream, JKSPassword);
			Enumeration<String> e = ks.aliases();

			while (e.hasMoreElements()) {
				alias = e.nextElement();
				// log.info("Alias of the e-Token : "+ alias);

				UserCert = (X509Certificate) ks.getCertificate(alias);

				UserCertPubKey = (PublicKey) ks.getCertificate(alias).getPublicKey();

				// log.info("loading Private key");
				UserCertPrivKey = (PrivateKey) ks.getKey(alias, JKSPassword);
			}

			// Method Call to generate Signature
			signature = MakeSignature(data);

			return signature;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("generateSignature" + e.getCause());
			throw new Exception();
		}

	}

	private static String MakeSignature(String data) {

		log.info("@@inside MakeSignature...");

		try {
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, JKSPassword);
			myPubCert = (X509Certificate) ks.getCertificate(alias);
			Store certs = new JcaCertStore(Arrays.asList(myPubCert));

			CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

			generator.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC")
					.build("SHA256withRSA", privateKey, myPubCert));

			generator.addCertificates(certs);

			CMSTypedData data1 = new CMSProcessableByteArray(data.getBytes());

			CMSSignedData signed = generator.generate(data1, true);

			String signedContent = Base64.getEncoder().encodeToString((byte[]) signed.getSignedContent().getContent());

			String envelopedData = Base64.getEncoder().encodeToString(signed.getEncoded());

			return envelopedData;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("MakeSignature ==" + e.getCause());
			return "";
		}
	}

	public static String getCurrTs() {
		log.info("@@inside getCurrTs...");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyyHHmmssSSS111");
		String tmpstmp = format1.format(cal.getTime());
		return tmpstmp;
	}

	public static void main(String[] args) {
		Gst gst = new Gst();
		getKeySignature("27ABCCN4567K000701", "", "tcs", "KeyStore.jks", gst);
		log.info("TS:" + gst.timestamp);
		log.info("sign:" + gst.sign);

	}
}