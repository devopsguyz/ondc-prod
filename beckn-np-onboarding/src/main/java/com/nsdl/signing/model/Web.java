package com.nsdl.signing.model;

import lombok.Data;

@Data
public class Web {
	String domain;

	public Web(String domain) {
		this.domain = domain;
	}

	public Web() {

	}

}
