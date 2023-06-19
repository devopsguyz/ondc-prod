package com.nsdl.beckn.lm.registry.model.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class LookupMapResponse {
	
	Integer pageSize;
	Map<String, List<LookupResponse>> map;
	Long records;
	
}
