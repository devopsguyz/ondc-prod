package com.nsdl.beckn.lm.audit.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.model.Select;
import com.nsdl.beckn.lm.audit.schedule.Scheduler;
import com.nsdl.beckn.lm.audit.utl.Constants;

@Component
public class ServerInitializer implements ApplicationRunner {

	@Autowired
	Scheduler scheduler;
	@Value("${db.name.server}")
	String dbname;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		System.out.println("start");
		String db[] = dbname.split(",");
		List<Select> list = new ArrayList();
		for (int i = 0; i < db.length; i++) {
			String name[] = db[i].split(":");
			list.add(new Select(name[1].trim(), name[0].trim()));
		}
		Constants.db = list;
//	 	scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditMumbai);
//	
//		if (Constants.db.size() > 1)
//			scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditDehli);
//		if (Constants.db.size() > 2)
//			scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlDehli);
//		if (Constants.db.size() > 3)
//			scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlMumbai);
//		if (Constants.db.size() > 4)
//			scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli);
//		if (Constants.db.size() > 5)
//			scheduler.scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai);
// For buyer and seller
		
		scheduler.scheduleFixedInsertBuyerSellerSchduleALL();
	 

	}
}