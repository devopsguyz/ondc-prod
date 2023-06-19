package com.nsdl.beckn.lm.audit.db.mumbai;

 
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.lm.audit.dao.ApiAuditParentRepository;
import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityProjecttion;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;
import com.nsdl.beckn.lm.audit.model.projection.CountSellerBuyerProjection;

@Repository
public interface ApiAuditMumbaiRepository extends ApiAuditParentRepository<ApiAuditMumbaiEntity> {
	/**
	 * Finds transaction details from ApiAuditParentEntity by transaction id.
	 */
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAsc(String transactionId);
	/**
	 * Finds transaction details from ApiAuditParentEntity by start and end local date time.
	 */
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Finds transaction details from ApiAuditParentEntity by start and end local date time.
	 */
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(LocalDateTime start,LocalDateTime end,Pageable pageable);
	/**
	 * Finds distinct transaction id from api audit by start and end local date time.
	 */
	@Query(value ="select transaction_id from api_audit apiAuditEntity where apiAuditEntity.created_on between ?1 and ?2 ", nativeQuery = true)
	List<String> findDistinctTransactionIdByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	/**
	 * Finds transaction details from ApiAuditParentEntity by transaction id.
	 */
	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAsc( List<String> transactionIds ); 
	
	/**
	 * Finds transaction details from ApiAuditParentEntity by type, action, start and end local date time.
	 */
	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(String type,String action,LocalDateTime start,LocalDateTime end);

	/**
	 * Selects distinct transaction id from api audit by start and end local date time.
	 */
	@Query( value = " select "
			+ "       DISTINCT ON  (transaction_id) transaction_id transactionId , created_on createdOn "
			+ "    from (select transaction_id,created_on from "
			+ "        api_audit  where created_on between ?1 and ?2 "
			+ "    order by "
			+ "        created_on desc ) a", nativeQuery = true)
	List<ApiAuditEntityProjecttion>  selectByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	/**
	 * Gets count by start and end local date time.
	 */
	Integer countByCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
	@Query(value = " select "
			+ "       DISTINCT ON  (transaction_id) transaction_id "
			+ "    from (select transaction_id from "
			+ "        api_audit  "
			+ "    order by "
			+ "        created_on desc limit 10000) a", nativeQuery = true)
	public List<String> getDistinctId();
	
