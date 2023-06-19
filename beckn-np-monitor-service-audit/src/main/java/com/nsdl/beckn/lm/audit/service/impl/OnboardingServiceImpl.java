package com.nsdl.beckn.lm.audit.service.impl;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.lm.audit.cache.CachingService;
import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.dao.impl.ApiAuditDbRepository;
import com.nsdl.beckn.lm.audit.event.EventPublisher;
import com.nsdl.beckn.lm.audit.event.FileEvent;
import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.FileBo;
import com.nsdl.beckn.lm.audit.model.Graph;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityProjecttion;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;
import com.nsdl.beckn.lm.audit.model.projection.CountSellerBuyerProjection;
import com.nsdl.beckn.lm.audit.model.response.TransactionMapResponse;
import com.nsdl.beckn.lm.audit.service.ApiAuditService;
import com.nsdl.beckn.lm.audit.utl.CommonUtl;
import com.nsdl.beckn.lm.audit.utl.Constants;
import com.nsdl.beckn.lm.audit.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class OnboardingServiceImpl implements ApiAuditService, ApplicationContextAware {

	@Value("${com.nsdl.logs.monitor.years}")
	String years;
	@Value("${com.nsdl.logs.monitor.months}")
	String months;
	@Value("${com.nsdl.logs.monitor.weeks}")
	String weeks;
	@Value("${com.nsdl.logs.monitor.days}")
	String days;

	@Value("${summary.report.dir}")
	String summaryReportDir;

	@Value("${page.web.size.transation}")
	Integer pageSize;

	@Value("${page.size.lookup}")
	Integer pageSizeL;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	CachingService cachingService;
	@Autowired
	ApiAuditDbRepository apiAuditRepository;

	private ApplicationContext applicationContext;

	@Autowired
	EventPublisher eventpublisher;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Map<String, List<Map<String, Object>>> getDefaultMap() {
		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		map.put(Constants.Buyer_to_Gateway, new ArrayList<Map<String, Object>>());
		map.put(Constants.Gateway_to_Seller, new ArrayList<Map<String, Object>>());
		map.put(Constants.Seller_to_Gateway, new ArrayList<Map<String, Object>>());
		map.put(Constants.Gateway_to_Buyer, new ArrayList<Map<String, Object>>());
		return map;
	}

	public Map<String, List<Map<String, Object>>> getTransationList(List<ApiAuditParentEntity> list) {
		Map<String, List<Map<String, Object>>> map = getDefaultMap(); // TransationCheck chk = new TransationCheck();

		Map<String, Boolean> mapFlag = new HashMap<>();
		Map<String, Map<String, Object>> lastRecord = new HashMap<>();
		list.forEach(item -> {

			if (Constants.REQUEST_BY_BUYER.equals(item.getType()) || Constants.ACK_TO_BUYER.equals(item.getType())
					|| Constants.NACK_TO_BUYER.equals(item.getType())) {

				if (Constants.REQUEST_BY_BUYER.equals(item.getType())) {
					Map<String, Object> record = lastRecord
							.get(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId());

					if (record == null) {
						record = new HashMap<>();
					}

					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_Search_received_timestamp, item.getCreatedOn());
					record.put(Constants.col_Buyer_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_sender_rec_id, item.getId());
					record.put(Constants.col_json, item.getId());

					lastRecord.put(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId(), record);

					map.get(Constants.Buyer_to_Gateway).add(record);
				} else {
					Map<String, Object> record = lastRecord
							.get(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId());
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag
							.get(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId()) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Buyer_to_Gateway).add(record);
					}
					record.put(Constants.col_Ack_Returned_Timestamp, item.getCreatedOn());
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_ack_rec_id, item.getId());
					record.put(Constants.col_error, item.getId());

					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId(), record);

					mapFlag.put(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId(), true);

				}
			} else if (Constants.REQUEST_TO_SELLER.equals(item.getType())
					|| Constants.ACK_BY_SELLER.equals(item.getType()) || Constants.NACK_BY_SELLER.equals(item.getType())
					|| Constants.INVALID_RESPONSE_BY_SELLER.equals(item.getType())
					|| Constants.BLOCKED_SELLER.equals(item.getType())
					|| Constants.ERROR_CALLING_SELLER.equals(item.getType())) {
//tran + buyeid + messagid+action
				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Gateway_to_Seller + item.getMessageId();
				if (Constants.REQUEST_TO_SELLER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Search_received_timestamp, item.getCreatedOn());
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getId());

					lastRecord.put(key, record);
					map.get(Constants.Gateway_to_Seller).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}

					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Gateway_to_Seller).add(record);
					}
					record.put(Constants.col_error, item.getId());
					record.put(Constants.col_Ack_Returned_Timestamp, item.getCreatedOn());
					record.put(Constants.col_Seller_Status, item.getType());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					mapFlag.put(key, true);

				}
			} else if (Constants.RESPONSE_BY_SELLER.equals(item.getType())
					|| Constants.ACK_TO_SELLER.equals(item.getType())
					|| Constants.NACK_TO_SELLER.equals(item.getType())) {

				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Seller_to_Gateway + item.getMessageId();

				if (Constants.RESPONSE_BY_SELLER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Search_received_timestamp, item.getCreatedOn());
					record.put(Constants.col_Seller_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getId());

					lastRecord.put(key, record);
					map.get(Constants.Seller_to_Gateway).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Seller_to_Gateway).add(record);
					}
					record.put(Constants.col_error, item.getId());
					record.put(Constants.col_Ack_Returned_Timestamp, item.getCreatedOn());
					record.put(Constants.col_Gatway_Status, item.getType());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					mapFlag.put(key, true);

				}
			} else if (Constants.RESPONSE_TO_BUYER.equals(item.getType())
					|| Constants.NACK_BY_BUYER.equals(item.getType()) || Constants.ACK_BY_BUYER.equals(item.getType())
					|| Constants.ERROR_CALLING_BUYER.equals(item.getType())
					|| Constants.INVALID_RESPONSE_BY_BUYER.equals(item.getType())
					|| Constants.BLOCKED_BUYER.equals(item.getType())) {
				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Gateway_to_Buyer + item.getMessageId();

				if (Constants.RESPONSE_TO_BUYER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_Search_received_timestamp, item.getCreatedOn());
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_json, item.getId());

					lastRecord.put(key, record);
					map.get(Constants.Gateway_to_Buyer).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Gateway_to_Buyer).add(record);
					}
					record.put(Constants.col_error, item.getId());
					record.put(Constants.col_Ack_Returned_Timestamp, item.getCreatedOn());
					record.put(Constants.col_Buyer_Status, item.getType());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					mapFlag.put(key, true);

				}
			} else {
				System.out.println("Not Found:" + item.getTransactionId() + ":" + item.getType());
			}

		});

		return map;

	}

