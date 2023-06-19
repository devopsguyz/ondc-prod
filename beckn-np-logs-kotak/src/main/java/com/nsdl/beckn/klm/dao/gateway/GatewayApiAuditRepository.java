package com.nsdl.beckn.klm.dao.gateway;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.klm.model.gateway.GatewayApiAuditEntity;

@Repository
public interface GatewayApiAuditRepository extends JpaRepository<GatewayApiAuditEntity, String> {
    /**
     * Finds transaction details from GatewayApiAuditEntity by transaction id and created on date time.
     * @param transactionId Unique id.
     * @return List of the GatewayApiAuditEntity based on the transaction id and created on date time.
     */
	List<GatewayApiAuditEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);

	//List<ApiAuditEntity> findByActionAndIdOrderByCreatedOnAsc(String action, String id);
    /**
     * Selects count of transaction from public.api_audit by transaction id, buyer id and seller id.
     * @param transactionId Unique id.
     * @param id Unique id.
     * @return List of the apiAudit based on the transaction id, buyer id and seller id. 
     */
	@Query(value="SELECT   count(transaction_id) FROM public.api_audit where "
			+ "transaction_id =?1  and (buyer_id =?2  or  seller_id=?2)"
			,nativeQuery = true)
	Integer countByTransactionIdAndbuyerIdOrsellerId(String transactionId,String id);
	
    /**
     * Finds transaction details from GatewayApiAuditEntity by action, transaction id and created on date time.
     * @param asList List of String.
     * @param id Unique id.
     * @return List of GatewayApiAuditEntity based on the action, transaction id and created on date time.
     */
	List<GatewayApiAuditEntity> findByActionInAndTransactionIdOrderByCreatedOnAsc(List<String> asList, String id);

}
