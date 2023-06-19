package com.nsdl.beckn.np.utl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtl {

	public static void logInfo(String logStr) {
		log.info(logStr);
		System.out.println(logStr);

	}

	public static String parseDomainNamefromUrl(String url) {

		try {

			URL aURL = new URL(url);
			return aURL.getAuthority();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";

	}

	public static String getSig(String url, Map<String, String> error) {
		URL u = null;
		String uri = (url == null ? "" : url) + "/ondc-site-verification.html";
		error.put("Signature_urk", uri);
		try {
			u = new URL(uri);
			// System.out.println("URL : " + u.getProtocol() + "://" + u.getAuthority() +
			// "/ondc-site-verification.html");
			// u = new URL(u.getProtocol() + "://" + u.getAuthority() +
			// "/ondc-site-verification.html");

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			error.put("Signature_MalformedURLException", e1.getMessage());

			System.out.println("ERROR URL " + url + " error {} " + e1.getMessage());

			// Log.error(e1.getMessage());
		}
		if (u != null) {
			try (InputStream in = u.openStream()) {
				String data = new String(in.readAllBytes(), StandardCharsets.UTF_8);
				int index = data.indexOf("content='");
				if (index != -1) {
					String sig = data.substring(index + 9, data.indexOf("'", index + 12));
					System.out.println("sig:" + sig);
					return sig;

				}
				// verify = data.indexOf("'" + ackCode + "'") != -1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				error.put("Signature_IOException", e.getMessage());

				System.out.println("ERROR URL " + u.getPath() + " error {} " + e.getMessage());

			}
		}
		error.put("Signature_NOTFOUND", "Signature not found");
		return null;
	}

	public static OffsetDateTime getDate(String start) {
		if ((start == null) || "".equals(start)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st;
		try {
			st = LocalDateTime.parse(start, formatter);
			return OffsetDateTime.of(st, ZoneOffset.UTC);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

	public static boolean isValidDate(String start) {
		if ((start == null) || "".equals(start)) {
			return false;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st;
		try {
			st = LocalDateTime.parse(start, formatter);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}

	public static boolean isValidCity(String city) {
		if ((city == null) || "".equals(city)) {
			return true;
		}
		try {
			city = city.substring(1, city.length() - 1);
			String[] sepCities = city.split(",");
			System.out.println(city);
			Map<String, Boolean> uniqueCity = new HashMap<String, Boolean>();
			// (hash_map.containsKey(city));
			String value = city;
			Boolean flagRepeat = false;
			for (String c : sepCities) {

				if (uniqueCity.get(c.toLowerCase().trim()) != null) {
					flagRepeat = true;
					break;
				} else {

					uniqueCity.put(c.toLowerCase().trim(), true);
				}
			}
			return flagRepeat;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;

	}

	public static boolean isValidEncrypt(String en) {
		if ((en == null) || "".equals(en)) {
			return true;
		}

		try {
			byte[] dataBytes = Base64.getDecoder().decode(en);
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(dataBytes);
			PublicKey key = KeyFactory.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME)
					.generatePublic(x509EncodedKeySpec);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}

	public static boolean isValidSign(String sign) {
		if ((sign == null) || "".equals(sign)) {
			return true;
		}
		try {
			Ed25519PublicKeyParameters publicKey = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(sign), 0);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}
	public static boolean isValidfrom1AndUntil(String from1, Map<String, Object> keys,String schemaKey) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		boolean validDateFlag=false;
		try {
			
			//System.out.println("******* "+from1);
			if ((keys == null)) {
				return false;
			}
			//System.out.println("******* "+schemaKey);
			String pareschemaKey[] = schemaKey.split("[.]");
			String parent ="";
			String key  = null;
			for(int i=0;i<pareschemaKey.length;i++) {
				if(i==0) {
					parent = parent+pareschemaKey[i];
				} else if(i!=((pareschemaKey.length)-1)) {
					parent = parent+"."+pareschemaKey[i];
				}
				
			}
			
			//System.out.println("##"+parent);
			
			String UntilDateStr = (String) keys.get(parent +".valid_until");
			String from2DateStr = (String) keys.get(parent+".valid_from");
			//String UntilDateStr = (String) keys.get("message.entity.key_pair.valid_until");
			//String from2DateStr = (String) keys.get("message.entity.key_pair.valid_from");

			//System.out.println("******UntilDateStr* "+UntilDateStr);
			//System.out.println("******from2DateStr* "+from2DateStr);
			if ((from2DateStr == null) || "".equals(from2DateStr)) {
				return false;
			}
			if ((UntilDateStr == null) || "".equals(UntilDateStr)) {
				return false;
			}

			LocalDateTime UntilDate = LocalDateTime.parse(UntilDateStr, formatter);
			LocalDateTime from2Date = LocalDateTime.parse(from2DateStr, formatter);

			if (from2Date.isEqual(UntilDate)) {
				System.out.println("Both dates are of same day");
				validDateFlag =  true;
			}
			// Checking for before case
			else if (from2Date.isBefore(UntilDate)) {
				System.out.println("Date From1 before Date Until");
				validDateFlag =  true;
			}
			// Checking for after case
			else if (from2Date.isAfter(UntilDate)) {
				System.out.println("Date From1 comes after Date Until");
				validDateFlag =  false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return validDateFlag;
	}
	
	public static boolean isValidfrom1(String from1) {
		if ((from1 == null) || "".equals(from1)) {
			return false;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st;
		try {
			st = LocalDateTime.parse(from1, formatter);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}
	
	public static String getDateString(OffsetDateTime date) {
		if ((date == null)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st;
		try {

			return formatter.format(date);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}
	
	public static String getDateString(Timestamp date) {
		if ((date == null)) {
			return null;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {

			return date.toLocalDateTime().format(formatter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

	public static LocalDate getLocalDate(String start) {
		if ((start == null) || "".equals(start)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate st;
		try {
			st = LocalDate.parse(start, formatter);
			return st;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

	public static boolean validTimeDate(String start, int window) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st = null;
		try {
			st = LocalDateTime.parse(start, formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((st != null) && LocalDateTime.now().minusMinutes(window).isBefore(st)
				&& st.isBefore(LocalDateTime.now().plusMinutes(window))) {
			return true;
		}
		return false;
	}

	public static String checkDate(String start, String end) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		// formatter.parse(start).get();
		LocalDateTime st;
		try {
			st = LocalDateTime.parse(start, formatter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "Start Date is Invalid date time format.";
		}
		if ((end != null) && !"".equals(end)) {
			LocalDateTime ed;
			try {
				ed = LocalDateTime.parse(end, formatter);
			} catch (Exception e) {
				// TODO: handle exception
				return "End Date is Invalid date time format.";
			}
			if (st.isBefore(ed)) {
				return null;
			} else {
				return "Start Date should be less than end date.";
			}
		}
		return null;
	}

	public static Map<String, Object> convertObjectToMap(Object obj) {
		Gson gson = new Gson();
		return gson.fromJson(gson.toJson(obj), Map.class);

	}
	
	public static String convertObjectToMapString(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);

	}

	public static void main(String[] args) {
		System.out.println(new CommonUtl().getSig("https://craftsvilla.com", new HashMap<>()));
	}

	public static String getSQLDateString(Timestamp date) {
		if ((date == null)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		try {
			LocalDateTime st = date.toLocalDateTime();
			return formatter.format(st);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

	public static String getPrintStackString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();

	}

	public static String toJsonStr(Object obj) {
		ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

		// ObjectMapper objectMapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return "";
	}
	
	public static long getTimeDiff(Instant start, Instant end) {
		long timeElapsed = Duration.between(start, end).toMillis();
		return timeElapsed;
	}

}
