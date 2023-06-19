package com.nsdl.beckn.klm.model;

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
