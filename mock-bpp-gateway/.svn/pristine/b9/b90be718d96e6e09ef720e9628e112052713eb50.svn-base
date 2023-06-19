package com.bpp.gateway.controller;

import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bpp.gateway.service.SearchService;
import com.bpp.gateway.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;
import com.nsdl.beckn.api.model.search.SearchRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SearchController {

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private SearchService service;

	@Autowired
	private ObjectMapper mapper;

	@PostMapping("/seller/search")
	public ResponseEntity<String> search(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException {
		log.info("The body in seller search is {}", body);

		SearchRequest model = this.jsonUtil.toModel(body, SearchRequest.class);

		this.service.send(model.getContext());

		// return new ResponseEntity<>("Success", HttpStatus.OK);
		return new ResponseEntity<>(buildAckResponse(model.getContext()), OK);
	}

	private String buildAckResponse(Context context) throws JsonProcessingException {
		Response response = new Response();
		ResponseMessage resMsg = new ResponseMessage();

		resMsg.setAck(new Ack(AckStatus.ACK));
		response.setMessage(resMsg);

		context.setKey("ack from seller");
		context.setTimestamp(LocalDateTime.now().toString());
		response.setContext(context);
		return this.mapper.writeValueAsString(response);
	}

}
