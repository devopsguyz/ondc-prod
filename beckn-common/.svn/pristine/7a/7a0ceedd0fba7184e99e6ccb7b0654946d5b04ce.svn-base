package com.nsdl.beckn.common.service;

import static com.nsdl.beckn.common.enums.OndcUserType.GATEWAY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.common.model.AuditModel;
import com.nsdl.beckn.common.model.ConfigModel;
import com.nsdl.beckn.common.model.HttpModel;
import com.nsdl.beckn.common.sender.Sender;
import com.nsdl.beckn.common.util.AdaptorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditHttpService {

	@Autowired
	private Sender sender;

	@Autowired
	private ApplicationConfigService appConfigService;

	@Autowired
	private AdaptorUtil adaptorUtil;

	@Value("${beckn.entity.type}")
	private String entityType;

	public void doPost(AuditModel auditModel) {
		log.info("in doPost of AuditHttpService...");
		ConfigModel configuration = this.appConfigService.loadApplicationConfiguration(auditModel.getSubscriberId(), auditModel.getApiName());

		if (GATEWAY.type().equalsIgnoreCase(this.entityType)) {
			String url = auditModel.getHttpModel().getUrl();
			HttpModel httpModel = auditModel.getHttpModel();
			log.info("sending the http response from gateway to buyer {}", url);
			this.sender.send(url, httpModel.getRequestHeaders(), httpModel.getRequestBody(), null);

		} else {
			// send the response back to real buyer api if configured in yml
			String url = configuration.getMatchedApi().getHttpEntityEndpoint();
			if (this.adaptorUtil.isHttpPersistanceConfigured() && isNotBlank(url)) {
				HttpModel httpModel = auditModel.getHttpModel();
				log.info("sending the http response back to buyer internal api {}", url);
				this.sender.send(url, httpModel.getRequestHeaders(), httpModel.getRequestBody(), null);
			} else {
				log.error("no http endpoint found in the config json file");
			}
		}

	}

}
