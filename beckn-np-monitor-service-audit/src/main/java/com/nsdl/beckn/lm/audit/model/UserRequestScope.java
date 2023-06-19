package com.nsdl.beckn.lm.audit.model;

import java.util.UUID;

import lombok.Data;

@Data
public class UserRequestScope {
	String userName;
	String id;
	String email;
	String ip;
	UUID logsId;
	String logsType;
}
