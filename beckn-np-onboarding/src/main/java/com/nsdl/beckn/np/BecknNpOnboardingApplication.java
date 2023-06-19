package com.nsdl.beckn.np;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nsdl.beckn.np.interceptor.ServiceInterceptor;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableEncryptableProperties
@EnableAsync
@ComponentScan({"com.nsdl.beckn.np","com.nsdl.signing"})
@Slf4j
public class BecknNpOnboardingApplication implements WebMvcConfigurer {

	@Autowired(required = true)
	ServiceInterceptor interceptor;
	
	@Value("${send.error.threads}")
	private int commonThreadCount;
	
	@Value("${audit.logs.threads}")
	private int auditThreadCount;
	
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
    
    
	@Bean("sendErrorExecutor")
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
	
	@Bean("auditExecutor")
	Executor auditExecutor() {
		log.warn("CommonExecutor thread count is {}", this.commonThreadCount);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(this.auditThreadCount);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setMaxPoolSize(Integer.MAX_VALUE);
		executor.setQueueCapacity(Integer.MAX_VALUE);
		executor.setThreadNamePrefix("task-common-");
		executor.initialize();
		return executor;
	}
	
   
   
	public static void main(String[] args) {
		SpringApplication.run(BecknNpOnboardingApplication.class, args);
	}
	
}