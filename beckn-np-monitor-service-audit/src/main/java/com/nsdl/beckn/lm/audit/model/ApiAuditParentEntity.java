package com.nsdl.beckn.lm.audit.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import lombok.Data;

@Data
 
public class ApiAuditParentEntity {

	
	private String id;

	private String remoteHost;

	private String messageId;

	private String transactionId;

	private String buyerId;

	private String sellerId;

	private String action;

	private String domain;

	private String coreVersion;

	private LocalDateTime createdOn;

	private String json;

	private String headers;

	private String status;

	private String type;

	private String error;

	private String timeTaken;

	private String hostId;
}