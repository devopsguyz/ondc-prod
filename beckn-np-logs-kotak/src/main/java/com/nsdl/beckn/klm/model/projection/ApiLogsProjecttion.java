package com.nsdl.beckn.klm.model.projection;

import java.time.OffsetDateTime;

public interface ApiLogsProjecttion {

	String getRequest();

	String getResponse();

	OffsetDateTime getCreated();
}