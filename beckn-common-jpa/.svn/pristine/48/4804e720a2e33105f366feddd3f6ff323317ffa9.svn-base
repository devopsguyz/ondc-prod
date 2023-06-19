package com.nsdl.beckn.common.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.common.entity.ApiAuditEntity;
import com.nsdl.beckn.common.entity.ApiAuditErrorEntity;

@Repository
public class AuditDao {

	@Autowired(required = false)
	private EntityManager em;

	public void saveApiAudit(ApiAuditEntity entity) {
		this.em.persist(entity);
	}

	public void saveApiAuditError(ApiAuditErrorEntity entity) {
		this.em.persist(entity);
	}

	public void saveSchemaError(ApiAuditErrorEntity entity) {
		this.em.persist(entity);

	}

}
