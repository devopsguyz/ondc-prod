package com.nsdl.beckn.np.model;

import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.http.ResponseEntity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "np_api_logs")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

	@Transient
	ResponseEntity response;
}
