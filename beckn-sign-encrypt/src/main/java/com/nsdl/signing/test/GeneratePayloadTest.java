package com.nsdl.signing.test;

import com.nsdl.signing.crypto.GeneratePayload;

public class GeneratePayloadTest {
	public static void main(String[] args) {
		GeneratePayload generatePayload = new GeneratePayload();
//	String requestData="Welcome";
		String requestData = "{\"context\":{\"country\":\"IND\",\"domain\":\"test-BPP-Mobility\",\"transaction_id\":\"3ad6ff68-2427-4e18-8f15-670020a84797\",\"action\":\"search\",\"message_id\":\"d951b9cc-4ced-4cb4-a0bd-ac8f7dc40a73\",\"city\":\"Kochi\",\"bap_uri\":\"https://api.beckn.juspay.in/bap/cab/v1\",\"timestamp\":\"2022-03-17T08:45:08.756463024Z\",\"core_version\":\"0.9.3\",\"bap_id\":\"api.beckn.juspay.in/bap/cab/v1\"},\"message\":{\"intent\":{\"fulfillment\":{\"start\":{\"time\":{\"timestamp\":\"2022-03-17T08:45:08.731307978Z\"},\"location\":{\"address\":{\"area\":\"Edappally \",\"state\":\"Kerala \",\"country\":\"India \",\"building\":\"Edappally Junction \",\"door\":\"LuLu Mall Old NH 47\",\"street\":\"Nethaji Nagar\",\"city\":\"Kochi \",\"area_code\":\"\"},\"gps\":\"10.0265216, 76.3085945\"}},\"distance\":\"17567.4880000000011932570487260\",\"end\":{\"location\":{\"address\":{\"area\":\" \",\"state\":\"Kerala \",\"country\":\"India \",\"building\":\" \",\"door\":\"\",\"street\":\"\",\"city\":\"Kochi \",\"area_code\":\"\"},\"gps\":\"9.9312328, 76.26730409999999\"}}}}}}";

		String publicKey = "K0el1kYfJI222a8Zja9jOsU68zU+zqT6/AiTobEl66k=";
		long testTimestamp = System.currentTimeMillis() / 1000L;
		String signingString = requestData;
//		try {
//
//			requestData = generatePayload.generateBlakeHash(requestData);
//			signingString = "(created): " + testTimestamp + "\n(expires): " + (testTimestamp + 60000)
//					+ "\ndigest: BLAKE-512=" + requestData + "";
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Data ................ \n" + signingString);
		String sign = generatePayload.generateSignature(signingString, GeneratePayload.privateKey);
		System.out.println("Sign Data ................ \n" + sign);

		System.out.println(generatePayload.verifySignature(sign, signingString, publicKey));

	}
}
