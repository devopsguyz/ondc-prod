package com.nsdl.beckn.klm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.klm.builder.ApiAuditParentEntityBuilder;
import com.nsdl.beckn.klm.dao.adapter.ApiAuditRepository;
import com.nsdl.beckn.klm.dao.adapter.TransationCheck;
import com.nsdl.beckn.klm.dao.archvl.ApiAuditArchvlRepository;
import com.nsdl.beckn.klm.dao.archvltemp.ApiAuditArchvlTempRepository;
import com.nsdl.beckn.klm.dto.ApiAuditParentEntityDTO;
import com.nsdl.beckn.klm.dto.TransactionDTO;
import com.nsdl.beckn.klm.model.ApiAuditParentEntity;
import com.nsdl.beckn.klm.model.adapter.ApiAuditEntity;
import com.nsdl.beckn.klm.model.archvl.ApiAuditArchvlEntity;
import com.nsdl.beckn.klm.model.archvltemp.ApiAuditArchvlTempEntity;
import com.nsdl.beckn.klm.service.ApiAuditService;
import com.nsdl.beckn.klm.utl.CommonUtl;
import com.nsdl.beckn.klm.utl.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class OnboardingServiceImpl implements ApiAuditService {

	@Value("${gateway.logs.id}")
	String subscriberId;

	@Autowired
	ApiAuditRepository apiAuditRepository;
	
	@Autowired
	ApiAuditArchvlRepository apiAuditArchvlRepository;
	
	@Autowired
	ApiAuditArchvlTempRepository apiAuditArchvlTempRepository;

	public void isValidId(String json, TransationCheck chk) {
		if (!chk.isFlag()) {
			chk.setFlag(json.toLowerCase().indexOf("\"" + this.subscriberId.toLowerCase() + "\"") != -1);
		}

	}

	public Map<String, List<Map<String, Object>>> getTransationList(List<ApiAuditParentEntityDTO> list) {
		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		map.put(Constants.Buyer_to_Gateway, new ArrayList<Map<String, Object>>());
		map.put(Constants.Gateway_to_Seller, new ArrayList<Map<String, Object>>());
		map.put(Constants.Seller_to_Gateway, new ArrayList<Map<String, Object>>());
		map.put(Constants.Adapter_to_Gateway, new ArrayList<Map<String, Object>>());
		map.put(Constants.Gateway_to_Adapter, new ArrayList<Map<String, Object>>());
		map.put(Constants.Adapter_to_Buyer, new ArrayList<Map<String, Object>>());
		// map.put(Constants.Gateway_to_Buyer, new ArrayList<Map<String, Object>>());
		TransationCheck chk = new TransationCheck();

		Map<String, Boolean> mapFlag = new HashMap<>();
		Map<String, Map<String, Object>> lastRecord = new HashMap<>();
		list.forEach(item -> {
			/*
			 * if (item.getJson() != null) { isValidId(item.getJson(), chk); }
			 */
			if (Constants.REQUEST_BY_BUYER.equals(item.getType()) || Constants.ACK_TO_BUYER.equals(item.getType())
					|| Constants.NACK_TO_BUYER.equals(item.getType())
					|| Constants.INVALID_RESPONSE_BY_BUYER.equals(item.getType())) {
				String key = item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId();
				if (Constants.REQUEST_BY_BUYER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Buyer_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_sender_rec_id, item.getId());
					record.put(Constants.col_json, item.getJson());

					lastRecord.put(key, record);

					map.get(Constants.Buyer_to_Gateway).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag
							.get(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId()) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Buyer_to_Gateway).add(record);
					}
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_ack_rec_id, item.getId());
					// record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					record.put(Constants.col_error, item.getError());
					lastRecord.put(key, record);
					mapFlag.put(item.getTransactionId() + Constants.Buyer_to_Gateway + item.getMessageId(), true);
					// lastRecord.remove(Constants.Gateway_to_Seller+item.getMessageId());

				}
			} else if (Constants.REQUEST_TO_SELLER.equals(item.getType())
					|| Constants.ACK_BY_SELLER.equals(item.getType()) || Constants.BLOCKED_SELLER.equals(item.getType())
					|| Constants.INVALID_RESPONSE_BY_SELLER.equals(item.getType())
					|| Constants.ERROR_CALLING_SELLER.equals(item.getType())) {
				// tran + buyeid + messagid+action
				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Gateway_to_Seller + item.getMessageId();
				if (Constants.REQUEST_TO_SELLER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getJson());

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
					record.put(Constants.col_error, item.getError());
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Seller_Status, item.getType());
					// record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					mapFlag.put(key, true);

					// lastRecord.remove(Constants.Gateway_to_Seller+item.getMessageId());

				}
			} else if (Constants.RESPONSE_BY_SELLER.equals(item.getType())
					|| Constants.NACK_TO_SELLER.equals(item.getType())
					|| Constants.ACK_TO_SELLER.equals(item.getType())) {

				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Seller_to_Gateway + item.getMessageId();

				if (Constants.RESPONSE_BY_SELLER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Seller_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getJson());
				 
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
					record.put(Constants.col_error, item.getError());
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					// record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					// lastRecord.remove(Constants.Seller_to_Gateway+item.getMessageId());
					mapFlag.put(key, true);

				}
			} else if (Constants.REQUEST_TO_GATEWAY.equals(item.getType())
					|| Constants.BLOCKED_SELLER.equals(item.getType())
					|| Constants.ACK_BY_GATEWAY.equals(item.getType())) {

				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Adapter_to_Gateway + item.getMessageId();

				if (Constants.REQUEST_TO_GATEWAY.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Seller_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getJson());

					lastRecord.put(key, record);
					map.get(Constants.Adapter_to_Gateway).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Adapter_to_Gateway).add(record);
					}
					record.put(Constants.col_error, item.getError());
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					// record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					// lastRecord.remove(Constants.Seller_to_Gateway+item.getMessageId());
					mapFlag.put(key, true);

				}
			}

			else if (Constants.RESPONSE_BY_GATEWAY.equals(item.getType())
					|| Constants.NACK_TO_GATEWAY.equals(item.getType())
					|| Constants.ACK_TO_GATEWAY.equals(item.getType())) {
//status
				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Gateway_to_Adapter + item.getMessageId();

				if (Constants.RESPONSE_BY_GATEWAY.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Seller_Status, item.getType());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_json, item.getJson());

					lastRecord.put(key, record);
					map.get(Constants.Gateway_to_Adapter).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Gateway_to_Adapter).add(record);
					}
					record.put(Constants.col_error, item.getError());
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					// record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					// lastRecord.remove(Constants.Seller_to_Gateway+item.getMessageId());
					mapFlag.put(key, true);

				}
			} else if (Constants.RESPONSE_TO_BUYER.equals(item.getType())
					|| Constants.NACK_BY_BUYER.equals(item.getType()) || Constants.ACK_BY_BUYER.equals(item.getType())
					|| Constants.ERROR_CALLING_BUYER.equals(item.getType())
					|| Constants.BLOCKED_BUYER.equals(item.getType())
					|| Constants.INVALID_RESPONSE_BY_BUYER.equals(item.getType())) {
				String key = item.getTransactionId() + ":" + item.getBuyerId() + ":" + item.getSellerId() + ":"
						+ item.getAction() + Constants.Adapter_to_Buyer + item.getMessageId();

				if (Constants.RESPONSE_TO_BUYER.equals(item.getType())) {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					record.put(Constants.col_Transaction_id, item.getTransactionId());
					record.put(Constants.col_Seller_Id, item.getSellerId());
					record.put(Constants.col_Buyer_Id, item.getBuyerId());
					record.put(Constants.col_message_id, item.getMessageId());
					record.put(Constants.col_Search_received_timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Gatway_Status, item.getType());
					record.put(Constants.col_json, item.getJson());

					lastRecord.put(key, record);
					map.get(Constants.Adapter_to_Buyer).add(record);
				} else {
					Map<String, Object> record = lastRecord.get(key);
					if (record == null) {
						record = new HashMap<>();
					}
					if (mapFlag.get(key) != null) {
						record = CommonUtl.mapClone(record);
						map.get(Constants.Adapter_to_Buyer).add(record);
					}
					record.put(Constants.col_error, item.getError());
					record.put(Constants.col_Ack_Returned_Timestamp,
							CommonUtl.convertDatetoString(item.getCreatedOn()));
					record.put(Constants.col_Buyer_Status, item.getType()); //
					record.put(Constants.col_ack_json, item.getJson());
					if (item.getType().indexOf("ACK_") != 0) {
						record.put(Constants.col_flag, true);
					}
					lastRecord.put(key, record);
					mapFlag.put(key, true); //
					lastRecord.remove(Constants.Adapter_to_Buyer + item.getMessageId());

				}
			}

			else {
				System.out.println(item.getTransactionId() + ":" + item.getType());
			}

		});
		/*
		 * if (chk.isFlag()) { return map; } else { return new HashMap<>(); }
		 */
		return map;
	}

	
	@Override
