package com.nsdl.beckn.np.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.SellerOnRecordMaster;

@Repository
public interface SellerOnRecordMasterRepository
		extends JpaRepository<SellerOnRecordMaster, String>, JpaSpecificationExecutor {
    /**
     * Finds seller on record master list by subscriber id.
     * @param subscriberId It is unique id.
     * @return Its returns list of the seller on record based on the subscriber id.
     */
	List<SellerOnRecordMaster> findBySubscriberId(String subscriberId);
//
//	@Query(value = "SELECT subscriber_id SubscriberId,unique_key_id UniqueKeyId, signing_public_key SigningPublicKey"
//			+ ", encryption_public_key EncryptionPublicKey , "
//			+ "	valid_from ValidFrom, valid_until ValidUntil, cast(city_code as varchar) AS CityCode,network_participant_master_id Id"
//			+ "	FROM seller_on_record_master where  status='SUBSCRIBED'"
//			+ "	and subscriber_id in(?1) ", nativeQuery = true)
//	List<SellerLookupProjection> lookupSeller(List<String> subscriberIds);
    /**
     * Checks exists by unique key id.
     * @param uniqueKeyId It is key.
     * @return Returns true or false based on the unique key id.
     */
	boolean existsByUniqueKeyIdIgnoreCase(String uniqueKeyId);

}
