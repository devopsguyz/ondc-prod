package com.nsdl.beckn.lm.registry.cache;

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
import com.nsdl.beckn.lm.registry.dao.GraphAnalysisRepository;
import com.nsdl.beckn.lm.registry.model.GraphAnalysis;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional
public class CachingService {

	@Autowired
	private GraphAnalysisRepository graphAnalysisRepository;

	public void putToCache(  String key, Object value) {
		log.info("putting in cache {} with key {}", key);
		GraphAnalysis graph = graphAnalysisRepository.findByKey(key);
		if (graph == null) {
			graph = new GraphAnalysis();
			graph.setKey(key);
			graph.setId(UUID.randomUUID().toString());
			graph.setDb("Registry");
		}

		ObjectMapper objectMapper = new ObjectMapper();
		graph.setUpdatedDate(OffsetDateTime.now());
		try {
			graph.setValue(objectMapper.writeValueAsString(value));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		graphAnalysisRepository.save(graph);
	}

	public Object getFromCache( String key) {
		GraphAnalysis graph = graphAnalysisRepository.findByKey(key);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(graph.getValue(),List.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}

}