package com.nsdl.beckn.lm.audit.model;

import lombok.Data;

@Data
public class Graph<T> {
String x;
T y;

public Graph(String x, T y) {
	super();
	this.x = x;
	this.y = y;
}

}
