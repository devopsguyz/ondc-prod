package com.nsdl.beckn.klm.dao.adapter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.klm.model.ApiAuditParentEntity;
import com.nsdl.beckn.klm.model.adapter.ApiAuditEntity;

@Repository
public interface ApiAuditRepository extends JpaRepository<ApiAuditEntity, String> {
	
    /**
     * Finds transaction details from ApiAuditEntity by transaction id.
     * @param transactionId It is a unique id.
     * @return Its returns list of the ApiAuditEntity based on the transaction id.
     */
	List<ApiAuditEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);

	//List<ApiAuditEntity> findByActionAndIdOrderByCreatedOnAsc(String action, String id);
	
    /**
     * Finds transaction details from ApiAuditEntity by transaction id and created on date. 
     * @param asList list of the String.
     * @param id Unique id.
     * @return List of the ApiAuditEntity based on the transaction id and created on date.
     */
	List<ApiAuditEntity> findByActionInAndTransactionIdOrderByCreatedOnAsc(List<String> asList, String id);

}