	/**
	 * Selects seller details where seller is not null and type = response by seller by seller id, start and end local date time.
	 */
	@Query( value = " Select  seller_id sellerId, count(1) from api_audit where seller_id is not null and "
			+ "type = 'RESPONSE_BY_SELLER' and created_on between ?1 and ?2 GROUP BY seller_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntitySellerProjection>  selectBySellerCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	/**
	 * Selects buyer details where seller id is not null and type is = response to buyer from api audit by start and end local date time.
	 */
	@Query( value = " Select  buyer_id buyerId, count(1) from api_audit where seller_id is not null and "
			+ "type = 'RESPONSE_TO_BUYER' and created_on between ?1 and ?2 GROUP BY buyer_id  order by count desc", nativeQuery = true)
	List<ApiAuditEntityBuyerProjection>  selectByBuyerCreatedOnBetween(LocalDateTime start,LocalDateTime end);
	
//	@Query( value = " select distinct date(created_on)  FROM public.api_audit "
//			+ "where  action in ( 'search','on_search') and date(created_on) != current_date-1 and date(created_on) not in (select distinct date from public.analytics_seller) "
//			 , nativeQuery = true)
//	List<String>  selectByDistnictDate();
//	
//	@Modifying
//	@Transactional(value = "apiAuditMumbaiTransactionManager")
//	@Query( value = " insert into analytics_seller (date,seller_id,domain,action,count) "
//			+ "select date(created_on),seller_id,domain,action,count(1) FROM public.api_audit "
//			+ "where cast(date(created_on) as varchar) = ?1 "
//			+ "and action in ( 'search','on_search') and seller_id is not null  "
//			+ "group by date(created_on),action , seller_id , domain "
//			+ "order by 1 , 2, 3,4 "
//			 , nativeQuery = true)
//	void  insertSeller(String date);
//	
//	@Query( value = " select distinct date(created_on)  FROM public.api_audit "
//			+ "where  action in ( 'search','on_search') and date(created_on) != current_date-1 and date(created_on) not in (select distinct date from public.analytics_seller) "
//			 , nativeQuery = true)
//	List<String>  selectByBuyerDistnictDate();
//	
//	@Modifying
//	@Transactional(value = "apiAuditMumbaiTransactionManager")
//	@Query( value = " insert into analytics_buyer (date,buyer_id,domain,action,count) "
//			+ "select date(created_on),buyer_id,domain,action,count(1) FROM api_audit "
//			+ "where cast(date(created_on)as varchar) = ?1 "
//			+ "and action in ( 'search','on_search') and buyer_id is not null  "
//			+ "group by date(created_on),action , buyer_id , domain "
//			+ "order by 1 , 2, 3,4 "
//			 , nativeQuery = true)
//	void  insertBuyer(String date);
	
	
	@Query( value = "  select count(*) from analytics_seller"
			 , nativeQuery = true)
	Integer  isBySellerIsEmpty();
	
	@Modifying
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	@Query( value = " insert into analytics_seller (date,seller_id,domain,action,count) "
			+ "select date(created_on),seller_id,domain,action,count(1) FROM public.api_audit "
			+ "where  date(created_on)  = current_date-1 "
			+ "and action in ( 'search','on_search') and seller_id is not null  "
			+ "group by date(created_on),action , seller_id , domain "
			+ "order by 1 , 2, 3,4 "
			 , nativeQuery = true)
	void  insertCurrentSeller();
	
	@Modifying
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	@Query( value = " insert into analytics_seller (date,seller_id,domain,action,count) "
			+ "select date(created_on),seller_id,domain,action,count(1) FROM public.api_audit "
			+ "where   action in ( 'search','on_search') and seller_id is not null  "
			+ "group by date(created_on),action , seller_id , domain "
			+ "order by 1 , 2, 3,4 "
			 , nativeQuery = true)
	void  insertAllDataSeller();
	
	@Query( value = "select count(*) from analytics_buyer"
			 , nativeQuery = true)
	Integer  isBuyerEmpty();
	
	@Modifying
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	@Query( value = " insert into analytics_buyer (date,buyer_id,domain,action,count) "
			+ "select date(created_on),buyer_id,domain,action,count(1) FROM api_audit "
			+ "where date(created_on)  = current_date-1  "
			+ "and action in ( 'search','on_search') and buyer_id is not null  "
			+ "group by date(created_on),action , buyer_id , domain "
			+ "order by 1,2,3,4 "
			 , nativeQuery = true)
	void  insertBuyer();
	
	@Modifying
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	@Query( value = " insert into analytics_buyer (date,buyer_id,domain,action,count) "
			+ "select date(created_on),buyer_id,domain,action,count(1) FROM api_audit "
			+ "where  action in ( 'search','on_search') and buyer_id is not null  "
			+ "group by date(created_on),action , buyer_id , domain "
			+ "order by 1,2,3,4"
			 , nativeQuery = true)
	void  insertAllDataBuyer();
	
 
	@Query( value = "SELECT   Domain, Action,buyer_id BuyerId,sum( count) Count "
			+ "	FROM analytics_buyer where date between ?1 and ?2 "
			+ "	group by domain,action,buyer_id  "
			 , nativeQuery = true)
	List<CountSellerBuyerProjection>  getBuyerList(LocalDateTime start,LocalDateTime end);
	
	@Query( value = "SELECT   domain Domain, action Action ,seller_id SellerId,sum( count) Count "
			+ "	FROM analytics_seller where date between ?1 and ?2 "
			+ "	group by domain,action,seller_id  "
			 , nativeQuery = true)
	List<CountSellerBuyerProjection>  getSellerList(LocalDateTime start,LocalDateTime end);
	
}
