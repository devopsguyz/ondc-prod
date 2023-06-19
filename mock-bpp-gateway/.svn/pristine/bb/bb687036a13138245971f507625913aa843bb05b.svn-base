package com.bpp.gateway.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.bpp.gateway.sender.Sender;
import com.bpp.gateway.util.JsonUtil;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.onsearch.OnSearchRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchService {

	@Autowired
	@Value("classpath:on_search.json")
	private Resource resource;

	@Autowired
	private Sender sender;

	@Autowired
	private JsonUtil jsonUtil;

	@Value("${seller.id}")
	private String sellerId;

	@Value("${gateway.url}")
	private String gatewayUrl;

	@Value("${server.port}")
	private String port;

	public void send(Context context) {

		try {
			final String hostname = InetAddress.getLocalHost().getHostName();

			InputStream inputStream = this.resource.getInputStream();
			String json = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

			// log.info("on_search response to be send back to adaptor {}", json);
			log.info("on_search response ready to be send back to gateway on url {}", this.gatewayUrl);

			OnSearchRequest model = this.jsonUtil.toModel(json, OnSearchRequest.class);
			model.getContext().setBapId(context.getBapId());
			model.getContext().setBapUri(context.getBapUri());
			model.getContext().setBppId(this.sellerId);
			model.getContext().setBppUri(hostname + ":" + this.port);
			model.getContext().setTransactionId(context.getTransactionId());
			model.getContext().setMessageId(context.getMessageId());
			model.getContext().setTimestamp(LocalDateTime.now().toString());

			String updatedJson = this.jsonUtil.toJson(model);

			this.sender.send(this.gatewayUrl, new HttpHeaders(), updatedJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
