package com.nsdl.beckn.klm.model.archvltemp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.nsdl.beckn.klm.model.ApiAuditParentEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "api_audit", indexes = { @Index(name = "created_on_index", columnList = "created_on"),
		@Index(name = "transaction_id_on_index", columnList = "transaction_id"),
		@Index(name = "type_action_created_on_index", columnList = "type, action, created_on") })
public class ApiAuditArchvlTempEntity extends ApiAuditParentEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	public String id;

}
