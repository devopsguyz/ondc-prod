package com.nsdl.beckn.np.config.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.SecurityUtils;

@Component
@EnableAsync
public class SpringApplicationEventListener {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	Gson gson;

	@Autowired
	OnboardingSubscirberService service;

	@Async
	@EventListener
	public void onSubscribeEven(OnSubscribeEvent subscription) {
		this.service.caheRefresh(subscription.getUrl());
	}

}