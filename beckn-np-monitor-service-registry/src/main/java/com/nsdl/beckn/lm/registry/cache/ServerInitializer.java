package com.nsdl.beckn.lm.registry.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.nsdl.beckn.lm.registry.schedule.Scheduler;

@Component
public class ServerInitializer implements ApplicationRunner {

	@Autowired
	Scheduler scheduler;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		System.out.println("start");
		scheduler.scheduleFixedDelayTask();
// code goes here

	}
}