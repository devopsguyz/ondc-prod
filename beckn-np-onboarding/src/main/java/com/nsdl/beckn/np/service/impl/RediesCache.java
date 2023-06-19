//package com.nsdl.beckn.np.service.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import com.nsdl.beckn.np.model.request.ReqLookup;
//import com.nsdl.beckn.np.model.response.ResponseNPMaster;
//import com.nsdl.beckn.np.utl.Constants;
//
//@Service
//public class RediesCache {
//	@Autowired
//	private RedisTemplate<String, List<ResponseNPMaster>> redisTemplate;
//
//	public void setLookup(ReqLookup key, List<ResponseNPMaster> list) {
//		redisTemplate.opsForValue().set(getLookupKey(key), list);
//
//	}
//
//	public List<ResponseNPMaster> getLookup(ReqLookup key) {
//		return redisTemplate.opsForValue().get(getLookupKey(key));
//	}
//
//	public String getLookupKey(ReqLookup reqLookup) {
//		return Constants.REDIES_KEY_LOOKUP + ":" + reqLookup.getCity() + "|" + reqLookup.getCountry() + "|"
//				+ reqLookup.getDomain() + "|" + reqLookup.getType() + "|" + reqLookup.getSubscriberId();
//	}
//
//	public void deleteLookupKey() {
//
//		redisTemplate.delete(redisTemplate.keys(Constants.REDIES_KEY_LOOKUP + ":*"));
//	}
//}
