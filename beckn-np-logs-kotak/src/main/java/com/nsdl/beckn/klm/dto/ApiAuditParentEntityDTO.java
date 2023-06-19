package com.nsdl.beckn.klm.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiAuditParentEntityDTO {
	
	public String id;
	
	private String remoteHost;

	private String messageId;

	private String transactionId;

	private String buyerId;

	private String sellerId;

	private String action;

	private String domain;

	private String coreVersion;

	// @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="EEE MMM dd HH:mm:ss Z
	// yyyy")
	private LocalDateTime createdOn;

	private String headers;

	private String status;

	private String type;

	private String error;

	private String timeTaken;

	private String hostId;

	private String json;
}
