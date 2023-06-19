package com.nsdl.beckn.common.builder;

import static com.nsdl.beckn.common.constant.ApplicationConstant.SIGN_ALGORITHM;

import java.util.Base64;
import java.util.Collections;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.api.model.lookup.LookupResponse;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.service.LookupServiceGateway;
import com.nsdl.beckn.common.util.SigningUtility;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HeaderBuilder {

	@Autowired
	private SigningUtility signingUtility;

	@Autowired
	private LookupServiceGateway lookupServiceGateway;

	public HttpHeaders buildHeaders(String requestBody, ConfigModel configModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		if (configModel.getMatchedApi().isSetAuthorizationHeader()) {
			String authHeader = buildAuthorizationHeader(requestBody, configModel, headers);
			headers.add(HttpHeaders.AUTHORIZATION, authHeader);
			log.info("Authorization header added to HttpHeaders");
		} else {
			log.info("Authorization header will not be set as it is switched off in the config file");
		}

		return headers;
	}

	public HttpHeaders buildGatewayHeaders(Context context, HttpHeaders requestHeaders, String requestBody, ConfigModel configModel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		LookupRequest lookupRequest = new LookupRequest(null, null, null, null, null);
		LookupResponse lookupResponse = this.lookupServiceGateway.getGatewayProviders(context, lookupRequest);
		String uniqueKeyId = lookupResponse.getUniqueKeyId();
		log.info("UniqueKeyId for this gateway is {}", uniqueKeyId);
		configModel.setKeyid(uniqueKeyId);

		if (configModel.getMatchedApi().isSetAuthorizationHeader()) {
			String authHeader = buildAuthorizationHeader(requestBody, configModel, headers);

			headers.set(HttpHeaders.AUTHORIZATION, requestHeaders.getFirst(HttpHeaders.AUTHORIZATION));
			headers.set(HttpHeaders.PROXY_AUTHORIZATION, authHeader);
			headers.set("X-Gateway-Authorization", authHeader);

			log.info("Authorization, Proxy-Authorization & X-Gateway-Authorization header added to HttpHeaders");
		} else {
			log.info("Authorization, Proxy-Authorization & X-Gateway-Authorization header will not be set as it is switched off in the config file");
		}

		return headers;
	}

	private String buildAuthorizationHeader(String requestBody, ConfigModel configModel, HttpHeaders headers) {
		long currentTime = System.currentTimeMillis() / 1000L;
		int headerValidity = configModel.getMatchedApi().getHeaderValidity();

		String blakeHash = generateBlakeHash(requestBody);
		String signingString = "(created): " + currentTime + "\n(expires): " + (currentTime + headerValidity)
				+ "\ndigest: BLAKE-512=" + blakeHash + "";

		String signature = this.signingUtility.generateSignature(signingString, configModel.getSigning());
		String kid = configModel.getKeyid() + "|" + SIGN_ALGORITHM;

		String authHeader = "Signature keyId=\"" + kid + "\",algorithm=\"" + SIGN_ALGORITHM + "\", created=\"" + currentTime
				+ "\", expires=\"" + (currentTime + headerValidity)
				+ "\", headers=\"(created) (expires) digest\", signature=\"" + signature + "\"";

		return authHeader;
	}

	private String generateBlakeHash(String request) {
		Blake2bDigest digest = new Blake2bDigest(512);

		byte[] test = request.getBytes();
		digest.update(test, 0, test.length);

		byte[] hash = new byte[digest.getDigestSize()];
		digest.doFinal(hash, 0);

		String hex = Hex.toHexString(hash);

		return Base64.getUrlEncoder().encodeToString(hex.getBytes());
	}

}
