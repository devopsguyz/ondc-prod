package com.nsdl.beckn.np.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequestSearchParam {
	@JsonProperty("city")
	@SerializedName("city")
	private String city = null;

	@JsonProperty("country")
	@SerializedName("country")
	private String country = null;

	@JsonProperty("domain")
	@SerializedName("domain")
	private String domain = null;

	@JsonProperty("type")
	@SerializedName("type")
	private String type = null;

	@JsonProperty("subscriber_id")
	@SerializedName("subscriber_id")
	String subscriberId;

	@JsonIgnore()
	String data;
	@JsonProperty("sor_required")
	@SerializedName("sor_required")
	Boolean sorRequired=true;

}
