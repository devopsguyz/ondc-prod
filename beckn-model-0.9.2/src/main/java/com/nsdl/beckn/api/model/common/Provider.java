package com.nsdl.beckn.api.model.common;

import java.util.List;

import lombok.Data;

@Data
public class Provider {
	private String id;
	private Descriptor descriptor;
	private String categoryId;
	private Time time;
	private List<Category> categories;
	private List<Fulfillment> fulfillments;
	private List<Payment> payments;
	private List<Location> locations;
	private List<Offer> offers;
	private List<Item> items;
	private String exp;
	private Tags tags;
}
