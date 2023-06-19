package com.nsdl.beckn.lm.registry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.beckn.lm.registry.model.request.RequestDomain;
import com.nsdl.beckn.lm.registry.model.response.ResponseDomain;
import com.nsdl.beckn.lm.registry.service.DomainService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class DomainController {
	@Autowired
	DomainService domainService;
	
	/**
	 * This method saves domain in domain table by uuid, domain name and boolean value.
	 * @param requestDomain It is a domain details.
	 * @return Saves domain in domain master and returns saved domain details and http status.
	 */
	@PostMapping("/domain/save")
	public ResponseEntity<?> save(@RequestBody RequestDomain requestDomain) {

		log.info("Starting save() method of DomainController");
		log.info("Req Body : " + requestDomain.toString());

		ResponseEntity<?> responseDomain = this.domainService.save(requestDomain);

		log.info("Exiting save() method of DomainController");

		if (responseDomain.getStatusCode() == HttpStatus.OK) {
			return ResponseEntity.ok(responseDomain.getBody());
		} else if (responseDomain.getStatusCode() == HttpStatus.NOT_FOUND) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDomain.getBody());
		}

	}
	/**
	 * This method gets domain list by domain id.
	 * @return List of the domain based on the domain details.
	 */
	@GetMapping("/domain/list")
	public ResponseEntity<?> find() {

		ResponseEntity<List<ResponseDomain>> responseDomain = this.domainService.getAllDomains();

		if (responseDomain.getStatusCode() == HttpStatus.OK) {
			return responseDomain;
		} else {
			return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);

		}
	}

}
