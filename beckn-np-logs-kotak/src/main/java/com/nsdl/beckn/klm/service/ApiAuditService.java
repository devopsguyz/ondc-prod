package com.nsdl.beckn.klm.service;

import java.util.List;
import java.util.Map;

import com.nsdl.beckn.klm.dto.TransactionDTO;

public interface ApiAuditService {

	//Map<String, List<Map<String, Object>>> findTransactionById(String action);

    /**
     * Finds transaction details by action transaction id and server.
     * @param action It is a String.
     * @param id unique id.
     * @param server It is a String.
     * @return List of map object based on the transaction id, action and server.
     */
	Map<String, List<Map<String, Object>>> findByActionAndTransactionId(String action, String id, String server);
	 
}
