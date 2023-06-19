package com.nsdl.beckn.common.model;

import org.springframework.http.HttpHeaders;

import lombok.Data;

@Data
public class HttpModel {
	private HttpHeaders requestHeaders;
	private String requestBody;
	private String url;
}