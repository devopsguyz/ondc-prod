package com.nsdl.beckn.np.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nsdl.beckn.np.model.request.ErrorResponse;
import com.nsdl.beckn.np.model.request.RequestOldSearch;
import com.nsdl.beckn.np.model.request.RequestSearch;
import com.nsdl.beckn.np.model.request.RequestText;
import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.model.response.ResponsEntityMaster;
import com.nsdl.beckn.np.model.response.ResponsOldEntityParentMaster;
import com.nsdl.beckn.np.model.response.Response;
import com.nsdl.beckn.np.model.response.ResponseText;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.service.OnboardingMViewLookupService;
import com.nsdl.beckn.np.utl.Constants;
import com.nsdl.beckn.np.utl.SecurityUtils;
import com.nsdl.beckn.np.utl.Validation;
import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.CryptoService;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OnboardingSubcriberController {
	@Autowired
	OnboardingSubscirberService onboardingService;
	
	@Autowired
	OnboardingMViewLookupService onboardingLookupService;

	@Autowired
	CryptoService cryptoService;
	@Autowired
	Gson gson;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	Validation validation;
	
	@Value("${jpa.flag}")
	Boolean jpaFlag;

	@PostMapping("/subscribe")
	public ResponseEntity<ErrorResponse> subscribe(@RequestBody String body, @RequestHeader HttpHeaders headers) {
		log.info("subscribe: of request body: {}", body);
		log.info("subscribe: of request hostname: {}", (headers == null || headers.getHost() == null) ? null : headers.getHost().getHostName());
		SubscribeBody subscribeBody = null;
		try {
			subscribeBody = gson.fromJson(body, SubscribeBody.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("subscribe: ERROR JSON: {}", e.getMessage());
			return validation.getValidationMsg("JSON Request Invalid format");

		}

		// validated schema structure
		String errorMsg = null;

		if (subscribeBody != null && subscribeBody.getContext() != null
				&& subscribeBody.getContext().getOperation() != null
				&& subscribeBody.getContext().getOperation().getOpsNo() != null)
			errorMsg = validation.validate(body, "option" + subscribeBody.getContext().getOperation().getOpsNo());
		if (errorMsg == null) {

			ErrorResponse error = this.onboardingService.onSubscribe(subscribeBody);
			Map<String, Object> map = new HashMap<>();
			map.put("Logs", subscribeBody != null ? subscribeBody.getMsgError() : null);
			map.put("response", error);
			Response.ok(map, this.onboardingService, this.securityUtils.getUserDetails().getLogs());
			log.info("subscribe: of response body: {}", gson.toJson(error));
			return ResponseEntity.ok(error);
		} else {
			return validation.getValidationMsg(errorMsg);
		}
	}

	@GetMapping("/health")
	public ResponseEntity<String> health() {

		return ResponseEntity.ok(this.onboardingService.health());
	}

	@PostMapping("/vlookup")
	public ResponseEntity<?> lookup(@RequestBody String body, @RequestHeader HttpHeaders headers) {
		log.info("vlookup: of request hostname: {}", (headers == null || headers.getHost() == null) ? null : headers.getHost().getHostName());
		RequestSearch reqLookup = null;
		try {
			reqLookup = gson.fromJson(body, RequestSearch.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("vlookup: ERROR JSON: {}", e.getMessage());
			return validation.getValidationMsg("JSON Request Invalid format");
		}
		if (reqLookup.getSearchParameters().getSorRequired() == null) {
			reqLookup.getSearchParameters().setSorRequired(true);
		}
		// validated schema structure
		String errorMsg = null;

		errorMsg = validation.validate(body, "vlookup");
		if (errorMsg == null) {

			log.info("vlookup: sender subsriber ID : {} of request body: {}", reqLookup.getSender_subscriber_id(),
					gson.toJson(reqLookup));

			List<ResponsEntityMaster> list = null;
			if (!jpaFlag)
				list = this.onboardingService.lookup(reqLookup);
			else
				list = this.onboardingLookupService.lookup(reqLookup);

			log.info("vlookup: sender subsriber ID : {}, status : {}, of response body: {}:",
					reqLookup.getSender_subscriber_id(), gson.toJson(reqLookup), gson.toJson(list));

			log.info(reqLookup.getStatusCode() + ":" + reqLookup.getSearchParameters());

			if ("200".equals(reqLookup.getStatusCode())) {
				return Response.ok(list, this.onboardingService, this.securityUtils.getUserDetails().getLogs());
			} else {
				Map<String, String> map = new HashMap();
				map.put("StatusCode", reqLookup.getStatusCode());
				map.put("Msg", reqLookup.getMsg());

				Response.error(map, this.onboardingService, this.securityUtils.getUserDetails().getLogs());

				if ("401".equals(reqLookup.getStatusCode())) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				} else if ("412".equals(reqLookup.getStatusCode())) {
					// sender subscriber id is not match
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
				} else if ("411".equals(reqLookup.getStatusCode())) {
					// time stamp is invalid
					return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).build();
				} else if ("416".equals(reqLookup.getStatusCode())) {
					// at least 2 params in search request
					return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
				}

			}
		} else {
			ErrorResponse error = new ErrorResponse();
			error.getMessage().getAck().setStatus("NACK");
			error.getError().setType(Constants.CONTEXT_ERROR);
			error.getError().setCode(Constants.VALIDSCHEMA[0]);
			error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
			log.info("subscribe: of response body: {}", gson.toJson(error));
			return ResponseEntity.ok(error);
		}

	}

	/**
	 * Lookup old.
	 *
	 * @param body
	 * @param headers the headers
	 * @return the response entity
	 */
	@PostMapping("/lookup")
	public ResponseEntity<?> lookupOld(@RequestBody String body, @RequestHeader HttpHeaders headers) {
		log.info("lookup: of request hostname: {}", (headers == null || headers.getHost() == null) ? null : headers.getHost().getHostName());
		RequestOldSearch reqLookup = null;
		try {
			reqLookup = gson.fromJson(body, RequestOldSearch.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("lookup: ERROR JSON: {}", e.getMessage());
			return validation.getValidationMsg("JSON Request Invalid format");
		}

		// validated schema structure
		String errorMsg = null;

		errorMsg = validation.validate(body, "lookup");
		if (errorMsg == null) {

			log.info("lookup: subsriber ID : {} of request body: {}", reqLookup.getSubscriberId(),
					gson.toJson(reqLookup));
			List<ResponsOldEntityParentMaster> list = null;
			if (!jpaFlag) {
				list = this.onboardingService.lookupOld(reqLookup);
			} else {
				list = this.onboardingLookupService.lookupOld(reqLookup);
			}
			log.info("lookup: subsriber ID : {}, status : {}, of response body: {}:", reqLookup.getSubscriberId(),
					gson.toJson(reqLookup), gson.toJson(list));

			log.info(reqLookup.getStatusCode() + ":" + reqLookup);

			if ("200".equals(reqLookup.getStatusCode())) {
				return Response.ok(list, this.onboardingService, this.securityUtils.getUserDetails().getLogs());
			} else {
				Map<String, String> map = new HashMap();
				map.put("StatusCode", reqLookup.getStatusCode());
				map.put("Msg", reqLookup.getMsg());

				Response.error(map, this.onboardingService, this.securityUtils.getUserDetails().getLogs());
				ResponseEntity response = null;
				if ("416".equals(reqLookup.getStatusCode())) {
					// at least 2 params in search request //
					response = ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();

				} else {
					response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
				}

				this.onboardingService.saveLogsResponse(response, this.securityUtils.getUserDetails().getLogs());

				return response;
			}
		} else {
			ErrorResponse error = new ErrorResponse();
			error.getMessage().getAck().setStatus("NACK");
			error.getError().setType(Constants.CONTEXT_ERROR);
			error.getError().setCode(Constants.VALIDSCHEMA[0]);
			error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
			log.info("subscribe: of response body: {}", gson.toJson(error));
			return ResponseEntity.ok(error);
		}
	}

	
	@PostMapping("/public/keys")
	public ResponseEntity<Map<String, String>> lookupOndcPublicKey() {
		return ResponseEntity.ok(this.onboardingService.getPublicKey());
	}

	@PostMapping("/challenge/encrypt/text")
	public ResponseEntity<ResponseText> encryptText(@RequestBody RequestText request) {
		return ResponseEntity.ok(this.onboardingService.getEncryptText(request));
	}

	@PostMapping("/challenge/decrypt/text")
	public ResponseEntity<ResponseText> decryptText(@RequestBody RequestText request) {
		return ResponseEntity.ok(this.onboardingService.decryptText(request));
	}

	@PostMapping("/ocsp/verify")
	public ResponseEntity<Map<String, String>> verifyText(@RequestBody Web request) {
		Map<String, String> map = new HashMap<>();
		this.cryptoService.checkOCSP(request, map);
		return ResponseEntity.ok(map);
	}

	@PostMapping("/generate/registry/key")
	public ResponseEntity<String> generateKey() {
		return Response.ok(this.onboardingService.initRKey(), this.onboardingService,
				this.securityUtils.getUserDetails().getLogs());
	}

//	@PostMapping("/komn/lookup")
//	public ResponseEntity<?> lookupKomn(@RequestBody String body,@RequestHeader HttpHeaders headers) {
//		log.info("komn lookup: of request hostname: {}", headers.getHost().getHostName());
//		RequestOldSearch reqLookup = null;
//		try {
//			reqLookup = gson.fromJson(body, RequestOldSearch.class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("lookup: ERROR JSON: {}", e.getMessage());
//			return validation.getValidationMsg("JSON Request Invalid format");
//		}
//
//		// validated schema structure
//		String errorMsg = null;
//
//		errorMsg = validation.validate(body, "komnlookup");
//		if (errorMsg == null) {
//
//			log.info("lookup: subsriber ID : {} of request body: {}", reqLookup.getSubscriberId(),
//					gson.toJson(reqLookup));
//			List<ResponsOldEntityParentMaster> list = this.onboardingService.lookupKomn(reqLookup);
//
//			log.info("lookup: subsriber ID : {}, status : {}, of response body: {}:", reqLookup.getSubscriberId(),
//					gson.toJson(reqLookup), gson.toJson(list));
//
//			log.info(reqLookup.getStatusCode() + ":" + reqLookup);
//
//			if ("200".equals(reqLookup.getStatusCode())) {
//				return Response.ok(list, this.onboardingService,this.securityUtils.getUserDetails().getLogs());
//			} else {
//				Map<String, String> map = new HashMap();
//				map.put("StatusCode", reqLookup.getStatusCode());
//				map.put("Msg", reqLookup.getMsg());
//
//				Response.error(map, this.onboardingService,this.securityUtils.getUserDetails().getLogs());
//				ResponseEntity response = null;
//				if ("416".equals(reqLookup.getStatusCode())) {
//					// at least 2 params in search request //
//					response = ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
//
//				} else {
//					response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//				}
//
//				this.onboardingService.saveLogsResponse(response,this.securityUtils.getUserDetails().getLogs());
//
//				return response;
//			}
//		} else {
//			ErrorResponse error = new ErrorResponse();
//			error.getMessage().getAck().setStatus("NACK");
//			error.getError().setType(Constants.CONTEXT_ERROR);
//			error.getError().setCode(Constants.VALIDSCHEMA[0]);
//			error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
//			log.info("komn lookup: of response body: {}", gson.toJson(error));
//			return ResponseEntity.ok(error);
//		}
//	}

//	@PostMapping("/test1/vlookup")
//	public ResponseEntity<?> lookup1(@RequestBody String body,@RequestHeader HttpHeaders headers) {
//		log.info("vlookup: of request hostname: {}", headers.getHost().getHostName());
//		RequestSearch reqLookup = null;
//		try {
//			reqLookup = gson.fromJson(body, RequestSearch.class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("vlookup: ERROR JSON: {}", e.getMessage());
//			return validation.getValidationMsg("JSON Request Invalid format");
//		}
//		if(reqLookup.getSearchParameters().getSorRequired()==null) {
//			reqLookup.getSearchParameters().setSorRequired(true);
//		}
// 	 	// validated schema structure
//		String errorMsg = null;
//
//		errorMsg = validation.validate(body, "vlookup");
//		if (errorMsg == null) {
//
//			log.info("vlookup: sender subsriber ID : {} of request body: {}", reqLookup.getSender_subscriber_id(),
//					gson.toJson(reqLookup));
//
//			List<ResponsEntityMaster> list = this.onboardingService.lookupTest(reqLookup,false);
//			log.info("vlookup: sender subsriber ID : {}, status : {}, of response body: {}:",
//					reqLookup.getSender_subscriber_id(), gson.toJson(reqLookup), gson.toJson(list));
//
//			log.info(reqLookup.getStatusCode() + ":" + reqLookup.getSearchParameters());
//
//			if ("200".equals(reqLookup.getStatusCode())) {
//				return Response.ok(list, this.onboardingService);
//			} else {
//				Map<String, String> map = new HashMap();
//				map.put("StatusCode", reqLookup.getStatusCode());				map.put("Msg", reqLookup.getMsg());
//
//				Response.error(map, this.onboardingService);
//
//				if ("401".equals(reqLookup.getStatusCode())) {
//					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//				} else if ("412".equals(reqLookup.getStatusCode())) {
//					// sender subscriber id is not match
//					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
//				} else if ("411".equals(reqLookup.getStatusCode())) {
//					// time stamp is invalid
//					return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).build();
//				} else if ("416".equals(reqLookup.getStatusCode())) {
//					// at least 2 params in search request
//					return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
//				} else {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//				}
//
//			}
//		} else {
//			ErrorResponse error = new ErrorResponse();
//			error.getMessage().getAck().setStatus("NACK");
//			error.getError().setType(Constants.CONTEXT_ERROR);
//			error.getError().setCode(Constants.VALIDSCHEMA[0]);
//			error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
//			log.info("subscribe: of response body: {}", gson.toJson(error));
//			return ResponseEntity.ok(error);
//		}
//
//	}
//	@PostMapping("/test2/vlookup")
//	public ResponseEntity<?> lookup2(@RequestBody String body,@RequestHeader HttpHeaders headers) {
//		log.info("vlookup: of request hostname: {}", headers.getHost().getHostName());
//		RequestSearch reqLookup = null;
//		try {
//			reqLookup = gson.fromJson(body, RequestSearch.class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("vlookup: ERROR JSON: {}", e.getMessage());
//			return validation.getValidationMsg("JSON Request Invalid format");
//		}
//		if(reqLookup.getSearchParameters().getSorRequired()==null) {
//			reqLookup.getSearchParameters().setSorRequired(true);
//		}
// 	 	// validated schema structure
//		String errorMsg = null;
//
//		errorMsg = validation.validate(body, "vlookup");
//		if (errorMsg == null) {
//
//			log.info("vlookup: sender subsriber ID : {} of request body: {}", reqLookup.getSender_subscriber_id(),
//					gson.toJson(reqLookup));
//
//			List<ResponsEntityMaster> list = this.onboardingService.lookupTest(reqLookup,true);
//			log.info("vlookup: sender subsriber ID : {}, status : {}, of r"
//					+ "esponse body: {}:",
//					reqLookup.getSender_subscriber_id(), gson.toJson(reqLookup), gson.toJson(list));
//
//			log.info(reqLookup.getStatusCode() + ":" + reqLookup.getSearchParameters());
//
//			if ("200".equals(reqLookup.getStatusCode())) {
//				return Response.ok(list, this.onboardingService);
//			} else {
//				Map<String, String> map = new HashMap();
//				map.put("StatusCode", reqLookup.getStatusCode());				map.put("Msg", reqLookup.getMsg());
//
//				Response.error(map, this.onboardingService);
//
//				if ("401".equals(reqLookup.getStatusCode())) {
//					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//				} else if ("412".equals(reqLookup.getStatusCode())) {
//					// sender subscriber id is not match
//					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
//				} else if ("411".equals(reqLookup.getStatusCode())) {
//					// time stamp is invalid
//					return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).build();
//				} else if ("416".equals(reqLookup.getStatusCode())) {
//					// at least 2 params in search request
//					return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
//				} else {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//				}
//
//			}
//		} else {
//			ErrorResponse error = new ErrorResponse();
//			error.getMessage().getAck().setStatus("NACK");
//			error.getError().setType(Constants.CONTEXT_ERROR);
//			error.getError().setCode(Constants.VALIDSCHEMA[0]);
//			error.getError().setMessage(Constants.VALIDSCHEMA[1] + " ERROR : " + errorMsg);
//			log.info("subscribe: of response body: {}", gson.toJson(error));
//			return ResponseEntity.ok(error);
//		}
//
//	}

}
