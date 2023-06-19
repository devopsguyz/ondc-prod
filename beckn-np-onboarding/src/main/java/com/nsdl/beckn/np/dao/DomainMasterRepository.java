
package com.nsdl.beckn.np.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.DomainMaster;

@Repository
public interface DomainMasterRepository extends JpaRepository<DomainMaster, UUID>, JpaSpecificationExecutor {
	List<DomainMaster> findByDomain(String domain);

	@Query(value = "SELECT count(domain) " + " FROM DomainMaster where  allow=true " + "	and domain = ?1 ")
	Integer isDomain(String domain);
	
	

}
