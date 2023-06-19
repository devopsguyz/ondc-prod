package com.nsdl.beckn.lm.registry.model.projection;

import java.time.OffsetDateTime;

public interface ApiLogsProjecttion {

	String getRequest();

	String getResponse();

	OffsetDateTime getCreated();
}