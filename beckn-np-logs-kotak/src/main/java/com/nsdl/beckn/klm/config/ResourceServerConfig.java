package com.nsdl.beckn.klm.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nsdl.beckn.klm.model.UserRequestScope;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Slf4j
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
//	extends WebSecurityConfigurerAdapter

	@Value("${logs.security.allowed-host}")
	private String allowedHost;

	@Value("${logs.security.flag}")
	private Boolean flag;

	@Value("${logs.security.max-age-in-seconds}")
	private Long seconds;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(seconds).and().and().cors()
				.and().csrf().disable().sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers("/**").authenticated().anyRequest().denyAll().and().oauth2ResourceServer().jwt();
//		http.cors().and().csrf().disable().authorizeRequests().mvcMatchers("/**").authenticated().anyRequest().denyAll()
//				.and().oauth2ResourceServer().jwt();

	}

	@Bean
	public HttpFirewall getHttpFirewall() {
		log.warn("allowed-host is {}", this.allowedHost);

		StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
		strictHttpFirewall.setAllowSemicolon(false);
		if (flag)
			strictHttpFirewall.setAllowedHostnames(this.allowedHost::equals);
		return strictHttpFirewall;
	}

	@RequestScope
	@Bean(name = "requestScopedUser")
	public UserRequestScope requestScopedUser() {
		return new UserRequestScope();
	}

	@Bean
	public FilterRegistrationBean corsFilterRegistrationBean() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.applyPermitDefaultValues();
		// config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));

		config.setExposedHeaders(Arrays.asList("content-length", "Access- Control-Allow-Headers",
				"Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers",
				"Origin", "Cache-Control", "Content-Type", "Authorization"));
		config.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}