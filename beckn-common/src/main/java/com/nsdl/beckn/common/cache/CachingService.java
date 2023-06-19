package com.nsdl.beckn.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CachingService {

	@Autowired
	private CacheManager cacheManager;

	public void putToCache(String cacheName, String key, Object value) {
		log.info("putting in cache {} with key {}", cacheName, key);
		this.cacheManager.getCache(cacheName).put(key, value);
	}

	public Object getFromCache(String cacheName, String key) {
		ValueWrapper wrapper = this.cacheManager.getCache(cacheName).get(key);
		if (wrapper != null) {
			Object object = wrapper.get();
			log.info("The value of getFromCache is {}", object);
			return object;
		}
		return null;
	}

	public String getFromCache1(String cacheName, String key) {
		String value = null;
		if (this.cacheManager.getCache(cacheName).get(key) != null) {
			value = this.cacheManager.getCache(cacheName).get(key).get().toString();
		}
		return value;
	}

	@CacheEvict(value = "first", key = "#cacheKey")
	public void evictSingleCacheValue(String cacheKey) {
	}

	@CacheEvict(value = "first", allEntries = true)
	public void evictAllCacheValues() {
	}

	public void evictSingleCacheValue(String cacheName, String cacheKey) {
		this.cacheManager.getCache(cacheName).evict(cacheKey);
	}

	public void evictAllCacheValues(String cacheName) {
		this.cacheManager.getCache(cacheName).clear();
	}

	public void evictAllCaches() {
		this.cacheManager.getCacheNames()
				.parallelStream()
				.forEach(cacheName -> this.cacheManager.getCache(cacheName).clear());
	}

	@Scheduled(fixedRate = 600000)
	public void evictAllcachesAtIntervals() {
		evictAllCaches();
	}
}