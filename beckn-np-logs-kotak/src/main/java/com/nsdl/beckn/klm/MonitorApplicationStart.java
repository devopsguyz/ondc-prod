package com.nsdl.beckn.klm;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.sun.crypto.provider.PBEKeyFactory.PBEWithMD5AndDES;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

//exclude = HibernateJpaAutoConfiguration.class
@SpringBootApplication()
@ComponentScan("com.nsdl.beckn.klm")
@EnableEncryptableProperties
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class MonitorApplicationStart implements WebMvcConfigurer {
 
//	PBEWithMD5AndDES aes;

//	@Bean(name = "encryptorBean")
//	public StringEncryptor stringEncryptor() {
//		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//		config.setPassword("ajd2213@#$@dsja3");
//		config.setAlgorithm("PBEWithMD5AndDES");
//		config.setKeyObtentionIterations("1000");
//		config.setPoolSize("1");
//		config.setProviderName("SunJCE");
//		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//		config.setStringOutputType("base64");
//		encryptor.setConfig(config);
//		return encryptor;
//	}

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		 
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200");
			}
		};
	}

	public static void main(String[] args) {

		if (System.getProperty("JASYPT_ENCRYPTOR_PASSWORD") == null) {
			System.setProperty("JASYPT_ENCRYPTOR_PASSWORD", "MY_SECRET");
		}

		if (System.getProperty("spring.profiles.active") == null) {
		 
			System.setProperty("spring.profiles.active", "dev");
			System.out.println("Setting profile : "+System.getProperty("spring.profiles.active"));
			System.out.println("JASYPT_ENCRYPTOR_PASSWORD: "+System.getProperty("JASYPT_ENCRYPTOR_PASSWORD"));
		}
		System.out.println(" spring.profiles.active : " + System.getProperty("spring.profiles.active"));

		SpringApplication.run(MonitorApplicationStart.class, args);
	}

}
