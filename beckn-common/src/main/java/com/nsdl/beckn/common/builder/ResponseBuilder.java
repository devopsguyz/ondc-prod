package com.nsdl.beckn.common.builder;

import static com.nsdl.beckn.common.constant.ApplicationConstant.SIGN_ALGORITHM;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Error;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;
import com.nsdl.beckn.common.exception.ApplicationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResponseBuilder {

	public String buildValidResponse() {
		return "";
	}

	public String buildErrorResponse(ApplicationException ae) throws JsonProcessingException {

		Response response = new Response();

		ResponseMessage message = new ResponseMessage();
		Error error = new Error();

		Ack ack = new Ack();
		ack.setStatus(AckStatus.NACK);

		message.setAck(ack);

		response.setMessage(message);

		error.setCode(ae.getErrorCode().toString());
		error.setMessage(ae.getMessage());

		response.setError(error);

		ObjectMapper mapper = new ObjectMapper();

		String header = "(created) (expires) digest";
		HttpHeaders headers = new HttpHeaders();
		headers.set("WWW-Authenticate", "Signature realm=\"" + SIGN_ALGORITHM + "\",headers=\"" + header + "\"");

		return mapper.writeValueAsString(response);

	}

	public ResponseEntity buildErrorResponse1() {

		String errorMessage = "error has occured";

		Response response = new Response();

		Ack ack = new Ack();
		ResponseMessage message = new ResponseMessage();
		Error error = new Error();

		ack.setStatus(AckStatus.NACK);
		message.setAck(ack);
		response.setMessage(message);
		error.setCode(errorMessage);
		error.setMessage("Unauthorized Request.");
		response.setError(error);

		try {
			ObjectMapper mapper = new ObjectMapper();

			String header = "(created) (expires) digest";
			HttpHeaders headers = new HttpHeaders();
			headers.set("WWW-Authenticate", "Signature realm=\"" + SIGN_ALGORITHM + "\",headers=\"" + header + "\"");

			String json = mapper.writeValueAsString(response);

			return new ResponseEntity<Object>(json, headers, HttpStatus.UNAUTHORIZED);
		} catch (JsonProcessingException e) {
			log.error("Couldn't serialize response for content type application/json", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
