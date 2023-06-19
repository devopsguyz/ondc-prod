package com.nsdl.beckn.lm.audit.db.dehliarchal;

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
public interface ApiAuditDehliArchalRepository extends ApiAuditParentRepository<ApiAuditDehliArchalEntity> {
    /**
     * Finds transaction details from ApiAuditParentEntity based on the transaction id.
     */
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);
    /**
     * Finds transaction details from ApiAuditParentEntity  based on the start and end local date time.
     */
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start, LocalDateTime end);
    /**
     * Finds transaction details from ApiAuditParentEntity  based on the start and end local date time and pagable.
     */
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start, LocalDateTime end,
			Pageable pageable);
    /**
     * Finds distinct transaction details based on the start and end local date time.
     */
	@Query(value = "select transaction_id from api_audit apiAuditEntity where apiAuditEntity.created_on between ?1 and ?2 ", nativeQuery = true)
	List<String> findDistinctTransactionIdByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Finds transaction details from ApiAuditParentEntity  based on the transaction id.
     */
	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAsc(List<String> transactionIds);
    /**
     * Finds transaction details from ApiAuditParentEntity  based on the type,action, start and end local date and time.
     */
	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(String type, String action,
			LocalDateTime start, LocalDateTime end);
    /**
     * Finds transaction details from ApiAuditEntityProjecttion  based on the start and end local date time.
     */
	@Query(value = " select "
			+ "       DISTINCT ON  (transaction_id) transaction_id transactionId , created_on createdOn "
			+ "    from (select transaction_id,created_on from "
			+ "        api_audit  where created_on between ?1 and ?2 " + "    order by "
			+ "        created_on desc ) a", nativeQuery = true)
	List<ApiAuditEntityProjecttion> selectByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Finds count of the transaction details based on the start and end local date time.
     */
	Integer countByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Gets transaction Distinct id based on the id.
     */
	@Query(value = " select " + "       DISTINCT ON  (transaction_id) transaction_id "
			+ "    from (select transaction_id from " + "        api_audit  " + "    order by "
			+ "        created_on desc limit 10000) a", nativeQuery = true)
	public List<String> getDistinctId();
    /**
     * Selects seller id from ApiAuditEntitySellerProjection  where seller id is not null and type is response by seller based on the start and end date time.
     */
	@Query(value = " Select  seller_id sellerId, count(1) from api_audit where seller_id is not null and "
			+ "type = 'RESPONSE_BY_SELLER' and created_on between ?1 and ?2 GROUP BY seller_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Selects buyer id from ApiAuditEntityBuyerProjection count where seller id is not null and type is response to buyer based on the start and end date time.
     */
	@Query(value = " Select  buyer_id buyerId, count(1) from api_audit where seller_id is not null and "
			+ "type = 'RESPONSE_TO_BUYER' and created_on between ?1 and ?2 GROUP BY buyer_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetween(LocalDateTime start, LocalDateTime end);

}
