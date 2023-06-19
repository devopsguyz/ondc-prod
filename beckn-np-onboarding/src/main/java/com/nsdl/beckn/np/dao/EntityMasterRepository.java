
package com.nsdl.beckn.np.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.EntityMaster;
import com.nsdl.beckn.np.model.response.ApiEntityMasterProjection;

@Repository
public interface EntityMasterRepository extends JpaRepository<EntityMaster, String>, JpaSpecificationExecutor {

	List<EntityMaster> findBySubscriberId(String subscriberId);

 

	@Query(value = "SELECT E.ID AS ID,  CAST(N.CITY_CODE AS VARCHAR) AS CITYCODE, "
			+ "CAST(E.CITY_CODE AS VARCHAR) AS ECITYCODE,  E.CALLBACK_URL AS CALLBACK, "
			+ "	E.COUNTRY AS COUNTRY,  	E.ENCRYPTION_PUBLIC_KEY AS ENCRYPT, "
			+ "	E.SIGNING_PUBLIC_KEY AS SIGNING,  	E.STATUS AS STATUS,  E.SUBSCRIBER_ID AS SUBSCRIBERID, "
			+ "	E.UNIQUE_KEY_ID AS UNIQUEKEYID,  N.DOMAIN,  N.TYPE,  E.VALID_FROM AS VALIDFROM, "
			+ "	E.VALID_UNTIL AS VALIDUNTIL, E.CREATED_ON AS CREATED, 	E.UPDATED_ON AS UPDATED, "
			+ "	NULL AS SELLERENCRYPTION,  NULL AS SELLERSIGNING,  	NULL AS SELLERCITYCODE, "
			+ "	NULL AS SELLERUNIQUEKEYID,  NULL AS SELLERVALIDFROM, NULL AS SELLERVALIDUNTIL,"
			+ " N.SUBSCRIBER_URL AS SUBSCRIBERURL, N.MSN  FROM ENTITY_MASTER E "
			+ "INNER JOIN NETWORK_PARTICIPANT_MASTER N ON E.ID = N.ENTITY_MASTER_ID "
			+ "WHERE (E.STATUS = 'SUBSCRIBED')  AND (N.STATUS = 'SUBSCRIBED') "
			+ "	AND (:SubscriberId = '' OR E.SUBSCRIBER_ID = :SubscriberId) "
			+ "	AND (:country = '' OR E.COUNTRY = :country)   AND (:domain= '' OR N.DOMAIN = :domain) "
			+ "	AND (:type = '' OR N.TYPE = :type)  AND (:ukid ='' OR E.UNIQUE_KEY_ID = :ukid  ) "
			+ "	AND (:city ='' OR :city ='*' OR CAST(N.CITY_CODE AS VARCHAR) like '%*%' OR CAST(N.CITY_CODE AS VARCHAR) like CONCAT('%', :city, '%') "
			+ "						 )  UNION all  SELECT E.ID AS ID, "
			+ "	CAST(N.CITY_CODE AS VARCHAR) AS CITYCODE,  CAST(E.CITY_CODE AS VARCHAR) AS ECITYCODE, "
			+ "	E.CALLBACK_URL AS CALLBACK, E.COUNTRY AS COUNTRY,  	E.ENCRYPTION_PUBLIC_KEY AS ENCRYPT, "
			+ "	E.SIGNING_PUBLIC_KEY AS SIGNING,  E.STATUS AS STATUS,  	E.SUBSCRIBER_ID AS SUBSCRIBERID, "
			+ "	E.UNIQUE_KEY_ID AS UNIQUEKEYID,   N.DOMAIN,  N.TYPE, 	E.VALID_FROM AS VALIDFROM, "
			+ "	E.VALID_UNTIL AS VALIDUNTIL, 	E.CREATED_ON AS CREATED,  	E.UPDATED_ON AS UPDATED, "
			+ "	S.ENCRYPTION_PUBLIC_KEY AS SELLERENCRYPTION, S.SIGNING_PUBLIC_KEY AS SELLERSIGNING, "
			+ "	CAST(S.CITY_CODE AS VARCHAR) AS SELLERCITYCODE,  S.UNIQUE_KEY_ID AS SELLERUNIQUEKEYID, "
			+ "	S.VALID_FROM AS SELLERVALIDFROM,S.VALID_UNTIL AS SELLERVALIDUNTIL,"
			+ " N.SUBSCRIBER_URL AS SUBSCRIBERURL, N.MSN  FROM ENTITY_MASTER E "
			+ "INNER JOIN NETWORK_PARTICIPANT_MASTER N ON E.ID = N.ENTITY_MASTER_ID "
			+ "INNER JOIN SELLER_ON_RECORD_MASTER S ON S.NETWORK_PARTICIPANT_MASTER_ID = N.ID "
			+ "WHERE (E.STATUS = 'SUBSCRIBED')  AND (N.STATUS = 'SUBSCRIBED') "
			+ "	AND (:SubscriberId = '' OR E.SUBSCRIBER_ID = :SubscriberId)  AND (S.STATUS = 'SUBSCRIBED') "
			+ "	AND (:country = '' OR E.COUNTRY = :country)   AND (:domain= '' OR N.DOMAIN = :domain) "
			+ "	AND (:type = '' OR N.TYPE = :type) "
			+ "	AND (:ukid ='' OR E.UNIQUE_KEY_ID = :ukid OR S.UNIQUE_KEY_ID = :ukid ) "
			+ "	AND (:city ='' OR :city ='*' OR ( (CAST(N.CITY_CODE AS VARCHAR) like '%*%' or CAST(N.CITY_CODE AS VARCHAR) like CONCAT('%', :city, '%') ) "
			+ "	AND (CAST(S.CITY_CODE AS VARCHAR) like  '%*%'  or CAST(S.CITY_CODE AS VARCHAR) like  CONCAT('%', :city, '%') )))", nativeQuery = true)
	List<ApiEntityMasterProjection> findByAll(String country, String city, String type, String ukid, String domain,
			String SubscriberId);

	
	@Query(value = "SELECT E.ID AS ID,  CAST(N.CITY_CODE AS VARCHAR) AS CITYCODE, "
			+ "CAST(E.CITY_CODE AS VARCHAR) AS ECITYCODE,  E.CALLBACK_URL AS CALLBACK, "
			+ "	E.COUNTRY AS COUNTRY,  	E.ENCRYPTION_PUBLIC_KEY AS ENCRYPT, "
			+ "	E.SIGNING_PUBLIC_KEY AS SIGNING,  	E.STATUS AS STATUS,  E.SUBSCRIBER_ID AS SUBSCRIBERID, "
			+ "	E.UNIQUE_KEY_ID AS UNIQUEKEYID,  N.DOMAIN,  N.TYPE,  E.VALID_FROM AS VALIDFROM, "
			+ "	E.VALID_UNTIL AS VALIDUNTIL, E.CREATED_ON AS CREATED, 	E.UPDATED_ON AS UPDATED, "
			+ "	NULL AS SELLERENCRYPTION,  NULL AS SELLERSIGNING,  	NULL AS SELLERCITYCODE, "
			+ "	NULL AS SELLERUNIQUEKEYID,  NULL AS SELLERVALIDFROM, NULL AS SELLERVALIDUNTIL,"
			+ " N.SUBSCRIBER_URL AS SUBSCRIBERURL, N.MSN  FROM ENTITY_MASTER E "
			+ "INNER JOIN NETWORK_PARTICIPANT_MASTER N ON E.ID = N.ENTITY_MASTER_ID "
			+ "WHERE (E.STATUS = 'SUBSCRIBED')  AND (N.STATUS = 'SUBSCRIBED') "
			+ "	AND (:SubscriberId = '' OR E.SUBSCRIBER_ID = :SubscriberId) "
			+ "	AND (:country = '' OR E.COUNTRY = :country)   AND (:domain= '' OR N.DOMAIN = :domain) "
			+ "	AND (:type = '' OR N.TYPE = :type)  AND (:ukid ='' OR E.UNIQUE_KEY_ID = :ukid  ) "
			+ "	AND (:city ='' OR :city ='*' OR  CAST(N.CITY_CODE AS VARCHAR) like '%*%' OR CAST(N.CITY_CODE AS VARCHAR) like CONCAT('%', :city, '%') "
			+ "						 )  ", nativeQuery = true)
	List<ApiEntityMasterProjection> findByAllWithoutSOR(String country, String city, String type, String ukid, String domain,
			String SubscriberId);


