package com.nsdl.beckn.search.controller;

import static com.nsdl.beckn.common.enums.BecknUserType.BAP;
import static com.nsdl.beckn.common.enums.OndcUserType.GATEWAY;
import static com.nsdl.beckn.search.extension.GatewayConstant.API_NAME;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.common.builder.HeaderBuilder;
import com.nsdl.beckn.common.exception.ApplicationException;
import com.nsdl.beckn.common.exception.ErrorCode;
import com.nsdl.beckn.common.model.ApiParamModel;
import com.nsdl.beckn.common.model.AuditDataModel;
import com.nsdl.beckn.common.model.AuditFlagModel;
import com.nsdl.beckn.common.model.AuditModel;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.model.HttpModel;
import com.nsdl.beckn.common.service.ApplicationConfigService;
import com.nsdl.beckn.common.service.AuditService;
import com.nsdl.beckn.common.util.JsonUtil;
import com.nsdl.beckn.common.validator.HeaderValidator;
import com.nsdl.beckn.search.extension.Schema;
import com.nsdl.beckn.search.service.GatewayService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewaySearchController {

	@Autowired
	private GatewayService service;

	@Autowired
	private HeaderValidator validator;

	@Autowired
	private HeaderBuilder authHeaderBuilder;

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private ApplicationConfigService configService;

	@Autowired
	private AuditService auditService;

	@Value("${beckn.entity.type}")
	private String entityType;

	@Value("${beckn.entity.id}")
	private String entityId;

	@PostMapping("/gateway/search")
	public ResponseEntity<String> search(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body, HttpServletRequest servletRequest)
			throws JsonProcessingException {

		log.info("The body in gateway {} is {}", API_NAME, this.jsonUtil.unpretty(body));

		log.info("Entity type is {}", this.entityType);
		if (!GATEWAY.type().equalsIgnoreCase(this.entityType)) {
			throw new ApplicationException(ErrorCode.INVALID_ENTITY_TYPE);
		}

		Schema model = this.jsonUtil.toModel(body, Schema.class);
		Context context = model.getContext();
		String bapId = context.getBapId();

		ConfigModel gatewayConfigModel = this.configService.loadApplicationConfiguration(this.entityId, API_NAME);
		ApiParamModel matchedApi = gatewayConfigModel.getMatchedApi();

		boolean authenticate = matchedApi.isHeaderAuthentication();

		log.info("does buyer {} requires to be authenticated ? {}", bapId, authenticate);
		if (authenticate) {
			LookupRequest lookupRequest = new LookupRequest(null, context.getCountry(), context.getCity(), context.getDomain(), BAP.type());
			this.validator.validateHeader(bapId, httpHeaders, body, lookupRequest);
		}

		// do audit if configured in yml
		this.auditService.audit(buildAuditModel(httpHeaders, body, model));

		// set auth and proxy-auth header
		HttpHeaders enrichedHeaders = this.authHeaderBuilder.buildGatewayHeaders(context, httpHeaders, body, gatewayConfigModel);

		return this.service.search(matchedApi, enrichedHeaders, model);
	}

	private AuditModel buildAuditModel(HttpHeaders httpHeaders, String body, Schema model) {
		AuditModel auditModel = new AuditModel();

		HttpModel httpModel = new HttpModel();
		httpModel.setRequestHeaders(httpHeaders);
		httpModel.setRequestBody(body);

		AuditFlagModel flagModel = new AuditFlagModel();
		flagModel.setHttp(false);
		flagModel.setFile(true);
		flagModel.setDatabase(true);

		auditModel.setApiName(API_NAME);
		auditModel.setSubscriberId(model.getContext().getBppId());
		auditModel.setAuditFlags(flagModel);
		auditModel.setDataModel(buildAuditDataModel(body, model));
		auditModel.setHttpModel(httpModel);

		return auditModel;
	}

	private AuditDataModel buildAuditDataModel(String body, Schema request) {
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

}