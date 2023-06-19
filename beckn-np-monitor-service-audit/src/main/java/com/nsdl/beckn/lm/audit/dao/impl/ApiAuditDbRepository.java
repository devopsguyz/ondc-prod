package com.nsdl.beckn.lm.audit.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.db.dehli.ApiAuditDehliRepository;
import com.nsdl.beckn.lm.audit.db.dehliarchal.ApiAuditDehliArchalRepository;
import com.nsdl.beckn.lm.audit.db.dehliarchaltmp.ApiAuditDehliArchalTmpRepository;
import com.nsdl.beckn.lm.audit.db.mumbai.ApiAuditMumbaiRepository;
import com.nsdl.beckn.lm.audit.db.mumbaiarchal.ApiAuditMumbaiArchalRepository;
import com.nsdl.beckn.lm.audit.db.mumbaiarchaltmp.ApiAuditMumbaiArchalTmpRepository;
import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityProjecttion;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;
import com.nsdl.beckn.lm.audit.model.projection.CountSellerBuyerProjection;

@Service
public class ApiAuditDbRepository {

	@Autowired
	ApiAuditMumbaiRepository apiAuditMumbaiRepository;

	@Autowired
	ApiAuditMumbaiArchalRepository apiAuditMumbaiArchalRepository;

	@Autowired
	ApiAuditMumbaiArchalTmpRepository apiAuditMumbaiArchalTmpRepository;

	@Autowired
	ApiAuditDehliRepository apiAuditDehliRepository;

	@Autowired
	ApiAuditDehliArchalTmpRepository apiAuditDehliArchalTmpRepository;

	@Autowired
	ApiAuditDehliArchalRepository apiAuditDehliArchalRepository;

