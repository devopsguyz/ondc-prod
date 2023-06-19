
package com.nsdl.beckn.lm.registry.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.registry.model.GraphAnalysis;

@Repository
public interface GraphAnalysisRepository extends JpaRepository<GraphAnalysis, String>, JpaSpecificationExecutor {
	GraphAnalysis findByKey(String key);

	 

}
