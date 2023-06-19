package com.nsdl.beckn.klm.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import lombok.Data;


@Data
@MappedSuperclass
public class ApiAuditParentEntity {


	@Column(name = "remote_host")
	private String remoteHost;

	@Column(name = "message_id")
	private String messageId;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "buyer_id")
	private String buyerId;

	@Column(name = "seller_id")
	private String sellerId;

	@Column(name = "action")
	private String action;

	@Column(name = "domain")
	private String domain;

	@Column(name = "core_version")
	private String coreVersion;

	@Column(name = "created_on")
	// @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="EEE MMM dd HH:mm:ss Z
	// yyyy")
	private LocalDateTime createdOn;

	@Column(name = "headers")
	private String headers;

	@Column(name = "status")
	private String status;

	@Column(name = "type")
	private String type;

	@Column(name = "error")
	private String error;

	@Column(name = "time_taken")
	private String timeTaken;

	@Column(name = "host_id")
	private String hostId;

	@Type(type = "json")
	@Column(name = "json")
	private String json;
}