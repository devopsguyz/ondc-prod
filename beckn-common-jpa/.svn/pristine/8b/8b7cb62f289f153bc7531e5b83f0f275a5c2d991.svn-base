package com.nsdl.beckn.common.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.common.dao.AuditDao;
import com.nsdl.beckn.common.entity.ApiAuditEntity;
import com.nsdl.beckn.common.entity.ApiAuditErrorEntity;
import com.nsdl.beckn.common.model.AuditDataModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditDbService {

	@Autowired
	private AuditDao dao;

	@Transactional
	public void dbAudit(AuditDataModel auditModel) {
		log.info("In dbAudit....");

		ApiAuditEntity auditEntity = new ApiAuditEntity();

		log.info("{} -> TransactionId: {} and MessageId: {}", auditModel.getAction(), auditModel.getTransactionId(), auditModel.getMessageId());

		auditEntity.setId(UUID.randomUUID().toString());
		auditEntity.setMessageId(auditModel.getMessageId());
		auditEntity.setTransactionId(auditModel.getTransactionId());
		auditEntity.setAction(auditModel.getAction());
		auditEntity.setDomain(auditModel.getDomain());
		auditEntity.setCoreVersion(auditModel.getCoreVersion());
		auditEntity.setJson(auditModel.getJson());
		auditEntity.setStatus(auditModel.getStatus());
		auditEntity.setCreatedOn(auditModel.getCreatedOn());

		log.info("going to save ApiAuditEntity");
		this.dao.saveApiAudit(auditEntity);
	}

	@Transactional
	public void auditSchemaError(Class<?> schemaClass, String body, String error) {
		log.info("In auditSchemaError....");

		ApiAuditErrorEntity entity = new ApiAuditErrorEntity();
		String id = UUID.randomUUID().toString();

		log.info("Id for schema error audit: {}", id);

		entity.setId(id);
		entity.setSchemaClass(schemaClass.getSimpleName());
		entity.setJson(body);
		entity.setError(error);
		entity.setCreatedOn(LocalDateTime.now());

		log.info("going to save ApiAuditEntity");
		this.dao.saveSchemaError(entity);
	}

}
