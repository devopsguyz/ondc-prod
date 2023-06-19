
package com.nsdl.beckn.lm.audit.db.dehliarchaltmp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.lm.audit.model.GraphAnalysis;
 
@Repository
public interface GraphAnalysisRepositoryDehliArchalTmp extends JpaRepository<GraphAnalysisDehliArchalTmp, String>, JpaSpecificationExecutor {
	GraphAnalysis findByKey(String key);

	 

}
