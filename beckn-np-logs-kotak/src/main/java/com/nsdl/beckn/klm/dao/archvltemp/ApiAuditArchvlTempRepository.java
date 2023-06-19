package com.nsdl.beckn.klm.dao.archvltemp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsdl.beckn.klm.model.archvl.ApiAuditArchvlEntity;
import com.nsdl.beckn.klm.model.archvltemp.ApiAuditArchvlTempEntity;

public interface ApiAuditArchvlTempRepository extends JpaRepository<ApiAuditArchvlTempEntity, String>{
	
    /**
     * Finds transaction details from ApiAuditArchvlTempEntity by transaction id and created on date time.
     * @param asList List of String.
     * @param id Unique id.
     * @return List of ApiAuditArchvlTempEntity based on the transaction id and created on date time. 
     */
	List<ApiAuditArchvlTempEntity> findByActionInAndTransactionIdOrderByCreatedOnAsc(List<String> asList, String id);
}
