package com.nsdl.beckn.np.utl;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	 
	public static Map<String, Boolean> mapType = new HashMap();
	static {
		mapType.put("buyerApp", true);
		mapType.put("sellerApp", true);

	}

	public static Integer API_VERSION = 1;
	public static String RESPONSE_ERROR = "error";
	public static String RESPONSE_OK = "success";
	public static String VALIDATION_ERROR = "VALIDATION_ERROR";
	public static String OCSP_NOT_VALID = "Not Valid";
	public static String SELLER_APP = "sellerApp";
	public static String BUYER_APP = "buyerApp";
	public static String GATEWAY = "gateway";
	public static String HIGH = "high";
	 

	// 0 : buyer , 1: seller, 2 :msn , 3: entity , 4:network participient , 5:
	// seller on rwcord
	public static boolean optionFlag[][] = new boolean[][] { { true, false, false, true, true, false },
			{ false, true, false, true, true, false }, { false, true, true, true, true, true },
			{ true, true, false, true, true, false }, { true, true, true, true, true, true },
			{ true, true, true, true, false, false }, { false, true, true, true, true, true },
			{ true, true, false, true, true, false }, { true, true, true, true, true, true },
			{ false, true, true, true, true, true }, { true, true, false, true, true, false },
			{ false, true, true, true, true, true } };

	public static String CONTEXT_ERROR = "CONTEXT-ERROR";

	public static String CORE_ERROR = "CORE-ERROR";
	public static String DOMAIN_ERROR = "DOMAIN-ERROR";
	public static String UNIQUE_KEY_ID_ERROR = "UNIQUE_KEY_ID_ERROR";
	public static String POLICY_ERROR = "POLICY-ERROR";
	public static String JSON_SCHEMA_ERROR = "JSON-SCHEMA-ERROR";

	public static String STATUS_SUBSCRIBED = "SUBSCRIBED";

	public static String REDIES_KEY_LOOKUP = "LOOKUP";

	public static String[] SUBSRCIBE_CONTEXT_INVALID = new String[] { "101", "Please provide valid context" };
	public static String[] JSON_SCHEMA_ERROR_ENTITY_INVALID = new String[] { "102", "Please provide valid Entity" };
	public static String[] JSON_SCHEMA_ERROR_PAN_INVALID = new String[] { "103", "Please provide valid Pan Details" };
	public static String[] JSON_SCHEMA_ERROR_SUBSCRIBER_INVALID = new String[] { "104",
			"Please provide valid Subscriber ID" };
	public static String[] JSON_SCHEMA_ERROR_EncryptionPublicKey_INVALID = new String[] { "105",
			"Please provide valid Encryption Public Key" };
	public static String[] JSON_SCHEMA_ERROR_SigningPublicKey_INVALID = new String[] { "106",
			"Please provide valid Signing Public Key ID" };
	public static String[] JSON_SCHEMA_ERROR_KEYPAIRDATE_INVALID = new String[] { "107",
			"Please provide valid Key pair Date" };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_INVALID = new String[] { "108",
			"Please provide valid Network Participant" };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_Type_INVALID = new String[] { "109",
			"Please provide valid Network Participant [index] Type" };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_MSN_INVALID = new String[] { "110",
			"Please provide valid Network Participant [index] MSN " };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_DOMAIN_INVALID = new String[] { "111",
			"Please provide valid Network Participant [index] Domain " };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_SUBSCRIBER_URL_INVALID = new String[] { "112",
			"Please provide valid Network Participant [index] Subscriber URL " };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_CALLBACK_URL_INVALID = new String[] { "113",
			"Please provide valid Entity Callback URL " };
	public static String[] JSON_SCHEMA_ERROR_Network_Participant_CITY_CODE_INVALID = new String[] { "114",
			"Please provide valid Network Participant [index] City " };
	public static String[] JSON_SCHEMA_ERROR_SELLER_INVALID = new String[] { "115",
			"Please provide valid Seller on Record of Network Participant [index] " };

	public static String[] JSON_SCHEMA_ERROR_SELLER_UNIQUE_INVALID = new String[] { "116",
			"Network Participant [index] : Please provide valid Seller [index-j] Unique Key ID" };

	public static String[] JSON_SCHEMA_ERROR_SELLER_CITY_INVALID = new String[] { "117",
			"Network Participant [index] : Please provide valid Seller [index-j] City " };

	public static String[] JSON_SCHEMA_SELLER_ERROR_SELLER_KEYPAIR_INVALID = new String[] { "118",
			"Network Participant [index]: Please provide valid Seller On Record [index-j] Key Pair" };

	public static String[] JSON_SCHEMA_SELLER_ERROR_EncryptionPublicKey_INVALID = new String[] { "119",
			"Network Participant [index] : Please provide valid Seller On Record [index-j] Encryption Public Key" };

	public static String[] JSON_SCHEMA_ERROR_SELLER_SigningPublicKey_INVALID = new String[] { "120",
			"Network Participant [index] : Please provide valid Seller On Record [index-j] Signing Public Key ID" };
	public static String[] JSON_SCHEMA_ERROR_SELLER_KEYPAIRDATE_INVALID = new String[] { "121",
			"Network Participant [index] : Please provide valid Seller On Record [index-j] Key pair Date" };

	public static String[] JSON_SCHEMA_ERROR_SELLER_NO_INVALID = new String[] { "122",
			"Please do not provide Seller on Record for this Network Participant [index]" };

	public static String[] DOMAIN_ERROR_OCSP_INVALID = new String[] { "123", "URL : OCSP Validation Failed" };

	public static String[] JSON_SCHEMA_ERROR_PAN_DATE_INVALID = new String[] { "124",
			"Please provide valid Pan date_of_incorporation" };

	public static String[] POLICY_ERROR_SUBSCRIBEID_EXITS_INVALID = new String[] { "125",
			"Subscriber id already exists" };

	public static String[] POLICY_ERROR_SUBSCRIBEID_NFOUND_INVALID = new String[] { "126", "Subscriber id not found." };
	public static String[] JSON_SCHEMA_ERROR_PAN_EXITS_INVALID = new String[] { "127",
			"Please do not provide PAN for this context " };
	public static String[] JSON_SCHEMA_ERROR_GST_EXITS_INVALID = new String[] { "128",
			"Please do not provide GST for this context " };

	public static String[] DOMAIN_ERROR_DOMAIN_FAIL_INVALID = new String[] { "129",
			"URL : Domain verification is failed " };
	public static String[] DOMAIN_ERROR_DOMAIN_FILE_NOT_FOUND_INVALID = new String[] { "130",
			"URL : Domain verification file (ondc-site-verification.html) is not found " };
	public static String[] CORE_ERROR_ENCR_FAIL_INVALID = new String[] { "131",
			"URL : Encryption verification is failed" };

	public static String[] POLICY_ERROR_TIMESTAMP_INVALID = new String[] { "132", "Timestamp is invalid" };

	public static String[] JSON_SCHEMA_ERROR_COUNTRY_INVALID = new String[] { "133", "Please provide valid Country" };

	public static String[] JSON_SCHEMA_ERROR_REQID_INVALID = new String[] { "134", "Please provide valid request_id" };

	public static String[] JSON_SCHEMA_ERROR_CITYCODE_INVALID = new String[] { "135", "URL : City code is invalid " };

	public static String[] JSON_SCHEMA_NP_INVALID = new String[] { "136", "Network Participant [index] is not found" };

	public static String[] JSON_SCHEMA_NP_VALID = new String[] { "137",
			"Network Participant [index] is  already exists." };

	public static String[] JSON_SCHEMA_SELLER_INVALID = new String[] { "138",
			"Network Participant [index] of Seller On Record [index-j]  is not found" };

	public static String[] JSON_SCHEMA_SELLER_VALID = new String[] { "139",
			"Network Participant [index] of Seller On Record [index-j] is  already exists." };

	public static String[] JSON_SCHEMA_ERROR_MSN_INVALID = new String[] { "140",
			"Msn should be false for this Network Participant [index]" };
	public static String[] JSON_SCHEMA_ERROR_SENDER_SUBSCRIBER_INVALID = new String[] { "141",
			"Please provide valid Sender Subscriber ID" };
	public static String[] JSON_SCHEMA_ERROR_LOOKUP_INVALID = new String[] { "142",
			"Please provide at least two parameters" };
	public static String[] JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID = new String[] { "142",
			"Please provide at least two parameters" };

	public static String[] JSON_SCHEMA_NO_NP_INVALID = new String[] { "143",
			"Please do not provide Network Participants " };

	public static String[] POLICY_ERROR_DOMAIN_INVALID = new String[] { "144",
			"The Subscriber id is not whitelisted in our database" };

	public static String[] JSON_SCHEMA_ERROR_UNIQUE_ID_INVALID = new String[] { "145",
			"Please provide valid unique_key_id" };

	public static String[] JSON_SCHEMA_ERROR_SELLER_Not_VALID = new String[] { "146",
			"Please do not provide  Seller on Record of Network Participant [index] " };

	public static String[] VALIDATION_ERROR_PAN_INVALID = new String[] { "147", "Invalid Pan Number" };

	public static final String[] VALIDATION_ERROR_GST_INVALID = new String[] { "148", "Invalid GST Number" };
	
	public static String[] ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_ENTITY_MASTER = new String[] { "149",
	"UNIQUE_KEY_ID is already exists in ENTITY_MASTER" };
	
	public static String[] ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_SELLER_ON_RECORDS_MASTER = new String[] { "150",
	"UNIQUE_KEY_ID is already exists in SELLER_ON_RECORDS_MASTER index" };
	public static String[] VALIDSCHEMA = new String[] { "151",
	"Please provide valid schema." };

	// public static String[] RESPONSE_ONBORDING_REQUEST_ERROR = new String[] {
	// "101",
