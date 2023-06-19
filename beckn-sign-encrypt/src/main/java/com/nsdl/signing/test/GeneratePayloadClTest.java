//package com.nsdl.signing.test;
//
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.SecureRandom;
//import java.security.Security;
//import java.util.Base64;
//import java.util.UUID;
//
//
//import org.apache.commons.lang.RandomStringUtils;
//import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
//import org.bouncycastle.crypto.CryptoException;
//import org.bouncycastle.crypto.DataLengthException;
//import org.bouncycastle.crypto.Signer;
//import org.bouncycastle.crypto.digests.Blake2bDigest;
//import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
//import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
//import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
//import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
//import org.bouncycastle.crypto.signers.Ed25519Signer;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.util.encoders.Hex;
//import java.io.File;
//import java.io.FileNotFoundException;
//
///**
// * @author piyushm
// *
// */
//
//public class GeneratePayloadClTest 
//{
//	//static String req = "{\"context\":{\"domain\":\"MOBILITY\",\"country\":\"IND\",\"action\":\"search\",\"core_version\":\"0.8.2\",\"domain_version\":\"0.8.2\",\"bap_uri\":\"https://api.sandbox.beckn.juspay.in/dev/app/v1\",\"transaction_id\":\"bf67a62b-f690-4e9d-95b2-55555f19f5ac\",\"message_id\":\"6ace310b-6440-4421-a2ed-b484c7548bd5\",\"timestamp\":\"2020-12-14T07:21:45.311511742Z\"},\"message\":{\"intent\":{\"pickups\":[{\"location\":{\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Buildings\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"8th Block Koramangala\",\"city\":\"Bangalore\",\"area_code\":\"560047\"},\"gps\":{\"lat\":\"10.0489006\",\"lon\":\"76.2992536\"}},\"id\":\"\",\"transfers\":[],\"departure_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"},\"arrival_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"}}],\"drops\":[{\"location\":{\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Apartments\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"6th Block Koramangala\",\"city\":\"Bangalore\",\"area_code\":\"560047\"},\"gps\":{\"lat\":\"10.0713777\",\"lon\":\"76.2948968\"}},\"id\":\"\",\"transfers\":[],\"departure_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"},\"arrival_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"}}],\"fare\":{\"value\":{\"integral\":\"100\"},\"currency\":\"INR\"},\"payload\":{\"travellers\":[]},\"vehicle\":{\"variant\":\"SUV\",\"category\":\"CAR\"},\"tags\":[{\"value\":\"3330.529\",\"key\":\"distance\"}]}}}";
//	//static String req = "{\"context\":{\"ttl\":null,\"country\":\"IND\",\"domain\":\"MOBILITY\",\"bpp_uri\":null,\"transaction_id\":\"8f29d750-bda1-4136-ad64-c53a2ae2b367\",\"action\":\"search\",\"message_id\":\"a4831adf-14d2-4b76-8e70-b0f23064190b\",\"city\":null,\"bap_uri\":\"https://api.sandbox.beckn.juspay.in/dev/app/v1\",\"domain_version\":\"0.8.2\",\"timestamp\":\"2021-05-12T07:33:55.764738733Z\",\"core_version\":\"0.8.2\"},\"message\":{\"intent\":{\"pickups\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Buildings\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"8th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0489006\",\"lon\":\"76.2992536\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2021-05-12T10:33:55Z\",\"act\":\"2021-05-12T10:33:55Z\"},\"arrival_time\":{\"est\":\"2021-05-12T10:33:55Z\",\"act\":\"2021-05-12T10:33:55Z\"}}],\"item_id\":null,\"drops\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Apartments\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"6th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0713777\",\"lon\":\"76.2948968\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2021-05-12T10:33:55Z\",\"act\":\"2021-05-12T10:33:55Z\"},\"arrival_time\":{\"est\":\"2021-05-12T10:33:55Z\",\"act\":\"2021-05-12T10:33:55Z\"}}],\"category_id\":null,\"fare\":{\"maximum_value\":null,\"computed_value\":null,\"value\":{\"fractional\":null,\"integral\":\"100\"},\"estimated_value\":null,\"listed_value\":null,\"currency\":\"INR\",\"minimum_value\":null,\"offered_value\":null},\"query_string\":null,\"provider_id\":null,\"payload\":{\"traveller_count\":null,\"travellers\":[],\"luggage\":null,\"travel_group\":null},\"vehicle\":{\"variant\":\"SUV\",\"energy_type\":null,\"color\":null,\"size\":null,\"category\":\"CAR\",\"capacity\":null,\"model\":null,\"make\":null,\"registration\":null},\"transfer\":null,\"tags\":[{\"value\":\"3330.57\",\"key\":\"distance\"}]}}}";
//	//static String privateKey = "R33D4kpv9XlvHEnHPSINfdePagxRCLNw0yaqZ9BkJLg=";
//	//static String publicKey = "Fhjwaka1Za+ld+7Nms7S0C675r24mZoyWVn8JbYTjSs=";
//
//	static String privateKey = "K2qouM7hs57AStiEcKfCvXTgLtVFxMRhhCsbBUrWVaI=";
//	static String publicKey = "K0el1kYfJI222a8Zja9jOsU68zU+zqT6/AiTobEl66k=";
//
//	//static String privateKey = "cDs6yVU5VpAVCe9MCtHUf/muUKok287sgJkJvsuL/5Q=";
//	//static String publicKey = "K/7cCtTF5eXxGfJkpPloFJ8Q3iXpj/GQmDf36hK6f9Q=";
//
//	//static String publicKey ="MCowBQYDK2VuAyEAqe/iT3XBu/3VfaH9muoQ7s6644LORdDc5KIcldN86wE=";
//	//static String privateKey="MFECAQEwBQYDK2VuBCIEIDi8orAdzNaZNJZ85xmwbSwBF3uN/Mup4R29f38IPOtsgSEAqe/iT3XBu/3VfaH9muoQ7s6644LORdDc5KIcldN86wE=";
//
//
//	//static String publicKey = "3DQAZxWBPD8g2iTaaqveQyIXNT3h13cwx++b9+6yjN4=";
//	//static String privateKey = "NaKRo2AEFoVPEEXt6AUX72GG4mq0EmMUmo1SJo38znvcNABnFYE8PyDaJNpqq95DIhc1PeHXdzDH75v37rKM3g==";
//	//static String kid = "nsdl.co.in|nsdl_bg_1234|ed25519";
//	//static String kid = "mock_bap1|bap_one|ed25519";
//	//static String kid = "mock_bpp1|bpp_one|ed25519";
//	static String kid = "nsdl.co.in.ba|nsdl_bap1|ed25519";
//	//static String publicKey = "kCa4OlmRVfCPcvzjPPGik0Ljei5dRYuuj/2K6upaf1E=";
//	//static String kid = "nsdl_retail.co.in.ba|nsdl_retail_ba_1234|ed25519";
//
//	public static void main(String[] args) 
//	{
//		//String req = args[0];
//		//String req = req;
//		
//		setup();
//		
//		try (PrintWriter writer = new PrintWriter(new File("search_test_17032022.csv"))) {
//		//try (PrintWriter writer = new PrintWriter(new File("E:\\Beckn Project\\LoadtestData\\on_search_50K_17032022.csv"))) {
//			for(int i=0; i<1; i++) {
//
//
//				StringBuilder sb = new StringBuilder();
//				System.out.println(i);
//				UUID uuid = UUID.randomUUID();
//				String generatedString = uuid.toString();
//
//				System.out.println("Your UUID is: " + generatedString);
//
//				//String req = "{\"context\":{\"domain\":\"Mobility\",\"country\":\"IND\",\"city\":\"Kochi\",\"action\":\"search\",\"core_version\":\"0.9.2\",\"bap_id\":\"mock_bap1\",\"bap_uri\":\"http://10.130.30.54:8099/buyer\",\"bpp_id\":\"\",\"bpp_uri\":\"\",\"transaction_id\":\"12387917398173\",\"message_id\":\"12387917398173\",\"timestamp\":\"2022-03-22T05:48:04.938Z\",\"key\":\"string\",\"ttl\":\"string\"},\"message\":{\"intent\":{\"provider\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\",\"code\":\"string\",\"symbol\":\"string\",\"short_desc\":\"string\",\"long_desc\":\"string\",\"images\":[\"string\"],\"audio\":\"string\",\"3d_render\":\"string\"},\"locations\":[{\"id\":\"string\"}]},\"fulfillment\":{\"id\":\"string\",\"start\":{\"location\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\",\"code\":\"string\",\"symbol\":\"string\",\"short_desc\":\"string\",\"long_desc\":\"string\",\"images\":[\"string\"],\"audio\":\"string\",\"3d_render\":\"string\"},\"gps\":\"string\",\"address\":{\"door\":\"string\",\"name\":\"string\",\"building\":\"string\",\"street\":\"string\",\"locality\":\"string\",\"ward\":\"string\",\"city\":\"string\",\"state\":\"string\",\"country\":\"string\",\"area_code\":\"string\"},\"station_code\":\"string\",\"city\":{\"name\":\"string\",\"code\":\"string\"},\"country\":{\"name\":\"string\",\"code\":\"string\"},\"circle\":\"string\",\"polygon\":\"string\",\"3dspace\":\"string\"},\"time\":{\"label\":\"string\",\"timestamp\":\"2021-03-22T05:48:04.938Z\",\"duration\":\"string\",\"range\":{\"start\":\"2021-03-22T05:48:04.938Z\",\"end\":\"2021-03-22T05:48:04.938Z\"},\"days\":\"string\"}},\"end\":{\"location\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\",\"code\":\"string\",\"symbol\":\"string\",\"short_desc\":\"string\",\"long_desc\":\"string\",\"images\":[\"string\"],\"audio\":\"string\",\"3d_render\":\"string\"},\"gps\":\"string\",\"address\":{\"door\":\"string\",\"name\":\"string\",\"building\":\"string\",\"street\":\"string\",\"locality\":\"string\",\"ward\":\"string\",\"city\":\"string\",\"state\":\"string\",\"country\":\"string\",\"area_code\":\"string\"},\"station_code\":\"string\",\"city\":{\"name\":\"string\",\"code\":\"string\"},\"country\":{\"name\":\"string\",\"code\":\"string\"},\"circle\":\"string\",\"polygon\":\"string\",\"3dspace\":\"string\"},\"time\":{\"label\":\"string\",\"timestamp\":\"2021-03-22T05:48:04.938Z\",\"duration\":\"string\",\"range\":{\"start\":\"2021-03-22T05:48:04.938Z\",\"end\":\"2021-03-22T05:48:04.938Z\"},\"days\":\"string\"}},\"tags\":{\"additionalProp1\":\"string\",\"additionalProp2\":\"string\",\"additionalProp3\":\"string\"}},\"payment\":{\"uri\":\"string\",\"tl_method\":\"http/get\",\"params\":{\"transaction_id\":\"string\",\"amount\":\"string\",\"additionalProp1\":\"string\",\"additionalProp2\":\"string\",\"additionalProp3\":\"string\"},\"type\":\"ON-ORDER\",\"status\":\"PAID\",\"time\":{\"label\":\"string\",\"timestamp\":\"2021-03-22T05:48:04.938Z\",\"duration\":\"string\",\"range\":{\"start\":\"2021-03-22T05:48:04.938Z\",\"end\":\"2021-03-22T05:48:04.938Z\"},\"days\":\"string\"}},\"category\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\",\"code\":\"string\",\"symbol\":\"string\",\"short_desc\":\"string\",\"long_desc\":\"string\",\"images\":[\"string\"],\"audio\":\"string\",\"3d_render\":\"string\"},\"time\":{\"label\":\"string\",\"timestamp\":\"2021-03-22T05:48:04.938Z\",\"duration\":\"string\",\"range\":{\"start\":\"2021-03-22T05:48:04.938Z\",\"end\":\"2021-03-22T05:48:04.938Z\"},\"days\":\"string\"},\"tags\":[{\"additionalProp1\":\"string\",\"additionalProp2\":\"string\",\"additionalProp3\":\"string\"}]},\"offer\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\"}},\"item\":{\"id\":\"string\",\"descriptor\":{\"name\":\"string\"}},\"tags\":{\"additionalProp1\":\"string\",\"additionalProp2\":\"string\",\"additionalProp3\":\"string\"}}}}";
//				//String req = "{\"context\":{\"domain\":\"Mobility\",\"country\":\"IND\",\"city\":\"Kochi\",\"action\":\"search\",\"core_version\":\"0.9.1\",\"bap_id\":\"mock_bap1\",\"bap_uri\":\"http://10.130.30.54:8099/buyer\",\"transaction_id\":\""+generatedString+"\",\"message_id\":\"153922db-69f7-4418-b7ce-1c6f28f9d9c4\",\"timestamp\":\"2022-03-07T07:05:37.976711Z\"},\"message\":{\"intent\":{\"fulfillment\":{\"start\":{\"location\":{}},\"end\":{\"location\":{\"gps\":\"19.1178548,72.8631304\"}}},\"item\":{\"descriptor\":{\"name\":\"printer\"}},\"provider\":{\"descriptor\":{}},\"category\":{\"descriptor\":{}}}}}";
//				//String req = "{\"context\":{\"domain\":\"Mobility\",\"country\":\"IND\",\"city\":\"Kochi\",\"action\":\"on_search\",\"bap_id\":\"mock_bap1\",\"bap_uri\":\"http://10.130.30.54:8099/buyer\",\"bpp_id\":\"mock_bpp1\",\"bpp_uri\":\"http://10.130.30.54:8089/seller\",\"transaction_id\":\""+generatedString+"\",\"message_id\":\"\",\"timestamp\":\"\",\"key\":\"string\",\"ttl\":\"string\",\"core_version\":\"0.9.2\"},\"message\":{\"catalog\":{\"bpp/descriptor\":{\"name\":\"Mock BPP\"},\"bpp/providers\":[{\"id\":\"mega-mall\",\"descriptor\":{\"name\":\"Mega Mall Mumbai\"},\"locations\":[{\"id\":\"mumbai-4th-block-location\",\"gps\":\"12.9349377,77.6055586\"}],\"categories\":[{\"id\":\"fresh_fruits\",\"descriptor\":{\"name\":\"Fresh Fruits\"}},{\"id\":\"beverages\",\"descriptor\":{\"name\":\"Beverages\"}}],\"items\":[{\"id\":\"item_1\",\"descriptor\":{\"name\":\"Green Apples Organic\",\"images\":[\"./assets/images/apple_1.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"170\"},\"matched\":true},{\"id\":\"item_2\",\"descriptor\":{\"name\":\"Red Apples\",\"images\":[\"./assets/images/apple_2.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"90\"},\"related\":true},{\"id\":\"item_3\",\"descriptor\":{\"name\":\"Kashmiri Apple Permium\",\"images\":[\"./assets/images/apple_3.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"120\"},\"matched\":true},{\"id\":\"item_4\",\"descriptor\":{\"name\":\"Fresh Australia Apple\",\"images\":[\"./assets/images/apple_4.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"180\"},\"matched\":true},{\"id\":\"item_5\",\"descriptor\":{\"name\":\"Pure Apple Juice Iceland\",\"images\":[\"./assets/images/apple_juice_1.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"110\"},\"matched\":true},{\"id\":\"item_6\",\"descriptor\":{\"name\":\"Real Fresh Apple Juice\",\"images\":[\"./assets/images/apple_juice_2.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"100\"},\"matched\":true},{\"id\":\"item_8\",\"descriptor\":{\"name\":\"Fresh Oranges\",\"images\":[\"./assets/images/orange_1.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"80\"},\"matched\":true},{\"id\":\"item_9\",\"descriptor\":{\"name\":\"Fresh Nagpur Orange\",\"images\":[\"./assets/images/orange_2.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"85\"},\"matched\":true},{\"id\":\"item_10\",\"descriptor\":{\"name\":\"Fresh Orange Juice\",\"images\":[\"./assets/images/orange_juice_1.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"140\"},\"matched\":true},{\"id\":\"item_11\",\"descriptor\":{\"name\":\"Tropicana Orange Juice\",\"images\":[\"./assets/images/orange_juice_2.jpg\"]},\"category_id\":\"fresh_fruits\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"160\"},\"matched\":true},{\"id\":\"item_12\",\"descriptor\":{\"name\":\"Samsung Galaxy S21\",\"images\":[\"./assets/images/samsung_s_21.jpg\"]},\"category_id\":\"mobile\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"57700\"},\"matched\":true},{\"id\":\"item_13\",\"descriptor\":{\"name\":\"Samsung Galaxy A52\",\"images\":[\"./assets/images/samsung_a_52.jpg\"]},\"category_id\":\"mobile\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"25600\"},\"matched\":true},{\"id\":\"item_14\",\"descriptor\":{\"name\":\"iPhone 13\",\"images\":[\"./assets/images/iphone_13.jpg\"]},\"category_id\":\"mobile\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"85700\"},\"matched\":true},{\"id\":\"item_15\",\"descriptor\":{\"name\":\"iPhone 13 Mini\",\"images\":[\"./assets/images/iphone_mini_13.jpg\"]},\"category_id\":\"mobile\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"65700\"},\"matched\":true},{\"id\":\"item_16\",\"descriptor\":{\"name\":\"One Plus 11 Pro\",\"images\":[\"./assets/images/oneplus_pro_11.jpg\"]},\"category_id\":\"mobile\",\"location_id\":\"mumbai-4th-block-location\",\"price\":{\"currency\":\"INR\",\"value\":\"45200\"},\"matched\":true}]},{\"id\":\"mega-mall\",\"descriptor\":{\"name\":\"Mega Mall\"},\"locations\":[{\"id\":\"mega-mall-location\",\"gps\":\"12.9349377,77.6055586\"}],\"categories\":[{\"id\":\"fresh-food\",\"descriptor\":{\"name\":\"Fresh food\"}}],\"items\":[{\"id\":\"item_2_1\",\"descriptor\":{\"name\":\"Dell Inspiron\",\"images\":[\"./assets/images/dell_inspiron.jpg\"]},\"category_id\":\"laptop\",\"location_id\":\"mega-mall-location\",\"price\":{\"currency\":\"INR\",\"value\":\"90000\"},\"matched\":true},{\"id\":\"item_2_2\",\"descriptor\":{\"name\":\"Dell Latitude\",\"images\":[\"./assets/images/dell_latitude.jpg\"]},\"category_id\":\"laptop\",\"location_id\":\"mega-mall-location\",\"price\":{\"currency\":\"INR\",\"value\":\"63000\"},\"matched\":true}]}],\"bpp/fulfillments\":[{\"type\":\"home-delivery\"}]}}}";
//				//String req = "{\"context\":{\"country\":\"IND\",\"city\":\"Pune\",\"domain\":\"nic2004:60221\",\"transaction_id\":\"5443cb0d-dfe5-43ea-88df-510b1cdb1a5e\",\"action\":\"search\",\"message_id\":\"9266293b-50a0-4d9d-9aeb-75bb9b8a6e72\",\"bap_uri\":\"https://api.sandbox.beckn.juspay.in/bap/cab/v1\",\"domain_version\":\"0.8.2\",\"timestamp\":\"2021-11-09T13:09:22.758877255Z\",\"core_version\":\"0.8.2\"},\"message\":{\"intent\":{\"pickups\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Buildings\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"8th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0489006\",\"lon\":\"76.2992536\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2021-11-10T04:09:21Z\",\"act\":\"2021-11-10T04:09:21Z\"},\"arrival_time\":{\"est\":\"2021-11-10T04:09:21Z\",\"act\":\"2021-11-10T04:09:21Z\"}}],\"item_id\":null,\"drops\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Apartments\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"6th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0713777\",\"lon\":\"76.2948968\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2021-11-10T04:09:21Z\",\"act\":\"2021-11-10T04:09:21Z\"},\"arrival_time\":{\"est\":\"2021-11-10T04:09:21Z\",\"act\":\"2021-11-10T04:09:21Z\"}}],\"category_id\":null,\"fare\":{\"maximum_value\":null,\"computed_value\":null,\"value\":{\"fractional\":null,\"integral\":\"100\"},\"estimated_value\":null,\"listed_value\":null,\"currency\":\"INR\",\"minimum_value\":null,\"offered_value\":null},\"query_string\":null,\"provider_id\":null,\"payload\":{\"traveller_count\":null,\"travellers\":[],\"luggage\":null,\"travel_group\":null},\"vehicle\":null,\"transfer\":null,\"tags\":[{\"value\":\"3330.57\",\"key\":\"distance\"}]}}}";
//				String req = "{\"context\":{\"country\":\"IND\",\"domain\":\"test-BPP-Mobility\",\"transaction_id\":\"3ad6ff68-2427-4e18-8f15-670020a84797\",\"action\":\"search\",\"message_id\":\"d951b9cc-4ced-4cb4-a0bd-ac8f7dc40a73\",\"city\":\"Kochi\",\"bap_uri\":\"https://api.beckn.juspay.in/bap/cab/v1\",\"timestamp\":\"2022-03-17T08:45:08.756463024Z\",\"core_version\":\"0.9.3\",\"bap_id\":\"api.beckn.juspay.in/bap/cab/v1\"},\"message\":{\"intent\":{\"fulfillment\":{\"start\":{\"time\":{\"timestamp\":\"2022-03-17T08:45:08.731307978Z\"},\"location\":{\"address\":{\"area\":\"Edappally \",\"state\":\"Kerala \",\"country\":\"India \",\"building\":\"Edappally Junction \",\"door\":\"LuLu Mall Old NH 47\",\"street\":\"Nethaji Nagar\",\"city\":\"Kochi \",\"area_code\":\"\"},\"gps\":\"10.0265216, 76.3085945\"}},\"distance\":\"17567.4880000000011932570487260\",\"end\":{\"location\":{\"address\":{\"area\":\" \",\"state\":\"Kerala \",\"country\":\"India \",\"building\":\" \",\"door\":\"\",\"street\":\"\",\"city\":\"Kochi \",\"area_code\":\"\"},\"gps\":\"9.9312328, 76.26730409999999\"}}}}}}";
//				//String req = "{\"context\":{\"ttl\":null,\"country\":\"IND\",\"domain\":\"test-BPP-Mobility\",\"bpp_uri\":null,\"transaction_id\":\""+generatedString+"\",\"action\":\"on_search\",\"message_id\":\"6ace310b-6440-4421-a2ed-b484c7548bd5\",\"city\":null,\"bap_uri\":\"http://localhost:8066/\",\"domain_version\":\"0.8.2\",\"timestamp\":\"2020-12-14T07:21:45.311511742Z\",\"core_version\":\"0.8.2\"},\"message\":{\"intent\":{\"pickups\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Buildings\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"8th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0489006\",\"lon\":\"76.2992536\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"},\"arrival_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"}}],\"item_id\":null,\"drops\":[{\"location\":{\"circle\":null,\"country\":null,\"3dspace\":null,\"address\":{\"state\":\"Karnataka\",\"country\":\"India\",\"building\":\"Juspay Apartments\",\"door\":\"#444\",\"street\":\"18th Main\",\"locality\":\"6th Block Koramangala\",\"name\":null,\"city\":\"Bangalore\",\"area_code\":\"560047\",\"ward\":null},\"gps\":{\"lat\":\"10.0713777\",\"lon\":\"76.2948968\"},\"polygon\":null,\"city\":null,\"station_code\":null},\"id\":\"\",\"transfers\":[],\"descriptor\":null,\"departure_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"},\"arrival_time\":{\"est\":\"2020-12-14T10:21:44Z\",\"act\":\"2020-12-14T10:21:44Z\"}}],\"category_id\":null,\"fare\":{\"maximum_value\":null,\"computed_value\":null,\"value\":{\"fractional\":null,\"integral\":\"100\"},\"estimated_value\":null,\"listed_value\":null,\"currency\":\"INR\",\"minimum_value\":null,\"offered_value\":null},\"query_string\":null,\"provider_id\":null,\"payload\":{\"traveller_count\":null,\"travellers\":[],\"luggage\":null,\"travel_group\":null},\"vehicle\":{\"variant\":\"SUV\",\"energy_type\":null,\"color\":null,\"size\":null,\"category\":\"CAR\",\"capacity\":null,\"model\":null,\"make\":null,\"registration\":null},\"transfer\":null,\"tags\":[{\"value\":\"3330.529\",\"key\":\"distance\"}]}}}";
//				//String req = "{\"domain\": \"MOBILITY\", \"country\": \"IND\", \"city\": \"Pune\"}";
//				//String req = "{\"context\":{\"country\":\"IND\",\"domain\":\"nic2004:60212\",\"transaction_id\":\"f01fc03d-8add-4d6b-8595-4e6cb74a7283\",\"action\":\"on_search\",\"message_id\":\"481b1f20-acea-41f1-9117-3a68df59eb72\",\"city\":\"\",\"bap_uri\":\"https://beckn.free.beeceptor.com\",\"timestamp\":\"2021-10-28T12:42:18.150954734Z\",\"core_version\":\"0.9.1\",\"bap_id\":\"https://api.sandbox.beckn.juspay.in/dev/bap/cab/v1\",\"bpp_id\":\"api.sandbox.beckn.juspay.in/bpp/metro/v1\",\"bpp_uri\":\"https://metro-bpp.com/\"},\"message\":{\"catalog\":{\"bpp/descriptor\":{\"name\":\"BPP\"},\"bpp/providers\":[{\"id\":\"KMRL\",\"descriptor\":{\"name\":\"Kochi Metro Rail Limited\"},\"locations\":[{\"id\":\"CCUV\",\"descriptor\":{\"name\":\"Cusat\"},\"station_code\":\"CCUV\",\"gps\":\"10.0467,76.3182\"},{\"id\":\"KLMT\",\"descriptor\":{\"name\":\"Kalamassery\"},\"station_code\":\"KLMT\",\"gps\":\"10.0586,76.322\"},{\"id\":\"PDPM\",\"descriptor\":{\"name\":\"Pathadipalam\"},\"station_code\":\"PDPM\",\"gps\":\"10.0361,76.3144\"}],\"items\":[{\"id\":\"CCUV_TO_KLMT\",\"descriptor\":{\"name\":\"Cusat to Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:23:00.000Z\",\"2021-10-30T02:35:00.000Z\",\"2021-10-30T03:48:40.000Z\",\"2021-10-30T04:58:40.000Z\",\"2021-10-30T06:08:40.000Z\",\"2021-10-30T07:18:40.000Z\",\"2021-10-30T08:28:40.000Z\",\"2021-10-30T09:38:40.000Z\",\"2021-10-30T10:48:40.000Z\",\"2021-10-30T11:58:40.000Z\",\"2021-10-30T13:08:40.000Z\",\"2021-10-30T14:17:42.000Z\",\"2021-10-30T00:47:59.000Z\",\"2021-10-30T01:58:16.000Z\",\"2021-10-30T03:10:25.000Z\",\"2021-10-30T04:23:39.000Z\",\"2021-10-30T05:33:39.000Z\",\"2021-10-30T06:43:39.000Z\",\"2021-10-30T07:53:39.000Z\",\"2021-10-30T09:03:39.000Z\",\"2021-10-30T10:13:39.000Z\",\"2021-10-30T11:23:39.000Z\",\"2021-10-30T12:33:39.000Z\",\"2021-10-30T13:43:39.000Z\",\"2021-10-30T14:52:41.000Z\",\"2021-10-30T00:28:20.000Z\",\"2021-10-30T01:38:42.000Z\",\"2021-10-30T02:46:49.000Z\",\"2021-10-30T03:57:14.000Z\",\"2021-10-30T05:07:14.000Z\",\"2021-10-30T06:17:14.000Z\",\"2021-10-30T07:27:14.000Z\",\"2021-10-30T08:37:14.000Z\",\"2021-10-30T09:47:14.000Z\",\"2021-10-30T10:57:14.000Z\",\"2021-10-30T12:07:14.000Z\",\"2021-10-30T13:17:14.000Z\",\"2021-10-30T14:25:44.000Z\",\"2021-10-30T15:31:18.000Z\",\"2021-10-30T16:38:09.000Z\",\"2021-10-30T01:05:12.000Z\",\"2021-10-30T02:16:13.000Z\",\"2021-10-30T03:29:23.000Z\",\"2021-10-30T04:40:52.000Z\",\"2021-10-30T05:50:52.000Z\",\"2021-10-30T07:00:52.000Z\",\"2021-10-30T08:10:52.000Z\",\"2021-10-30T09:20:52.000Z\",\"2021-10-30T10:30:52.000Z\",\"2021-10-30T11:40:52.000Z\",\"2021-10-30T12:50:52.000Z\",\"2021-10-30T14:00:52.000Z\",\"2021-10-30T15:12:38.000Z\",\"2021-10-30T16:24:02.000Z\",\"2021-10-30T02:56:40.000Z\",\"2021-10-30T04:06:10.000Z\",\"2021-10-30T05:16:10.000Z\",\"2021-10-30T06:26:10.000Z\",\"2021-10-30T07:36:10.000Z\",\"2021-10-30T08:46:10.000Z\",\"2021-10-30T09:56:10.000Z\",\"2021-10-30T11:06:10.000Z\",\"2021-10-30T12:16:10.000Z\",\"2021-10-30T13:26:10.000Z\",\"2021-10-30T14:36:10.000Z\",\"2021-10-30T15:44:26.000Z\",\"2021-10-30T16:46:29.000Z\",\"2021-10-30T03:19:59.000Z\",\"2021-10-30T04:32:25.000Z\",\"2021-10-30T05:42:25.000Z\",\"2021-10-30T06:52:25.000Z\",\"2021-10-30T08:02:25.000Z\",\"2021-10-30T09:12:25.000Z\",\"2021-10-30T10:22:25.000Z\",\"2021-10-30T11:32:25.000Z\",\"2021-10-30T12:42:25.000Z\",\"2021-10-30T13:52:25.000Z\",\"2021-10-30T15:02:01.000Z\",\"2021-10-30T16:12:07.000Z\",\"2021-10-30T03:39:11.000Z\",\"2021-10-30T04:49:55.000Z\",\"2021-10-30T05:59:55.000Z\",\"2021-10-30T07:09:55.000Z\",\"2021-10-30T08:19:55.000Z\",\"2021-10-30T09:29:55.000Z\",\"2021-10-30T10:39:55.000Z\",\"2021-10-30T11:49:55.000Z\",\"2021-10-30T12:59:55.000Z\",\"2021-10-30T14:10:43.000Z\",\"2021-10-30T15:21:20.000Z\",\"2021-10-30T04:14:55.000Z\",\"2021-10-30T05:24:55.000Z\",\"2021-10-30T06:34:55.000Z\",\"2021-10-30T07:44:55.000Z\",\"2021-10-30T08:54:55.000Z\",\"2021-10-30T10:04:55.000Z\",\"2021-10-30T11:14:55.000Z\",\"2021-10-30T12:24:55.000Z\",\"2021-10-30T13:34:55.000Z\",\"2021-10-30T14:45:56.000Z\",\"2021-10-30T15:57:50.000Z\"]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:25:18.000Z\",\"2021-10-30T02:37:50.000Z\",\"2021-10-30T03:50:58.000Z\",\"2021-10-30T05:00:58.000Z\",\"2021-10-30T06:10:58.000Z\",\"2021-10-30T07:20:58.000Z\",\"2021-10-30T08:30:58.000Z\",\"2021-10-30T09:40:58.000Z\",\"2021-10-30T10:50:58.000Z\",\"2021-10-30T12:00:58.000Z\",\"2021-10-30T13:10:58.000Z\",\"2021-10-30T14:19:52.000Z\",\"2021-10-30T00:50:17.000Z\",\"2021-10-30T02:00:35.000Z\",\"2021-10-30T03:13:11.000Z\",\"2021-10-30T04:25:57.000Z\",\"2021-10-30T05:35:57.000Z\",\"2021-10-30T06:45:57.000Z\",\"2021-10-30T07:55:57.000Z\",\"2021-10-30T09:05:57.000Z\",\"2021-10-30T10:15:57.000Z\",\"2021-10-30T11:25:57.000Z\",\"2021-10-30T12:35:57.000Z\",\"2021-10-30T13:45:57.000Z\",\"2021-10-30T14:54:51.000Z\",\"2021-10-30T00:31:23.000Z\",\"2021-10-30T01:40:37.000Z\",\"2021-10-30T02:49:07.000Z\",\"2021-10-30T03:59:32.000Z\",\"2021-10-30T05:09:32.000Z\",\"2021-10-30T06:19:32.000Z\",\"2021-10-30T07:29:32.000Z\",\"2021-10-30T08:39:32.000Z\",\"2021-10-30T09:49:32.000Z\",\"2021-10-30T10:59:32.000Z\",\"2021-10-30T12:09:32.000Z\",\"2021-10-30T13:19:32.000Z\",\"2021-10-30T14:27:50.000Z\",\"2021-10-30T15:33:08.000Z\",\"2021-10-30T16:40:15.000Z\",\"2021-10-30T01:07:30.000Z\",\"2021-10-30T02:18:45.000Z\",\"2021-10-30T03:31:55.000Z\",\"2021-10-30T04:43:10.000Z\",\"2021-10-30T05:53:10.000Z\",\"2021-10-30T07:03:10.000Z\",\"2021-10-30T08:13:10.000Z\",\"2021-10-30T09:23:10.000Z\",\"2021-10-30T10:33:10.000Z\",\"2021-10-30T11:43:10.000Z\",\"2021-10-30T12:53:10.000Z\",\"2021-10-30T14:03:10.000Z\",\"2021-10-30T15:15:24.000Z\",\"2021-10-30T16:26:08.000Z\",\"2021-10-30T02:58:40.000Z\",\"2021-10-30T04:08:28.000Z\",\"2021-10-30T05:18:28.000Z\",\"2021-10-30T06:28:28.000Z\",\"2021-10-30T07:38:28.000Z\",\"2021-10-30T08:48:28.000Z\",\"2021-10-30T09:58:28.000Z\",\"2021-10-30T11:08:28.000Z\",\"2021-10-30T12:18:28.000Z\",\"2021-10-30T13:28:28.000Z\",\"2021-10-30T14:38:28.000Z\",\"2021-10-30T15:46:32.000Z\",\"2021-10-30T16:48:35.000Z\",\"2021-10-30T03:22:36.000Z\",\"2021-10-30T04:34:43.000Z\",\"2021-10-30T05:44:43.000Z\",\"2021-10-30T06:54:43.000Z\",\"2021-10-30T08:04:43.000Z\",\"2021-10-30T09:14:43.000Z\",\"2021-10-30T10:24:43.000Z\",\"2021-10-30T11:34:43.000Z\",\"2021-10-30T12:44:43.000Z\",\"2021-10-30T13:54:43.000Z\",\"2021-10-30T15:04:15.000Z\",\"2021-10-30T16:14:26.000Z\",\"2021-10-30T03:41:34.000Z\",\"2021-10-30T04:52:13.000Z\",\"2021-10-30T06:02:13.000Z\",\"2021-10-30T07:12:13.000Z\",\"2021-10-30T08:22:13.000Z\",\"2021-10-30T09:32:13.000Z\",\"2021-10-30T10:42:13.000Z\",\"2021-10-30T11:52:13.000Z\",\"2021-10-30T13:02:13.000Z\",\"2021-10-30T14:13:11.000Z\",\"2021-10-30T15:23:26.000Z\",\"2021-10-30T04:17:13.000Z\",\"2021-10-30T05:27:13.000Z\",\"2021-10-30T06:37:13.000Z\",\"2021-10-30T07:47:13.000Z\",\"2021-10-30T08:57:13.000Z\",\"2021-10-30T10:07:13.000Z\",\"2021-10-30T11:17:13.000Z\",\"2021-10-30T12:27:13.000Z\",\"2021-10-30T13:37:13.000Z\",\"2021-10-30T14:48:28.000Z\",\"2021-10-30T16:00:08.000Z\"]}}}],\"location_id\":\"CCUV\",\"matched\":true},{\"id\":\"PDPM_TO_KLMT\",\"descriptor\":{\"name\":\"Pathadipalam to Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"20\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true},{\"id\":\"PDPM_TO_CCUV\",\"descriptor\":{\"name\":\"Pathadipalam to Cusat\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true}]}]}}}";
//				//String req = "{\"context\": {\"domain\": \"nic2004:55204\", \"country\": \"IND\", \"city\": \"std:080\", \"action\": \"search\", \"core_version\": \"0.9.2\", \"bap_id\": \"sandbox.hitbyseo.com:9000/delivery/bap\", \"bap_uri\": \"https://sandbox.hitbyseo.com:9000/delivery/bap/\", \"bpp_id\": \"\", \"bpp_uri\": \"\", \"transaction_id\":\""+generatedString+"\", \"message_id\": \"BoTxMJ3zqDrjreNS9D3esp\", \"timestamp\": \"2021-12-16T09:55:19.019832Z\", \"key\": \"\", \"ttl\": \"\"}, \"message\": {\"intent\": {\"fulfillment\": {\"start\": {\"location\": {\"gps\": \"12.4535445,77.9283792\"}}, \"end\": {\"location\": {\"gps\": \"12.969686811388264, 77.60998311489479\"}}}}}}";
//				//String req = "{\"context\": {\"domain\": \"nic2004:55204\", \"country\": \"IND\", \"city\": \"std:080\", \"action\": \"on_search\", \"core_version\": \"0.9.2\", \"bap_id\": \"sandbox.hitbyseo.com:9000/delivery/bap\", \"bap_uri\": \"https://sandbox.hitbyseo.com:9000/delivery/bap/\", \"bpp_id\": \"\", \"bpp_uri\": \"\",\"transaction_id\":\"\+generatedString+\", \"message_id\": \"BoTxMJ3zqDrjreNS9D3esp\", \"timestamp\": \"2021-12-16T09:55:19.019832Z\", \"key\": \"\", \"ttl\": \"\"}, \"message\": {\"intent\": {\"fulfillment\": {\"start\": {\"location\": {\"gps\": \"12.4535445,77.9283792\"}}, \"end\": {\"location\": {\"gps\": \"12.969686811388264, 77.60998311489479\"}}}}}}";
//				long testTimestamp = System.currentTimeMillis() / 1000L;
//
//				sb.append(req);
//				sb.append("^");
//				System.out.println("Test Timestamp :" + testTimestamp);
//
//				System.out.println("\n==============================Json Request===================================");
//				System.out.println(req);
//
//				String blakeValue;
//				try {
//					blakeValue = generateBlakeHash(req);
//				
//
//				System.out.println("\n==============================Digest Value ===================================");
//				String signingString = "(created): "+testTimestamp+"\n(expires): "+(testTimestamp + 60000)
//						+"\ndigest: BLAKE-512="+blakeValue+"";
//
//				System.out.println("\n==============================Data to Sign===================================");
//				System.out.println(signingString);
//
//				String header = "("+testTimestamp+") ("+(testTimestamp + 60000)+") BLAKE-512="+ blakeValue +"";
//
//				String signedReq = generateSignature(signingString, privateKey);
//
//				System.out.println("Signature : " + signedReq);
//				String authHeader = "Signature keyId=\""+kid+"\",algorithm=\"ed25519\", created=\""+testTimestamp+"\", expires=\""+(testTimestamp + 60000)+"\", headers=\"(created) (expires) digest\", signature=\""+ signedReq +"\"";
//
//				System.out.println("\n==============================Signed Request=================================");
//				System.out.println(authHeader);
//				sb.append(authHeader);
//				sb.append("\n");
//
//				writer.write(sb.toString());
//
//				System.out.println("File write done!!!");
//				//To Verify Signature
//				System.out.println("\n==============================Verify Signature================================");
//
//				//signedReq = "8cZbtVdtBwIlEdYyEp7bMf31I5J+toJpTmvQKyiWkbnLjKLduIcsnbzu7dlURHuXHGnuQ+pgc2GBmi2ZG49QBw";
//				//signingString = "(created): 1607074370\n(expires): 1607160770\ndigest: BLAKE-512=w1C1nPKuym9rOszLnqSWjnXTCmmo4i9JcIeUJGiESd2rJHUFAMGQ0wEAlVGh4C5DTjH7y4poIss5kDehariaJw==";
//				//String pk = "kCa4OlmRVfCPcvzjPPGik0Ljei5dRYuuj/2K6upaf1E=";
//
//				verifySignature(signedReq, signingString, publicKey);
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println(e.getMessage());
//		}
//	}
//
//
//
//	public static String generateSignature(String req, String pk) 
//	{
//		String signature = null;
//		try 
//		{
//			Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(Base64.getDecoder().decode(pk.getBytes()), 0);
//			Signer sig = new Ed25519Signer();
//			sig.init(true, privateKey);
//			sig.update(req.getBytes(), 0, req.length());
//			byte[] s1 = sig.generateSignature();
//			signature = Base64.getEncoder().encodeToString(s1);
//		} 
//		catch (DataLengthException | CryptoException e) 
//		{
//			e.printStackTrace();
//		}
//		return signature;
//	}
//
//	/*	public static String generateBlakeHash(String req) 
//	{
//		Blake2bDigest blake2bDigest = new Blake2bDigest(512);
//		byte[] test = req.getBytes();
//		blake2bDigest.update(test, 0, test.length);
//		byte[] hash = new byte[blake2bDigest.getDigestSize()];
//		blake2bDigest.doFinal(hash, 0);
//		//return Base64.getEncoder().encodeToString(hash);
//
//		//String hex = Hex.toHexString(hash);
//		//System.out.println("Hex : " + hex);
//		//System.out.println("returning hash: " + Base64.getEncoder().encodeToString(hash));
//		String bs64 = Base64.getUrlEncoder().encodeToString(hash);
//		System.out.println("Base64 URL Encoded : " + bs64);
//		return bs64;
//	}	*/
//
//	public static void setup(){
//        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
//            Security.addProvider(new BouncyCastleProvider());
//            System.out.println(Security.addProvider(new BouncyCastleProvider()));
//        }
//    }
//    public static String generateBlakeHash(String req) throws Exception{
//        //String stringToHash = "Beckn!";
//        //String stringToHash = "{\"context\":{\"country\":\"IND\",\"domain\":\"nic2004:60212\",\"transaction_id\":\"f01fc03d-8add-4d6b-8595-4e6cb74a7283\",\"action\":\"on_search\",\"message_id\":\"481b1f20-acea-41f1-9117-3a68df59eb72\",\"city\":\"\",\"bap_uri\":\"https://beckn.free.beeceptor.com\",\"timestamp\":\"2021-10-28T12:42:18.150954734Z\",\"core_version\":\"0.9.1\",\"bap_id\":\"https://api.sandbox.beckn.juspay.in/dev/bap/cab/v1\",\"bpp_id\":\"api.sandbox.beckn.juspay.in/bpp/metro/v1\",\"bpp_uri\":\"https://metro-bpp.com/\"},\"message\":{\"catalog\":{\"bpp/descriptor\":{\"name\":\"BPP\"},\"bpp/providers\":[{\"id\":\"KMRL\",\"descriptor\":{\"name\":\"Kochi Metro Rail Limited\"},\"locations\":[{\"id\":\"CCUV\",\"descriptor\":{\"name\":\"Cusat\"},\"station_code\":\"CCUV\",\"gps\":\"10.0467,76.3182\"},{\"id\":\"KLMT\",\"descriptor\":{\"name\":\"Kalamassery\"},\"station_code\":\"KLMT\",\"gps\":\"10.0586,76.322\"},{\"id\":\"PDPM\",\"descriptor\":{\"name\":\"Pathadipalam\"},\"station_code\":\"PDPM\",\"gps\":\"10.0361,76.3144\"}],\"items\":[{\"id\":\"CCUV_TO_KLMT\",\"descriptor\":{\"name\":\"Cusat to Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:23:00.000Z\",\"2021-10-30T02:35:00.000Z\",\"2021-10-30T03:48:40.000Z\",\"2021-10-30T04:58:40.000Z\",\"2021-10-30T06:08:40.000Z\",\"2021-10-30T07:18:40.000Z\",\"2021-10-30T08:28:40.000Z\",\"2021-10-30T09:38:40.000Z\",\"2021-10-30T10:48:40.000Z\",\"2021-10-30T11:58:40.000Z\",\"2021-10-30T13:08:40.000Z\",\"2021-10-30T14:17:42.000Z\",\"2021-10-30T00:47:59.000Z\",\"2021-10-30T01:58:16.000Z\",\"2021-10-30T03:10:25.000Z\",\"2021-10-30T04:23:39.000Z\",\"2021-10-30T05:33:39.000Z\",\"2021-10-30T06:43:39.000Z\",\"2021-10-30T07:53:39.000Z\",\"2021-10-30T09:03:39.000Z\",\"2021-10-30T10:13:39.000Z\",\"2021-10-30T11:23:39.000Z\",\"2021-10-30T12:33:39.000Z\",\"2021-10-30T13:43:39.000Z\",\"2021-10-30T14:52:41.000Z\",\"2021-10-30T00:28:20.000Z\",\"2021-10-30T01:38:42.000Z\",\"2021-10-30T02:46:49.000Z\",\"2021-10-30T03:57:14.000Z\",\"2021-10-30T05:07:14.000Z\",\"2021-10-30T06:17:14.000Z\",\"2021-10-30T07:27:14.000Z\",\"2021-10-30T08:37:14.000Z\",\"2021-10-30T09:47:14.000Z\",\"2021-10-30T10:57:14.000Z\",\"2021-10-30T12:07:14.000Z\",\"2021-10-30T13:17:14.000Z\",\"2021-10-30T14:25:44.000Z\",\"2021-10-30T15:31:18.000Z\",\"2021-10-30T16:38:09.000Z\",\"2021-10-30T01:05:12.000Z\",\"2021-10-30T02:16:13.000Z\",\"2021-10-30T03:29:23.000Z\",\"2021-10-30T04:40:52.000Z\",\"2021-10-30T05:50:52.000Z\",\"2021-10-30T07:00:52.000Z\",\"2021-10-30T08:10:52.000Z\",\"2021-10-30T09:20:52.000Z\",\"2021-10-30T10:30:52.000Z\",\"2021-10-30T11:40:52.000Z\",\"2021-10-30T12:50:52.000Z\",\"2021-10-30T14:00:52.000Z\",\"2021-10-30T15:12:38.000Z\",\"2021-10-30T16:24:02.000Z\",\"2021-10-30T02:56:40.000Z\",\"2021-10-30T04:06:10.000Z\",\"2021-10-30T05:16:10.000Z\",\"2021-10-30T06:26:10.000Z\",\"2021-10-30T07:36:10.000Z\",\"2021-10-30T08:46:10.000Z\",\"2021-10-30T09:56:10.000Z\",\"2021-10-30T11:06:10.000Z\",\"2021-10-30T12:16:10.000Z\",\"2021-10-30T13:26:10.000Z\",\"2021-10-30T14:36:10.000Z\",\"2021-10-30T15:44:26.000Z\",\"2021-10-30T16:46:29.000Z\",\"2021-10-30T03:19:59.000Z\",\"2021-10-30T04:32:25.000Z\",\"2021-10-30T05:42:25.000Z\",\"2021-10-30T06:52:25.000Z\",\"2021-10-30T08:02:25.000Z\",\"2021-10-30T09:12:25.000Z\",\"2021-10-30T10:22:25.000Z\",\"2021-10-30T11:32:25.000Z\",\"2021-10-30T12:42:25.000Z\",\"2021-10-30T13:52:25.000Z\",\"2021-10-30T15:02:01.000Z\",\"2021-10-30T16:12:07.000Z\",\"2021-10-30T03:39:11.000Z\",\"2021-10-30T04:49:55.000Z\",\"2021-10-30T05:59:55.000Z\",\"2021-10-30T07:09:55.000Z\",\"2021-10-30T08:19:55.000Z\",\"2021-10-30T09:29:55.000Z\",\"2021-10-30T10:39:55.000Z\",\"2021-10-30T11:49:55.000Z\",\"2021-10-30T12:59:55.000Z\",\"2021-10-30T14:10:43.000Z\",\"2021-10-30T15:21:20.000Z\",\"2021-10-30T04:14:55.000Z\",\"2021-10-30T05:24:55.000Z\",\"2021-10-30T06:34:55.000Z\",\"2021-10-30T07:44:55.000Z\",\"2021-10-30T08:54:55.000Z\",\"2021-10-30T10:04:55.000Z\",\"2021-10-30T11:14:55.000Z\",\"2021-10-30T12:24:55.000Z\",\"2021-10-30T13:34:55.000Z\",\"2021-10-30T14:45:56.000Z\",\"2021-10-30T15:57:50.000Z\"]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[\"2021-10-30T01:25:18.000Z\",\"2021-10-30T02:37:50.000Z\",\"2021-10-30T03:50:58.000Z\",\"2021-10-30T05:00:58.000Z\",\"2021-10-30T06:10:58.000Z\",\"2021-10-30T07:20:58.000Z\",\"2021-10-30T08:30:58.000Z\",\"2021-10-30T09:40:58.000Z\",\"2021-10-30T10:50:58.000Z\",\"2021-10-30T12:00:58.000Z\",\"2021-10-30T13:10:58.000Z\",\"2021-10-30T14:19:52.000Z\",\"2021-10-30T00:50:17.000Z\",\"2021-10-30T02:00:35.000Z\",\"2021-10-30T03:13:11.000Z\",\"2021-10-30T04:25:57.000Z\",\"2021-10-30T05:35:57.000Z\",\"2021-10-30T06:45:57.000Z\",\"2021-10-30T07:55:57.000Z\",\"2021-10-30T09:05:57.000Z\",\"2021-10-30T10:15:57.000Z\",\"2021-10-30T11:25:57.000Z\",\"2021-10-30T12:35:57.000Z\",\"2021-10-30T13:45:57.000Z\",\"2021-10-30T14:54:51.000Z\",\"2021-10-30T00:31:23.000Z\",\"2021-10-30T01:40:37.000Z\",\"2021-10-30T02:49:07.000Z\",\"2021-10-30T03:59:32.000Z\",\"2021-10-30T05:09:32.000Z\",\"2021-10-30T06:19:32.000Z\",\"2021-10-30T07:29:32.000Z\",\"2021-10-30T08:39:32.000Z\",\"2021-10-30T09:49:32.000Z\",\"2021-10-30T10:59:32.000Z\",\"2021-10-30T12:09:32.000Z\",\"2021-10-30T13:19:32.000Z\",\"2021-10-30T14:27:50.000Z\",\"2021-10-30T15:33:08.000Z\",\"2021-10-30T16:40:15.000Z\",\"2021-10-30T01:07:30.000Z\",\"2021-10-30T02:18:45.000Z\",\"2021-10-30T03:31:55.000Z\",\"2021-10-30T04:43:10.000Z\",\"2021-10-30T05:53:10.000Z\",\"2021-10-30T07:03:10.000Z\",\"2021-10-30T08:13:10.000Z\",\"2021-10-30T09:23:10.000Z\",\"2021-10-30T10:33:10.000Z\",\"2021-10-30T11:43:10.000Z\",\"2021-10-30T12:53:10.000Z\",\"2021-10-30T14:03:10.000Z\",\"2021-10-30T15:15:24.000Z\",\"2021-10-30T16:26:08.000Z\",\"2021-10-30T02:58:40.000Z\",\"2021-10-30T04:08:28.000Z\",\"2021-10-30T05:18:28.000Z\",\"2021-10-30T06:28:28.000Z\",\"2021-10-30T07:38:28.000Z\",\"2021-10-30T08:48:28.000Z\",\"2021-10-30T09:58:28.000Z\",\"2021-10-30T11:08:28.000Z\",\"2021-10-30T12:18:28.000Z\",\"2021-10-30T13:28:28.000Z\",\"2021-10-30T14:38:28.000Z\",\"2021-10-30T15:46:32.000Z\",\"2021-10-30T16:48:35.000Z\",\"2021-10-30T03:22:36.000Z\",\"2021-10-30T04:34:43.000Z\",\"2021-10-30T05:44:43.000Z\",\"2021-10-30T06:54:43.000Z\",\"2021-10-30T08:04:43.000Z\",\"2021-10-30T09:14:43.000Z\",\"2021-10-30T10:24:43.000Z\",\"2021-10-30T11:34:43.000Z\",\"2021-10-30T12:44:43.000Z\",\"2021-10-30T13:54:43.000Z\",\"2021-10-30T15:04:15.000Z\",\"2021-10-30T16:14:26.000Z\",\"2021-10-30T03:41:34.000Z\",\"2021-10-30T04:52:13.000Z\",\"2021-10-30T06:02:13.000Z\",\"2021-10-30T07:12:13.000Z\",\"2021-10-30T08:22:13.000Z\",\"2021-10-30T09:32:13.000Z\",\"2021-10-30T10:42:13.000Z\",\"2021-10-30T11:52:13.000Z\",\"2021-10-30T13:02:13.000Z\",\"2021-10-30T14:13:11.000Z\",\"2021-10-30T15:23:26.000Z\",\"2021-10-30T04:17:13.000Z\",\"2021-10-30T05:27:13.000Z\",\"2021-10-30T06:37:13.000Z\",\"2021-10-30T07:47:13.000Z\",\"2021-10-30T08:57:13.000Z\",\"2021-10-30T10:07:13.000Z\",\"2021-10-30T11:17:13.000Z\",\"2021-10-30T12:27:13.000Z\",\"2021-10-30T13:37:13.000Z\",\"2021-10-30T14:48:28.000Z\",\"2021-10-30T16:00:08.000Z\"]}}}],\"location_id\":\"CCUV\",\"matched\":true},{\"id\":\"PDPM_TO_KLMT\",\"descriptor\":{\"name\":\"Pathadipalam to Kalamassery\"},\"price\":{\"currency\":\"INR\",\"value\":\"20\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"KLMT\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true},{\"id\":\"PDPM_TO_CCUV\",\"descriptor\":{\"name\":\"Pathadipalam to Cusat\"},\"price\":{\"currency\":\"INR\",\"value\":\"10\"},\"stops\":[{\"id\":\"PDPM\",\"time\":{\"schedule\":{\"times\":[]}}},{\"id\":\"CCUV\",\"time\":{\"schedule\":{\"times\":[]}}}],\"location_id\":\"PDPM\",\"matched\":true}]}]}}}";
//
//        MessageDigest digest = MessageDigest.getInstance("BLAKE2B-512",BouncyCastleProvider.PROVIDER_NAME);
//        digest.reset();
//        digest.update(req.getBytes(StandardCharsets.UTF_8));
//        byte[] hash = digest.digest();
//        String bs64 = Base64.getEncoder().encodeToString(hash);
//        System.out.println(bs64);
//        return bs64;
//        
//        //System.out.println(toHex(hash)); // We could use bs64 or hex of the hash (Not sure why we need to hex and then base64 it!! Seems overkill.
//        //https://8gwifi.org/MessageDigest.jsp
//
//    }
//	
///*	public static String generateBlakeHash(String req) 
//	{
//		Blake2bDigest blake2bDigest = new Blake2bDigest(512);
//
//		byte[] test = req.getBytes();
//		blake2bDigest.update(test, 0, test.length);
//
//		byte[] hash = new byte[blake2bDigest.getDigestSize()];
//		blake2bDigest.doFinal(hash, 0);
//
//		String bs64 = Base64.getUrlEncoder().encodeToString(hash);
//		return bs64;
//	}*/
//
//
//	public static boolean verifySignature(String sign, String requestData, String dbPublicKey)
//	{
//		boolean isVerified = false;
//		try 
//		{
//			System.out.println("Sign : " + sign + " requestData : " + requestData + " PublicKey : " + dbPublicKey);
//			//Ed25519PublicKeyParameters publicKey = new Ed25519PublicKeyParameters(Hex.decode(dbPublicKey), 0);
//			Ed25519PublicKeyParameters publicKey = new Ed25519PublicKeyParameters(Base64.getDecoder().decode(dbPublicKey), 0);
//			Signer sv = new Ed25519Signer();
//			sv.init(false, publicKey);
//			sv.update(requestData.getBytes(), 0, requestData.length());
//
//			byte[] decodedSign = Base64.getDecoder().decode(sign);
//			isVerified = sv.verifySignature(decodedSign);
//			System.out.println("Is Sign Verified : " + isVerified);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//		return isVerified;
//	}
//}