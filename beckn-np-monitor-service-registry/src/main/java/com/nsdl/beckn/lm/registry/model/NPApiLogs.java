package com.nsdl.beckn.lm.registry.model;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "np_api_logs", indexes = { @Index(name = "created_on", columnList = "created_on"),
		@Index(name = "typ_index", columnList = "typ"), })
public class NPApiLogs extends CommonModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	UUID id;

	@Type(type = "jsonb")
	@Column(name = "jsn_rqst", columnDefinition = "jsonb")
	Map<String, Object> jsonRequest;

	@Type(type = "jsonb")
	@Column(name = "jsn_rspns", columnDefinition = "jsonb")
	Map<String, Object> jsonResponse;

	@Column(name = "typ")
	String type;

	@Column(name = "rspns_tm")
	Integer responseTime;

	@Column(name = "subscriber_id")
	String subscriberId;
	
	@CreationTimestamp
	@Column(name = "crt_tm_stmp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime createdTm;

}
