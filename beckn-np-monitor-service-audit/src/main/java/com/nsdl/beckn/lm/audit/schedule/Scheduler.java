package com.nsdl.beckn.lm.audit.schedule;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.service.ApiAuditService;
import com.nsdl.beckn.lm.audit.utl.Constants;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class Scheduler {
	SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

	@Value("${summary.report.dir}")
	String dirPath;

	@Autowired
	ApiAuditService apiAuditService;

	Map<ApiAuditDBTypeEnum, Boolean> flag = new HashMap();
	Date start = new Date();
	Date endDt = new Date();

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskDehli() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditDehli);

	}

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskMumbai() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditMumbai);

	}

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskArchvlDehli() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlDehli);

	}

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskArchvlMumbai() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlMumbai);

	}

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskArchvlTmpDehli() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlTmpDehli);

	}

	@Scheduled(cron = "${cron.dashboard.analysis}")
	public void scheduleFixedDelayTaskArchvlTmpMumbai() {
		scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum.apiAuditArchvlTmpMumbai);
	}

	@Async(value = "commonExecutor")
	public void scheduleFixedDelayTaskDb(ApiAuditDBTypeEnum db) {
		try {

			// getDashBoard|custom|WEEKS|07-10-2022|17-10-2022

			if (flag.get(db) == null || !flag.get(db)) {
				flag.put(db, true);

				System.out.println("Fixed delay task - " + (System.currentTimeMillis() / 1000));
				System.out.println("scheduleFixedDelayTask Start Time : " + sf.format(new Date()));
				log.info("scheduleFixedDelayTask Start Time : " + sf.format(new Date()));

				String startDayTime = this.getStartDay(10);
				String startWeekTime = this.getStartDay(28);
				String startMonthTime = this.getStartDay(90);
				String startYearTime = this.getStartDay(365);
				String end = getFormat(new Date());

				Date startSH = new Date();
				logs("Start Sechduler............");
				// 1. /api/audit/dashboard/auto
				// Start Home Transation page
				startLogs("/api/audit/dashboard|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);

				this.apiAuditService.getDashBoard(db, "auto", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "auto", "WEEKS", startWeekTime, end, true);
				endLogs("/api/audit/dashboard|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "auto", "MONTHS", startMonthTime, end, true);
				endLogs("/api/audit/dashboard|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "auto", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "custom" + "DAYS" + "|" + startDayTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "custom", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard|" + "custom" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "custom" + "DAYS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "custom", "DAYS", startYearTime, end, true);
				endLogs("/api/audit/dashboard|" + "custom" + "DAYS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "custom" + "MONTHS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "custom", "MONTHS", startYearTime, end, true);
				endLogs("/api/audit/dashboard|" + "custom" + "MONTHS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "custom" + "WEEKS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "custom", "WEEKS", startYearTime, end, true);
				endLogs("/api/audit/dashboard|" + "custom" + "WEEKS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard|" + "custom" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoard(db, "custom", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard|" + "custom" + "YEARS" + "|" + startYearTime + "|" + end);

				// End Home Transaction page

				/// 2. start api/audit/dashboard/lookup

				/// End /api/audit/dashboard/seller

				/// 3. start api/audit/dashboard/buyer

				startLogs("/api/audit/dashboard/buyer|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);
				this.apiAuditService.getDashBoardBuyerId(db, "auto", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard/buyer|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard/buyer|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);
				this.apiAuditService.getDashBoardBuyerId(db, "auto", "WEEKS", startWeekTime, end, true);
				endLogs("/api/audit/dashboard/buyer|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);

				startLogs("/api/audit/dashboard/buyer|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);
				this.apiAuditService.getDashBoardBuyerId(db, "auto", "MONTHS", startMonthTime, end, true);
				endLogs("/api/audit/dashboard/buyer|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);

				startLogs("/api/audit/dashboard/buyer|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardBuyerId(db, "auto", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/buyer|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/grid/buyer|" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardBuyerAllList(db, startYearTime, end, true);
				endLogs("/api/audit/dashboard/grid/buyer|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				/// End /api/audit/dashboard/buyer

				/// 4 start /api/audit/dashboard/seller

				startLogs("/api/audit/dashboard/seller|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);
				this.apiAuditService.getDashBoardSellerId(db, "auto", "DAYS", startDayTime, end, true);
				endLogs("/api/audit/dashboard/seller|" + "auto" + "DAYS" + "|" + startDayTime + "|" + end);

				startLogs("/api/audit/dashboard/seller|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);
				this.apiAuditService.getDashBoardSellerId(db, "auto", "WEEKS", startWeekTime, end, true);
				endLogs("/api/audit/dashboard/seller|" + "auto" + "WEEKS" + "|" + startWeekTime + "|" + end);

				startLogs("/api/audit/dashboard/seller|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);
				this.apiAuditService.getDashBoardSellerId(db, "auto", "MONTHS", startMonthTime, end, true);
				endLogs("/api/audit/dashboard/seller|" + "auto" + "MONTHS" + "|" + startMonthTime + "|" + end);

				startLogs("/api/audit/dashboard/seller|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardSellerId(db, "auto", "YEARS", startYearTime, end, true);
				endLogs("/api/audit/dashboard/seller|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				startLogs("/api/audit/dashboard/grid/seller|" + startYearTime + "|" + end);
				this.apiAuditService.getDashBoardSellerAllList(db, startYearTime, end, true);
				endLogs("/api/audit/dashboard/grid/seller|" + "auto" + "YEARS" + "|" + startYearTime + "|" + end);

				// End /api/audit/dashboard/seller

				logs("End Sechduler............");
				logs("Completed in " + ((new Date().getTime() - startSH.getTime()) / 1000) + " Sec");
				logs("--------------------------------------------------------");
				System.out.println("scheduleFixedDelayTask End Time : " + sf.format(new Date()));
				log.info("scheduleFixedDelayTask End Time : " + sf.format(new Date()));

				flag.put(db, false);
			} else {
				System.out.println("Alredy Running");
			}
		} catch (Exception e) {

			flag.put(db, false);
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

	@Async
	@Scheduled(cron = "${cron.summary.report}")
	public void recursiveDeleteFilesOlder() throws IOException {
		log.info("Enter in recursiveDeleteFilesOlder() of Scheduler ");
		SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		System.out.println("recursiveDeleteFilesOlder Start Time:" + sf.format(new Date()));
		log.info("recursiveDeleteFilesOlder Start Time:" + sf.format(new Date()));
		// Converting directories list to Array
		List<String> dirFiles = new ArrayList<String>();

		File folder = new File(dirPath);
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				dirFiles.add(fileEntry.getName());
			} else {
				continue;
			}
		}
		log.info("Summery report directories : " + dirFiles.toString());
		List<String> stringTime = dirFiles.stream().map(x -> x.split("-")[0]).collect(Collectors.toList());

		// Shows current Date and Time
		LocalDateTime currentTime = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

		// Converting String to date
		List<LocalDateTime> dates = stringTime.stream().map(str -> LocalDateTime
				.ofInstant(Instant.ofEpochMilli(Long.parseLong(str)), TimeZone.getDefault().toZoneId())).sorted()
				.collect(Collectors.toList());

		// Make the list of directories which is older than 12 hrs
		List<LocalDateTime> deleteFiles = dates.stream().filter(
				x -> Long.valueOf(12) < ChronoUnit.HOURS.between(x, LocalDateTime.parse(currentTime.toString())))
				.collect(Collectors.toList());

		// Converting deletefile list to String
		List<String> delFf = deleteFiles.stream()
				.map(x -> String.valueOf(ZonedDateTime.of(x, ZoneId.systemDefault()).toInstant().toEpochMilli()))
				.collect(Collectors.toList());

		// make the list of directories to convert into fullname of directories
		List<String> finalDeleteDir = dirFiles.stream().filter(element -> delFf.contains(element.split("-")[0]))
				.collect(Collectors.toList());

		// Delete the directories
		finalDeleteDir.stream().forEach(x -> {
			try {
				FileUtils.deleteDirectory(new File(dirPath + "/" + x));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		log.info("Directory deleted  successfully");
		log.info("Exit from recursiveDeleteFilesOlder() of Scheduler ");
		System.out.println("recursiveDeleteFilesOlder Start Time:" + sf.format(new Date()));
		log.info("recursiveDeleteFilesOlder Start Time:" + sf.format(new Date()));
	}

	// buyer scheduler count

	@Scheduled(cron = "${cron.dashboard.insert.seller}")
	public void scheduleFixedInsertSellerSchdulerMubai() {
		scheduleFixedInsertSellerSchduler(ApiAuditDBTypeEnum.apiAuditMumbai, false);

	}

	@Async(value = "commonExecutor")
	public void scheduleFixedInsertSellerSchduler(ApiAuditDBTypeEnum db, boolean force) {
		try {
			log.info("Start scheduleFixedInsertSellerSchduler : " + db.toString());

			this.apiAuditService.apiAuditinsertSeller(db, force);

			log.info("End scheduleFixedInsertSellerSchduler : " + db.toString());
		} catch (Exception e) {

			flag.put(db, false);
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Scheduled(cron = "${cron.dashboard.insert.buyer}")
	public void scheduleFixedInsertBuyerSchdulerMubai() {
		scheduleFixedInsertBuyerSchduler(ApiAuditDBTypeEnum.apiAuditMumbai, false);

	}

	public void scheduleFixedInsertBuyerSellerSchduleALL() {
		scheduleFixedInsertBuyerSchduler(ApiAuditDBTypeEnum.apiAuditMumbai, true);
		scheduleFixedInsertBuyerSchduler(ApiAuditDBTypeEnum.apiAuditMumbai, true);
	}

	@Async(value = "commonExecutor")
	public void scheduleFixedInsertBuyerSchduler(ApiAuditDBTypeEnum db, boolean force) {
		try {
			log.info("Start scheduleFixedInsertBuyerSchduler : " + db.toString());

			this.apiAuditService.apiAuditinsertBuyer(db, force);

			log.info("Complete scheduleFixedInsertBuyerSchduler  : " + db.toString());
		} catch (Exception e) {

			flag.put(db, false);
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
