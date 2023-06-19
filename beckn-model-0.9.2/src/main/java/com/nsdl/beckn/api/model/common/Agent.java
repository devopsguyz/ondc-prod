package com.nsdl.beckn.api.model.common;

import lombok.Data;

@Data
public class Agent {
	private String name; // there is a pattern for it
	private String image; // there is a pattern for it
	private String dob;
	private String gender;
	private String cred;
	private String phone;
	private String email;
	private Tags tags;
}
