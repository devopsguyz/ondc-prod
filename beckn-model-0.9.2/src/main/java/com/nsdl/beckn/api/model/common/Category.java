package com.nsdl.beckn.api.model.common;

import java.util.List;

import lombok.Data;

@Data
public class Category {
	private String id;
	private String parentCategoryId;
	private Descriptor descriptor;
	private Time time;
	private List<Tags> tags;
}
