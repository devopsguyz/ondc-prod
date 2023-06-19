package com.nsdl.beckn.lm.audit;
 
import java.util.HashMap;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nsdl.beckn.lm.audit.interceptor.ServiceInterceptor;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;

//exclude = HibernateJpaAutoConfiguration.class
@SpringBootApplication()
@ComponentScan({"com.nsdl.beckn.lm.audit"})
@EnableAsync
@EnableEncryptableProperties
@Slf4j

@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class MonitorApplicationStart implements WebMvcConfigurer {

	@Autowired(required = true)
	ServiceInterceptor interceptor;

	
	@Value("${common.threads}")
	private int commonThreadCount;
	
	@Value("${spring.profiles.active}")
	String url;
	
	@Value("spring.datasource.lookup.password")
	String pwd;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("URL:" + this.pwd);
		registry.addInterceptor(this.interceptor);
	}
	
	@Bean("commonExecutor")
	Executor commonTaskExecutor() {
		log.warn("CommonExecutor thread count is {}", this.commonThreadCount);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(this.commonThreadCount);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setMaxPoolSize(Integer.MAX_VALUE);
		executor.setQueueCapacity(Integer.MAX_VALUE);
		executor.setThreadNamePrefix("task-common-");
		executor.initialize();
		return executor;
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

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

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
