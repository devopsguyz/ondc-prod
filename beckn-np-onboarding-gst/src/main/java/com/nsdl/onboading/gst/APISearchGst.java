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
import java.util.Base64;
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
public class APISearchGst {

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

	public static String getGstSearch(String urlSearchGst, Gst gst) throws Exception {

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

//		String urlParameters = "";
//		try {
//			urlParameters = "{\"action\":\"" + gst.action + "\" , \"username\":\"" + gst.username + "\"}";
//			log.info(urlParameters);
//		} catch (Exception e) {
//			logMsg += "::Exception: " + e.getMessage() + " ::Program Start Time:" + startTime + "::nonce= " + nonce;
//			e.printStackTrace();
//
//		}

		try {
			URL url;
			HttpsURLConnection connection;
			InputStream is = null;
			urlSearchGst = urlSearchGst + "?action=" + gst.searchGstAction + "&gstin=" + gst.gst;
			url = new URL(urlSearchGst);
			log.info("URL " + urlSearchGst);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("aspid", gst.aspId);
			connection.setRequestProperty("message-id", gst.timestamp);

			connection.setRequestProperty("asp-secret", gst.enc_asp_secret);
			connection.setRequestProperty("state-cd", gst.state_cd);

			connection.setRequestProperty("ip-usr", gst.ip_usr);
			connection.setRequestProperty("txn", gst.timestamp);

			connection.setRequestProperty("session-id", gst.responseGstKey.session_id);
			connection.setRequestProperty("filler1", gst.filler1);

			connection.setRequestProperty("filler2", gst.filler2);
			connection.setRequestProperty("auth-token", gst.responseAspSecret.auth_token);

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setSSLSocketFactory(factory);
			connection.setHostnameVerifier(new DummyHostnameVerifier());
//			OutputStream os = connection.getOutputStream();
//			OutputStreamWriter osw = new OutputStreamWriter(os);
////			osw.write(urlParameters);
//			osw.flush();
//			connectionStartTime = new Date();
//			osw.close();
			is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			line = in.readLine();
			logMsg = line;
			gst.responseGstSearch = new Gson().fromJson(line, ResponseGstSearch.class);
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
		gst.aspId = "27ABCCN4567K000701";
		gst.gst = "27AVXPM6863B1Z0";
		String asp_secret = "66I5GT49zu9u6g6v193ni4031zy06mrF";
		CryptoGetKeyGenerateSignature.getKeySignature("27ABCCN4567K000701", "", "tcs", "KeyStore.jks", gst);

		gst.state_cd = "27";

		gst.ip_usr = "172.18.2.154";
		gst.filler1 = "filler1";
		gst.filler2 = "filler2";

		gst.action = "ACCESSTOKEN";
		gst.username = "commonapiuser";
		// gst.encKey="J6wVWToLIxe5SJJustrMe61bG4ddQD8r5p2EoXAMqTsP9md31Sw7ogc9jmPXDrui";
		gst.searchGstAction = "TP";
		try {
			APIGetKey.getGstgetKeyDtl("https://test.nsdlgsp.co.in/GSPUtility/getKey", gst);
			log.info(gst.responseGstKey.enc_key);

			gst.encKey = gst.responseGstKey.enc_key;

			CryptoAspSecretAESEncryption.createAspScreate(gst, asp_secret);
			log.info("asp_secret:"+gst.enc_asp_secret);
			APIAspSecret.getGstAspSecret("https://test.nsdlgsp.co.in/NGCSGSP/callApi/commonapi/v0.2/authenticate", gst);
			log.info("auth_token:"+gst.responseAspSecret.auth_token);

			getGstSearch("https://test.nsdlgsp.co.in/NGCSGSP/callApi/commonapi/v0.3/search", gst);

			log.info(gst.responseGstSearch.data);
			log.info(new String(Base64.getDecoder().decode(gst.responseGstSearch.data)));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
