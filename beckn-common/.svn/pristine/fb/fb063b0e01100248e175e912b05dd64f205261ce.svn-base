package com.nsdl.beckn.common.service;

import static com.nsdl.beckn.common.constant.ApplicationConstant.BECKN_API_LOOKUP_CACHE;
import static com.nsdl.beckn.common.enums.BecknUserType.BPP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.api.model.lookup.LookupResponse;
import com.nsdl.beckn.common.builder.HeaderBuilder;
import com.nsdl.beckn.common.cache.CachingService;
import com.nsdl.beckn.common.model.ApiParamModel;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.sender.Sender;
import com.nsdl.beckn.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LookupServiceGateway {

	@Autowired
	private Sender sender;

	@Autowired
	private ApplicationConfigService appConfigService;

	@Autowired
	private HeaderBuilder authHeaderBuilder;

	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	private CachingService cachingService;

	@Autowired
	@Value("classpath:mock_lookup1.json")
	private Resource resource;

	@Value("${beckn.entity.type}")
	private String entityType;

	@Value("${beckn.entity.id}")
	private String entityId;

	@Value("${beckn.lookup.mock}")
	private boolean mockLookup;

	private static final String ACTIVE_SELLERS = "ACTIVE_SELLERS";
	private static final String ACTIVE_GATEWAYS = "ACTIVE_GATEWAYS";
	private static final String API_NAME = "lookup";
	private static final String DATE_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public LookupResponse getGatewayProviders(Context context, LookupRequest lookupRequest) {
		log.info("finding matching gateway for subscriberId: {}", this.entityId);

		List<LookupResponse> lookupList = (List<LookupResponse>) this.cachingService.getFromCache(BECKN_API_LOOKUP_CACHE, ACTIVE_GATEWAYS);

		if (lookupList == null || lookupList.size() == 0) {
			lookupList = lookup(this.entityId, lookupRequest);
			log.info("lookup response list is {}", lookupList);

			putInCache(lookupList);
		} else {
			log.info("active gateways found in cache. no lookup call will be done");
		}

		String match = context.getDomain() + "|" + context.getCity();
		LookupResponse lookupResponse = lookupList.stream()
				.filter(response -> response.getSubscriberId().equalsIgnoreCase(this.entityId))
				.filter(response -> (response.getDomain() + "|" + response.getCity()).equalsIgnoreCase(match))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Gateway not found"));

		if (isCacheValid(lookupResponse)) {
			log.info("The matching gateway for subscriber {} is {}", this.entityId, lookupResponse);
			return lookupResponse;
		}
		log.error("The matching gateway for subscriber {} found but it is expired", this.entityId);

		return lookupResponse;
	}

	public List<String> getActiveSellers(String subscriberId, LookupRequest lookupRequest) {

		List<String> fromCache = (List<String>) this.cachingService.getFromCache(BECKN_API_LOOKUP_CACHE, ACTIVE_SELLERS);
		if (fromCache != null && fromCache.size() > 0) {
			log.info("active seller list found in cache");
			return fromCache;
		}

		log.info("active seller list not found in cache. going to make lookup call");
		List<LookupResponse> list = lookup(subscriberId, lookupRequest);

		List<String> bppUrlList = list.stream()
				.filter(model -> BPP.type().equalsIgnoreCase(model.getType()))
				.map(LookupResponse::getSubscriberUrl)
				.collect(Collectors.toList());

		this.cachingService.putToCache(BECKN_API_LOOKUP_CACHE, ACTIVE_SELLERS, bppUrlList);

		return bppUrlList;
	}

	private List<LookupResponse> lookup(String entityId, LookupRequest request) {
		List<LookupResponse> lookupList = new ArrayList<>();

		String response = null;
		if (this.mockLookup) {
			response = getMockLookupJson();
		} else {
			response = lookupJson(entityId, request);
		}

		if (response != null) {
			lookupList = this.jsonUtil.toModelList(response, LookupResponse.class);
		} else {
			log.error("lookup response json is null");
		}
		return lookupList;
	}

	private String lookupJson(String subscriberId, LookupRequest request) {
		ConfigModel configModel = this.appConfigService.loadApplicationConfiguration(subscriberId, API_NAME);
		ApiParamModel matchedApiModel = configModel.getMatchedApi();

		String url = matchedApiModel.getHttpEntityEndpoint();
		String json = this.jsonUtil.toJson(request);

		log.info("final json to be send {}", json);

		HttpHeaders headers = new HttpHeaders();
		if (matchedApiModel.isSetAuthorizationHeader()) {
			headers = this.authHeaderBuilder.buildHeaders(json, configModel);
		}

		log.info("calling the lookup to url: {}", url);
		String response = this.sender.send(url, headers, json, configModel.getMatchedApi());
		log.info("lookup response json is {}", response);

		return response;
	}

	private void putInCache(List<LookupResponse> list) {
		this.cachingService.putToCache(BECKN_API_LOOKUP_CACHE, ACTIVE_GATEWAYS, list);
	}

	private boolean isCacheValid(LookupResponse fromCache) {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TZ);
		Date currentDate = new Date();

		Date validFrom = null;
		Date validUntil = null;
		try {
			validFrom = sdf.parse(fromCache.getValidFrom());
			validUntil = sdf.parse(fromCache.getValidUntil());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		log.info("ValidFrom: {} & ValidUntil: {}", validFrom, validUntil);

		boolean isValidFrom = validFrom.before(currentDate);
		boolean isValidUntil = validUntil.after(currentDate);

		log.info("isValidFrom: {} & isValidUntil: {}", isValidFrom, isValidUntil);

		return isValidFrom && isValidUntil;
	}

	private String getMockLookupJson() {
		InputStream inputStream = null;
		try {
			inputStream = this.resource.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
		log.info("mock lookup json is {}", json);
		return json;
	}

}
