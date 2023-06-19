package com.nsdl.beckn.lm.audit.interceptor;

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

import com.nsdl.beckn.lm.audit.model.UserRequestScope;
import com.nsdl.beckn.lm.audit.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

	@Autowired
	SecurityUtils securityUtils;

//	@Autowired
//	OnboardingService onboardingService;

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
			String url = request.getRequestURL().toString();
			log.info("--------------------URL : " + url + " Started at " + startTime);
			request.setAttribute("startTime", startTime);
			UserRequestScope user = applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			user.setIp(request.getHeader("X-Forwarded-For"));
			// if (url.indexOf("/secure") != -1) {
			if(url.indexOf("verifyParticipant")!=-1) {
				url=url.substring(url.indexOf("verifyParticipant")).replaceAll("verifyParticipant/", "").replaceAll("/", "-");
				user.setLogsType(url);
			}else if(url.indexOf("/ondc")!=-1)  {
				url=url.substring(url.indexOf("/ondc")).replaceAll("/ondc/", "").replaceAll("/", "-");
				user.setLogsType(url);
			}
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			if (auth != null) {
//				org.springframework.security.oauth2.jwt.Jwt principal = (Jwt) auth.getPrincipal();
//
//				if (principal != null) {
//					user.setUserName(principal.getClaimAsString("preferred_username"));
//					user.setEmail(principal.getClaimAsString("email"));
//					user.setId(principal.getClaimAsString("sid"));
//					log.info("--------------------Login With : " + user.getEmail() + ":" + user.getId() + ":"
//							+ user.getUserName());
//				}
//			}else {
				user.setUserName("nsdl");
				user.setEmail("contact@nsdl.co.in");
				user.setId("d23a7863-fcd7-4a83-8b08-d968f9d0ad99");
//			}

			// }
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
		//	onboardingService.saveLogsResponseTime(Long.valueOf(endTime - startTime).intValue());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}