//			"Server Side Error. Call Helpdesk for details." };
//
//	public static String[] RESPONSE_ONBORDING_REQUEST_OCSP_ERROR = new String[] { "102", "OCSP Validation Failed" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_OK = new String[] { "200", "Success" };
//
//	public static String[] RESPONSE_VERIFY_DOMAIN_ERROR = new String[] { "103", "Domain verification is failed" };
//	public static String[] RESPONSE_VERIFY_DOMAIN_OK = new String[] { "200",
//			"Domain Verified. Please save ackCode(DverifyAckCode) for verification of keys" };
//
//	public static String[] RESPONSE_VERIFYK_SIG_INIT_ERROR = new String[] { "104", "Signature verification is failed" };
//	public static String[] RESPONSE_VERIFYK_ENCRYPT_INIT_ERROR = new String[] { "105",
//			"Encryption verification is failed" };
//
//	public static String[] RESPONSE_ONSUBSCRIBE_REQUEST_ERROR = new String[] { "106", "previous_req_id is incorrect" };
//	public static String[] RESPONSE_ONSUBSCRIBE_SUBSCRIBE_ID_ERROR = new String[] { "107",
//			"subscriber_id is incorrect" };
//	public static String[] RESPONSE_ONSUBSCRIBE_OK = new String[] { "200", "INITIATED" };
//	public static String[] RESPONSE_LOOKP_ERROR = new String[] { "108", "Provide at least one attribute" };
//	public static String[] RESPONSE_VERIFY_ACK_ERROR = new String[] { "109",
//			"Ack code  is not associated with confReqID " };
//	public static String[] RESPONSE_REQUEST_ID_ERROR = new String[] { "110", "Conf ReqID is not found" };
//	public static String[] RESPONSE_VERIFYK_REQ_ID_INIT_ERROR = new String[] { "111", "Verify Ack Code is invalid" };
//	public static String[] RESPONSE_SERVER_ENC_ERROR = new String[] { "112", "Server Encryption keys not found" };

