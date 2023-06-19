package com.nsdl.beckn.np.model.response;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.nsdl.beckn.np.model.NPApiLogs;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.Constants;
import com.nsdl.beckn.np.utl.SecurityUtils;

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
	
	@Autowired
	SecurityUtils securityUtils;
//
//	public static <T> ResponseEntity<Response<T>> ok(T message, OnboardingSubscirberService onboardingService) {
//		if (message instanceof MessageErrorResponse) {
//			return error(message, onboardingService);
//		} else {
//			ResponseEntity res = ResponseEntity.ok(new Response<T>(Constants.RESPONSE_OK, message));
//			try {
//				onboardingService.saveLogsResponse(res);
//			} catch (Exception e) {
//				// TODO: handle exception
//				System.out.println("Try catch Exception");
//				e.printStackTrace();
//
//			}
//			return res;
//		}
//
//	}

	public static <T> ResponseEntity<T> ok(T message, OnboardingSubscirberService onboardingService,NPApiLogs logs) {
//		if (message instanceof MessageErrorResponse) {
//			return error(message, onboardingService);
//		} else {
			ResponseEntity res = ResponseEntity.ok( message);
			try {
				onboardingService.saveLogsResponse(res,logs);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Try catch Exception");
				e.printStackTrace();

			}
			return res;
//		}

	}

	//
	public static <T> ResponseEntity<Response<T>> error(T message, OnboardingSubscirberService onboardingService,NPApiLogs logs) {
		ResponseEntity res = ResponseEntity.ok(new Response<T>(Constants.RESPONSE_ERROR, message));
		try {

			onboardingService.saveLogsResponse(res,logs);
			res = ResponseEntity.ok(new Response<String>(Constants.RESPONSE_ERROR, "Server Error"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Try catch Exception");
			e.printStackTrace();
  
		}
		return res;
	}

}