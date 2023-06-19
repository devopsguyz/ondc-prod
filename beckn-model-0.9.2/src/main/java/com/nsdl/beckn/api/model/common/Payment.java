package com.nsdl.beckn.api.model.common;

import lombok.Data;

@Data
public class Payment {
	private String uri;
	private String tlMethod;
	private PaymentParams params;
	private String type;
	private String status;
	private Time time;
}
