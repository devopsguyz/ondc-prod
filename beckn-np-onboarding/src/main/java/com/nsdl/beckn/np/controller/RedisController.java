package com.nsdl.beckn.np.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
	 private static final String KEY = "VINSGURU";

//	 @Autowired
//	 RediesCache radiesCache;
//	    @Autowired
//	    private StringRedisTemplate template;
//
//	    @GetMapping("/redies/{key}/{name}")
//	    public void addToSet(@PathVariable String key,@PathVariable String name) {
//	        this.template.opsForValue().set(key, name);
//	    }
//
//
//	    @GetMapping("/redies/delete")
//	    public void delet() {
//	        this.radiesCache.deleteLookupKey();;
//	    }
//	    @GetMapping("/redies/get/{key}")
//	    public String getKeyValues(@PathVariable String key) {
//	        return this.template.opsForValue().get(key);
//	    }

}
