package com.nsdl.beckn.common.validator;

import static com.nsdl.beckn.common.constant.ApplicationConstant.SIGN_ALGORITHM;
import static com.nsdl.beckn.common.enums.OndcUserType.GATEWAY;
import static com.nsdl.beckn.common.exception.ErrorCode.AUTH_HEADER_NOT_FOUND;
import static com.nsdl.beckn.common.exception.ErrorCode.SIGNATURE_VERIFICATION_FAILED;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.nsdl.beckn.api.model.lookup.LookupRequest;
import com.nsdl.beckn.api.model.lookup.LookupResponse;
import com.nsdl.beckn.common.dto.ApplicationDto;
import com.nsdl.beckn.common.dto.KeyIdDto;
import com.nsdl.beckn.common.exception.ApplicationException;
import com.nsdl.beckn.common.exception.ErrorCode;
import com.nsdl.beckn.common.model.HeaderParams;
import com.nsdl.beckn.common.model.SigningModel;
import com.nsdl.beckn.common.service.ApplicationConfigService;
import com.nsdl.beckn.common.service.LookupService;
import com.nsdl.beckn.common.util.SigningUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HeaderValidator {

	@Autowired
	private SigningUtility signingUtility;

	@Autowired
	private LookupService lookupService;

	@Autowired
	private ApplicationConfigService configService;

	@Value("${beckn.entity.type}")
	private String entityType;

	@Value("${beckn.entity.id}")
	private String entityId;

	public ApplicationDto validateHeader(String subscriberId, HttpHeaders httpHeaders, String requestBody, LookupRequest lookupRequest) {
		ApplicationDto selectDto = new ApplicationDto();
		String authHeader = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isBlank(authHeader)) {
			authHeader = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION.toLowerCase());
		}
		if (StringUtils.isBlank(authHeader)) {
			throw new ApplicationException(AUTH_HEADER_NOT_FOUND);
		}

		log.info("Going to validate authHeader header {}", authHeader);

		KeyIdDto keyIdDto = validate(subscriberId, requestBody, lookupRequest, authHeader, true);
		selectDto.setKeyIdDto(keyIdDto);

		// code for proxy header
		String proxyAuthHeader = httpHeaders.getFirst(HttpHeaders.PROXY_AUTHORIZATION);
		if (StringUtils.isBlank(authHeader)) {
			proxyAuthHeader = httpHeaders.getFirst(HttpHeaders.PROXY_AUTHORIZATION.toLowerCase());
		}
		if (StringUtils.isNotBlank(proxyAuthHeader)) {
			log.info("Going to validate proxy header {}", proxyAuthHeader);
			validate(subscriberId, requestBody, lookupRequest, proxyAuthHeader, false);
		}

		return selectDto;
	}

	private KeyIdDto validate(String subscriberId, String requestBody, LookupRequest lookupRequest, String authHeader, boolean isAuthHeader) {
		Map<String, String> headersMap = this.signingUtility.parseAuthorizationHeader(authHeader);

		if (MapUtils.isEmpty(headersMap)) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.INVALID_AUTH_HEADER : ErrorCode.INVALID_PROXY_AUTH_HEADER;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		KeyIdDto keyIdDto = this.signingUtility.splitKeyId(headersMap.get("keyId"));
		String key = keyIdDto.getKeyId() + "|" + keyIdDto.getUniqueKeyId();
		log.info("BAP key is {}", key);
		LookupResponse lookupResponse = this.lookupService.getProvidersByKeyId(subscriberId, key, lookupRequest);

		boolean allMatch = Stream.of(keyIdDto.getKeyId(), keyIdDto.getUniqueKeyId(), keyIdDto.getAlgo()).anyMatch(Objects::isNull);

		if (allMatch) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.INVALID_KEY_ID_HEADER : ErrorCode.INVALID_PROXY_KEY_ID_HEADER;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		// #Algo Validation
		String algoParam = headersMap.get("algorithm");
		if (!SIGN_ALGORITHM.equals(keyIdDto.getAlgo()) || algoParam == null || !SIGN_ALGORITHM.equals(algoParam.replace("\"", ""))) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.ALGORITHM_MISMATCH : ErrorCode.PROXY_ALGORITHM_MISMATCH;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		// #Headers param validation
		HeaderParams headerParams = this.signingUtility.splitHeadersParam(headersMap.get("headers"));
		if (headerParams == null) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.INVALID_HEADERS_PARAM : ErrorCode.INVALID_PROXY_HEADERS_PARAM;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		// #Timestamp Expiry check
		if (!this.signingUtility.validateTime(headersMap.get("created"), headersMap.get("expires"))) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.REQUEST_EXPIRED : ErrorCode.PROXY_REQUEST_EXPIRED;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		log.info("checking lookup for the key {}", key);
		if (lookupResponse == null || lookupResponse.getSigningPublicKey() == null) {
			ErrorCode errorCode = isAuthHeader ? ErrorCode.AUTH_FAILED : ErrorCode.PROXY_AUTH_FAILED;
			log.error(errorCode.getMessage());
			throw new ApplicationException(errorCode);
		}

		SigningModel signingModel = null;
		if (GATEWAY.type().equalsIgnoreCase(this.entityType)) {
			signingModel = this.configService.getSigningConfiguration(this.entityId);
		} else {
			signingModel = this.configService.getSigningConfiguration(subscriberId);
		}

		if (signingModel.isCertificateUsed()) {
			verifySignatureUsingCertificate(headersMap, lookupResponse, requestBody);
		} else {
			verifySignatureUsingPublicKey(headersMap, lookupResponse, requestBody);
		}
		return keyIdDto;
	}

	private void verifySignatureUsingCertificate(Map<String, String> headersMap, LookupResponse lookupResponse, String requestBody) {
		String signed = recreateSignedString(headersMap, requestBody);
		if (!this.signingUtility.verifyWithP12PublicKey(headersMap.get("signature").replace("\"", ""), signed, lookupResponse.getSigningPublicKey())) {
			log.error(SIGNATURE_VERIFICATION_FAILED.toString());
			throw new ApplicationException(SIGNATURE_VERIFICATION_FAILED);
		}
	}

	private void verifySignatureUsingPublicKey(Map<String, String> headersMap, LookupResponse lookupResponse, String requestBody) {
		String signed = recreateSignedString(headersMap, requestBody);
		if (!this.signingUtility.verifySignature(headersMap.get("signature").replace("\"", ""), signed, lookupResponse.getSigningPublicKey())) {
			log.error(SIGNATURE_VERIFICATION_FAILED.toString());
			throw new ApplicationException(SIGNATURE_VERIFICATION_FAILED);
		}
	}

	private String recreateSignedString(Map<String, String> headersMap, String requestBody) {
		String reqBleckHash = this.signingUtility.generateBlakeHash(requestBody);

		StringBuilder sb = new StringBuilder();
		sb.append("(created): ");
		sb.append(headersMap.get("created").replace("\"", ""));
		sb.append("\n");
		sb.append("(expires): ");
		sb.append(headersMap.get("expires").replace("\"", ""));
		sb.append("\n");
		sb.append("digest: ");
		sb.append("BLAKE-512=" + reqBleckHash);

		return sb.toString();
	}

}
