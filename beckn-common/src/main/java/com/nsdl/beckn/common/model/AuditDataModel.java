package com.nsdl.beckn.common.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuditDataModel {
	private String messageId;
	private String transactionId;
	private String action;
	private String domain;
	private String coreVersion;
	private LocalDateTime createdOn;
	private String json;
	private String status;
}
