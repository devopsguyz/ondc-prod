package com.nsdl.beckn.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConfigModel implements Serializable {

	private static final long serialVersionUID = -5465633216093570849L;

	private String subscriberId;
	private String keyid;

	private SigningModel signing = new SigningModel();

	private List<ApiParamModel> api = new ArrayList<>();
	private ApiParamModel matchedApi;

}
