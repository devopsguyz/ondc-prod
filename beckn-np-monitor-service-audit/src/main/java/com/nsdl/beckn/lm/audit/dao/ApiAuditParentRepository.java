package com.nsdl.beckn.lm.audit.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityProjecttion;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;

 
@NoRepositoryBean
public interface ApiAuditParentRepository<T> extends PagingAndSortingRepository<T, String> {
    /**
     * Finds transaction details from ApiAuditParentEntity by transaction id.
     * @param transactionId It is a unique id.
     * @return Returns list of the transaction details based on the transaction id.
     */
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);
	
    /**
     * Finds transaction details from  by ApiAuditParentEntity start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the transaction based on the start and end local date time.
     */
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start, LocalDateTime end);
	
    /**
     * Finds transaction details from ApiAuditParentEntity by start and end local date time and pageble format.
     * @param start It is a local date time.
     * @param end It id a local date time.
     * @param pageable It is a page size.
     * @return Returns List of the transaction details based on the start and end local date time.
     */
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start, LocalDateTime end,
			Pageable pageable);
    /**
     * Finds transaction details by start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the transaction details based on the start and end local date time.
     */
	List<String> findDistinctTransactionIdByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Finds transaction details from ApiAuditParentEntity by transaction id.
     * @param transactionIds It is a unique id.
     * @return Returns list of the transaction details based on the transaction id.
     */
	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAsc(List<String> transactionIds);
    /**
     * Finds transaction details from ApiAuditParentEntity by type,action,start and end local date time.
     * @param type It is a String type. 
     * @param action It is String action.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the transaction details based on type,action start and end local date time.
     */
	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(String type, String action,
			LocalDateTime start, LocalDateTime end);
    /**
     * Selects transaction  details from ApiAuditEntityProjecttion by start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the transaction based on the start and end local date time.
     */
	List<ApiAuditEntityProjecttion> selectByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Gets count of the transaction by start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time. 
     * @return Returns Integer count based on the start and end local date time.
     */
	Integer countByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Gets Distinct id.
     * @return Returns list of the distinct id.
     */
	public List<String> getDistinctId();
    /**
     * Selects seller transaction details from ApiAuditEntitySellerProjection  by start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the seller transaction details based on the start and end local date time.
     */
	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetween(LocalDateTime start, LocalDateTime end);
    /**
     * Selects buyer transaction details from ApiAuditEntityBuyerProjection by start and end local date time.
     * @param start It is a local date time.
     * @param end It is a local date time.
     * @return Returns list of the buyer transaction based on the start and end local date time.
     */
	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetween(LocalDateTime start, LocalDateTime end);
	 
}
