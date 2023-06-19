package com.nsdl.beckn.lm.registry.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.lm.registry.dao.DomainMasterRepository;
import com.nsdl.beckn.lm.registry.model.DomainMaster;
import com.nsdl.beckn.lm.registry.model.request.RequestDomain;
import com.nsdl.beckn.lm.registry.model.response.ResponseDomain;
import com.nsdl.beckn.lm.registry.service.DomainService;
import com.nsdl.beckn.lm.registry.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional 
public class DomainServiceImpl implements DomainService {
	@Autowired
	DomainMasterRepository domainMasterRepository;

	@Autowired
	SecurityUtils securityUtils;

	@Override

	public ResponseEntity<?> save(RequestDomain requestDomain) {
		log.info("Starting save() method of DomainServiceImpl");
		Boolean isExists = false;
		boolean edit = true;
		if ((requestDomain.getUuid() == null) || "".equals(requestDomain.getUuid())) {
			edit = false;
			isExists = this.domainMasterRepository.existsByDomainIgnoreCase(requestDomain.getDomain());
		} else {
			isExists = this.domainMasterRepository.existsByDomainIgnoreCaseAndIdNot(requestDomain.getDomain(),
					requestDomain.getUuid());
		}
		if (isExists) {
			return new ResponseEntity<>("Domain already exists", HttpStatus.BAD_REQUEST);
		} else if (edit) {
			Optional<DomainMaster> domainMaster = this.domainMasterRepository.findById(requestDomain.getUuid());
			if (domainMaster.isPresent()) {
				domainMaster.get().setDomain(requestDomain.getDomain());
				domainMaster.get().setAllow(requestDomain.isAllow());

				this.domainMasterRepository.save(domainMaster.get());

				ResponseDomain responseDomain = new ResponseDomain(domainMaster.get().getId(),
						domainMaster.get().getDomain(), domainMaster.get().getAllow(),
					 domainMaster.get().getCreatedDate(),
						 domainMaster.get().getUpdatedDate());

				return new ResponseEntity<>(responseDomain, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Bad Request : UUID is no exists", HttpStatus.BAD_REQUEST);
			}
		} else {

			UUID uuid = this.securityUtils.getUserId();
			DomainMaster domainMaster = new DomainMaster();

			domainMaster.setId(UUID.randomUUID().toString());
			domainMaster.setDomain(requestDomain.getDomain());
			domainMaster.setAllow(requestDomain.isAllow());

			this.securityUtils.initCommonProperties(domainMaster);

//			domainMaster.setApiVersion(commenModel.getApiVersion());
//			domainMaster.setCreatedBy(commenModel.getCreatedBy());
//			domainMaster.setCreatedDate(commenModel.getCreatedDate());
//			domainMaster.setSourceIp(commenModel.getSourceIp());
//			domainMaster.setUpdatedBy(commenModel.getUpdatedBy());
//			domainMaster.setUpdatedDate(commenModel.getUpdatedDate());
//			domainMaster.setUpdatedSourceIp(commenModel.getUpdatedSourceIp());
//			domainMaster.setVersion(commenModel.getVersion());

			this.domainMasterRepository.save(domainMaster);

			ResponseDomain responseDomain = new ResponseDomain(domainMaster.getId(), domainMaster.getDomain(),
					domainMaster.getAllow(), domainMaster.getCreatedDate(),
					 domainMaster.getUpdatedDate());

			return new ResponseEntity<>(responseDomain, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<List<ResponseDomain>> getAllDomains() {
		log.info("Starting getAllDomains() method of DomainServiceImpl");
		List<DomainMaster> domainMasters = this.domainMasterRepository.findAll();

		List<ResponseDomain> responseDomainsList = domainMasters.stream()
				.map(dm -> new ResponseDomain(dm.getId(), dm.getDomain(), dm.getAllow(),
						dm.getCreatedDate(), dm.getUpdatedDate()))
				.collect(Collectors.toList());
		log.info("Exiting getAllDomains() method of DomainServiceImpl");
		return ResponseEntity.ok(responseDomainsList);
	}
}
