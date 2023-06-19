package com.nsdl.beckn.search.service;

import static com.nsdl.beckn.search.extension.GatewayConstant.API_NAME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;
import com.nsdl.beckn.common.model.ApiParamModel;
import com.nsdl.beckn.common.sender.Sender;
import com.nsdl.beckn.common.service.LookupService;
import com.nsdl.beckn.common.util.JsonUtil;
import com.nsdl.beckn.common.validator.BodyValidator;
import com.nsdl.beckn.search.extension.Schema;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GatewayService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BodyValidator bodyValidator;

	@Autowired
	private LookupService lookupService;

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private Sender sendRequest;

	@Value("${beckn.entity.id}")
	private String entityId;

	public ResponseEntity<String> search(ApiParamModel matchedApi, HttpHeaders httpHeaders, Schema request) throws JsonProcessingException {
		log.info("Going to validate json request before sending to multiple sellers...");

		Response errorResponse = this.bodyValidator.validateRequestBody(request.getContext(), API_NAME);

		if (errorResponse != null) {
			return new ResponseEntity<>(this.mapper.writeValueAsString(errorResponse), BAD_REQUEST);
		}

		Response response = new Response();
		ResponseMessage resMsg = new ResponseMessage();

		resMsg.setAck(new Ack(AckStatus.ACK));
		response.setMessage(resMsg);

		Context ctx = request.getContext();
		response.setContext(ctx);

		// find active sellers and send request
		LookupRequest sellerLookupRequest = new LookupRequest(null, null, null, null, null);
		List<String> sellerUrlList = this.lookupService.getActiveSellers(this.entityId, sellerLookupRequest);

		for (String url : sellerUrlList) {
			CompletableFuture.runAsync(() -> {
				String sellerUrl = url + "/search";
				sendRequestToSeller(httpHeaders, request, sellerUrl, matchedApi);
			});
		}

		return new ResponseEntity<>(this.mapper.writeValueAsString(response), OK);
	}

	private void sendRequestToSeller(HttpHeaders httpHeaders, Schema request, String url, ApiParamModel matchedApi) {
		log.info("sending request[in seperate thread] to seller {}", url);

		try {
			String json = this.jsonUtil.toJson(request);
			this.sendRequest.send(url, httpHeaders, json, matchedApi);
		} catch (Exception e) {
			log.error("error while sending post request to seller ", e);
			e.printStackTrace();
		}

	}

}
