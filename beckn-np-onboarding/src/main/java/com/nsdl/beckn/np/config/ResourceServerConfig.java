package com.nsdl.beckn.np.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.nsdl.beckn.np.model.UserRequestScope;

//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class ResourceServerConfig {
//	extends WebSecurityConfigurerAdapter 
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().authorizeRequests().mvcMatchers("/**").hasAuthority("SCOPE_profile").anyRequest().denyAll()
//				.and().oauth2ResourceServer().jwt();
//	}

	@RequestScope
	@Bean(name = "requestScopedUser")
	public UserRequestScope requestScopedUser() {
		return new UserRequestScope();
	}

//    @Bean
//	public FilterRegistrationBean corsFilterRegistrationBean() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.applyPermitDefaultValues();
//		//config.setAllowCredentials(true);
//		config.setAllowedOrigins(Arrays.asList("*"));
//		config.setAllowedHeaders(Arrays.asList("*"));
//		config.setAllowedMethods(Arrays.asList("*"));
//	 
//		config.setExposedHeaders(Arrays.asList("content-length","Access- Control-Allow-Headers","Access-Control-Allow-Origin","Access-Control-Request-Method", "Access-Control-Request-Headers","Origin","Cache-Control", "Content-Type", "Authorization"));
//		config.setMaxAge(3600L);
//		source.registerCorsConfiguration("/**", config);
//		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		return bean;
//	}

}