//	public static String[] RESPONSE_ONBORDING_REQUEST_SubscriberId_ERROR = new String[] { "113",
//			"subscriber_id is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_subscriber_url_ERROR = new String[] { "114",
//			"subscriber_url is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_signing_public_key_ERROR = new String[] { "115",
//			"signing_public_key is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_encr_public_key_ERROR = new String[] { "116",
//			"encr_public_key is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_callback_url_ERROR = new String[] { "117",
//			"callback_url is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_confReqID_ERROR = new String[] { "118", "confReqID is required" };
//
//	public static String[] RESPONSE_ONBORDING_REQUEST_VERIFY_confReqID_ERROR = new String[] { "119",
//			"confReqID is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_VERIFY_verReqID_ERROR = new String[] { "124",
//			"verReqID is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_VERIFY_dInitAckCode_ERROR = new String[] { "125",
//			"dInitAckCode is required" };
//
//	public static String[] RESPONSE_ONBORDING_REQUEST_KINIT_keyVerReqID_ERROR = new String[] { "120",
//			"keyVerReqID is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_KINIT_dVerifyAckCode_ERROR = new String[] { "121",
//			"dVerifyAckCode is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_KINIT_signature_ERROR = new String[] { "122",
//			"signature is required" };
//	public static String[] RESPONSE_ONBORDING_REQUEST_KINIT_encMessage_ERROR = new String[] { "123",
//			"encMessage is required" };
//
//	public static String[] RESPONSE_SUBCRIBE_REQUEST_previous_req_id_ERROR = new String[] { "126",
//			"previous_req_id is required" };
//
//	public static String[] RESPONSE_SUBCRIBE_REQUEST_signature_ERROR = new String[] { "127", "signature is required" };
//	public static String[] RESPONSE_SUBCRIBE_REQUEST_reqID_ERROR = new String[] { "128", "request_id is required" };
//	public static String[] RESPONSE_DATE_ERROR = new String[] { "129", "" };

}