	boolean existsByUniqueKeyIdIgnoreCase(String uniqueKeyId);

//	@Query(value = "SELECT  " + "	CREATED, " + "	UPDATED, " + "	CALLBACK, "
//			+ "	CITY_CODE AS CITYCODE, " + "	COUNTRY, " + "	ENCRYPT, " + "	SIGNING, "
//			+ "	STATUS, " + "	SUBSCRIBER_ID AS SUBSCRIBERID, " + "	UNIQUE_KEY_ID As UNIQUEKEYID, "
//			+ "	VALID_FROM AS VALIDFROM, " + "	VALID_UNTIL AS VALIDUNTIL, " + "	DOMAIN, " + "	TYPE, "
//			+ "	SELLER_ENCRYPTION_PUBLIC_KEY AS SELLERENCRYPTION, " + "	SELLER_SIGNING_PUBLIC_KEY AS SELLERSIGNING, "
//			+ "	SELLER_CITY_CODE  AS SELLERCITYCODE, " + "	SELLER_UNIQUE_KEY_ID AS SELLERUNIQUEKEYID, "
//			+ "	SELLER_VALID_FROM as SELLERVALIDFROM, " + "	SELLER_VALID_UNTIL as SELLERVALIDUNTIL," + "MSN "
//			+ "FROM PUBLIC.LOOKUP_DATA3  " + "WHERE (STATUS = 'SUBSCRIBED') "
//
//			+ "	AND (:SubscriberId = '' OR SUBSCRIBER_ID = :SubscriberId) "
//
//			+ "	AND (:country = '' OR COUNTRY = :country) " + " AND (:domain= '' OR DOMAIN = :domain) "
//			+ "	AND (:type = '' OR TYPE = :type) "
//			+ "	AND (:ukid ='' OR UNIQUE_KEY_ID = :ukid OR SELLER_UNIQUE_KEY_ID = :ukid ) "
//			+ "	AND (:city ='' OR :city ='*' OR (  CITY_CODE like CONCAT('%', :city, '%') "
//			+ "						AND SELLER_CITY_CODE  like  CONCAT('%', :city, '%'))) ", nativeQuery = true)
//	List<ApiEntityMasterProjection> findLookupDataByAll(String country, String city, String type, String ukid,
//			String domain, String SubscriberId);
//
//	 
 
//	@Query(value = "SELECT e.subscriber_id, cast(m.city_code as varchar) AS city_code,  e.signing_public_key , e.encryption_public_key, "
//			+ "   e.valid_from, e.valid_until,  e.created_on,  e.updated_on, m.type , m.domain, m.msn "
//			+ "   FROM entity_master e , network_participant_master m where" + "	e.subscriber_id = m.subscriber_id "
//			+ "	and e.status='SUBSCRIBED' and m.domain=?1  "
//			+ "	and( m.type=?2) and m.status='SUBSCRIBED'", nativeQuery = true)
//	@Query(value = " SELECT e.subscriber_id SubscriberId,e.country Country,cast(m.city_code as varchar) AS City,  e.signing_public_key SigningPublicKey,"
//			+ " e.encryption_public_key EncryptionPublicKey,  "
//			+ "   e.valid_from ValidFrom, e.valid_until ValidUntil,  e.created_on Created,  e.updated_on Updated, m.type Type,"
//			+ "  m.domain Domain, m.msn Msn,m.subscriber_url SubscriberUrl ,m.callback_url CallbackUrl ,m.id Id "
//			+ "   FROM entity_master e , network_participant_master m where " + "	e.subscriber_id = m.subscriber_id  "
//			+ "	and e.status='SUBSCRIBED' and (?1 ='' or m.domain=?1)  "
//			+ "	and (?2 = '' or m.type=?2) and m.status='SUBSCRIBED' and "
//			+ "  (?3 = '' or e.subscriber_id=?3) and (?4 = '' or e.country=?4)", nativeQuery = true)
//	List<EntityMasterLookupProjection> lookupEntity(String domain, String type, String subscriberId, String country);
	@Query(value = "SELECT count(*) FROM information_schema.tables" , nativeQuery = true)
	Integer health();
}
