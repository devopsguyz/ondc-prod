package com.nsdl.beckn.search.controller;

import static com.nsdl.beckn.common.enums.BecknUserType.BPP;
import static com.nsdl.beckn.search.extension.GatewayConstant.API_NAME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;
import com.nsdl.beckn.common.model.AuditDataModel;
import com.nsdl.beckn.common.model.AuditFlagModel;
import com.nsdl.beckn.common.model.AuditModel;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.model.HttpModel;
import com.nsdl.beckn.common.service.ApplicationConfigService;
import com.nsdl.beckn.common.service.AuditService;
import com.nsdl.beckn.common.util.JsonUtil;
import com.nsdl.beckn.common.validator.HeaderValidator;
import com.nsdl.beckn.search.extension.OnSchema;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayOnSearchController {

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private ApplicationConfigService configService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private HeaderValidator validator;

	@Autowired
	private ObjectMapper mapper;

	@Value("${beckn.entity.type}")
	private String entityType;

	@Value("${beckn.entity.id}")
	private String entityId;

	@PostMapping("/gateway/on_search")
	public ResponseEntity<String> onSearch(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException {
		log.info("The body in gateway on_search is {}", this.jsonUtil.unpretty(body));
		Context context = null;
		try {
			OnSchema model = this.jsonUtil.toModel(body, OnSchema.class);

			context = model.getContext();
			String bppId = context.getBppId();

			ConfigModel configuration = this.configService.loadApplicationConfiguration(this.entityId, API_NAME);

			boolean authenticate = configuration.getMatchedApi().isHeaderAuthentication();
			log.info("does seller {} requires to be authenticated ? {}", bppId, authenticate);
			if (authenticate) {
				LookupRequest lookupRequest = new LookupRequest(null, context.getCountry(), context.getCity(), context.getDomain(), BPP.type());
				this.validator.validateHeader(bppId, httpHeaders, body, lookupRequest);
			}

			// do required http call and audits as configured in yml
			this.auditService.audit(buildAuditModel(httpHeaders, body, model));

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(this.mapper.writeValueAsString(e.getMessage()), BAD_REQUEST);
		}

		return new ResponseEntity<>(buildAckResponse(context), OK);
	}

	private AuditModel buildAuditModel(HttpHeaders httpHeaders, String body, OnSchema model) {
		AuditModel auditModel = new AuditModel();

		HttpModel httpModel = new HttpModel();
		httpModel.setRequestHeaders(httpHeaders);
		httpModel.setRequestBody(body);
		httpModel.setUrl(model.getContext().getBapUri() + "/on_search");

		AuditFlagModel flagModel = new AuditFlagModel();
		flagModel.setHttp(true);
		flagModel.setFile(true);
		flagModel.setDatabase(true);

		auditModel.setApiName(API_NAME);
		// auditModel.setSubscriberId(model.getContext().getBapId());
		auditModel.setSubscriberId(this.entityId);
		auditModel.setAuditFlags(flagModel);
		auditModel.setDataModel(buildAuditDataModel(body, model));
		auditModel.setHttpModel(httpModel);

		return auditModel;
	}

	private AuditDataModel buildAuditDataModel(String body, OnSchema request) {
		AuditDataModel model = new AuditDataModel();
		model.setAction(request.getContext().getAction());
		model.setCoreVersion(request.getContext().getCoreVersion());
		model.setDomain(request.getContext().getDomain());
		model.setTransactionId(request.getContext().getTransactionId());
		model.setMessageId(request.getContext().getMessageId());
		model.setCreatedOn(LocalDateTime.now());
		model.setJson(body);
		model.setStatus("N");
		return model;

	}

	private String buildAckResponse(Context context) throws JsonProcessingException {
		Response response = new Response();
		ResponseMessage resMsg = new ResponseMessage();

		resMsg.setAck(new Ack(AckStatus.ACK));
		response.setMessage(resMsg);

		context.setTimestamp(LocalDateTime.now().toString());
		response.setContext(context);
		return this.mapper.writeValueAsString(response);
	}

}
