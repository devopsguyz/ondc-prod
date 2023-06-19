package com.bap.gateway.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
		log.info("The excpetion caught is {} and request is {}", ex, request);

		HttpHeaders headers = new HttpHeaders();

		String bodyOfResponse = "Something went wrong";

		if (ex instanceof ApplicationException) {
			log.error("The error is of type ApplicationException");
			ErrorCode errorCode = ((ApplicationException) ex).getErrorCode();
			int code = errorCode.getCode();
			String message = errorCode.getMessage();
			HttpStatus status = HttpStatus.BAD_REQUEST;
			return handleExceptionInternal(ex, code + "|" + message, headers, status, request);
		}

		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}