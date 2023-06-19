package com.nsdl.onsubscribe.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Decrypt {

	public static String clientPrivateKey = "MFECAQEwBQYDK2VuBCIEIGDMqcp4yHbFFcH2eTqPK7pOUerhubc0Xggqfps9H8ZDgSEAHH8kACAM75hrDMdSjX3sfulqVdEE0f3ZOpy0EtVR3wg=";
	public static String clientPublicKey = "MCowBQYDK2VuAyEASfWOME2kQQ75i5iMHx0ZodBn0P9UTHcOkeczDmeOVkU=";

	public static String proteanPublicKey = "MCowBQYDK2VuAyEALtPj74XkIrkyxTqyssjtYJ3KRND5FnzK5MDrwlK3kC8=";
	public static String proteanPrivateKey = "MFECAQEwBQYDK2VuBCIEIAj5U1DVAX5eGI1jIIcjmzWgPQlIg/T1Q6A3pZ0AIWp6gSEAJGnKRTAEcSvpgD0mw9gBHv94E3w8sTtmPlszuXIEAF0=";

	public static String secretKey = "TlsPremasterSecret";

	public Decrypt() {
		setup();
	}

	public static void setup() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public String decrypt(String clientPrivateKey, String proteanPublicKey, String value) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(proteanPublicKey);
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(clientPrivateKey);
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			Cipher cipher2 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher2.init(Cipher.DECRYPT_MODE, key1); // Same derived key in server 2same as key1
			byte[] decrypted2 = cipher2.doFinal(Base64.getDecoder().decode(value)); // b64 decode the
																					// message before

			return new String(decrypted2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public PublicKey getPublicKey(String algo, byte[] jceBytes) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(jceBytes);
		PublicKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePublic(x509EncodedKeySpec);
		return key;
	}

	public PrivateKey getPrivateKey(String algo, byte[] jceBytes) throws Exception {
		PrivateKey key = KeyFactory.getInstance(algo, BouncyCastleProvider.PROVIDER_NAME)
				.generatePrivate(new PKCS8EncodedKeySpec(jceBytes));
		return key;
	}

	public String encrypt(String clientPublicKey, String proteanPrivateKey, String value) {

		try {
			byte[] dataBytes = Base64.getDecoder().decode(clientPublicKey);
			PublicKey publicKey = getPublicKey("X25519", dataBytes);

			dataBytes = Base64.getDecoder().decode(proteanPrivateKey);
			PrivateKey privateKey = getPrivateKey("X25519", dataBytes);

			KeyAgreement atServer1 = KeyAgreement.getInstance("X25519", BouncyCastleProvider.PROVIDER_NAME);
			atServer1.init(privateKey); // Server1 uses its private key to initialize the aggreement object
			atServer1.doPhase(publicKey, true); // Uses Server2's ppublic Key
			SecretKey key1 = atServer1.generateSecret(secretKey); // derive secret at server 1.
																	// "TlsPremasterSecret" is the algorithm for

			// *Server1
			Cipher cipher1 = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher1.init(Cipher.ENCRYPT_MODE, key1);
			byte[] encrypted1 = cipher1.doFinal(value.getBytes(StandardCharsets.UTF_8));
			String b64Encryped1 = Base64.getEncoder().encodeToString(encrypted1);

			return b64Encryped1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}



	 	public static void main(String[] args) {
		Decrypt decr = new Decrypt();
//		{
//		    "publicKey": "ag5vB84plQQZ0b7HAyXu6ayCyGdnl+cNSAMhaYCHwew=",
//		    "privateKey": "f0t/bU4ENWwoUOGCS5FZKN96IRRF+tXrhjeNrplGeQ0="
//		}
//		{
//		    "publicKey": "MCowBQYDK2VuAyEAJpc9QT17Ibv65HzdunDzCgvn3ol49ZPCxE0+m3krDys=",
//		    "privateKey": "MFECAQEwBQYDK2VuBCIEINhIMJJlirmrMXpAbBzpM8job5Qgp2Wo/7n5/OfLshxIgSEAJpc9QT17Ibv65HzdunDzCgvn3ol49ZPCxE0+m3krDys="
//		}
//		{
//		    "publicKey": "MCowBQYDK2VuAyEAz8ZuoES0HbBPxOsnSLINK32D1eK84rGYOO0b0MsqgxM=",
//		    "privateKey": "MFECAQEwBQYDK2VuBCIEIAB2u2jvlkTWL1A+v9ZgUvzqAdz28KuLfmzcO+cKnFxKgSEAz8ZuoES0HbBPxOsnSLINK32D1eK84rGYOO0b0MsqgxM="
//		}
		decr.setup();
		decr.clientPublicKey = "MCowBQYDK2VuAyEAJpc9QT17Ibv65HzdunDzCgvn3ol49ZPCxE0+m3krDys=";
		decr.clientPrivateKey = "MFECAQEwBQYDK2VuBCIEINhIMJJlirmrMXpAbBzpM8job5Qgp2Wo/7n5/OfLshxIgSEAJpc9QT17Ibv65HzdunDzCgvn3ol49ZPCxE0+m3krDys=";

		decr.proteanPublicKey = "MCowBQYDK2VuAyEAz8ZuoES0HbBPxOsnSLINK32D1eK84rGYOO0b0MsqgxM=";
		decr.proteanPrivateKey = "MFECAQEwBQYDK2VuBCIEIAB2u2jvlkTWL1A+v9ZgUvzqAdz28KuLfmzcO+cKnFxKgSEAz8ZuoES0HbBPxOsnSLINK32D1eK84rGYOO0b0MsqgxM=";

		String enc = decr.encrypt(decr.clientPublicKey, decr.proteanPrivateKey, "abc sa,md ");
		System.out.println(enc);
		System.out.println(decr.decrypt(decr.clientPrivateKey, decr.proteanPublicKey, enc));
//		String time = "2022-02-02T05:56:52.470618Z3";
//		DateTimeFormatter f =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'");
//		Instant.from(f.parse(time));
		String str = "{“context”:{“domain”:“nic2004:52110”,“country”:“IND”,“city”:“std:080”,“action”:“on_search”,“core_version”:“0.9.3-draft”,“bap_id”:“buyer-app.ondc.org”,“bap_uri”:“https://buyer-app.ondc.org/protocol/v1”,“bpp_id”:“retailconnect.co.in”,“bpp_uri”:“https://retailconnect.co.in/ondc/public/Ondc/callback”,“transaction_id”:“c7b48ef9-2686-4908-8a59-7b87a76f3cc5”,“message_id”:“798c2a62-5f4a-4a26-a165-d5833794f8b4”,“timestamp”:“2022-08-28T17:00:44.750Z”},“message”:{“catalog”:{“bpp/descriptor”:{“name”:“ITC Store”,“symbol”:“https://abc.com/images/18275/18275-1-shop-img”,“short_desc”:“Online eCommerce Store”,“long_desc”:“Online eCommerce Store”,“images”:[“https://abc.com/images/18275/18275-1-shop-img”]},“bpp/providers”:[{“id”:“278”,“descriptor”:{“name”:“ITC Store”,“symbol”:“https://abc.com/images/18275/18275-1-shop-img”,“short_desc”:“ITC Store”,“long_desc”:“ITC Store”,“images”:[“https://abc.com/images/18275/18275-1-shop-img”]},“@ondc/org/fssai_license_no”:“ABCDEFGH”,“ttl”:“P1D”,“locations”:[{“id”:“store-location-id-1”,“gps”:“12.967555,77.749666”,“address”:{“street”:“Jayanagar 4th Block”,“city”:“Bengaluru”,“area_code”:“560076\",“state”:“KA”}}],“items”:[{“id”:“B natural Mixed Fruit, 1 Ltr”,“descriptor”:{“name”:“B natural Mixed Fruit, 1 Ltr”,“symbol”:“https://cdn.shopify.com/s/files/1/0173/7644/4470/products/BNNCTMXFRT1000_8901725100025_1_dda38586-8025-44cc-956f-ecdb3ed985f1_1024x1024.jpg?v=1631975702”,“short_desc”:“B natural Mixed Fruit, 1 Ltr”,“long_desc”:“B Natural Mixed Fruit Juice is packed with the great taste of eight different Indian fruits such as Apples, Pineapple, Oranges, Mango and more. Only the most carefully selected Indian fruits go into the making of B Natural Mixed Fruit Juice. With B Natural, our consumers can indulge in the goodness of 100% natural fruit juice, and 0% concentrate in every sip”,“images”:[“https://cdn.shopify.com/s/files/1/0173/7644/4470/products/BNNCTMXFRT1000_8901725100025_1_dda38586-8025-44cc-956f-ecdb3ed985f1_1024x1024.jpg?v=1631975702”]},“price”:{“currency”:“INR”,“value”:“90.0”,“maximum_value”:“100.0”},“category_id”:“Packaged Commodities”,“fulfillment_id”:“1”,“location_id”:“abc-store-location-id-1”,“@ondc/org/returnable”:“true”,“@ondc/org/cancellable”:“true”,“@ondc/org/return_window”:“P7D”,“@ondc/org/seller_pickup_return”:“false”,“@ondc/org/time_to_ship”:“PT45M”,“@ondc/org/available_on_cod”:“false”,“@ondc/org/contact_details_consumer_care”:“Ramesh, Koramangala, Bengaluru, ramesh@abc.com, 9876543210”,“@ondc/org/statutory_reqs_packaged_commodities”:{“manufacturer_or_packer_name”:“ITC Limited”,“manufacturer_or_packer_address”:“123, xyz street, Bengaluru”,“common_or_generic_name_of_commodity”:“Juices”,“net_quantity_or_measure_of_commodity_in_pkg”:“100\",“month_year_of_manufacture_packing_import”:“08/2022\"},“@ondc/org/statutory_reqs_prepackaged_food”:{“nutritional_info”:“Energy(KCal)-(per 100kg) 420, (per serving 50g)250; Protein(g)-(per 100kg) 12, (per serving 50g) 6”,“additives_info”:“Preservatives, Artificial Colours”},“@ondc/org/mandatory_reqs_veggies_fruits”:{“net_quantity”:“100g”}}]}]}}}";

	}

}
