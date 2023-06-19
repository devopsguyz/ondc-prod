package com.nsdl.beckn.common.validator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nsdl.beckn.api.enums.AckStatus;
import com.nsdl.beckn.api.model.common.Ack;
import com.nsdl.beckn.api.model.common.Context;
import com.nsdl.beckn.api.model.common.Error;
import com.nsdl.beckn.api.model.response.Response;
import com.nsdl.beckn.api.model.response.ResponseMessage;
import com.nsdl.beckn.common.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BodyValidator {

	public Response validateRequestBody(Context ctx, String actionType) throws JsonProcessingException {
		Response adaptorResponse = new Response();
		ResponseMessage resMsg = new ResponseMessage();

		resMsg.setAck(new Ack(AckStatus.NACK));
		adaptorResponse.setMessage(resMsg);

		Error error = new Error();

		error.setCode(ErrorCode.INVALID_REQUEST.toString());
		adaptorResponse.setContext(ctx);
		adaptorResponse.setError(error);

		adaptorResponse.setContext(ctx);

		if (ctx == null) {
			error.setMessage("Invalid Context");
			return adaptorResponse;
		}

		if (ctx.getTransactionId() == null) {
			error.setMessage("Invalid TransactionId");
			return adaptorResponse;
		}

		if (ctx.getDomain() == null) {
			error.setMessage("Invalid Domain");
			return adaptorResponse;
		}

		if (!actionType.equalsIgnoreCase(ctx.getAction())) {
			error.setMessage("Invalid Action");
			return adaptorResponse;
		}

		log.info("body validation of the request is ok");
		return null;
	}

}
