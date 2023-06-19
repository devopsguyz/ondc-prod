package com.nsdl.beckn.common.model;

import lombok.Data;

@Data
public class AuditFlagModel {
	private boolean http;
	private boolean database;
	private boolean file;
}
