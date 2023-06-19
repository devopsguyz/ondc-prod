package com.nsdl.beckn.lm.registry.service.impl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.lm.registry.cache.CachingService;
import com.nsdl.beckn.lm.registry.dao.NPApiLogsRepository;
import com.nsdl.beckn.lm.registry.model.Graph;
import com.nsdl.beckn.lm.registry.model.NPApiLogs;
import com.nsdl.beckn.lm.registry.model.NPApiLogsDate;
import com.nsdl.beckn.lm.registry.model.request.LogsRequest;
import com.nsdl.beckn.lm.registry.model.response.LogsResponse;
import com.nsdl.beckn.lm.registry.model.response.LookupMapResponse;
import com.nsdl.beckn.lm.registry.model.response.LookupResponse;
import com.nsdl.beckn.lm.registry.service.ApiAuditService;
import com.nsdl.beckn.lm.registry.utl.CommonUtl;
import com.nsdl.beckn.lm.registry.utl.Constants;
import com.nsdl.beckn.lm.registry.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OnboardingServiceImpl implements ApiAuditService, ApplicationContextAware {

	@Value("${com.nsdl.logs.monitor.years}")
	String years;
	@Value("${com.nsdl.logs.monitor.months}")
	String months;
	@Value("${com.nsdl.logs.monitor.weeks}")
	String weeks;
	@Value("${com.nsdl.logs.monitor.days}")
	String days;
 

	@Value("${page.web.size.transation}")
	Integer pageSize;

	@Value("${page.size.lookup}")
	Integer pageSizeL;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	CachingService cachingService;

	@Autowired
	NPApiLogsRepository apiLogsRepository;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override

	public List<Graph<Integer>> getDashBoardLookup(String select, String type, String start, String end, boolean flag) {
		String key = "getDashBoardLookup|" + select + "|" + type + "|" + start + "|" + end;
		if (flag ) {

			List<Graph<Integer>> mapAL = new ArrayList<>();
			List<OffsetDateTime> al = null;
			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			startDt = CommonUtl.setSatrtDate(startDt, select, type);
			if (Constants.dash_type_WEEKS.equals(type)) {
				al = CommonUtl.convertOffsetToLocalDateTime(CommonUtl.getLastWeeksWithEndDate(startDt, endDt));
			} else if (Constants.dash_type_MONTHS.equals(type)) {
				al = CommonUtl.convertOffsetToLocalDateTime(CommonUtl.getLastMonthsWithEndDate(startDt, endDt));
			} else if (Constants.dash_type_YEARS.equals(type)) {
				al = CommonUtl.convertOffsetToLocalDateTime(CommonUtl.getLastYearsWithEndDated(startDt, endDt));
			} else { // days
				al = CommonUtl.convertOffsetToLocalDateTime(CommonUtl.getLastDays(endDt, Integer.parseInt(this.days)));
			}
			Date dt = new Date();
			
			System.out.println("time db:" + (new Date().getTime() - dt.getTime()));
			for (int i = 0; i < al.size(); i += 2) {
				Date dt1 = new Date();
				String x = "";
				if (Constants.dash_type_WEEKS.equals(type)) {
					x = String.valueOf((i / 2) + 1);
				} else if (Constants.dash_type_MONTHS.equals(type)) {
					x = al.get(i).getMonth().name();
				} else if (Constants.dash_type_YEARS.equals(type)) {
					x = String.valueOf(al.get(i).getYear());
				} else { // days
					x = String.valueOf(al.get(i).getDayOfMonth());
				}
				Integer cnt = 0;

				cnt = this.apiLogsRepository.findByTypeInAndCreatedDateBetweenOrderByCreatedDateAsc(
					Arrays.asList(Constants.type_lookup,Constants.type_vlookup),  al.get(i),
						al.get(i + 1));
				
//				for (NPApiLogsDate element : list) {
//					if (element.getCreatedDate().isAfter(al.get(i))
//							&& element.getCreatedDate().isBefore(al.get(i + 1))) {
//						// remove.add(list.get(i));
//						cnt++;
//					}
//
//				}

				mapAL.add(new Graph<>(x, cnt));
			}
			this.cachingService.putToCache(key, mapAL);
			return mapAL;
		} else {
			if(this.cachingService.getFromCache(key) == null) {
				return new ArrayList();
			}
			return (List<Graph<Integer>>) this.cachingService.getFromCache(key);

		}

	}

	@Override
	public List<LogsResponse> findLogsBySubscriberId(LogsRequest body) {

		List<NPApiLogs> listI = this.apiLogsRepository
				.findTop100BySubscriberIdAndTypeOrderByCreatedDateDesc(body.getSubsciberId(), Constants.type_subscribe);

		List<LogsResponse> list = new ArrayList<>();
		listI.forEach(item -> {
			list.add(new LogsResponse(item));
		});
		return list;
	}

	  
	@Override
	public LookupMapResponse findLookupByType(String type, String select, String start, String end, Integer page) {
		PageRequest pageRequest = PageRequest.of(page, pageSizeL, Sort.by(Direction.ASC, "createdDate"));
		LookupMapResponse mapResponse = new LookupMapResponse();

		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
		startDt = CommonUtl.setSatrtDate(startDt, select, type);
		OffsetDateTime startDate = CommonUtl.convertOffsetToLocalDateTime(startDt);
		OffsetDateTime endDate = CommonUtl.convertOffsetToLocalDateTime(endDt);

		Page<NPApiLogs> resultPage = this.apiLogsRepository.findByTypeAndCreatedDateBetween(type, startDate, endDate,
				pageRequest);
		if (resultPage.getSize() == 0) {
			try {

				resultPage = this.apiLogsRepository.findByTypeAndCreatedTmBetween(type, startDate, endDate,
						pageRequest);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		List<LookupResponse> listL = new ArrayList<>();

		for (NPApiLogs npApiLogs : resultPage.getContent()) {
			LookupResponse lookup = new LookupResponse(npApiLogs);
			listL.add(lookup);
		}

		Map<String, List<LookupResponse>> map = new HashMap<>();
		map.put(Constants.LOOKUP_CHANGES_DATA, listL);

		mapResponse.setMap(map);
		mapResponse.setPageSize(pageSizeL);
		mapResponse.setRecords(resultPage.getTotalElements());
		int size = mapResponse.getPageSize();
		return mapResponse;
	}

	@Override
	public List<String> findByType() {
		// TODO Auto-generated method stub
		return this.apiLogsRepository.findByType();
	}

	@Override
	public List<Map<String, Object>> getPerformance(String date) {
		log.info("Entered inside getPerformance() method with input date : " + date);

		List<Map<String, Object>> resList = new ArrayList<>(50);

		LocalDate startDt = CommonUtl.convertStartStringtoDate(date).toLocalDate();
		LocalDateTime startTime = LocalDateTime.of(startDt, LocalTime.MIN);
		LocalDateTime endTime = LocalDateTime.of(startDt, LocalTime.MAX);

		while (startTime.isBefore(endTime)) {

			Map<String, Object> resMap = new LinkedHashMap<String, Object>(3);

			String countRecords = apiLogsRepository.countRecordsInBetweenTime(startTime, startTime.plusMinutes(30));
			String avgResponseTime = apiLogsRepository.countRecordsResponseTimeAVg(startTime,
					startTime.plusMinutes(30));

			resMap.put("time", startTime.toString().substring(11));

			if (avgResponseTime == null) {
				avgResponseTime = "0 ms";
				resMap.put("avgResponseTime", avgResponseTime);
			} else {
				// milliseconds converting to seconds and minutes
				if (Integer.parseInt(avgResponseTime) >= 1000) {

					float seconds = Float.parseFloat(avgResponseTime) / 1000;
					if (seconds >= 60) {
						float minutes = seconds / 60;
						resMap.put("avgResponseTime", Float.valueOf(new DecimalFormat("#.##").format(minutes)) + " m");
					} else {
						resMap.put("avgResponseTime", Float.valueOf(new DecimalFormat("#.##").format(seconds)) + " s");
					}
				} else {
					resMap.put("avgResponseTime", avgResponseTime + " ms");
				}
			}

			resMap.put("countRecords", countRecords);

			resList.add(resMap);
			log.info("Fetching data between" + startTime + " AND " + startTime.plusMinutes(30) + ", Counts :"
					+ countRecords + ", ResponseTime : :" + avgResponseTime);
			startTime = startTime.plusMinutes(30);
		}

		log.info("Exiting from getPerformance() with Response : " + resList);
		return resList;
	}

}
