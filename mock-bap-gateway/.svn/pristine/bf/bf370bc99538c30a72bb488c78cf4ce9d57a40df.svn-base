package com.bap.gateway.controller;

import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bap.gateway.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.onsearch.OnSearchRequest;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OnSearchController {

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private ObjectMapper mapper;

	@PostMapping("/buyer/on_search")
	public ResponseEntity<String> search(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException {
		log.info("The body in buyer on_search is {}", body);

		OnSearchRequest model = this.jsonUtil.toModel(body, OnSearchRequest.class);

		Context context = model.getContext();
		// return new ResponseEntity<>("Success", HttpStatus.OK);
		return new ResponseEntity<>(buildAckResponse(context), OK);
	}

	private String buildAckResponse(Context context) throws JsonProcessingException {
		Response response = new Response();
		ResponseMessage resMsg = new ResponseMessage();

		resMsg.setAck(new Ack(AckStatus.ACK));
		response.setMessage(resMsg);

		context.setKey("ack from buyer");
		context.setTimestamp(LocalDateTime.now().toString());
		response.setContext(context);
		return this.mapper.writeValueAsString(response);
	}

}
