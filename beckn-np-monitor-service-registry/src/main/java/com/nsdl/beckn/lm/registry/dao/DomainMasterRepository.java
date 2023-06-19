
package com.nsdl.beckn.lm.registry.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.registry.model.DomainMaster;

@Repository
public interface DomainMasterRepository extends JpaRepository<DomainMaster, String>, JpaSpecificationExecutor {
	/**
	 * Finds domain from domain master by domain.
	 * @param domain it is a domain name.
	 * @return List of domain by domain name.
	 */
	DomainMaster findByDomain(String domain);
	
	/**
	 * Selects counts of domain from domain master where allow true by domain.
	 * @param domain
	 * @return
	 */
	@Query(value = "SELECT count(domain) " + " FROM DomainMaster where  allow=true " + "	and domain = ?1 ")
	Integer isDomain(String domain);
	/**
	 * Finds exists domain by domain.
	 * @param domain It is a domain name.
	 * @return List of the existing domain by domain.
	 */
	Boolean existsByDomainIgnoreCase(String domain);
	
	/**
	 * Finds exists domain by domain and id.
	 * @param domain It is a domain name.
	 * @param uuid It is a unique id.
	 * @return List of the exists domain by domain and id.
	 */
	Boolean existsByDomainIgnoreCaseAndIdNot(String domain, String uuid);

}
