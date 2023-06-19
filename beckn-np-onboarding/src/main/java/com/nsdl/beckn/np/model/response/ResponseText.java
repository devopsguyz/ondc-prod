package com.nsdl.beckn.np.model.response;

import lombok.Data;

@Data
public class ResponseText {
	String answer;

	public ResponseText(String answer) {
		this.answer = answer;
	}

}
