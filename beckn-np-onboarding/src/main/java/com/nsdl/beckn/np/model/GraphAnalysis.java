package com.nsdl.beckn.np.model;

import java.time.OffsetDateTime;

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
@Table(name = "graph_analysis")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class GraphAnalysis  {

	@Id
	@Column(name = "id", columnDefinition = "char(36)")
	String id;

	@Column(name = "key")
	String key;

	@Column(name = "updated_date")
	OffsetDateTime updatedDate;

	@Type(type = "jsonb")
	@Column(name = "value", columnDefinition = "jsonb")
	String value;

}
