package com.nsdl.beckn.lm.registry.utl;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.lm.registry.model.CommonModel;
import com.nsdl.beckn.lm.registry.model.UserRequestScope;

@Service
public class SecurityUtils implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public UserRequestScope getUserDetails() {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			return user;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public UUID getUserId() {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			return UUID.fromString(user.getId());

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
		return null;
	}

	public void setLogId(UUID id) {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			user.setLogsId(id);

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}

	}

	public String getSrcIP() {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			return user.getIp();

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
		return null;
	}

	public CommonModel initCommonProperties(CommonModel cm) {
		this.initCommonPropertiesWithId(cm, getUserId(), getSrcIP());

		return cm;
	}

	public CommonModel initCommonPropertiesWithId(CommonModel cm, UUID id, String ip) {

		if (cm.getVersion() == null) {
			// cm.setActive(true);
			cm.setCreatedBy(id);
			cm.setApiVersion(Constants.API_VERSION);
			cm.setVersion(1);
			cm.setCreatedDate(OffsetDateTime.now());
			cm.setSourceIp(ip);
		} else {
			cm.setUpdatedBy(id);
			cm.setUpdatedDate(OffsetDateTime.now());
			cm.setVersion(cm.getVersion() + 1);
			cm.setUpdatedSourceIp(ip);
		}
		return cm;
	}

}