//	@Transactional(value = "adapterTransactionManager")
	public Map<String, List<Map<String, Object>>> findByActionAndTransactionId(String action, String id, String server) {
		List<ApiAuditParentEntityDTO> dtoList = null;
		if(server.equals("api_audit")) {
			List<ApiAuditEntity> list  = getAuditDetailsByAdapter(action, id);
			dtoList = ApiAuditParentEntityBuilder.aApiAuditParentEntityBuilder()
					.convertListOfApiAuditEntityToListApiAuditParentEntityDTO(list);
			 
		}else if (server.equals("api_audit_archvl")) {
			List<ApiAuditArchvlEntity> list = getAuditDetailsByArchvl(action, id);
			dtoList = ApiAuditParentEntityBuilder.aApiAuditParentEntityBuilder()
					.convertListOfApiAuditArchvlEntityToListApiAuditParentEntityDTO(list);
		}else if (server.equals("api_audit_archvl_tmp")) {
				List<ApiAuditArchvlTempEntity> list = getAuditDetailsByArchvlTemp(action, id);
				dtoList = ApiAuditParentEntityBuilder.aApiAuditParentEntityBuilder()
						.convertListOfApiAuditArchvlTempEntityToListApiAuditParentEntityDTO(list);
		}
		return getTransationList(dtoList);
	}
	
	

	 
	@Transactional(value = "adapterTransactionManager")
	private List<ApiAuditEntity> getAuditDetailsByAdapter(String action, String id){
		List<ApiAuditEntity> list = this.apiAuditRepository
				.findByActionInAndTransactionIdOrderByCreatedOnAsc(Arrays.asList(action, "on_" + action), id);
		return list;
	}
	@Transactional(value = "archvlAdapterEntityManagerFactory")
	private List<ApiAuditArchvlEntity> getAuditDetailsByArchvl(String action, String id){
		List<ApiAuditArchvlEntity> list = this.apiAuditArchvlRepository
				.findByActionInAndTransactionIdOrderByCreatedOnAsc(Arrays.asList(action, "on_" + action), id);
		return list;
	}
	@Transactional(value = "archvlTempAdapterEntityManagerFactory")
	private List<ApiAuditArchvlTempEntity> getAuditDetailsByArchvlTemp(String action, String id){
		List<ApiAuditArchvlTempEntity> list = this.apiAuditArchvlTempRepository
				.findByActionInAndTransactionIdOrderByCreatedOnAsc(Arrays.asList(action, "on_" + action), id);
		return list;
	}


}
