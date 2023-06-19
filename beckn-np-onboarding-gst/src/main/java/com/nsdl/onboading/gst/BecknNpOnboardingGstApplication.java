package com.nsdl.onboading.gst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan({ "com.nsdl.onboading.gst"  })
@Slf4j
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class BecknNpOnboardingGstApplication {

	public static void main(String[] args) {
		if (System.getProperty("spring.profiles.active") == null) {
			System.setProperty("spring.profiles.active","dev");
			System.out.println("Setting profile : "+System.getProperty("spring.profiles.active"));
		 
		}
		System.out.println(" spring.profiles.active : " + System.getProperty("spring.profiles.active"));

		SpringApplication.run(BecknNpOnboardingGstApplication.class, args);
	}

}