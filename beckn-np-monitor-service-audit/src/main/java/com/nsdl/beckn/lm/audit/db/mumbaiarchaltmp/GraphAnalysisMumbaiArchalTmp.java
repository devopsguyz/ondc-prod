package com.nsdl.beckn.lm.audit.db.mumbaiarchaltmp;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.nsdl.beckn.lm.audit.model.GraphAnalysis;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

@Entity
@Table(name = "graph_analysis")
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class GraphAnalysisMumbaiArchalTmp extends GraphAnalysis{

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
	String db;
	
	public GraphAnalysisMumbaiArchalTmp() {
		
	}
	public GraphAnalysisMumbaiArchalTmp(GraphAnalysis graph) {
		this.db=graph.getDb();
		this.id=graph.getId();
		this.key=graph.getKey();
		this.value=graph.getValue();
		this.updatedDate=graph.getUpdatedDate();
	}

}
