package com.nsdl.beckn.klm.model.response;

import org.springframework.http.ResponseEntity;

import com.nsdl.beckn.klm.utl.Constants;

import lombok.Data;

@Data
public class Response<T> {

	public Response(String status, T message) {
		super();
		this.status = status;
		this.message = message;

	}

	private String status;
	private T message;

	public static <T> ResponseEntity<Response<T>> ok(T message) {

		ResponseEntity res = ResponseEntity.ok(new Response<T>(Constants.RESPONSE_OK, message));

		return res;

	}
	
	public static <T> ResponseEntity<Response<T>> error(T message) {

		ResponseEntity res = ResponseEntity.ok(new Response<T>(Constants.RESPONSE_ERROR, message));

		return res;

	}
	

}