package com.nsdl.beckn.lm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//exclude = HibernateJpaAutoConfiguration.class
@EnableConfigServer
@SpringBootApplication
public class MonitorApplicationStart implements WebMvcConfigurer {

	 
 
	public static void main(String[] args) {
		if (System.getProperty("spring.profiles.active") == null) {
			System.setProperty("spring.profiles.active","dev");
			System.out.println("Setting profile : "+System.getProperty("spring.profiles.active"));
			System.out.println("pwd:"+ System.getProperty("JASYPT_ENCRYPTOR_PASSWORD"));
		}
		System.out.println(" spring.profiles.active : " + System.getProperty("spring.profiles.active"));

		SpringApplication.run(MonitorApplicationStart.class, args);
	}

}
