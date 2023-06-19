package com.nsdl.beckn.np.model.response;

import java.util.ArrayList;
import java.util.List;

public class SchemaError {
	List<String> error = new ArrayList();

	public void push(String msg) {
		error.add(msg);
	}

	public String getError() {
		if (error.size() > 0)
			return error.toString();
		else
			return null;
	}

}
