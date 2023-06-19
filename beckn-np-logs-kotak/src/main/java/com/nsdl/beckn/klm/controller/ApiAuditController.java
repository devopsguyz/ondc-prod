package com.nsdl.beckn.klm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.beckn.klm.dto.TransactionDTO;
import com.nsdl.beckn.klm.model.response.Response;
import com.nsdl.beckn.klm.service.ApiAuditService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/audit")
public class ApiAuditController {
	@Autowired
	ApiAuditService apiAuditService;
	 
    /**
     * This method gets transaction details from apiAuditEntity by action,server and transaction id.
     * @param action It is a String, 
     * @param id It is unique id.
     * @param server It is String.
     * @return Its returns list of the api audit entity based on action, id, server.
     */
	@GetMapping("/get/transaction/{action}/{id}/{server}")
	public ResponseEntity<Response<Map<String, List<Map<String, Object>>>>> findByActionAndTransactionId(
			@PathVariable String action, @PathVariable String id, @PathVariable String server) {
		return Response.ok(this.apiAuditService.findByActionAndTransactionId(action, id, server));
	}
 

}
