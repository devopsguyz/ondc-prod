package com.nsdl.beckn.np.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "domain_master")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DomainMaster extends CommonModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	String id;

	@Column(name = "domain")
	String domain;

	Boolean allow;
}
