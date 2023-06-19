package com.nsdl.beckn.api.model.common;

import lombok.Data;

@Data
public class Fulfillment {
	private String id;
	private String type;
	private String providerId;
	private State state;
	private boolean tracking;
	private Object customer;
	private Agent agent;
	private Vehicle vehicle;
	private Start start;
	private End end;
	private Tags tags;
}
