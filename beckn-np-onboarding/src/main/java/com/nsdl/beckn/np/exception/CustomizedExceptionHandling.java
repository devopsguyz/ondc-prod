package com.nsdl.beckn.np.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nsdl.beckn.np.model.response.Response;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.CommonUtl;
import com.nsdl.beckn.np.utl.Constants;
import com.nsdl.beckn.np.utl.SecurityUtils;
import com.nsdl.beckn.np.utl.SendError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

	@Autowired
	OnboardingSubscirberService onboardingService;
	
	@Autowired
	SendError sendError;
	
	@Autowired
	SecurityUtils securityUtils;
 

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<String>> handleExceptions(Exception exception, WebRequest webRequest) {
		try {
			log.info(exception.getMessage());
			log.info("handleExceptions: exception : {} , request : {}", CommonUtl.getPrintStackString(exception),CommonUtl.toJsonStr(webRequest));
			sendError.sendError(exception, webRequest, Constants.HIGH);
		} catch (Exception e) {		
			e.printStackTrace();
		}
		 
		return Response.error(exception.getMessage(), this.onboardingService,this.securityUtils.getUserDetails().getLogs());
	}
}