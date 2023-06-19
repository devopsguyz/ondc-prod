package com.nsdl.beckn.lm.registry.utl;

public class Constants {

	public static Integer API_VERSION = 1;
	public static String RESPONSE_ERROR = "error";
	public static String RESPONSE_OK = "success";

 
	public static String[] RESPONSE_MSG_ERROR = new String[] { "101", "Server Side Error. Call Helpdesk for details." };

	public static String[] RESPONSE_ONBORDING_REQUEST_OK = new String[] { "200", "Success" };

	public static String Buyer_to_Gateway = "Buyer_to_Gateway";
	public static String Gateway_to_Seller = "Gateway_to_Seller";
	public static String Seller_to_Gateway = "Seller_to_Gateway";
	public static String Gateway_to_Buyer = "Gateway_to_Buyer";

	public static String REQUEST_BY_BUYER = "REQUEST_BY_BUYER";
//	public static String ACK_TO_BUYER = "ACK_TO_BUYER";
	public static String NACK_TO_BUYER = "NACK_TO_BUYER";
	public static String REQUEST_TO_SELLER = "REQUEST_TO_SELLER";
	public static String ACK_BY_SELLER = "ACK_BY_SELLER";
	public static String NACK_BY_SELLER="NACK_BY_SELLER";
	public static String BLOCKED_SELLER="BLOCKED_SELLER";
	public static String INVALID_RESPONSE_BY_SELLER="INVALID_RESPONSE_BY_SELLER";
	public static String ERROR_CALLING_SELLER = "ERROR_CALLING_SELLER";
	public static String RESPONSE_BY_SELLER = "RESPONSE_BY_SELLER";
	public static String ACK_TO_SELLER = "ACK_TO_SELLER";
	public static String NACK_TO_SELLER = "NACK_TO_SELLER";

	public static String RESPONSE_TO_BUYER = "RESPONSE_TO_BUYER";
	public static String NACK_BY_BUYER = "NACK_BY_BUYER";
	public static String ACK_BY_BUYER = "ACK_BY_BUYER";

	public static String ACK_TO_BUYER = "ACK_TO_BUYER";
	public static String ERROR_CALLING_BUYER = "ERROR_CALLING_BUYER";
	public static String INVALID_RESPONSE_BY_BUYER="INVALID_RESPONSE_BY_BUYER";
	public static String BLOCKED_BUYER = "BLOCKED_BUYER";

	public static String col_Transaction_id = "Transaction_id";
	public static String col_Buyer_Id = "Buyer_id";
	public static String col_Seller_Id = "Seller_id";
	public static String col_Search_received_timestamp = "Search_received_timestampp";
	public static String col_Buyer_Status = "Buyer_status";
	public static String col_Ack_Returned_Timestamp = "Ack_Returned_Timestamp";
	public static String col_Gatway_Status = "Gatway_Status";
	public static String col_Seller_Status = "Seller_Status";
	public static String col_message_id = "message_id";
	public static String col_json = "json";
	public static String col_sender_rec_id = "sender_rec_id";
	public static String col_ack_rec_id = "ack_rec_id";
	public static String col_error = "error";
	public static String col_flag = "flag";
	
	public static String dash_type_WEEKS = "WEEKS";
	public static String dash_type_MONTHS = "MONTHS";
	public static String dash_type_DAYS = "DAYS";
	public static String dash_type_YEARS = "YEARS";
	public static String action_search = "search";
	public static String action_on_search = "on_search";

	public static String type_lookup = "lookup";
	public static String type_vlookup = "vlookup";
	public static String type_subscribe = "subscribe";
	
	public static String LOOKUP_CHANGES_DATA = "lookup_changes_data";

}
