
package com.nsdl.beckn.lm.audit.db.mumbaiarchal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.audit.model.GraphAnalysis;

@Repository
public interface GraphAnalysisRepositoryMumbaiArchal extends JpaRepository<GraphAnalysisMumbaiArchal, String>, JpaSpecificationExecutor {
	GraphAnalysis findByKey(String key);

	 

}
