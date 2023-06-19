package com.nsdl.onboading.pan;

import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.*;

public class APIBased {

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

	public static String getPanDtl(String urlPan, String userid, String pan, String sig) throws Exception {

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

		String urlOfNsdl = urlPan;
		String data = null;
		String signature = null;
		final String version = "2";

		try {

			data = userid.trim() + "^^" + pan.toString();
			signature = sig;

		} catch (Exception e) {
			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
		}

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

		String urlParameters = "data=";
		try {
			urlParameters = urlParameters + URLEncoder.encode(data, "UTF-8") + "&signature="
					+ URLEncoder.encode(signature, "UTF-8") + "&version=" + URLEncoder.encode(version, "UTF-8");
		} catch (Exception e) {
			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
			e.printStackTrace();
			 
		}

		try {
			URL url;
			HttpsURLConnection connection;
			InputStream is = null;

			String ip = urlOfNsdl;
			url = new URL(ip);
			System.out.println("URL " + ip);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
//			logMsg+="::Request Sent At: " + connectionStartTime;
//			logMsg+="::Request Data: "+ data;
//			logMsg+="::Version: "+ version;
			osw.close();
			is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			line = in.readLine();
			logMsg = line;
			System.out.println("Output: " + line);
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
}
