package com.nsdl.beckn.lm.audit.db.mumbaiarchaltmp;

 
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.audit.dao.ApiAuditParentRepository;
import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityProjecttion;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;

@Repository
public interface ApiAuditMumbaiArchalTmpRepository extends ApiAuditParentRepository<ApiAuditMumbaiArchalTmpEntity> {
	/**
	 * Finds transaction details from ApiAuditEntitySellerProjection by transaction id.
	 */
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);
	/**
	 * Finds transaction details from ApiAuditEntitySellerProjection by start and local date time.
	 */
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Finds transaction details based on pagination from ApiAuditEntitySellerProjection by start and end local date time.
	 */
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start,LocalDateTime end,Pageable pageable);
	/**
	 * Finds distinct transaction details frim api audit by start and end local date time. 
	 */
	@Query(value ="select transaction_id from api_audit_archvl_tmp apiAuditEntity where apiAuditEntity.created_on between ?1 and ?2 ", nativeQuery = true)
	List<String> findDistinctTransactionIdByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Finds transaction details from ApiAuditEntitySellerProjection by transaction id.
	 */
	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAsc( List<String> transactionIds );
	
	/**
	 * Finds transaction details from ApiAuditEntitySellerProjection by type, action, start and end local date time.
	 */
	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(String type,String action,LocalDateTime start,LocalDateTime end);

	/**
	 * Selects distinct transaction details from api audit by start and end local date time.
	 */
	@Query( value = " select "
			+ "       DISTINCT ON  (transaction_id) transaction_id transactionId , created_on createdOn "
			+ "    from (select transaction_id,created_on from "
			+ "        api_audit_archvl_tmp  where created_on between ?1 and ?2 "
			+ "    order by "
			+ "        created_on desc ) a", nativeQuery = true)
	List<ApiAuditEntityProjecttion>  selectByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Gets count by start and local date time.
	 */
	Integer countByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Selects distinct transaction id details from api audit by transaction id.
	 */
	@Query(value = " select "
			+ "       DISTINCT ON  (transaction_id) transaction_id "
			+ "    from (select transaction_id from "
			+ "        api_audit_archvl_tmp  "
			+ "    order by "
			+ "        created_on desc limit 10000) a", nativeQuery = true)
	public List<String> getDistinctId();
	
	/**
	 * Selects seller transaction details from api audit where seller id is not null and type = response by seller
	 * by seller id, start and end local date time.
	 */
	@Query( value = " Select  seller_id sellerId, count(1) from api_audit_archvl_tmp where seller_id is not null and "
			+ "type = 'RESPONSE_BY_SELLER' and created_on between ?1 and ?2 GROUP BY seller_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntitySellerProjection>  selectBySellerCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Selects buyer transaction details from api audit where seller id is not null and type = response to buyer by start and end local date time.
	 */
	@Query( value = " Select  buyer_id buyerId, count(1) from api_audit_archvl_tmp where seller_id is not null and "
			+ "type = 'RESPONSE_TO_BUYER' and created_on between ?1 and ?2 GROUP BY buyer_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntityBuyerProjection>  selectByBuyerCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
}
