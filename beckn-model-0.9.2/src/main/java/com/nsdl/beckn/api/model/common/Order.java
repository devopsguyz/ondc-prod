package com.nsdl.beckn.api.model.common;

import java.util.List;

import lombok.Data;

@Data
public class Order {
	private String id;
	private String state;
	private List<Item> items;
	private List<AddOn> addOns;
	private List<Offer> offers;
	private Billing billing;
	private Fulfillment fulfillment;
	private Quotation quote;
	private Payment payment;
	private String createdAt;
	private String updatedAt;
}
