package com.nsdl.beckn.klm.service;

import java.util.List;
import java.util.Map;

public interface GatwayService {
    /**
     * Finds transaction details by transaction id.
     * @param id unique id.
     * @return List of map object based on the transaction id.
     */
	public Map<String, List<Map<String, Object>>> findTransactionById(String id) ;
}
