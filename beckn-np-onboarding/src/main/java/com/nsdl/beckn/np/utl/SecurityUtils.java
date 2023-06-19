package com.nsdl.beckn.np.utl;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.np.model.CommonModel;
import com.nsdl.beckn.np.model.NPApiLogs;
import com.nsdl.beckn.np.model.UserRequestScope;

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

	public void setLogId(NPApiLogs logs) {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			user.setLogs(logs);

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}

	}

	public NPApiLogs  getLogs( ) {
		try {
			// User user=applicationContext.getBean("requestScopedUser", User.class);

			UserRequestScope user = this.applicationContext.getBean("requestScopedUser", UserRequestScope.class);
			return user.getLogs();

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
		}
		return null;
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
