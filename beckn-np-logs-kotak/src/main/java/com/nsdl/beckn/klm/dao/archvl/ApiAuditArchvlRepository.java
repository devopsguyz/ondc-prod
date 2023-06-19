package com.nsdl.beckn.klm.dao.archvl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsdl.beckn.klm.model.ApiAuditParentEntity;
import com.nsdl.beckn.klm.model.adapter.ApiAuditEntity;
import com.nsdl.beckn.klm.model.archvl.ApiAuditArchvlEntity;

public interface ApiAuditArchvlRepository extends JpaRepository<ApiAuditArchvlEntity, String>{
	
    /**
     * Finds transaction details from ApiAuditArchvlEntity by transaction id and created on date time.
     * @param asList List of String.
     * @param id Unique id.
     * @return List of the ApiAuditArchvlEntity based on the transaction id and created on date time.
     */
	List<ApiAuditArchvlEntity> findByActionInAndTransactionIdOrderByCreatedOnAsc(List<String> asList, String id);
}