//	public ApiAuditParentRepository getApiRepositoryInstence(ApiAuditDBTypeEnum db){
//		if(db.toString().indexOf("ArchvlTmp")!=0) {
//			return this.apiAuditArchvlTmpRepository;
//		}else if(db.toString().indexOf("Archvl")!=0) {
//			return this.apiAuditArchvlRepository;
//		}else {
//			return this.apiAuditRepository;
//		}
//	}

	@Override
	public Map<String, List<Map<String, Object>>> findTransactionById(ApiAuditDBTypeEnum db, String id) {
		// TODO Auto-generated method stub
		// ApiAuditDBContextHolder.setCurrentDb(db);
		List<ApiAuditParentEntity> list = this.apiAuditRepository.findByTransactionIdOrderByCreatedOnAsc(db, id);
		return getTransationList(list);
	}

	@Override
	public TransactionMapResponse findTransactionByDate(ApiAuditDBTypeEnum db, String start, String end, Integer page) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		TransactionMapResponse transactionMapResponse = new TransactionMapResponse();
		transactionMapResponse.setPageSize(pageSize);

		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
		List<String> ids = this.apiAuditRepository.findDistinctTransactionIdByCreatedOnBetween(db, startDt, endDt);// ,pageable
		ids = ids.stream().distinct().collect(Collectors.toList());
		transactionMapResponse.setRecords(ids.size());
		int size = (page) * pageSize;
		int startPage = (page - 1) * pageSize;
		int endPage = size < ids.size() ? size : ids.size();
		if (endPage > startPage) {
			List<String> newIds = ids.subList(startPage, endPage);

			List<ApiAuditParentEntity> list = this.apiAuditRepository.findByTransactionIdInOrderByCreatedOnAsc(db,
					newIds);// ,pageable
			transactionMapResponse.setMap(getTransationList(list));
			return transactionMapResponse;
		}
		transactionMapResponse.setMap(getDefaultMap());
		return transactionMapResponse;
	}

	@Override
	public List<String> getDistinctId(ApiAuditDBTypeEnum db) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		return this.apiAuditRepository.getDistinctId(db);
	}

	@Override
	public List<Graph<Integer>> getDashBoard(ApiAuditDBTypeEnum db, String type) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		List<Graph<Integer>> mapAL = new ArrayList<>();
		List<LocalDateTime> al = null;
		if (Constants.dash_type_WEEKS.equals(type)) {
			al = CommonUtl.getLastWeeks(LocalDateTime.now(), Integer.parseInt(this.weeks));
		} else if (Constants.dash_type_MONTHS.equals(type)) {
			al = CommonUtl.getLastMonths(LocalDateTime.now(), Integer.parseInt(this.months));
		} else if (Constants.dash_type_YEARS.equals(type)) {
			al = CommonUtl.getLastYears(LocalDateTime.now(), Integer.parseInt(this.years));
		} else { // days
			al = CommonUtl.getLastDays(LocalDateTime.now(), Integer.parseInt(this.days));
		}
		for (int i = 0; i < al.size(); i += 2) {
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
			mapAL.add(new Graph<>(x, this.apiAuditRepository.countByCreatedOnBetween(db, al.get(i), al.get(i + 1))));
		}
		return mapAL;
	}

	@Override
	public List<Graph<Integer>> getDashBoard(ApiAuditDBTypeEnum db, String select, String type, String start,
			String end, boolean force) {
		//// ApiAuditDBContextHolder.setCurrentDb(db);
		String key = "getDashBoard|" + select + "|" + type + "|" + start + "|" + end;
		if (force) {

			List<Graph<Integer>> mapAL = new ArrayList<>();
			List<LocalDateTime> al = null;
			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			startDt = CommonUtl.setSatrtDate(startDt, select, type);
			if (Constants.dash_type_WEEKS.equals(type)) {
				al = CommonUtl.getLastWeeksWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_MONTHS.equals(type)) {
				al = CommonUtl.getLastMonthsWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_YEARS.equals(type)) {
				al = CommonUtl.getLastYearsWithEndDated(startDt, endDt);
			} else { // days
				al = CommonUtl.getLastDays(endDt, Integer.parseInt(this.days));
			}
			Date dt = new Date();
			List<ApiAuditEntityProjecttion> list = this.apiAuditRepository.selectByCreatedOnBetween(db, startDt, endDt);
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
				// List<ApiAuditEntityProjecttion> remove = new ArrayList();

				for (ApiAuditEntityProjecttion element : list) {
					if (element.getCreatedOn().isAfter(al.get(i)) && element.getCreatedOn().isBefore(al.get(i + 1))) {
						// remove.add(list.get(i));
						cnt++;
					}

				}

				mapAL.add(new Graph<>(x, cnt));

			}

			this.cachingService.putToCache(db, key, mapAL);
			return mapAL;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			return (List<Graph<Integer>>) this.cachingService.getFromCache(db, key);
		}

	}

	public Map<String, Integer> getBuyerSellerMap(List<ApiAuditParentEntity> list, boolean buyer) {

		Map<String, Integer> mapInt = new HashMap<>();
		list.forEach(item -> {
			if (buyer) {
				if (mapInt.get(item.getBuyerId()) == null) {
					mapInt.put(item.getBuyerId(), 0);
				}

				mapInt.put(item.getBuyerId(), mapInt.get(item.getBuyerId()) + 1);

			} else {

				if (mapInt.get(item.getSellerId()) == null) {
					mapInt.put(item.getSellerId(), 0);
				}

				mapInt.put(item.getSellerId(), mapInt.get(item.getSellerId()) + 1);

			}
		});
		return mapInt;

	}

	public List<Map<String, Object>> findByBuyerSellerTransactionByDate(ApiAuditDBTypeEnum db, String typeSearch,
			String typeOnSerarch, String start, String end, String type) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);

		List<ApiAuditParentEntity> list = this.apiAuditRepository
				.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(db, typeSearch, Constants.action_search,
						startDt, endDt);
		boolean buyer = Constants.REQUEST_BY_BUYER.equals(typeSearch);
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, Integer> mapSearchInt = getBuyerSellerMap(list, buyer);
		list = this.apiAuditRepository.findByTypeAndActionAndCreatedOnBetweenOrderByCreatedOnAsc(db, typeOnSerarch,
				Constants.action_on_search, startDt, endDt);
		Map<String, Integer> mapOnSearchInt = getBuyerSellerMap(list, buyer);

		mapSearchInt.entrySet().forEach(item -> {
			Map<String, Object> obj = new HashMap<>();
			mapList.add(obj);
			obj.put(Constants.action_search, item.getValue());
			obj.put(Constants.action_on_search,
					mapOnSearchInt.get(item.getKey()) == null ? 0 : mapOnSearchInt.get(item.getKey()));
			obj.put("name", item.getKey());
		});
		return mapList;
	}

	@Override

	public List<Map<String, Object>> findByBuyerSellerTransactionByDate(ApiAuditDBTypeEnum db, String type,
			String start, String end, boolean flag) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		String key = "findByBuyerSellerTransactionByDate|" + type + "|" + start + "|" + end;
		if (flag) {
			List<Map<String, Object>> ret = null;
			if (type.equals("buyer")) {
				ret = this.findByBuyerSellerTransactionByDate(db, Constants.REQUEST_BY_BUYER, Constants.ACK_TO_BUYER,
						start, end, type);
			} else if (type.equals("seller")) {
				ret = this.findByBuyerSellerTransactionByDate(db, Constants.REQUEST_TO_SELLER,
						Constants.RESPONSE_BY_SELLER, start, end, type);
			}

			this.cachingService.putToCache(db, key, ret);
			return ret;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			return (List<Map<String, Object>>) this.cachingService.getFromCache(db, key);

		}
	}

	@Override

	public String summaryReportExportToExcel(ApiAuditDBTypeEnum db, String start, String end) {
		// LOGGER.trace("Starting summaryReportExportToExcel () from
		// OnboardingServiceImpl with arguments:: no arg ");
		// ApiAuditDBContextHolder.setCurrentDb(db);
		// event publisher
		String userId = this.securityUtils.getUserDetails().getId();
		FileEvent event = new FileEvent(start, end, userId);
		event.setDate(String.valueOf(new Date().getTime()));
		eventpublisher.PublishFileEvent(event);
		// String publisher = new File ("file is under process")+date+userId;

		String Fname = "file is under process.....-" + event.getDate() + "-" + event.getUserId();
		return Fname;
	}

	@Override
	public List<ApiAuditEntitySellerProjection> getDashBoardSellerAllList(ApiAuditDBTypeEnum db, String start,
			String end, boolean flag) {
		String key = "getDashBoardSellerAllList|" + start + "|" + end;
		// ApiAuditDBContextHolder.setCurrentDb(db);
		if (flag) {

			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			List<ApiAuditEntitySellerProjection> listAll = new ArrayList<>();
			listAll = this.apiAuditRepository.selectBySellerCreatedOnBetween(db, startDt, endDt);
			this.cachingService.putToCache(db, key, listAll);
			return listAll;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			return (List<ApiAuditEntitySellerProjection>) this.cachingService.getFromCache(db, key);

		}
	}

	@Override
	public List<Graph<Integer>> getDashBoardSellerId(ApiAuditDBTypeEnum db, String select, String type, String start,
			String end, boolean flag) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		String key = "getDashBoardSellerId|" + select + "|" + type + "|" + start + "|" + end;
		if (flag) {

			List<Graph<Integer>> mapAL = new ArrayList<>();
			List<LocalDateTime> al = null;
			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			startDt = CommonUtl.setSatrtDate(startDt, select, type);
			if (Constants.dash_type_WEEKS.equals(type)) {
				al = CommonUtl.getLastWeeksWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_MONTHS.equals(type)) {
				al = CommonUtl.getLastMonthsWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_YEARS.equals(type)) {
				al = CommonUtl.getLastYearsWithEndDated(startDt, endDt);
			} else { // days
				al = CommonUtl.getLastDays(endDt, Integer.parseInt(this.days));
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
				List<ApiAuditEntitySellerProjection> list = this.apiAuditRepository.selectBySellerCreatedOnBetween(db,
						al.get(i), al.get(i + 1));

				for (ApiAuditEntitySellerProjection element : list) {

					cnt += element.getCount();
				}

				mapAL.add(new Graph<>(x, cnt));

			}

			this.cachingService.putToCache(db, key, mapAL);
			return mapAL;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			List<Graph<Integer>> list = (List<Graph<Integer>>) this.cachingService.getFromCache(db, key);
			return list;
		}

	}

	@Override

	public List<ApiAuditEntityBuyerProjection> getDashBoardBuyerAllList(ApiAuditDBTypeEnum db, String start, String end,
			boolean flag) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		String key = "getDashBoardBuyerAllList|" + start + "|" + end;
		if (flag) {

			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			List<ApiAuditEntityBuyerProjection> listAll = new ArrayList<>();
			listAll = this.apiAuditRepository.selectByBuyerCreatedOnBetween(db, startDt, endDt);

			this.cachingService.putToCache(db, key, listAll);
			return listAll;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			return (List<ApiAuditEntityBuyerProjection>) this.cachingService.getFromCache(db, key);
		}
	}

	@Override

	public List<Graph<Integer>> getDashBoardBuyerId(ApiAuditDBTypeEnum db, String select, String type, String start,
			String end, boolean flag) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		String key = "getDashBoardBuyerId|" + select + "|" + type + "|" + start + "|" + end;
		if (flag) {

			List<Graph<Integer>> mapAL = new ArrayList<>();
			List<LocalDateTime> al = null;
			LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
			LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
			startDt = CommonUtl.setSatrtDate(startDt, select, type);
			if (Constants.dash_type_WEEKS.equals(type)) {
				al = CommonUtl.getLastWeeksWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_MONTHS.equals(type)) {
				al = CommonUtl.getLastMonthsWithEndDate(startDt, endDt);
			} else if (Constants.dash_type_YEARS.equals(type)) {
				al = CommonUtl.getLastYearsWithEndDated(startDt, endDt);
			} else { // days
				al = CommonUtl.getLastDays(endDt, Integer.parseInt(this.days));
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
				// List<ApiAuditEntityProjecttion> remove = new ArrayList();
				List<ApiAuditEntityBuyerProjection> list = this.apiAuditRepository.selectByBuyerCreatedOnBetween(db,
						al.get(i), al.get(i + 1));
				// System.out.println(list.size());

				for (ApiAuditEntityBuyerProjection element : list) {

					cnt += element.getCount();

				}

				mapAL.add(new Graph<>(x, cnt));

			}

			this.cachingService.putToCache(db, key, mapAL);
			return mapAL;
		} else {
			if ((this.cachingService.getFromCache(db, key) == null)) {
				return new ArrayList();
			}
			return (List<Graph<Integer>>) this.cachingService.getFromCache(db, key);
		}
	}

	@Override
	public List<String> getTransationDtl(ApiAuditDBTypeEnum db, String jsonId, String errorId) {
		List<String> ret = new ArrayList();
		// ApiAuditDBContextHolder.setCurrentDb(db);
		if (!"undefined".equals(jsonId)) {
			ApiAuditParentEntity json = (ApiAuditParentEntity) this.apiAuditRepository.findById(db, jsonId);
			if (json != null && json.getJson() != null) {
				ret.add(json.getJson());
			} else {
				ret.add("");
			}
		} else {
			ret.add("");
		}
		if (!"undefined".equals(errorId)) {
			ApiAuditParentEntity error = (ApiAuditParentEntity) this.apiAuditRepository.findById(db, errorId);
			if (error != null && error.getJson() != null) {
				ret.add(error.getError());
			} else {
				ret.add("");
			}
		} else {
			ret.add("");
		}
		return ret;
	}

	@Override
	public Map<String, List<String>> getAllFiles(ApiAuditDBTypeEnum db, String fileName) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		log.info("Inside getAllFiles() for OnboardingServiceImpl class inputs :" + fileName);
		Map<String, List<String>> dirFilesMap = new HashMap<String, List<String>>();
		File directoryPath = new File(summaryReportDir);
		log.info("Directory Path:" + directoryPath.toString());
