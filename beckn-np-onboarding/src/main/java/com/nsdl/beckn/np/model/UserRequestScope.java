package com.nsdl.beckn.np.model;

import java.util.UUID;

import lombok.Data;

@Data
public class UserRequestScope {
	String userName;
	String id;
	String email;
	String ip;
	NPApiLogs logs;
	String logsType;
}
