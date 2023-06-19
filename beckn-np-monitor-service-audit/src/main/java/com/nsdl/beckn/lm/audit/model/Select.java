package com.nsdl.beckn.lm.audit.model;

import lombok.Data;

@Data
public class Select {
String name;
String value;
public Select( ) {
	
}
public Select(String name, String value) {
	super();
	this.name = name;
	this.value = value;
}

}
