package com.nsdl.beckn.lm.registry.service;

import java.util.List;
import java.util.Map;

import com.nsdl.beckn.lm.registry.model.Graph;
import com.nsdl.beckn.lm.registry.model.request.LogsRequest;
import com.nsdl.beckn.lm.registry.model.response.LogsResponse;
import com.nsdl.beckn.lm.registry.model.response.LookupMapResponse;

public interface ApiAuditService {

	 
 	
	//LookupResponse findLookupByType (String type, String end ,Integer page );

	/**
	 * Finds list of the NP API logs by type.
	 * @return List of the NP API logs based on the type.
	 */
	List<String> findByType();

	 
	// List<Map<String, Object>> findByBuyerSellerTransactionByDate(String type,
	// String start, String end);
	/**
	 * Gets dashboard lookup from NP API logs by type, start and end local date time.
	 * @param select It is a String.
	 * @param type It is a lookup type.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the NP API Logs based on the type, start and end local date time.
	 */
	List<Graph<Integer>> getDashBoardLookup(String select, String type, String start, String end, boolean flag);

	 
	/**
	 * Finds list of the logs by subscriber id.
	 * @param body It is a subscriber id.
	 * @return List of the logs based on the subscriber id.
	 */
	List<LogsResponse> findLogsBySubscriberId(LogsRequest body);
	 
	 
 	 
	/**
	 * Finds lookup details by type, start and end local date time.
	 * @param type It is a lookup type.
	 * @param select It is a String.
	 * @param start It is local date time.
	 * @param end It is a local date time.
	 * @param page It is a pagination.
	 * @return List of the lookup based on the type,start and end local date time.
	 */
	LookupMapResponse findLookupByType(String type,String select, String start, String end, Integer page);
	
	/**
	 * Gets performance list by date.
	 * @param date It is local date time.
	 * @return List of the performance based on the date.
	 */
	public List<Map<String,Object>> getPerformance(String date);
}
