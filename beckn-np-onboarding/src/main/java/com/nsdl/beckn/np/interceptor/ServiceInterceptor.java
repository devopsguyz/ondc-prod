package com.nsdl.beckn.np.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nsdl.beckn.np.model.UserRequestScope;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	OnboardingSubscirberService onboardingService;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {

			long startTime = System.currentTimeMillis();
			System.out.println("Path:" + request.getServletPath());
			String url = request.getRequestURL().toString();
			log.info("--------------------URL : " + url + " Started at " + startTime);
			request.setAttribute("startTime", startTime);
			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			user.setIp(request.getHeader("X-Forwarded-For"));
		 
			url = request.getServletPath();
 
			if (url != null) {
				user.setLogsType(url.replaceAll("/", ""));
			}
 
			user.setUserName("nsdl");
			user.setEmail("contact@nsdl.co.in");
			user.setId("d23a7863-fcd7-4a83-8b08-d968f9d0ad99");
 
		} catch (Exception e) {
			// TODO: handle exception
			log.error("EERROR:" + e.getMessage());

			throw e;

		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		try {

			long startTime = (Long) request.getAttribute("startTime");
			long endTime = System.currentTimeMillis();
			log.info(" URL::" + request.getRequestURL().toString() + ":: End Time=" + endTime + "::"
					+ (endTime - startTime));
			this.onboardingService.saveLogsResponseTime(Integer.valueOf((int) (endTime - startTime)),this.securityUtils.getUserDetails().getLogs());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}