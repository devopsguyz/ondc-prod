package com.nsdl.beckn.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Data;

@Entity
@Table(name = "api_audit")
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
public class ApiAuditEntity {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "message_id")
	private String messageId;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "action")
	private String action;

	@Column(name = "domain")
	private String domain;

	@Column(name = "core_version")
	private String coreVersion;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Type(type = "json")
	@Column(name = "json")
	private String json;

	@Column(name = "status")
	private String status;

}
