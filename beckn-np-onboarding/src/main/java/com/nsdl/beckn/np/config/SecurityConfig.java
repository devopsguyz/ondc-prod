package com.nsdl.beckn.np.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Value("${registry.security.allowed-host}")
	private String allowedHost;

	@Value("${registry.security.flag}")
	private Boolean flag;

	
	@Value("${registry.security.max-age-in-seconds}")
	private Long seconds;

	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests(requests -> requests.antMatchers("/**").permitAll().anyRequest().authenticated())
				.headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(seconds);
		return http.build();
	}

	// @Bean
	public Filter configure(WebSecurity web) throws Exception {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		if (flag)
			firewall.setAllowedHostnames(this.allowedHost::equals);
		web.httpFirewall(firewall);
		return web.build();
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
}
