package com.nsdl.beckn.lm.audit.cache;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.db.dehli.GraphAnalysisDehli;
import com.nsdl.beckn.lm.audit.db.dehli.GraphAnalysisRepositoryDehli;
import com.nsdl.beckn.lm.audit.db.dehliarchal.GraphAnalysisDehliArchal;
import com.nsdl.beckn.lm.audit.db.dehliarchal.GraphAnalysisRepositoryDehliArchal;
import com.nsdl.beckn.lm.audit.db.dehliarchaltmp.GraphAnalysisDehliArchalTmp;
import com.nsdl.beckn.lm.audit.db.dehliarchaltmp.GraphAnalysisRepositoryDehliArchalTmp;
import com.nsdl.beckn.lm.audit.db.mumbai.GraphAnalysisMumbai;
import com.nsdl.beckn.lm.audit.db.mumbai.GraphAnalysisRepositoryMumbai;
import com.nsdl.beckn.lm.audit.db.mumbaiarchal.GraphAnalysisMumbaiArchal;
import com.nsdl.beckn.lm.audit.db.mumbaiarchal.GraphAnalysisRepositoryMumbaiArchal;
import com.nsdl.beckn.lm.audit.db.mumbaiarchaltmp.GraphAnalysisMumbaiArchalTmp;
import com.nsdl.beckn.lm.audit.db.mumbaiarchaltmp.GraphAnalysisRepositoryMumbaiArchalTmp;
import com.nsdl.beckn.lm.audit.model.GraphAnalysis;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class CachingService {

	@Autowired
	private GraphAnalysisRepositoryDehli graphAnalysisRepositoryDehli;
	
	
	@Autowired
	private GraphAnalysisRepositoryMumbai graphAnalysisRepositoryMumbai;

	@Autowired
	private GraphAnalysisRepositoryDehliArchal graphAnalysisRepositoryDehliArchal;
	
	
	@Autowired
	private GraphAnalysisRepositoryDehliArchalTmp graphAnalysisRepositoryDehliArchalTmp;

	@Autowired
	private GraphAnalysisRepositoryMumbaiArchal graphAnalysisRepositoryMumbaiArchal;
	
	
	@Autowired
	private GraphAnalysisRepositoryMumbaiArchalTmp graphAnalysisRepositoryMumbaiArchalTmp;
	
    /**
     * This Method is putting cache generated key by DB + key , GraphAnalysis and sets the key, Id,
     * DB in graph. Used ObjectMapper and sets updated date and time in graph.
     * @param db  Database server name. 
     * @param key Unique key.
     * @param value Object value.
     * 
     */
	public void putToCache(ApiAuditDBTypeEnum db, String key, Object value) {
		log.info("putting in cache {} with key {}", key);
		key=key+'-'+db.toString();
		GraphAnalysis graph = findByKey(db,key);
		if (graph == null) {
			graph = new GraphAnalysis();
			graph.setKey(key);
			graph.setId(UUID.randomUUID().toString());
			graph.setDb(db.toString());
		}

		ObjectMapper objectMapper = new ObjectMapper();
		graph.setUpdatedDate(OffsetDateTime.now());
		try {
			graph.setValue(objectMapper.writeValueAsString(value));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			this.saveGraph(db,graph);
		
	}
	
    /**
     * This method checks database server equality and saves graph based on DB server and graph.
     * @param db Database server.
     * @param graph It is a graph.
     */
	public void saveGraph(ApiAuditDBTypeEnum db,GraphAnalysis graph) {
		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			 this.saveDelhi(graph);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			 this.saveDelhiArchvl(graph);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			 this.saveDehliArchvlTmp(graph);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			 this.saveMumbaiArchvl(graph);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			 this.saveMumbaiArchvlTmp(graph);
		}else {
			 this.saveMumbai(graph);
		}

	}
    /**
     * This method saves Delhi sever graph. 
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditDehliTransactionManager")
	public void saveDelhi(GraphAnalysis obj) {
		graphAnalysisRepositoryDehli.save(new GraphAnalysisDehli(obj));

	}
	
    /**
     * This method save Mumbai server graph.
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	public void saveMumbai(GraphAnalysis obj) {
		graphAnalysisRepositoryMumbai.save(new GraphAnalysisMumbai(obj));

	}

    /**
     * This method save DelhiArchal server graph.
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")
	public void saveDelhiArchvl(GraphAnalysis obj) {
		graphAnalysisRepositoryDehliArchal.save(new GraphAnalysisDehliArchal(obj));

	}
    /**
     * This method save DelhiArchalTemp server graph.
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")
	public void saveDehliArchvlTmp(GraphAnalysis obj) {
		graphAnalysisRepositoryDehliArchalTmp.save(new GraphAnalysisDehliArchalTmp(obj));

	}

    /**
     * This method save MumbaiArchal server graph.
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")
	public void saveMumbaiArchvl(GraphAnalysis obj) {
		graphAnalysisRepositoryMumbaiArchal.save(new GraphAnalysisMumbaiArchal(obj));

	}
    /**
     * This method save MumbaiArchalTemp server graph.
     * @param obj It is a graph analysis object.
     */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")
	public void saveMumbaiArchvlTmp(GraphAnalysis obj) {
	graphAnalysisRepositoryMumbaiArchalTmp.save(new GraphAnalysisMumbaiArchalTmp(obj));

	}
    /**
     * This Method returns API audit list by DB and key.
     * @param db It is a database server.
     * @param key It is a unique key.
     * @return Returns ArrayList based on DB and key.
     */
	public Object getFromCache(ApiAuditDBTypeEnum db, String key) {
		key=key+'-'+db.toString();
		GraphAnalysis graph =findByKey(db,key);
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(graph.getValue(), List.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}  
		return new ArrayList();
	}
    /**
     * This method finds API audit database server key and returns key details.
     * @param db It is database server.
     * @param key It is a unique key.
     * @return Returns database server key details.
     */
	public GraphAnalysis findByKey(ApiAuditDBTypeEnum db,String key) {

		if (ApiAuditDBTypeEnum.apiAuditDehli.equals(db)) {
			return this.findByKeyDehli(key);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlDehli.equals(db)) {
			return this.findByKeyDehliArchvl(key);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli.equals(db)) {
			return this.findByKeyDehliArchvlTmp(key);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlMumbai.equals(db)) {
			return this.findByKeyMumbaiArchvl(key);
		} else if (ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai.equals(db)) {
			return this.findByKeyMumbaiArchvlTmp(key);
		}else {
			return this.findByKeyMumbai(key);
		}
		
	}
    /**
     * This method finds Delhi server key.
     * @param key It is a unique key.
     * @return Returns Delhi server key details.
     */
	@Transactional(value = "apiAuditDehliTransactionManager")
	public GraphAnalysis findByKeyDehli(String key) {
		return graphAnalysisRepositoryDehli.findByKey(key);

	}
    /**
     * This method finds Mumbai server key.
     * @param key It is a unique key.
     * @return Returns Mumbai server key details.
     */
	@Transactional(value = "apiAuditMumbaiTransactionManager")
	public GraphAnalysis findByKeyMumbai(String key) {
		return graphAnalysisRepositoryMumbai.findByKey(key);

	}
    /**
     * This method finds DelhiArchvl server key.
     * @param key It is a unique key.
     * @return Returns Delhi server key details.
     */
	@Transactional(value = "apiAuditArchvlDehliTransactionManager")
	public GraphAnalysis findByKeyDehliArchvl(String key) {
		return graphAnalysisRepositoryDehliArchal.findByKey(key);

	}
    /**
     * This method finds DelhiArchvlTemp server key. 
     * @param key It is a unique key.
     * @return Returns DelhiArchvlTemp key details.
     */
	@Transactional(value = "apiAuditArchvlTmpDehliTransactionManager")
	public GraphAnalysis findByKeyDehliArchvlTmp(String key) {
		return graphAnalysisRepositoryDehliArchalTmp.findByKey(key);

	}
    /**
     * This method finds ArchvlMumbai server key.
     * @param key It is unique key.
     * @return Returns ArchvlMumbai key details.
     */
	@Transactional(value = "apiAuditArchvlMumbaiTransactionManager")
	public GraphAnalysis findByKeyMumbaiArchvl(String key) {
		return graphAnalysisRepositoryMumbaiArchal.findByKey(key);

	}
    /**
     * This method finds ArchvlTmpMumbai server key.
     * @param key It is unique key.
     * @return Returns ArchvlTmpMumbai key details.
     */
	@Transactional(value = "apiAuditArchvlTmpMumbaiEntityManager")
	public GraphAnalysis findByKeyMumbaiArchvlTmp(String key) {
		return graphAnalysisRepositoryMumbaiArchalTmp.findByKey(key);

	}
}