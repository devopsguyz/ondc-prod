package com.nsdl.beckn.np.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nsdl.beckn.np.dao.DomainMasterRepository;
import com.nsdl.beckn.np.dao.EntityMasterRepository;
import com.nsdl.beckn.np.dao.NPApiLogsRepository;
import com.nsdl.beckn.np.dao.NetworkParticipantMasterRepository;
import com.nsdl.beckn.np.dao.RegistryRepository;
import com.nsdl.beckn.np.dao.MatViewRepository;
import com.nsdl.beckn.np.dao.SellerOnRecordMasterRepository;
import com.nsdl.beckn.np.model.EntityMaster;
import com.nsdl.beckn.np.model.NPApiLogs;
import com.nsdl.beckn.np.model.request.RequestOldSearch;
import com.nsdl.beckn.np.model.request.RequestSearch;
import com.nsdl.beckn.np.model.request.RequestSearchParam;
import com.nsdl.beckn.np.model.response.MessageResponse;
import com.nsdl.beckn.np.model.response.ResponsEntityMaster;
import com.nsdl.beckn.np.model.response.ResponsOldEntityParentMaster;
import com.nsdl.beckn.np.service.OnboardingMViewLookupService;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.CommonUtl;
import com.nsdl.beckn.np.utl.Constants;
import com.nsdl.beckn.np.utl.SecurityUtils;
import com.nsdl.beckn.np.utl.SendError;
import com.nsdl.signing.crypto.EncryptDecrypt;
import com.nsdl.signing.crypto.GeneratePayload;
import com.nsdl.signing.service.CryptoService;
import com.nsdl.beckn.np.model.response.MatViewResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OnboardingMViewLookupServiceImpl implements OnboardingMViewLookupService, ApplicationContextAware {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	CryptoService cryptoService;

	@Autowired
	NPApiLogsRepository logsRepository;

	@Autowired
	DomainMasterRepository domainMasterRepository;

	@Autowired
	NetworkParticipantMasterRepository networkParticipantMasterRepository;
	@Autowired
	SellerOnRecordMasterRepository sellerOnRecordMasterRepository;
	@Autowired
	EntityMasterRepository entityMasterRepository;

	@Autowired
	RegistryRepository registryRepository;
	
	@Autowired
	MatViewRepository matviewRepository;

	@Autowired
	SendError sendError;

	@Value("${time.window.timestamp.minutes}")
	Integer windowTimestamp;

	@Value("${cache.reload.url}")
	String cacheReloadUrl;
	@Value("${whitelist.domain}")
	Boolean whitelistDomain;

	@Autowired
	Gson gson;

	@Autowired
	EncryptDecrypt encryptDecrypt;

	private ApplicationContext applicationContext;

	@Value("${gst.url}")
	private String gstUrl;

	@Value("${gst.flag}")
	Boolean gstFlag;

	@Value("${pan.flag}")
	Boolean panFlag;

	@Value("${pan.url}")
	private String panUrl;

//	@Value("${enc.algo}")
//	String encAlgo;

	@Autowired
	OnboardingSubscirberService service;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public boolean verifyAuthheader(String signature, String data, String publickey) {
		try {

			if (GeneratePayload.verifySignaturePK(signature, data, publickey)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	public boolean checkAuthorization(String authorization, String data, String subscriberId) {
		List<EntityMaster> list = this.entityMasterRepository.findBySubscriberId(subscriberId);
		if (list.size() > 0) {
			list.get(0).getSigningPublicKey();
			return verifyAuthheader(authorization, data, subscriberId);
		}
		return false;
	}

	boolean validateLookup(RequestSearchParam reqLookup) {
		int cnt = 0;
		reqLookup.setData("");

		if (reqLookup.getCountry() == null) {
			reqLookup.setCountry("");
			cnt++;
		} else {
			reqLookup.setCountry(reqLookup.getCountry().trim());
			reqLookup.setData(reqLookup.getData() + "|" + reqLookup.getCountry());
		}
		if (reqLookup.getDomain() == null) {
			reqLookup.setDomain("");
			cnt++;
		} else {
			reqLookup.setDomain(reqLookup.getDomain().trim());
			reqLookup.setData(reqLookup.getData() + "|" + reqLookup.getDomain());
		}
		if (reqLookup.getType() == null) {
			reqLookup.setType("");
			cnt++;
		} else {
			reqLookup.setType(reqLookup.getType().trim());
			reqLookup.setData(reqLookup.getData() + "|" + reqLookup.getType());
		}

		if (reqLookup.getCity() == null || "*".equals(reqLookup.getCity())) {
			reqLookup.setCity("");
			cnt++;
		} else {
			reqLookup.setCity(reqLookup.getCity().trim());
			reqLookup.setData(reqLookup.getData() + "|" + reqLookup.getCity());
		}
		if (reqLookup.getSubscriberId() == null) {
			reqLookup.setSubscriberId("");
			cnt++;
		} else {
			reqLookup.setSubscriberId(reqLookup.getSubscriberId().trim());
			reqLookup.setData(reqLookup.getData() + "|" + reqLookup.getSubscriberId());
		}

		if (reqLookup.getData().length() > 0) {
			reqLookup.setData(reqLookup.getData().substring(1, reqLookup.getData().length()));
		}

		if ((5 - cnt) < 2) {
			return false;
		}
		return true;
	}

	boolean validateOldLookup(RequestOldSearch reqLookup) {
		int cnt = 0;
		boolean flag = false;
		if (reqLookup.getCountry() == null) {
			reqLookup.setCountry("");
			cnt++;
		} else {
			reqLookup.setCountry(reqLookup.getCountry().trim());
		}
		if (reqLookup.getDomain() == null) {
			reqLookup.setDomain("");
			cnt++;
		} else {
			reqLookup.setDomain(reqLookup.getDomain().trim());
		}
		if (reqLookup.getType() == null) {
			reqLookup.setType("");
			cnt++;
		} else {
			reqLookup.setType(reqLookup.getType().trim());
		}

		if (reqLookup.getCity() == null || reqLookup.getCity().indexOf("*") != -1) {
			reqLookup.setCity("");
			cnt++;
		} else {
			reqLookup.setCity(reqLookup.getCity().trim());
		}
		if (reqLookup.getSubscriberId() == null) {
			reqLookup.setSubscriberId("");
			cnt++;
		} else {
			reqLookup.setSubscriberId(reqLookup.getSubscriberId().trim());
		}

		if (reqLookup.getUkId() == null) {
			reqLookup.setUkId("");
			flag = true;
			cnt++;
		} else {
			flag = true;
			reqLookup.setUkId(reqLookup.getUkId().trim());
		}

		if (reqLookup.getUniqueKeyId() == null) {
			if (reqLookup.getUkId() == null) {
				reqLookup.setUkId("");
			}
			if (!flag)
				cnt++;
		} else {
			reqLookup.setUkId(reqLookup.getUniqueKeyId().trim());
		}

		if ((6 - cnt) < 2) {
			return false;
		}
		return true;
	}

	public List<MatViewResponse> excuteNPQuery(String subscriberId, String domain, String type, String city, String country,
			String ukid) {
		Instant start = Instant.now();


		if (isBlank(domain)) {
			domain="";
		}
		if (isBlank(country)) {
			country="";
		}

		if (isBlank(type)) {
			type="";
		}

		if (isBlank(subscriberId)) {
			subscriberId="";
		}
		if (isBlank(ukid)) {
			ukid="";
		}
		
		List<MatViewResponse> list = matviewRepository.findByAll(domain,country,type,subscriberId,ukid);

		if (isNotBlank(city)) {
			Gson gson = new Gson();
			list = list.stream().filter(item -> {

				boolean flag = false;
				
				// 1:  np citycode 
				if(item.getCITYCODE()!=null) {
					List citycodes=gson.fromJson(item.getCITYCODE(), List.class);
					if(citycodes.contains("*") || citycodes.contains(city)) {
						flag=true;
					}
				}
				
				// 3 : SOR city code
				if(item.getSELLERCITYCODE()!=null) {
					List sellercitycodes=gson.fromJson(item.getSELLERCITYCODE(), List.class);
					if(sellercitycodes.contains("*") || sellercitycodes.contains(city)) {
						flag=true;
					}else if(item.getSELLERUNIQUEKEYID()!=null) {
						flag=false;
					}
				}
				
				return flag;
			}).collect(Collectors.toList());
		}

		long timer = CommonUtl.getTimeDiff(start, Instant.now());
		log.info("lookup np query with result list size[{}] executed in {} ms", list.size(), timer);
		return list;
	}

	@Override
	public List<ResponsEntityMaster> lookup(RequestSearch request) {

		List<ResponsEntityMaster> oList = new ArrayList<ResponsEntityMaster>();
		try {

			RequestSearchParam reqLookup = request.getSearchParameters();
			NPApiLogs logs = service.saveLogs(request);
			List<EntityMaster> listS = this.entityMasterRepository
					.findBySubscriberId(request.getSender_subscriber_id());
			request.setStatusCode("400");

			if (!CommonUtl.validTimeDate(request.getTimestamp(), this.windowTimestamp)) {
				request.setStatusCode("411");
				request.setMsg(Constants.POLICY_ERROR_TIMESTAMP_INVALID[0] + ":"
						+ Constants.POLICY_ERROR_TIMESTAMP_INVALID[1]);
			} else if ((listS.size() == 0)) {
				request.setStatusCode("412");
				request.setMsg(Constants.JSON_SCHEMA_ERROR_SENDER_SUBSCRIBER_INVALID[0] + ":"
						+ Constants.JSON_SCHEMA_ERROR_SENDER_SUBSCRIBER_INVALID[1]);

			} else if (!validateLookup(reqLookup)) {
				request.setStatusCode("416");
				request.setMsg(Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[0] + ":"
						+ Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[1]);

			} else if (!verifyAuthheader(request.getSignature(), reqLookup.getData(),
					listS.get(0).getSigningPublicKey())) {
				request.setMsg(Constants.JSON_SCHEMA_ERROR_LOOKUP_INVALID[0] + ":"
						+ Constants.JSON_SCHEMA_ERROR_LOOKUP_INVALID[1]);
				request.setStatusCode("401");
			} else {
				request.setStatusCode("200");
				MessageResponse reponse = new MessageResponse();
				Instant start = Instant.now();

				Map<String, Integer> map = new HashMap();

				// Network participiant
				List<MatViewResponse> list = excuteNPQuery(request.getSearchParameters().getSubscriberId(),
						request.getSearchParameters().getDomain(), request.getSearchParameters().getType(),
						request.getSearchParameters().getCity(), request.getSearchParameters().getCountry(), "");
				list.forEach(item -> {
					String key = item.getSUBSCRIBERID(); // suscriber id
					if (map.get(key) == null) {
						ResponsEntityMaster rp = new ResponsEntityMaster(item);
						oList.add(rp);
						map.put(key, oList.size() - 1);
					}
					// Network SellerOnRecord
					oList.get(map.get(key)).addNp(item, request.getSearchParameters().getSorRequired());
				});
//				// Network SellerOnRecord
//				if (request.getSearchParameters().getSorRequired()) {
//
//					list.forEach(item -> {
//						String key = item[9].toString(); // suscriber id
//						oList.get(map.get(key)).addNp(item);
//					});
//				}
				long timer = CommonUtl.getTimeDiff(start, Instant.now());
				log.info("vlookup query with prepare result list in {} ms", timer);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info("lookup: exception : {} , request : {}", CommonUtl.getPrintStackString(e),
					CommonUtl.toJsonStr(request));
			sendError.sendError(e, request, Constants.HIGH);
		}
		return oList;

	}

	@SuppressWarnings("unchecked")
	@Override
	// Old lookup function without signature verification
	public List<ResponsOldEntityParentMaster> lookupOld(RequestOldSearch reqLookup) {
		List<ResponsOldEntityParentMaster> returnList = new ArrayList<>();
		try {

			NPApiLogs logs = service.saveLogs(reqLookup);

			if (!validateOldLookup(reqLookup)) {
				reqLookup.setStatusCode("416");
				reqLookup.setMsg(Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[0] + ":"
						+ Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[1]);

			} else {
				reqLookup.setStatusCode("200");
				MessageResponse reponse = new MessageResponse();

				if ("BAP".equals(reqLookup.getType())) {
					reqLookup.setType(Constants.BUYER_APP);
				} else if ("BPP".equals(reqLookup.getType())) {
					reqLookup.setType(Constants.SELLER_APP);
				} else if ("BG".equals(reqLookup.getType())) {
					reqLookup.setType(Constants.GATEWAY);
				} else {
					reqLookup.setType(reqLookup.getType());
				}

				Instant start = Instant.now();

				Map<String, Integer> map = new HashMap();

				ArrayList<List<MatViewResponse>> al = new ArrayList<>();
				// network Participiant
				al.add(excuteNPQuery(reqLookup.getSubscriberId(), reqLookup.getDomain(), reqLookup.getType(),
						reqLookup.getCity(), reqLookup.getCountry(), reqLookup.getUkId()));
				List<ResponsOldEntityParentMaster> oList = new ArrayList<ResponsOldEntityParentMaster>();

				al.stream().forEach(list -> {
					list.stream().forEach(item -> {
						// network Participiant
						// ukId:subscriberID:type:domain:subscriberURL
						String key = item.getUNIQUEKEYID() +":" + ":" + item.getSUBSCRIBERID() + ":" + item.getTYPE()
						+ ":" + item.getDOMAIN()+ ":" + item.getSUBSCRIBERURL();

						if (map.get(key) == null && ("".equals(reqLookup.getUkId())
								|| reqLookup.getUkId().equalsIgnoreCase(item.getUNIQUEKEYID()))) {
							ResponsOldEntityParentMaster rp = new ResponsOldEntityParentMaster(item, "np",
									reqLookup.getCity());
							oList.add(rp);
							map.put(key, oList.size() - 1);

						}

						if (item.getSELLERUNIQUEKEYID()!= null) {
							// seller on record
							// ukid:suscriberId:type:domain:subscriberURL:msn
							String keySeller = "Seller" + item.getSELLERUNIQUEKEYID() + ":" + item.getSUBSCRIBERID() + ":"
									+ item.getTYPE() + ":" + item.getDOMAIN() + ":" + item.getSUBSCRIBERURL()
									+ item.getMSN();
							if (map.get(keySeller) == null && ("".equals(reqLookup.getUkId())
									|| item.getSELLERUNIQUEKEYID().equalsIgnoreCase(reqLookup.getUkId()))) {

								ResponsOldEntityParentMaster rp = null;

								rp = new ResponsOldEntityParentMaster(item, "seller", reqLookup.getCity());

								oList.add(rp);
								map.put(keySeller, oList.size() - 1);

							}
						}
					});
				});

				long timer = CommonUtl.getTimeDiff(start, Instant.now());
				log.info("lookup query with prepare result list in {} ms", timer);

				return oList;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("lookup: exception : {} , request : {}", CommonUtl.getPrintStackString(e), gson.toJson(reqLookup));
			sendError.sendError(e, reqLookup, Constants.HIGH);
		}
		return returnList;
	}

}
