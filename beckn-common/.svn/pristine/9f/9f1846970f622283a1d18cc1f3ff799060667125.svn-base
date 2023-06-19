package com.nsdl.beckn.common.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.common.model.AuditDataModel;
import com.nsdl.beckn.common.model.AuditModel;
import com.nsdl.beckn.common.util.AdaptorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditService {

	@Autowired
	private AdaptorUtil adaptorUtil;

	@Autowired
	private AuditFileService auditFileService;

	@Autowired
	private AuditHttpService auditHttpService;

	public void audit(AuditModel auditModel) {

		boolean isDbConfigured = this.adaptorUtil.isDataBasePersistanceConfigured();
		boolean isFileAudit = this.adaptorUtil.isFilePersistanceConfigured();

		if (auditModel.getAuditFlags().isHttp()) {
			this.auditHttpService.doPost(auditModel);
		}

		AuditDataModel dataModel = auditModel.getDataModel();

		if (auditModel.getAuditFlags().isDatabase() && isDbConfigured) {
			log.warn("no jpa available in this version of jar. please use correct version");
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
