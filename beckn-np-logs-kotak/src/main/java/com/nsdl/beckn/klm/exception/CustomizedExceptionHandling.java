package com.nsdl.beckn.klm.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nsdl.beckn.klm.model.response.Response;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

	 
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleExceptions( Exception exception, WebRequest webRequest) {
        //Response<String> response = new Response<String>(Constants.RESPONSE_ERROR,exception.getMessage());
    	exception.printStackTrace();
        //ResponseEntity<Object> entity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        return Response.error(exception.getMessage());
    }
}