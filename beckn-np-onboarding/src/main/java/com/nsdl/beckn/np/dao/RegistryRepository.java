package com.nsdl.beckn.np.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.RegistryEnum;
import com.nsdl.beckn.np.model.RegistryKeys;
 
 
@Repository
public interface RegistryRepository extends JpaRepository<RegistryKeys, UUID>{
	//OrderBycreatedByDescLimitedTo
	
    /**
     * Finds Registry keys from Registry Keys by type and created date.
     * @param type It is a registry type.
     * @return Its returns RegistryKeys list based on the type and created on date.
     */
	   List<RegistryKeys>  findFirstByTypeOrderByCreatedDateDesc(RegistryEnum type);
		
}
