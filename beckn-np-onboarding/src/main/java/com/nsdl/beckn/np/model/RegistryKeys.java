package com.nsdl.beckn.np.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "registry_keys")
@Data 
public class RegistryKeys extends CommonModel {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType" )	
	UUID id;

	
	@Column(name = "pblc_ky")
	String publicKey;

	@Column(name = "prvt_ky")
	String privateKey;

	@Column(name = "typ")
	@Enumerated(EnumType.STRING)
	RegistryEnum type; // Encryption, Signing

}