	/**
	 * Finds Transaction List from ApiAuditDBTypeEnum bases on Transaction Id.
	 * 
	 * @param db            it is a database server name.
	 * @param transactionId it is unique id.
	 * @return Its return the transaction list based on transaction id.
	 */
	public List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAsc(ApiAuditDBTypeEnum db,
			String transactionId) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByTransactionIdOrderByCreatedOnAscDelhi(transactionId);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByTransactionIdOrderByCreatedOnAscArchvlDehli(transactionId);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByTransactionIdOrderByCreatedOnAscDelhiArchvlTmp(transactionId);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByTransactionIdOrderByCreatedOnAscMumbaiArchvl(transactionId);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByTransactionIdOrderByCreatedOnAscMumbaiArchvlTmp(transactionId);
		} else {
			return this.findByTransactionIdOrderByCreatedOnAscMumbai(transactionId);
		}
	}

	/**
	 * Finds Delhi server details from ApiAuditParentEntity by transaction id.
	 * 
	 * @param transactionId it is a unique id.
	 * @return Its return Delhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscDelhi(String transactionId) {
		return apiAuditDehliRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Finds ArchvlDelhi server details from ApiAuditParentEntity by transaction id
	 * 
	 * @param transactionId it is a unique id.
	 * @return Its returns ArchvlDelhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscArchvlDehli(String transactionId) {
		return apiAuditDehliArchalRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Finds ArchvlTmpDelhi server details from ApiAuditParentEntity by transaction
	 * id.
	 * 
	 * @param transactionId it is a unique id.
	 * @return Its return ArchvlTmpDelhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscDelhiArchvlTmp(String transactionId) {
		return apiAuditDehliArchalTmpRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Finds Mumbai server details from ApiAuditParentEntity by transaction id.
	 * 
	 * @param transactionId it is a unique id.
	 * @return Its return Mumbai server details based on transaction id.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscMumbai(String transactionId) {
		return apiAuditMumbaiRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Finds MumbaiArchvl server details from ApiAuditParentEntity by transaction
	 * id.
	 * 
	 * @param transactionId it is a unique id.
	 * @return Its return MumbaiArchvl server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscMumbaiArchvl(String transactionId) {
		return apiAuditMumbaiArchalRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Finds MumbaiArchvlTemp server details from ApiAuditParentEntity by
	 * transaction id.
	 * 
	 * @param transactionId id is a unique id
	 * @return Its return MumbaiArchvl server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")
	List<ApiAuditParentEntity> findByTransactionIdOrderByCreatedOnAscMumbaiArchvlTmp(String transactionId) {
		return apiAuditMumbaiArchalTmpRepository.findByTransactionIdOrderByCreatedOnAsc(transactionId);
	}

	/**
	 * Checks equality of DB and Finds local start and end date from
	 * ApiAuditParentEntity by start and end date format.
	 * 
	 * @param db    it is a database server value.
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return database server local date time details based on start and
	 *         end date format.
	 */
	public List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(ApiAuditDBTypeEnum db,
			LocalDateTime start, LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(start, end);
		} else {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbai(start, end);
		}

	}

	/**
	 * Finds Delhi server local start and end date from ApiAuditParentEntity by
	 * start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value
	 * @return Its return Delhi server details based on start and end.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscDehli(LocalDateTime start, LocalDateTime end) {
		return apiAuditDehliRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Finds ArchvlDelhi server local start and end date from ApiAuditParentEntity
	 * by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlDelhi server details based on start and end.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(LocalDateTime start,
			LocalDateTime end) {
		return apiAuditDehliArchalRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Finds ArchvlTempDelhi server local start and end date from
	 * ApiAuditParentEntity by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlTempDelhi server details based on start and end.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return apiAuditDehliArchalTmpRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Finds Mumbai server local start and end date from ApiAuditParentEntity by
	 * start and end date format.
	 * 
	 * @param start start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return Mumbai server details based on start and end.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbai(LocalDateTime start, LocalDateTime end) {
		return apiAuditMumbaiRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Finds ArchvlMumbai server local start and end date from ApiAuditParentEntity
	 * by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlMumbai server details based on start and end.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(LocalDateTime start,
			LocalDateTime end) {
		return apiAuditMumbaiArchalRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Finds ArchvlTempMumbai server local start and end date from
	 * ApiAuditParentEntity by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlTempMumbai server details based on start and end.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")
	List<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return apiAuditMumbaiArchalTmpRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end);
	}

	/**
	 * Checks equality of DB and Finds local start,end date from
	 * ApiAuditParentEntity by start,end date and page format.
	 * 
	 * @param db       it is a database server value.
	 * @param start    it is a start date.
	 * @param end      it is a end date.
	 * @param pageable it is a page.
	 * @return Its return database server details based on start,end and page
	 *         format.
	 */
	public Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAsc(ApiAuditDBTypeEnum db,
			LocalDateTime start, LocalDateTime end, Pageable pageable) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscDehli(start, end, pageable);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(start, end, pageable);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(start, end, pageable);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(start, end, pageable);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(start, end, pageable);
		} else {
			return this.findByCreatedOnBetweenOrderByCreatedOnAscMumbai(start, end, pageable);
		}
	}

	/**
	 * Finds Delhi server details by start,end date and page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      start it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return Delhi serve details based on start,end and page format.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscDehli(LocalDateTime start, LocalDateTime end,
			Pageable pageable) {
		return this.apiAuditDehliRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Finds ArcvlDelhi server details from ApiAuditParentEntity by start,end date
	 * and page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      start it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return ArcvlDelhi serve details based on start,end and page
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(LocalDateTime start,
			LocalDateTime end, Pageable pageable) {
		return this.apiAuditDehliArchalRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Finds ArcvlTempDelhi server details from ApiAuditParentEntity by start,end
	 * date and page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return ArcvlTempDelhi serve details based on start,end and page
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(LocalDateTime start,
			LocalDateTime end, Pageable pageable) {
		return this.apiAuditDehliArchalTmpRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Finds Mumbai server details from ApiAuditParentEntity by start,end date and
	 * page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return Mumbai serve details based on start,end and page format.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbai(LocalDateTime start, LocalDateTime end,
			Pageable pageable) {
		return this.apiAuditMumbaiRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Finds ArcvlMumbai server details from ApiAuditParentEntity by start,end date
	 * and page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return ArcvlMumbai serve details based on start,end and page
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(LocalDateTime start,
			LocalDateTime end, Pageable pageable) {
		return this.apiAuditMumbaiArchalRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Finds ArcvlTempMumbai server details from ApiAuditParentEntity by start,end
	 * date and page format.
	 * 
	 * @param start    it is a date value.
	 * @param end      it is a date value.
	 * @param pageable it is a page value.
	 * @return Its return ArcvlTempMumbai serve details based on start,end and page
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")
	Page<ApiAuditParentEntity> findByCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(LocalDateTime start,
			LocalDateTime end, Pageable pageable) {
		return this.apiAuditMumbaiArchalTmpRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(start, end, pageable);
	}

	/**
	 * Checks equality of database and finds DB server details by start and end date
	 * format.
	 * 
	 * @param db    it is a database server value.
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return database server details based on start and end date
	 *         format.
	 */
	// @Query("select transactionId from ApiAuditEntity apiAuditEntity where
	// apiAuditEntity.createdOn between ?1 and ?2 ")
	public List<String> findDistinctTransactionIdByCreatedOnBetween(ApiAuditDBTypeEnum db, LocalDateTime start,
			LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findDistinctTransactionIdByCreatedOnBetweenDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findDistinctTransactionIdByCreatedOnBetweenArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findDistinctTransactionIdByCreatedOnBetweenDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findDistinctTransactionIdByCreatedOnBetweenMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findDistinctTransactionIdByCreatedOnBetweenMumbaiArchvlTmp(start, end);
		} else {
			return this.findDistinctTransactionIdByCreatedOnBetweenMumbai(start, end);
		}

	}

	/**
	 * Finds Delhi Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return Delhi server details based on start and end date format.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);

	}

	/**
	 * Finds ArchvlDelhi Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlDelhi server details based on start and end date
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenArchvlDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);
	}

	/**
	 * Finds DelhiArchvlTemp Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return DelhiArchvlTemp server details based on start and end date
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenDehliArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);

	}

	/**
	 * Finds Mumbai Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return Mumbai server details based on start and end date format.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenMumbai(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);
	}

	/**
	 * Finds ArchvlMumbai Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlMumbai server details based on start and end date
	 *         format.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenMumbaiArchvl(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);

	}

	/**
	 * Finds ArchvlTempMumbai Server DB details by start and end date format.
	 * 
	 * @param start it is a date value.
	 * @param end   it is a date value.
	 * @return Its return ArchvlTempMumbai server details based on start and end
	 *         date format.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<String> findDistinctTransactionIdByCreatedOnBetweenMumbaiArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.findDistinctTransactionIdByCreatedOnBetween(start, end);
	}

	/**
	 * Checks equality of database and finds details from ApiAuditParentEntity of
	 * database by transaction id.
	 * 
	 * @param db             it is a database value.
	 * @param transactionIds it is a unique id.
	 * @return Its return database server details based on transaction id.
	 */
	public List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAsc(ApiAuditDBTypeEnum db,
			List<String> transactionIds) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByTransactionIdInOrderByCreatedOnAscDelhi(transactionIds);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByTransactionIdInOrderByCreatedOnAscArchvlDehli(transactionIds);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByTransactionIdInOrderByCreatedOnAscDelhiArchvlTmp(transactionIds);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByTransactionIdInOrderByCreatedOnAscMumbaiArchvl(transactionIds);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByTransactionIdInOrderByCreatedOnAscMumbaiArchvlTmp(transactionIds);
		} else {
			return this.findByTransactionIdInOrderByCreatedOnAscMumbai(transactionIds);
		}

	}

	/**
	 * Finds Delhi server details from ApiAuditParentEntity by transaction id.
	 * 
	 * @param transactionIds it is a unique id.
	 * @return Its return Delhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscDelhi(List<String> transactionIds) {
		return this.apiAuditDehliRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);
	}

	/**
	 * Finds ArchvlDelhi server details from ApiAuditParentEntity by Transaction id.
	 * 
	 * @param transactionIds it is a unique id.
	 * @return Its return ArchvlDelhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscArchvlDehli(List<String> transactionIds) {
		return this.apiAuditDehliArchalRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);

	}

	/**
	 * Finds ArchvlTempDelhi server details from ApiAuditParentEntity by Transaction
	 * id.
	 * 
	 * @param transaction it is a unique id.
	 * @return Its return ArchvlTempDelhi server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscDelhiArchvlTmp(List<String> transactionIds) {
		return this.apiAuditDehliArchalTmpRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);
	}

	/**
	 * Finds Mumbai server details from ApiAuditParentEntity by Transaction id.
	 * 
	 * @param transactionIds it is a unique id.
	 * @return Its return Mumbai server details based on transaction id.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscMumbai(List<String> transactionIds) {
		return this.apiAuditMumbaiRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);

	}

	/**
	 * Finds ArchvlMumbai server details from ApiAuditParentEntity by Transaction
	 * id.
	 * 
	 * @param transactionIds it is a unique id.
	 * @return Its return ArchvlMumbai server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscMumbaiArchvl(List<String> transactionIds) {
		return this.apiAuditMumbaiArchalRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);
	}

	/**
	 * Finds ArchvlTempMumbai server details from ApiAuditParentEntity by
	 * Transaction id.
	 * 
	 * @param transactionIds it is a unique id.
	 * @return Its return ArchvlTempMumbai server details based on transaction id.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<ApiAuditParentEntity> findByTransactionIdInOrderByCreatedOnAscMumbaiArchvlTmp(List<String> transactionIds) {
		return this.apiAuditMumbaiArchalTmpRepository.findByTransactionIdInOrderByCreatedOnAsc(transactionIds);

	}

	/**
	 * Checks equality of database and finds database server details from
	 * ApiAuditParentEntity by type,action,start and end date format.
	 * 
	 * @param db     it is a database server.
	 * @param type   it is a registry type.
	 * @param action it is a String value
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return database server details based on type,action,start and
	 *         end.
	 */
	public List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(ApiAuditDBTypeEnum db,
			String type, String action, LocalDateTime start, LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscDehli(type, action, start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(type, action, start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(type, action, start,
					end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(type, action, start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(type, action, start,
					end);
		} else {
			return this.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbai(type, action, start, end);
		}

	}

	/**
	 * Finds Delhi server details from ApiAuditParentEntity by type,action,start and
	 * end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return Delhi server details based on type,action,start and end
	 *         format.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscDehli(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type, action,
				start, end);
	}

	/**
	 * Finds ArchvlDelhi server details from ApiAuditParentEntity by
	 * type,action,start and end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return ArchvlDelhi server details based on type,action,start and
	 *         end format.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscArchvlDehli(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type,
				action, start, end);
	}

	/**
	 * Finds ArchvlTempDelhi server details from ApiAuditParentEntity by
	 * type,action,start and end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return ArchvlTempDelhi server details based on type,action,start
	 *         and end format.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscDehliArchvlTmp(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type,
				action, start, end);
	}

	/**
	 * Finds Mumbai server details from ApiAuditParentEntity by type,action,start
	 * and end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return Mumbai server details based on type,action,start and end
	 *         format.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbai(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type, action,
				start, end);
	}

	/**
	 * Finds ArchvlMumbai server details from ApiAuditParentEntity by
	 * type,action,start and end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return ArchvlMumbai server details based on type,action,start and
	 *         end format.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvl(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type,
				action, start, end);
	}

	/**
	 * Finds ArchvlTempMumbai server details from ApiAuditParentEntity by
	 * type,action,start and end date format.
	 * 
	 * @param type   it is a registry type.
	 * @param action it is a String.
	 * @param start  it is a date value.
	 * @param end    it is a date value.
	 * @return Its return ArchvlTempMumbai server details based on type,action,start
	 *         and end format.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<ApiAuditParentEntity> findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAscMumbaiArchvlTmp(String type,
			String action, LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(type,
				action, start, end);
	}

	/**
	 * Checks database server equality and finds database details from
	 * ApiAuditEntityProjecttion by dp,start and end date.
	 * 
	 * @param db    it is a database server name.
	 * @param start it is a start date value.
	 * @param end   is is a end date value.
	 * @return Its return database server details based on database start and end
	 *         date time.
	 */
	public List<ApiAuditEntityProjecttion> selectByCreatedOnBetween(ApiAuditDBTypeEnum db, LocalDateTime start,
			LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.selectByCreatedOnBetweenDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.selectByCreatedOnBetweenArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.selectByCreatedOnBetweenDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.selectByCreatedOnBetweenMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.selectByCreatedOnBetweenMumbaiArchvlTmp(start, end);
		} else {
			return this.selectByCreatedOnBetweenMumbai(start, end);
		}

	}

	/**
	 * Finds Delhi server details from ApiAuditEntityProjecttion by start and end
	 * local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return Delhi database server details based on start and end date
	 *         time.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Finds ArchvlDelhi server details from ApiAuditEntityProjecttion by start and
	 * end local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return ArchvlDelhi database server details based on start and end
	 *         date time.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenArchvlDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Finds ArchvlTempDelhi server details from ApiAuditEntityProjecttion by start
	 * and end local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return ArchvlTempDelhi database server details based on start and
	 *         end date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenDehliArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Finds Mumbai server details from ApiAuditEntityProjecttion by start and end
	 * local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return Mumbai database server details based on start and end date
	 *         time.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenMumbai(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Finds ArchvlMumbai server details from ApiAuditEntityProjecttion by start and
	 * end local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return ArchvlMumbai database server details based on start and
	 *         end date time.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenMumbaiArchvl(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Finds ArchvlTempMumbai server details from ApiAuditEntityProjecttion by start
	 * and end local date time.
	 * 
	 * @param start it is a start date time value.
	 * @param end   it is end date time value.
	 * @return Its return ArchvlTempMumbai database server details based on start
	 *         and end date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<ApiAuditEntityProjecttion> selectByCreatedOnBetweenMumbaiArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.selectByCreatedOnBetween(start, end);
	}

	/**
	 * Checks equality of database and gets Integer count of database details by
	 * start and end date time.
	 * 
	 * @param db    it is database value.
	 * @param start it is a start date time value.
	 * @param end   it is a end date time value.
	 * @return Its return integer count of database server details based in start
	 *         and end local date time.
	 */
	public Integer countByCreatedOnBetween(ApiAuditDBTypeEnum db, LocalDateTime start, LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.countByCreatedOnBetweenDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.countByCreatedOnBetweenArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.countByCreatedOnBetweenDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.countByCreatedOnBetweenMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.countByCreatedOnBetweenMumbaiArchvlTmp(start, end);
		} else {
			return this.countByCreatedOnBetweenMumbai(start, end);
		}

	}

	/**
	 * Gets count of Delhi server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of Delhi server details based on local date and
	 *         time.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	Integer countByCreatedOnBetweenDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets count of ArchvlDelhi server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of ArchvlDelhi server details based on local date
	 *         and time.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	Integer countByCreatedOnBetweenArchvlDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets count of ArchvlTempDelhi server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of ArchvlDelhi server details based on local date
	 *         and time.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	Integer countByCreatedOnBetweenDehliArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets count of Mumbai server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of Mumbai server details based on local date and
	 *         time.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	Integer countByCreatedOnBetweenMumbai(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets count of ArchvlMumbai server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of ArchvlMumbai server details based on local date
	 *         and time.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	Integer countByCreatedOnBetweenMumbaiArchvl(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets count of ArchvlTempMumbai server by start and end local date time.
	 * 
	 * @param start it is a start local date time.
	 * @param end   it is a end local date time.
	 * @return Its return count of ArchvlTempMumbai server details based on local
	 *         date and time.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	Integer countByCreatedOnBetweenMumbaiArchvlTmp(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.countByCreatedOnBetween(start, end);
	}

	/**
	 * Gets database server DistinctId by DB.
	 * 
	 * @param db it is a database server name.
	 * @return Returns string database DistinctId based on database name.
	 */
	public List<String> getDistinctId(ApiAuditDBTypeEnum db) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.getDistinctIdDehli();
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.getDistinctIdArchvlDehli();
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.getDistinctIdDehliArchvlTmp();
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.getDistinctIdMumbaiArchvl();
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.getDistinctIdMumbaiArchvlTmp();
		} else {
			return this.getDistinctIdMumbai();
		}

	}

	/**
	 * Gets Dehli server DistinctId.
	 * 
	 * @return Returns string of Dehli server DistinctId.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	public List<String> getDistinctIdDehli() {
		return this.apiAuditDehliRepository.getDistinctId();
	}

	/**
	 * Gets ArchvlDelhi server DistinctId.
	 * 
	 * @return Returns string of ArchvlDelhi server DistinctId.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	public List<String> getDistinctIdArchvlDehli() {
		return this.apiAuditDehliArchalRepository.getDistinctId();
	}

	/**
	 * Gets ArchvlTempDelhi server DistinctId.
	 * 
	 * @return Returns string of ArchvlTempDelhi server DistinctId.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	public List<String> getDistinctIdDehliArchvlTmp() {
		return this.apiAuditDehliArchalTmpRepository.getDistinctId();
	}

	/**
	 * Gets Mumbai server DistinctId.
	 * 
	 * @return Returns string of Mumbai server DistinctId.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	public List<String> getDistinctIdMumbai() {
		return this.apiAuditMumbaiRepository.getDistinctId();
	}

	/**
	 * Gets ArchvlMumbai server DistinctId.
	 * 
	 * @return Returns string of ArchvlMumbai server DistinctId.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	public List<String> getDistinctIdMumbaiArchvl() {
		return this.apiAuditMumbaiArchalRepository.getDistinctId();
	}

	/**
	 * Gets ArchvlTempMumbai server DistinctId.
	 * 
	 * @return Returns string of ArchvlTempMumbai server DistinctId.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	public List<String> getDistinctIdMumbaiArchvlTmp() {
		return this.apiAuditMumbaiArchalTmpRepository.getDistinctId();
	}

	/**
	 * Gets seller server details from ApiAuditEntitySellerProjection by database
	 * start and end local date time.
	 * 
	 * @param db    it is a database name.
	 * @param start it is a start data.
	 * @param end   it is a local date
	 * @return Returns seller database server details based on start and end local
	 *         date time.
	 */
	public List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetween(ApiAuditDBTypeEnum db,
			LocalDateTime start, LocalDateTime end) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.selectBySellerCreatedOnBetweenDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.selectBySellerCreatedOnBetweenArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.selectBySellerCreatedOnBetweenDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.selectBySellerCreatedOnBetweenMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.selectBySellerCreatedOnBetweenMumbaiArchvlTmp(start, end);
		} else {
			return this.selectBySellerCreatedOnBetweenMumbai(start, end);
		}

	}

	/**
	 * Gets Delhi server seller details from ApiAuditEntitySellerProjection by start
	 * and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns Delhi server seller details based on local start and end date
	 *         time.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlDelhi server seller details from ApiAuditEntitySellerProjection by
	 * start and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns ArchvlDelhi server seller details based on local start and
	 *         end date time.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenArchvlDehli(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlTempDelhi server seller details from
	 * ApiAuditEntitySellerProjection by start and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns ArchvlTempDelhi server seller details based on local start
	 *         and end date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenDehliArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets Mumbai server seller details from ApiAuditEntitySellerProjection by
	 * start and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns Mumbai server seller details based on local start and end
	 *         date time.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenMumbai(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlMumbai server seller details from ApiAuditEntitySellerProjection
	 * by start and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns ArchvlMumbai server seller details based on local start and
	 *         end date time.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenMumbaiArchvl(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlTempMumbai server seller details from
	 * ApiAuditEntitySellerProjection by start and end local date time.
	 * 
	 * @param start it is a local date time value.
	 * @param end   it is a local date time value.
	 * @return Returns ArchvlTempMumbai server seller details based on local start
	 *         and end date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<ApiAuditEntitySellerProjection> selectBySellerCreatedOnBetweenMumbaiArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.selectBySellerCreatedOnBetween(start, end);
	}

	/**
	 * Gets Buyer server details from ApiAuditEntityBuyerProjection by database
	 * start and end local date time.
	 * 
	 * @param db    it is a database name.
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns Buyer database server details based on start and end local
	 *         date time.
	 */
	public List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetween(ApiAuditDBTypeEnum db, LocalDateTime start,
			LocalDateTime end) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.selectByBuyerCreatedOnBetweenDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.selectByBuyerCreatedOnBetweenArchvlDehli(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.selectByBuyerCreatedOnBetweenDehliArchvlTmp(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.selectByBuyerCreatedOnBetweenMumbaiArchvl(start, end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.selectByBuyerCreatedOnBetweenMumbaiArchvlTmp(start, end);
		} else {
			return this.selectByBuyerCreatedOnBetweenMumbai(start, end);
		}

	}

	/**
	 * Gets Delhi serve Buyer details from ApiAuditEntityBuyerProjection by start
	 * and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns Delhi server buyer details based on start and end local date
	 *         time.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenDehli(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditDehliRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlDelhi serve Buyer details from ApiAuditEntityBuyerProjection by
	 * start and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns ArchvlDelhi server buyer details based on start and end local
	 *         date time.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenArchvlDehli(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditDehliArchalRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlTempDelhi serve Buyer details from ApiAuditEntityBuyerProjection
	 * by start and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns ArchvlTempDelhi server buyer details based on start and end
	 *         local date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenDehliArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditDehliArchalTmpRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Gets Mumbai serve Buyer details from ApiAuditEntityBuyerProjection by start
	 * and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns Mumbai server buyer details based on start and end local date
	 *         time.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenMumbai(LocalDateTime start, LocalDateTime end) {
		return this.apiAuditMumbaiRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlMumbai serve Buyer details from ApiAuditEntityBuyerProjection by
	 * start and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns ArchvlMumbai server buyer details based on start and end
	 *         local date time.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenMumbaiArchvl(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditMumbaiArchalRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Gets ArchvlTempMumbai serve Buyer details from ApiAuditEntityBuyerProjection
	 * by start and end local date time.
	 * 
	 * @param start it is a local date time.
	 * @param end   it is a local date time.
	 * @return Returns ArchvlTempMumbai server buyer details based on start and end
	 *         local date time.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	List<ApiAuditEntityBuyerProjection> selectByBuyerCreatedOnBetweenMumbaiArchvlTmp(LocalDateTime start,
			LocalDateTime end) {
		return this.apiAuditMumbaiArchalTmpRepository.selectByBuyerCreatedOnBetween(start, end);
	}

	/**
	 * Finds Id of the database server from API audit by DB and id.
	 * 
	 * @param db it is database server name.
	 * @param id it is a unique id.
	 * @return Return database server id based in id.
	 */
	public ApiAuditParentEntity findById(ApiAuditDBTypeEnum db, String id) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByIdDehli(id);
		} else {
			return this.findByIdMumbai(id);
		}
	}

	/**
	 * Finds Delhi server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return Delhi server id based in id.
	 */
	@Transactional(value = "apiAuditDehliTransactionManager")

	public ApiAuditParentEntity findByIdDehli(String id) {
		return this.apiAuditDehliRepository.findById(id).get();
	}

	/**
	 * Finds ArchvlDelhi server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return ArchvlDelhi server id based in id.
	 */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")

	public ApiAuditParentEntity findByIdArchvlDehli(String id) {
		return this.apiAuditDehliArchalRepository.findById(id).get();
	}

	/**
	 * Finds ArchvlTempDelhi server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return ArchvlTempDelhi server id based in id.
	 */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")

	public ApiAuditParentEntity findByIdDehliArchvlTmp(String id) {
		return this.apiAuditDehliArchalTmpRepository.findById(id).get();
	}

	/**
	 * Finds Mumbai server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return Mumbai server id based in id.
	 */
	@Transactional(value = "apiAuditMumbaiTransactionManager")

	public ApiAuditParentEntity findByIdMumbai(String id) {
		return this.apiAuditMumbaiRepository.findById(id).get();
	}

	/**
	 * Finds ArchvlMumbai server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return ArchvlMumbai server id based in id.
	 */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")

	public ApiAuditParentEntity findByIdMumbaiArchvl(String id) {
		return this.apiAuditMumbaiArchalRepository.findById(id).get();
	}

	/**
	 * Finds ArchvlTempMumbai server id by id.
	 * 
	 * @param id it is a unique id.
	 * @return Return ArchvlTempMumbai server id based in id.
	 */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")

	public ApiAuditParentEntity findByIdMumbaiArchvlTmp(String id) {
		return this.apiAuditMumbaiArchalTmpRepository.findById(id).get();
	}

 
	 	 
	public void apiAuditinsertSeller(ApiAuditDBTypeEnum db, boolean  force) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			this.apiAuditInsertSellerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			this.apiAuditInsertSellerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			this.apiAuditInsertSellerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			this.apiAuditInsertSellerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			this.apiAuditInsertSellerDate(force);
		} else {
			this.apiAuditInsertSellerDate(force);
		}
	}

 	void apiAuditInsertSellerDate(boolean force) {
 		if(force && this.apiAuditMumbaiRepository.isBySellerIsEmpty()==0) {
 			 this.apiAuditMumbaiRepository.insertAllDataSeller();
 		} else {
 			 this.apiAuditMumbaiRepository.insertCurrentSeller();
 		}
	}
	
	 

	 
	public void apiAuditinsertBuyer(ApiAuditDBTypeEnum db, boolean force) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			this.apiAuditInsertBuyerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			this.apiAuditInsertBuyerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			this.apiAuditInsertBuyerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			this.apiAuditInsertBuyerDate(force);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			this.apiAuditInsertBuyerDate(force);
		} else {
			this.apiAuditInsertBuyerDate(force);
		}
	}


	void apiAuditInsertBuyerDate(boolean force) {
		if(force && this.apiAuditMumbaiRepository.isBuyerEmpty()==0) {
			
			 this.apiAuditMumbaiRepository.insertAllDataBuyer();
		} else {
			 this.apiAuditMumbaiRepository.insertCurrentSeller();
		}
	}

	
	public List <CountSellerBuyerProjection> getSellerList(ApiAuditDBTypeEnum db, LocalDateTime start,LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.getSellerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.getSellerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.getSellerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.getSellerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.getSellerListMubai(start,end);
		} else {
			return this.getSellerListMubai(start,end);
		}
	}

	@Transactional(value = "apiAuditMumbaiTransactionManager")
	List <CountSellerBuyerProjection> getSellerListMubai(LocalDateTime start,LocalDateTime end){
		List <CountSellerBuyerProjection> list=this.apiAuditMumbaiRepository.getSellerList(start, end);
		return list;
	}
	public List <CountSellerBuyerProjection> getBuyerList(ApiAuditDBTypeEnum db, LocalDateTime start,LocalDateTime end) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.getBuyerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.getBuyerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.getBuyerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.getBuyerListMubai(start,end);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.getBuyerListMubai(start,end);
		} else {
			return this.getBuyerListMubai(start,end);
		}
	}
	
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	List <CountSellerBuyerProjection> getBuyerListMubai(LocalDateTime start,LocalDateTime end){
		return this.apiAuditMumbaiRepository.getBuyerList(start, end);
	}
}
