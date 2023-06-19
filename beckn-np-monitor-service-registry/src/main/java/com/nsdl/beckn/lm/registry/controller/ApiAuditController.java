package com.nsdl.beckn.lm.registry.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.beckn.lm.registry.dao.NPApiLogsRepository;
import com.nsdl.beckn.lm.registry.model.Graph;
import com.nsdl.beckn.lm.registry.model.request.LogsRequest;
import com.nsdl.beckn.lm.registry.model.request.LookupRequest;
import com.nsdl.beckn.lm.registry.model.response.LogsResponse;
import com.nsdl.beckn.lm.registry.model.response.LookupMapResponse;
import com.nsdl.beckn.lm.registry.model.response.Response;
import com.nsdl.beckn.lm.registry.service.ApiAuditService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class ApiAuditController {
	@Autowired
	ApiAuditService apiAuditService;
	
	@Autowired
	NPApiLogsRepository apiLogsRepository;
	

//	@Value("${page.size.transation}")
//	Integer pageSize;
	/**
	 * This method gets dashboard lookup details by type, start, end date.
	 * @param select It is String.
	 * @param type It is a lookup type.
	 * @param start It is local date time.
	 * @param end It is local date time.
	 * @return List of the NP Api logs based on the type, start and end local date time.
	 */
	@GetMapping("/dashboard/lookup/{select}/{type}/{start}/{end}")
	public ResponseEntity<Response<List<Graph<Integer>>>> getDashBoardLookup(@PathVariable String select,
			@PathVariable String type, @PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoardLookup(select, type, start, end, false));
	}

	/**
	 * This method gets subscriber details by subscriber id.
	 * @param body It is a subscriber id.
	 * @return List of the NP APi logs based on the subscriber id.
	 */
	@PostMapping("/dashboard/logs")
	public ResponseEntity<Response<List<LogsResponse>>> getLogs(@RequestBody LogsRequest body) {
		return Response.ok(this.apiAuditService.findLogsBySubscriberId(body));
	}
	
	/**
	 * This method finds list of the lookup logs by type, strat end end local date time.
	 * @param body It is a subscriber id.
	 * @param select It is a lookup type.
	 * @param start It is local date time.
	 * @param end It is a local date time.
	 * @param page It is pagination.
	 * @return List of the Np Api logs based on the lookup type start and end local date time.
	 */
	@PostMapping("/dashboard/lookup/{select}/{start}/{end}/{page}")
	public ResponseEntity<Response<LookupMapResponse>> findLookupByType(@RequestBody LookupRequest body, @PathVariable String select, @PathVariable String start,@PathVariable String end,@PathVariable Integer page ){
		return Response.ok( this.apiAuditService.findLookupByType(body.getType(),select,start,end, page));
	}
	
	/**
	 * This method gets lookup api logs details by lookup type.
	 * @return List of the Np Api logs based on the type.
	 */
	@GetMapping("/lookup/type")
	public ResponseEntity<Response<List<String>>>getlookuptype( ){
		return Response.ok(this.apiAuditService.findByType());
	}
	/**
	 * This method gets dashboard performance by date.
	 * @param date It is local date time.
	 * @return List of Np api logs based on the date.
	 */
	@GetMapping(value = "/dashboard/performance/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<List<Map<String, Object>>>> getPerformance(@PathVariable String date) {
		return Response.ok(this.apiAuditService.getPerformance(date));
	}
	
}
