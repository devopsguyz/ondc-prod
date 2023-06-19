package com.nsdl.beckn.np.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequestSearch {

	@JsonProperty("sender_subscriber_id")
	@SerializedName("sender_subscriber_id")
	String sender_subscriber_id;

	@JsonProperty("request_id")
	@SerializedName("request_id")
	String requestId;

	@JsonProperty("timestamp")
	@SerializedName("timestamp")
	String timestamp;

	@JsonProperty("search_parameters")
	@SerializedName("search_parameters")
	RequestSearchParam searchParameters;

	@JsonProperty("signature")
	@SerializedName("signature")
	String signature;

	@JsonIgnore()
	String statusCode;

	@JsonIgnore()
	String msg;
 
}