//		for (File dirEntry : directoryPath.listFiles()) {
		// if (dirEntry.getName().equals(fileName)) {
		File files = new File(summaryReportDir + "/" + fileName);
		List<String> dirFiles = new ArrayList<String>();
		for (File fileEntry : files.listFiles()) {
			if (fileEntry.isFile()) {
				dirFiles.add(fileEntry.getName());
			}
		}
		dirFilesMap.put(fileName, dirFiles);
//			}
//		}
		log.info("Exiting getAllFiles() for OnboardingServiceImpl class inputs :" + dirFilesMap.toString());
		return dirFilesMap;
	}

	@Override
	public FileBo getDownloadFilesByName(ApiAuditDBTypeEnum db, String dirName, String fileName) {
		// ApiAuditDBContextHolder.setCurrentDb(db);
		log.info("Inside getDownloadFilesByName() for OnboardingServiceImpl class inputs :" + dirName + "and"
				+ fileName);
		FileBo bo = new FileBo();
		String zipFilePath = summaryReportDir + '/' + dirName + "/" + fileName;
		log.info("Directory Path:" + zipFilePath);
		try {
			byte[] fileByteArray = java.nio.file.Files.readAllBytes(Paths.get(zipFilePath));
			log.info("File to Byte array :" + fileByteArray.toString());
			bo.setBlob(Base64.getEncoder().encodeToString(fileByteArray));
			bo.setFileName(fileName);
			bo.setFileType(".zip");

		} catch (Exception e) {
			log.error("Exception : File not found");
			e.printStackTrace();
		}
		log.info("Exiting getDownloadFilesByName() for OnboardingServiceImpl class reponse :" + bo.toString());
		return bo;
	}

	 
	
	@Override
	public void apiAuditinsertSeller(ApiAuditDBTypeEnum db, boolean force) {
		 this.apiAuditRepository.apiAuditinsertSeller(db,force);
	}
 
	@Override
	public void apiAuditinsertBuyer(ApiAuditDBTypeEnum db, boolean force) {
		 this.apiAuditRepository.apiAuditinsertBuyer(db,force);
	}

	@Override
	public  List <CountSellerBuyerProjection> getBuyerList(ApiAuditDBTypeEnum db, LocalDateTime start,LocalDateTime end ) {
		return this.apiAuditRepository.getBuyerList(db,start,end);
	}
	
	@Override
	public  List <CountSellerBuyerProjection> getSellerList(ApiAuditDBTypeEnum db, LocalDateTime start,LocalDateTime end ) {
		return this.apiAuditRepository.getSellerList(db,start,end);
	}
}
