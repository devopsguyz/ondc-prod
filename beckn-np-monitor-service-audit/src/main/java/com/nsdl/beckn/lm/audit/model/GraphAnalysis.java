package com.nsdl.beckn.lm.audit.model;

import java.time.OffsetDateTime;

import lombok.Data;

 
@Data
public class GraphAnalysis {
 
	String id;
 
	String key;
 
	OffsetDateTime updatedDate;
 
	String value;
	String db;

}
