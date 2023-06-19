package com.nsdl.beckn.lm.registry.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nsdl.beckn.lm.registry.model.request.RequestDomain;
import com.nsdl.beckn.lm.registry.model.response.ResponseDomain;

public interface DomainService {
	/**
	 * Saves domain by  uuid, domain name and boolean value. 
	 * @param requestDomain it is a domain object.
	 * @return Saves domain in domain master based on the uuid, domain name and boolean value.
	 */
	ResponseEntity<?> save(RequestDomain requestDomain);
	
	/**
	 * Gets all domain list by domain.
	 * @return List of domain based on the domain.
	 */
	ResponseEntity<List<ResponseDomain>> getAllDomains();
}
