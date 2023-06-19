package com.nsdl.beckn.lm.registry.schedule;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.nsdl.beckn.lm.registry.service.ApiAuditService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class Scheduler {
	SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat sf=new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
	
	 
	@Autowired
	ApiAuditService apiAuditService;

	boolean flag = false;
	Date start = new Date();
	Date endDt = new Date();

	@Async
	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTask() {
		try {
			
			//getDashBoard|custom|WEEKS|07-10-2022|17-10-2022

			if (!flag) {
				flag = true;
				System.out.println("Fixed delay task - " + (System.currentTimeMillis() / 1000));
				System.out.println("scheduleFixedDelayTask Start Time : "+ sf.format(new Date()));
				log.info("scheduleFixedDelayTask Start Time : "+ sf.format(new Date()));
				
				String startDayTime = this.getStartDay(10);
				String startWeekTime = this.getStartDay(28);
				String startMonthTime = this.getStartDay(90);
				String startYearTime = this.getStartDay(365);
				String end = getFormat(new Date());

				Date startSH = new Date();
				logs("Start Sechduler............");
				// 1. /api/audit/dashboard/auto
				// Start Home Transation page
			  
				// End Home Transaction page

				/// 2. start api/audit/dashboard/lookup

				startLogs("/api/audit/dashboard/lookup|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("auto", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard/auto|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("auto", "WEEKS", startWeekTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("auto", "MONTHS", startMonthTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("auto", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "custom" + "DAYS" + "|" + startDayTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("custom", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "custom" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "custom" + "DAYS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("custom", "DAYS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "custom" + "DAYS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "custom" + "MONTHS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("custom", "MONTHS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "custom" + "MONTHS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "custom" + "WEEKS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("custom", "WEEKS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "custom" + "WEEKS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/lookup|" + "custom" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardLookup("custom", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/lookup|" + "custom" + "YEARS" + "|" + startYearTime + "|" + end);

				
				/// End /api/audit/dashboard/seller

				  
				logs("End Sechduler............");
				logs("Completed in " + ((new Date().getTime() - startSH.getTime()) / 1000) + " Sec");
				logs("--------------------------------------------------------");
				System.out.println("scheduleFixedDelayTask End Time : "+ sf.format(new Date()));
				log.info("scheduleFixedDelayTask End Time : "+ sf.format(new Date()));
				
				flag = false;
			} else {
				System.out.println("Alredy Running");
			}
		} catch (Exception e) {

			flag = false;
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void startLogs(String data) {
		this.start = new Date();
		String dataPrint = "\n---------------------start---------------------";
		dataPrint += data;
		System.out.println(dataPrint);
		log.info(dataPrint);
	}

	public void endLogs(String data) {
		this.endDt = new Date();
		String dataPrint = "\nCompleted in : " + (((this.endDt.getTime() - this.start.getTime()) / 1000) + " Sec or "
				+ (this.endDt.getTime() - this.start.getTime()) + " MS");
		dataPrint += "\n" + data;
		dataPrint += "\n---------------------end---------------------";
		System.out.println(dataPrint);
		log.info(dataPrint);

	}

	public void logs(String data) {
		log.info(data);
		System.out.println(data);
	}

	public String getStartDay(long days) {
		return getFormat(new Date(new Date().getTime() - (days * 24 * 60 * 60 * 1000)));
	}

	public String getFormat(Date dt) {

		return this.sd.format(dt);
	}

	 
}
