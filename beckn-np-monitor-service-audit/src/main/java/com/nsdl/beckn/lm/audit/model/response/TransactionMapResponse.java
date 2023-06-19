package com.nsdl.beckn.lm.audit.model.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TransactionMapResponse {

	Integer pageSize;
	Map<String, List<Map<String, Object>>> map;
	Integer records;
}
