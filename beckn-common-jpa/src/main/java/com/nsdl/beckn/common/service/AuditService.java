package com.nsdl.beckn.common.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.common.model.AuditDataModel;
import com.nsdl.beckn.common.model.AuditModel;
import com.nsdl.beckn.common.util.AdaptorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Primary
public class AuditService {

	@Autowired
	private AdaptorUtil adaptorUtil;

	@Autowired
	private AuditDbService auditDbService;

	@Autowired
	private AuditFileService auditFileService;

	@Autowired
	private AuditHttpService auditHttpService;

	public void audit(AuditModel auditModel) {

		boolean isDbConfigured = this.adaptorUtil.isDataBasePersistanceConfigured();
		boolean isFileAudit = this.adaptorUtil.isFilePersistanceConfigured();

		if (auditModel.getAuditFlags().isHttp()) {
			log.info("http service is enabled");
			this.auditHttpService.doPost(auditModel);
		}

		AuditDataModel dataModel = auditModel.getDataModel();

		if (auditModel.getAuditFlags().isDatabase() && isDbConfigured) {
			CompletableFuture.runAsync(() -> {
				log.info("going to audit response in database[in seperate thread]");
				try {
					this.auditDbService.dbAudit(dataModel);
				} catch (Exception e) {
					log.error("exception while saving api audit logs in table" + e);
					e.printStackTrace();
				}
			});
		}

		if (auditModel.getAuditFlags().isFile() && isFileAudit) {
			try {
				this.auditFileService.fileAudit(dataModel);
			} catch (IOException e) {
				log.error("file creating failed {}", e);
			}
		}

	}

}
