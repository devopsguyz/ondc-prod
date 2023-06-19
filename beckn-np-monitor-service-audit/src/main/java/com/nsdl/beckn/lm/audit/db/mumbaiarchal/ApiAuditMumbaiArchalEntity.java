package com.nsdl.beckn.lm.audit.db.mumbaiarchal;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Data;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "api_audit_archvl", indexes = { @Index(name = "created_on_index", columnList = "created_on"),
		@Index(name = "transaction_id_on_index", columnList = "transaction_id"),
		@Index(name = "type_action_created_on_index", columnList = "type, action, created_on"),
		@Index(name = "api_audit_db_idx", columnList = "created_on,type"),
		@Index(name = "api_audit_db_idx1 ", columnList = "created_on,type,transaction_id") })
public class ApiAuditMumbaiArchalEntity extends ApiAuditParentEntity {
	@Id
	@Column(name = "id")
	private String id;

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

	@Type(type = "json")
	@Column(name = "json")
	private String json;

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
 }