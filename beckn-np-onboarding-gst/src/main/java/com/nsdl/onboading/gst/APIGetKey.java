package com.nsdl.onboading.gst;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIGetKey {

	public static class DummyTrustManager implements X509TrustManager {

		public DummyTrustManager() {
		}

		public boolean isClientTrusted(X509Certificate cert[]) {
			return true;
		}

		public boolean isServerTrusted(X509Certificate cert[]) {
			return true;
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}
	}

	public static class DummyHostnameVerifier implements HostnameVerifier {

		public boolean verify(String urlHostname, String certHostname) {
			return true;
		}

		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	public static String getGstgetKeyDtl(String urlGetKeyGst, Gst gst) throws Exception {

		Date startTime = null;
		Calendar c1 = Calendar.getInstance();
		startTime = c1.getTime();

		Date connectionStartTime = null;
		String logMsg = "";
		BufferedWriter out1 = null;
		FileWriter fstream = null;
		FileWriter fstream1 = null;
		Calendar c = Calendar.getInstance();
		long nonce = c.getTimeInMillis();

		 
		String data = null;
		String signature = null;
		final String version = "2";

	 

		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL");

			sslcontext.init(new KeyManager[0], new TrustManager[] { new DummyTrustManager() }, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
			e.printStackTrace(System.err);

		} catch (KeyManagementException e) {
			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
			e.printStackTrace(System.err);

		}

		SSLSocketFactory factory = sslcontext.getSocketFactory();

		String urlParameters = "";
		try {
			urlParameters = "{\"timestamp\":\""+gst.timestamp+"\" , \"signed_content\":\""+gst.sign+"\"}";
		} catch (Exception e) {
			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
			e.printStackTrace();
			 
		}

		try {
			URL url;
			HttpsURLConnection connection;
			InputStream is = null;
 
			url = new URL(urlGetKeyGst);
			log.info("URL " + urlGetKeyGst);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("aspId", gst.aspId);
			connection.setRequestProperty("message-id", gst.timestamp);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setSSLSocketFactory(factory);
			connection.setHostnameVerifier(new DummyHostnameVerifier());
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(urlParameters);
			osw.flush();
			connectionStartTime = new Date(); 
			osw.close();
			is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			line = in.readLine();
			logMsg = line;
			gst.responseGstKey= new Gson().fromJson(line,ResponseGetKey.class);
			log.info("Output: " + line);
			is.close();
			in.close();
		} catch (ConnectException e) {
			logMsg += "::Exception: " + e.getMessage() + "::Program Start Time:" + startTime + "::nonce= " + nonce;

		} catch (Exception e) {
			logMsg += "::Exception: " + e.getMessage() + "::Program Start Time:" + startTime + "::nonce= " + nonce;

			e.printStackTrace();
		}

		return logMsg;
	}
	public static void main(String[] args) {
		Gst gst = new Gst();
		gst.aspId="27ABCCN4567K000701";
		CryptoGetKeyGenerateSignature.getKeySignature("27ABCCN4567K000701", "", "tcs", "KeyStore.jks", gst);	
		
		//gst.encKey="J6wVWToLIxe5SJJustrMe61bG4ddQD8r5p2EoXAMqTsP9md31Sw7ogc9jmPXDrui";
	
		try {
			getGstgetKeyDtl("https://test.nsdlgsp.co.in/GSPUtility/getKey",gst);
			log.info(gst.responseGstKey.enc_key);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
