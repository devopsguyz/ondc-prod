package com.nsdl.beckn.lm.registry.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@MappedSuperclass
public class CommonModel {
//	@Id
//	@GeneratedValue(generator = "UUID")
//	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//	@Column(name = "id", columnDefinition = "char(36)")
//	@Type(type = "org.hibernate.type.UUIDCharType" )
//	UUID id;

	@Column(name = "api_version")
	Integer apiVersion;

	@Column(name = "version")
	Integer version;

	@Column(name = "created_by", columnDefinition = "char(36)")
	UUID createdBy;

	@CreationTimestamp
	@Column(name = "created_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime createdDate;

	@Column(name = "updated_by", columnDefinition = "char(36)")
	UUID updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	OffsetDateTime updatedDate;

	@Column(name = "source_ip")
	String sourceIp;

	@Column(name = "updated_source_ip")
	String updatedSourceIp;
}
