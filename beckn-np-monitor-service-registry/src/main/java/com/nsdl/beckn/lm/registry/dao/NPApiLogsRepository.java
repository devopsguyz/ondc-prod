package com.nsdl.beckn.lm.registry.dao;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.registry.model.NPApiLogs;
import com.nsdl.beckn.lm.registry.model.NPApiLogsDate;

@Repository
public interface NPApiLogsRepository extends PagingAndSortingRepository<NPApiLogs, UUID> {
	
	/**
	 * Finds list of the logs by type and start and end date time.
	 * @param type It is a lookup type.
	 * @param start It is a local date time.
	 * @param end It is a local date time. 
	 * @return List of the Np API logs based on the type and start and end local date time.
	 */
	@Query(value = "Select count(1) CreatedDate from np_api_logs where typ in (?1) and created_on BETWEEN ?2 AND ?3", nativeQuery = true)
	Integer findByTypeInAndCreatedDateBetweenOrderByCreatedDateAsc(List<String> type, OffsetDateTime start,
			OffsetDateTime end);
	 
	//	List<OffsetDateTime> findByTypeAndCreatedDateBetweenOrderByCreatedDateAsc(String type, OffsetDateTime start,
//	OffsetDateTime end);
	/**
	 * Finds NP API logs list by subscriber id.
	 * @param subscriberId It is a unique id.
	 * @return List of the NP API Logs based on the subscriber id.
	 */
	@Query(value = "SELECT lt from NPApiLogs lt ORDER BY createdDate desc ")
	List<NPApiLogs> findLogsBySubscriberId(String subscriberId);
	/**
	 * Finds Np API Logs list by subscriber id, type subscriber.
	 * @param subsciberId It is a unique id.
	 * @param type_subscribe It is a subscriber type.
	 * @return List of the NP Api Logs based on the subscriber id and subscriber type.
	 */
	List<NPApiLogs> findBySubscriberIdAndTypeOrderByCreatedDateDesc(String subsciberId, String type_subscribe);
	/**
	 * Finds to 100 subscriber list by subscriber id and subscriber type.
	 * @param subsciberId It is a unique id.
	 * @param type_subscribe It is a subscriber type.
	 * @return List of the top 100 subscriber based on the subscriber id and subscriber type.
	 */
	List<NPApiLogs> findTop100BySubscriberIdAndTypeOrderByCreatedDateDesc(String subsciberId, String type_subscribe);
	
	
	/**
	 * Finds NP API logs list based on the type, start and end local date time.
	 * @param type It is lookup type.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param pageable It is page size.
	 * @return List of the NP API logs based on the type, start and end local date time.
	 */
	Page<NPApiLogs> findByTypeAndCreatedDateBetween(String type, OffsetDateTime start,OffsetDateTime end, Pageable pageable);
	/**
	 * Finds NP API logs list by type, start and end local date time.
	 * @param type It is a lookup type.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param pageable It is a page size.
	 * @return List of the NP API Logs based on the type, start and end local date time.
	 */
	Page<NPApiLogs> findByTypeAndCreatedTmBetween(String type, OffsetDateTime start,OffsetDateTime end, Pageable pageable);
	
	/**
	 * Finds distinct type from NP API logs where type is not null by type.
	 * @return List of the distinct type from NP API logs based on the type.
	 */
	@Query("SELECT DISTINCT type FROM NPApiLogs where type is not null")
	List<String> findByType();
	
	/**
	 * Finds count of the record from np_api_logs where type = vlookup by local time min and local time plus30.
	 * @param min It is a local date time.
	 * @param plus30 It is a local date time.
	 * @return List of the NP API logs based on the local date min and local date plus30.
	 */
	@Query(value = "Select count(*) from np_api_logs where typ='vlookup' and created_on BETWEEN ?1 AND ?2", nativeQuery = true)
	String countRecordsInBetweenTime(@Param("created_on")LocalDateTime min, @Param("created_on")LocalDateTime plus30);
	
	/**
	 * Finds list of the np api logs where type = vlookup by start time and plus minutes.
	 * @param startTime It is local datet time.
	 * @param plusMinutes It is a local date time.
	 * @return List of the np api logs based on the start time and plus minutes.
	 */
	@Query(value = "Select FLOOR(AVG(rspns_tm)) from np_api_logs where typ='vlookup'and created_on BETWEEN ?1 AND ?2", nativeQuery = true)
	String countRecordsResponseTimeAVg(LocalDateTime startTime, LocalDateTime plusMinutes);

//	@Query(value = "Select to_json(jsn_rqst::TEXT) request, to_json(jsn_rspns::TEXT) response,created_on created from np_api_logs "
//			+ "where jsn_rqst->'message'->'entity'->>'subscriberId' = ?1 "
//			+ "order by created_on desc limit 500", nativeQuery = true)
//	List<ApiLogsProjecttion> findLogsBySubscriberId(String subscriberId);
}
