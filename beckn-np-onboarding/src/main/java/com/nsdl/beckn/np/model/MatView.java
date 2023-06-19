package com.nsdl.beckn.np.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "mv_np_sor_dtls")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class MatView extends CommonModel {

	@Id
	String id;

	String citycode;
	@Transient
	List<String> citycodeAL;

	String ecitycode;
	
	@Transient
	List<String> ecitycodeAL;

	String callback;

	String encrypt;

	String country;

	String signing;

	String status;

	String subscriberid;

	String uniquekeyid;

	String domain;
	String type;

	OffsetDateTime validfrom;
	OffsetDateTime validuntil;
	OffsetDateTime created;
	OffsetDateTime updated;

	String sellerencryption;
	String sellersigning;

	String sellercitycode;

	@Transient
	List<String> sellercitycodeAL;

	String selleruniquekeyid;

	OffsetDateTime sellervalidfrom;
	OffsetDateTime sellervaliduntil;
	String subscriberurl;
	Boolean msn;
}
