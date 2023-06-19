package com.nsdl.beckn.np.service.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsdl.signing.crypto.*;
import com.nsdl.signing.utl.GCMKeyUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nsdl.beckn.np.config.listener.OnSubscribeEvent;
import com.nsdl.beckn.np.dao.DomainMasterRepository;
import com.nsdl.beckn.np.dao.EntityMasterRepository;
import com.nsdl.beckn.np.dao.NPApiLogsRepository;
import com.nsdl.beckn.np.dao.NetworkParticipantMasterRepository;
import com.nsdl.beckn.np.dao.RegistryRepository;
import com.nsdl.beckn.np.dao.SellerOnRecordMasterRepository;
import com.nsdl.beckn.np.model.EntityMaster;

import com.nsdl.beckn.np.model.NPApiLogs;
import com.nsdl.beckn.np.model.NetworkParticipantMaster;
import com.nsdl.beckn.np.model.RegistryEnum;
import com.nsdl.beckn.np.model.RegistryKeys;
import com.nsdl.beckn.np.model.SellerOnRecordMaster;
import com.nsdl.beckn.np.model.TransationCheck;
import com.nsdl.beckn.np.model.request.EntityGst;
import com.nsdl.beckn.np.model.request.EntityPan;
import com.nsdl.beckn.np.model.request.ErrorResponse;
import com.nsdl.beckn.np.model.request.KeyPair;
import com.nsdl.beckn.np.model.request.NetworkParticipant;
import com.nsdl.beckn.np.model.request.ReqOnSubsribe;
import com.nsdl.beckn.np.model.request.RequestOldSearch;
import com.nsdl.beckn.np.model.request.RequestSearch;
import com.nsdl.beckn.np.model.request.RequestSearchParam;
import com.nsdl.beckn.np.model.request.RequestText;
import com.nsdl.beckn.np.model.request.SellerOnRecord;
import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.model.response.ApiEntityMasterProjection;
import com.nsdl.beckn.np.model.response.MessageResponse;
import com.nsdl.beckn.np.model.response.ResponsEntityMaster;
import com.nsdl.beckn.np.model.response.ResponsOldEntityParentMaster;
import com.nsdl.beckn.np.model.response.ResponseOnSubsribe;
import com.nsdl.beckn.np.model.response.ResponseText;
import com.nsdl.beckn.np.service.OnboardingSubscirberService;
import com.nsdl.beckn.np.utl.CommonUtl;
import com.nsdl.beckn.np.utl.Constants;
import com.nsdl.beckn.np.utl.SecurityUtils;
import com.nsdl.beckn.np.utl.SendError;
import com.nsdl.signing.model.KeyData;
import com.nsdl.signing.model.Web;
import com.nsdl.signing.service.CryptoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OnboardingSubscrierServiceImpl implements OnboardingSubscirberService, ApplicationContextAware {

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
	SendError sendError;

	@Value("${time.window.timestamp.minutes}")
	Integer windowTimestamp;

	@Value("${cache.reload.url}")
	String cacheReloadUrl;
	@Value("${whitelist.domain}")
	Boolean whitelistDomain;
//	@Value("${default.type}")
//	String defaultType;
//
//	@Value("${default.domain}")
//	String defaultDomain;

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

	@Value("${enc.algo}")
	private String encAlgo;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public OnboardingSubscrierServiceImpl() {
		// TODO Auto-generated constructor stub

	}

	public void saveLogsResponse(ResponseEntity resonse, NPApiLogs log) {
		// TODO Auto-generated method stub
		try {
			if (log != null) {

				log.setResponse(resonse);
				// this.logsRepository.save(log);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public NPApiLogs saveLogs(Object req) {
		NPApiLogs logs = new NPApiLogs();
		try {

			this.securityUtils.initCommonProperties(logs);
			if (req != null)
				logs.setJsonRequest(CommonUtl.convertObjectToMap(req));
			logs.setType(this.securityUtils.getUserDetails().getLogsType());

			if (req instanceof SubscribeBody) {
				SubscribeBody obj = (SubscribeBody) req;
				try {
					logs.setSubscriberId(obj.getMessage().getEntity().getSubscriberId());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (req instanceof RequestSearchParam) {
				RequestSearchParam obj = (RequestSearchParam) req;
				try {
					logs.setSubscriberId(obj.getSubscriberId());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (req instanceof RequestSearch) {
				RequestSearch obj = (RequestSearch) req;
				try {
					logs.setSubscriberId(obj.getSender_subscriber_id());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (req instanceof RequestOldSearch) {
				RequestOldSearch obj = (RequestOldSearch) req;
				try {
					logs.setSubscriberId(obj.getSubscriberId());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			this.securityUtils.setLogId(logs);
			// saveLogsRequest(logs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return logs;

	}

	@Override
	@Async(value = "auditExecutor")
	@Transactional
	public void saveLogsResponseTime(Integer time, NPApiLogs logs) {
		// TODO Auto-generated method stub
		try {

			if (logs != null) {
				logs.setResponseTime(time);
				Map<String, Object> resp = logs.getJsonResponse();
				logs.setJsonResponse(CommonUtl.convertObjectToMap(logs.getResponse()));
				this.logsRepository.save(logs);
				log.info("Request Log id: " + logs.getId() + ":"
						+ CommonUtl.convertObjectToMapString(logs.getResponse()));

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	void setError(ErrorResponse response, String type, String error[]) {
		response.getError().setType(type);
		response.getError().setCode(error[0]);
		response.getError().setMessage(error[1]);

	}

	void setError(ErrorResponse response, String type, String error[], int index) {
		setError(response, type, error);
		response.getError().setMessage(error[1].replaceAll("index", index + ""));

	}

	void setError(ErrorResponse response, String type, String error[], String id) {
		setError(response, type, error);
		response.getError().setMessage(error[1].replaceAll("index", "id : " + id));

	}

	void setError(ErrorResponse response, String type, String error[], int index, int j) {
		setError(response, type, error);
		response.getError().setMessage(error[1].replaceAll("index-j", j + "").replaceAll("index", index + ""));

	}

	void setErrorURL(ErrorResponse response, String type, String error[], String url) {
		setError(response, type, error);
		response.getError().setMessage(error[1].replaceAll("URL", url + ""));

	}

	@Override
	public void caheRefresh(String url) {
		CloseableHttpResponse response = null;

		try(CloseableHttpClient httpClient = HttpClients.createDefault();) {

			if (cacheReloadUrl.indexOf("cache-evict") != -1) {

				StringEntity requestEntity = new StringEntity("'{" + "\"key\": \"beckn-api-cache-lookup\"" + "}",
						ContentType.APPLICATION_JSON);

				HttpPost postMethod = new HttpPost(this.cacheReloadUrl);
				postMethod.setEntity(requestEntity);

				response = httpClient.execute(postMethod);
				if (response.getStatusLine().getStatusCode() == 200) {
					log.info("caheRefresh", this.cacheReloadUrl + ":ok");
				} else {
					log.info("caheRefresh", this.cacheReloadUrl + ":ERROR:" + response.getStatusLine().getStatusCode()
							+ response.getStatusLine().toString());
				}

			} else {
				HttpGet request = new HttpGet(this.cacheReloadUrl);

				response = httpClient.execute(request);

				System.out.println(response.getStatusLine().getStatusCode()); // 200

				if (response.getStatusLine().getStatusCode() == 200) {
					log.info("caheRefresh", this.cacheReloadUrl + ":ok");
					// body.getMsgError().put("caheRefresh", this.cacheReloadUrl + ":ok");
				} else {

					log.info("caheRefresh", this.cacheReloadUrl + ":ERROR:" + response.getStatusLine().getStatusCode()
							+ response.getStatusLine().toString());
//				body.getMsgError().put("caheRefresh", this.cacheReloadUrl + ":ERROR:"
//						+ response.getStatusLine().getStatusCode() + response.getStatusLine().toString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("caheRefresh_exception", this.cacheReloadUrl + ":" + e.getMessage());

//			body.getMsgError().put("caheRefresh_exception", this.cacheReloadUrl + ":" + e.getMessage());

			e.printStackTrace();
		} finally {
			try {
				if(response != null) {
					response.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	boolean valdidateKeys(ErrorResponse responseError, SubscribeBody body) {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		boolean verify = false;
		String url;
		if ("/".equals(body.getMessage().getEntity().getCallbackUrl())) {
			url = "https://" + body.getMessage().getEntity().getSubscriberId();

		} else {
			url = "https://" + body.getMessage().getEntity().getSubscriberId()
					+ body.getMessage().getEntity().getCallbackUrl();

		}
		try {

			URL u = new URL(url);

			// For testing purpose only
			if (url.indexOf("https://fossgentechnologies.com") != -1) {
				url = url.replaceAll("https://fossgentechnologies.com", "http://localhost:9003");
			}

			url = url + "/on_subscribe";
			body.getMsgError().put("valdidateKeys_url", url);
			System.out.println("URL ENCR:" + url);
			HttpPost request = new HttpPost(url);

			ReqOnSubsribe sub = new ReqOnSubsribe();
			ISubscribeEncryptDecrypt encr;
			if ("GCM".equals(encAlgo)) {
				encr = new SubscribeEncryptDecryptGCM();
			} else {
				encr = new SubscribeEncryptDecrypt();
			}

			List<RegistryKeys> listReg = this.registryRepository
					.findFirstByTypeOrderByCreatedDateDesc(RegistryEnum.ENCRYPTION);
			sub.setSubscriberId(body.getMessage().getEntity().getSubscriberId());

			String str = UUID.randomUUID().toString();
			sub.setChallenge(encr.encrypt(body.getMessage().getEntity().getKeyPair().getEncryptionPublicKey(),
					listReg.get(0).getPrivateKey(), str));
			log.info("Challenge Enc Key:" + body.getMessage().getEntity().getKeyPair().getEncryptionPublicKey());
			log.info("Challenge:" + sub.getChallenge());
			body.getMsgError().put("valdidateKeys_Challenge", sub.getChallenge());
			body.getMsgError().put("valdidateKeys_PublicKey",
					body.getMessage().getEntity().getKeyPair().getEncryptionPublicKey());
			String json = CommonUtl.toJsonStr(sub);
			StringEntity entity = new StringEntity(json);
			request.setEntity(entity);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			CloseableHttpResponse response = httpClient.execute(request);

			try (response) {

				System.out.println(response.getStatusLine().getStatusCode()); // 200

				body.getMsgError().put("valdidateKeys_On_subsribe_call", response.getStatusLine().getStatusCode() + "");

				if (response.getStatusLine().getStatusCode() == 200) {

					String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

					ResponseOnSubsribe responseSubscribe = this.gson.fromJson(responseBody, ResponseOnSubsribe.class);
					if (!str.equals(responseSubscribe.getAnswer())) {
						setErrorURL(responseError, Constants.CORE_ERROR, Constants.CORE_ERROR_ENCR_FAIL_INVALID, url);
						body.getMsgError().put("valdidateKeys_challenge_not_match", "Challenge Not match");
						return false;
					}
					verify = true;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			body.getMsgError().put("valdidateKeys_challenge_Exception", e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!verify) {
			setErrorURL(responseError, Constants.CORE_ERROR, Constants.CORE_ERROR_ENCR_FAIL_INVALID, url);
			body.getMsgError().put("valdidateKeys_challenge_No", "No challenge Match");
			return false;

		}

//		}
		return true;

	}

	boolean validateEntity(SubscribeBody reqSubscribe, ErrorResponse response) {
		int opNo = getOpNo(reqSubscribe);
		if (reqSubscribe.getMessage().getEntity() == null) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_ENTITY_INVALID);
			return false;
		}
		if (!CommonUtl.validTimeDate(reqSubscribe.getMessage().getTimestamp(), this.windowTimestamp)) {
			setError(response, Constants.POLICY_ERROR, Constants.POLICY_ERROR_TIMESTAMP_INVALID);
			return false;
		}
		if ((reqSubscribe.getMessage().getReqId() == null) || "".equals(reqSubscribe.getMessage().getReqId())) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_REQID_INVALID);
			return false;
		}

		if (!reqSubscribe.isEdit() && ((reqSubscribe.getMessage().getEntity().getPan() == null)
				|| (reqSubscribe.getMessage().getEntity().getPan().getDateOfIncorporation() == null)
				|| (reqSubscribe.getMessage().getEntity().getPan().getPanNo() == null)
				|| (reqSubscribe.getMessage().getEntity().getPan().getNameAsPerPan() == null)

				|| "".equals(reqSubscribe.getMessage().getEntity().getPan().getDateOfIncorporation())
				|| "".equals(reqSubscribe.getMessage().getEntity().getPan().getPanNo())
				|| "".equals(reqSubscribe.getMessage().getEntity().getPan().getNameAsPerPan()))) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_PAN_INVALID);
			return false;
		} else if (reqSubscribe.isEdit() && (reqSubscribe.getMessage().getEntity().getPan() != null)) {
			setError(response, Constants.POLICY_ERROR, Constants.JSON_SCHEMA_ERROR_PAN_EXITS_INVALID);
			return false;
		}

		if (!reqSubscribe.isEdit() && (reqSubscribe.getMessage().getEntity().getGst() != null)
				&& (reqSubscribe.getMessage().getEntity().getGst().getCityCode() != null)
				&& !validateCityCode(response, reqSubscribe.getMessage().getEntity().getGst().getCityCode())) {
			return false;
		}

		if (!reqSubscribe.isEdit() && (CommonUtl
				.getLocalDate(reqSubscribe.getMessage().getEntity().getPan().getDateOfIncorporation()) == null))

		{
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_PAN_DATE_INVALID);
			return false;
		}
		if ((!reqSubscribe.isEdit() && (reqSubscribe.getMessage().getEntity().getCallbackUrl() == null))
				|| "".equals(reqSubscribe.getMessage().getEntity().getCallbackUrl())) {
			setError(response, Constants.JSON_SCHEMA_ERROR,
					Constants.JSON_SCHEMA_ERROR_Network_Participant_CALLBACK_URL_INVALID);
			return false;
		}
		if ((reqSubscribe.getMessage().getEntity().getSubscriberId() == null)
				|| "".equals(reqSubscribe.getMessage().getEntity().getSubscriberId())) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SUBSCRIBER_INVALID);
			return false;
		}
		if ((!reqSubscribe.isEdit() && ((reqSubscribe.getMessage().getEntity().getCountry() == null)))
				|| "".equals(reqSubscribe.getMessage().getEntity().getCountry())) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_COUNTRY_INVALID);
			return false;
		}
		if ((!reqSubscribe.isEdit() && ((reqSubscribe.getMessage().getEntity().getUniqueKeyId() == null)))
				|| "".equals(reqSubscribe.getMessage().getEntity().getUniqueKeyId())) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_UNIQUE_ID_INVALID);
			return false;
		}
		if ((opNo >= 1) && (opNo <= 6)) {
			if (((reqSubscribe.getMessage().getEntity().getKeyPair() == null)
					|| (reqSubscribe.getMessage().getEntity().getKeyPair().getEncryptionPublicKey() == null)
					|| "".equals(reqSubscribe.getMessage().getEntity().getKeyPair().getEncryptionPublicKey()))) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_EncryptionPublicKey_INVALID);
				return false;
			}
			if (((reqSubscribe.getMessage().getEntity().getKeyPair() == null)
					|| (reqSubscribe.getMessage().getEntity().getKeyPair().getSigningPublicKey() == null)
					|| "".equals(reqSubscribe.getMessage().getEntity().getKeyPair().getSigningPublicKey()))) {
				setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SigningPublicKey_INVALID);
				return false;
			}
			if (((reqSubscribe.getMessage().getEntity().getKeyPair() != null)
					&& (reqSubscribe.getMessage().getEntity().getKeyPair().getValidFrom() != null))) {
				String error = CommonUtl.checkDate(reqSubscribe.getMessage().getEntity().getKeyPair().getValidFrom(),
						reqSubscribe.getMessage().getEntity().getKeyPair().getValidUntil());
				if (error != null) {
					setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_KEYPAIRDATE_INVALID);

				}
			}
		}
		return true;
	}

	public boolean verifyAuthheader(String signature, String data, String publickey) {
		try {

			// String blakeValue = GeneratePayload.generateBlakeHash(data);

			if (GeneratePayload.verifySignaturePK(signature, data, publickey)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	public boolean verifyDomain(SubscribeBody body, ErrorResponse response) {
		try {

			Map<String, Boolean> map = new HashMap<>();

//			for (NetworkParticipant np : body.getMessage().getNetworkParticipant()) {
			try {

//					if (map.get(np.getSubscriberUrl()) == null) {
//						map.put(np.getSubscriberUrl(), true);
				String sig = CommonUtl.getSig("https://" + body.getMessage().getEntity().getSubscriberId(),
						body.getMsgError());
				if (sig != null) {
					// String blakeValue =
					// GeneratePayload.generateBlakeHash(body.getMessage().getReqId());

					if (!GeneratePayload.verifySignaturePK(sig, body.getMessage().getReqId(),
							body.getMessage().getEntity().getKeyPair().getSigningPublicKey())) {
						setErrorURL(response, Constants.DOMAIN_ERROR, Constants.DOMAIN_ERROR_DOMAIN_FAIL_INVALID,
								"https://" + body.getMessage().getEntity().getSubscriberId());
						body.getMsgError().put("Signature_InValid", "Signature is not valid");
						return false;
					}

				} else {
					setErrorURL(response, Constants.DOMAIN_ERROR, Constants.DOMAIN_ERROR_DOMAIN_FILE_NOT_FOUND_INVALID,
							"https://" + body.getMessage().getEntity().getSubscriberId());
					body.getMsgError().put("Signature_FILE", "Signature is not found");
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				body.getMsgError().put("Signature_Exception", e.getMessage());
				log.info("--------\nERROR:" + e.getMessage() + "\n---------------");
				setErrorURL(response, Constants.DOMAIN_ERROR, Constants.DOMAIN_ERROR_DOMAIN_FILE_NOT_FOUND_INVALID,
						"https://" + body.getMessage().getEntity().getSubscriberId());
				return false;

			}

		} catch (Exception e) {
			// e.printStackTrace();
			// TODO: handle exception
			body.getMsgError().put("Signature_Exception", e.getMessage());

			log.error("ERROR URL  error {} ", e.getMessage());

		}

		return true;
	}

	public boolean verifyUniqueKeyID(SubscribeBody body, ErrorResponse response) {
		HashMap<String, String> hs = new HashMap();
		TransationCheck flag = new TransationCheck();
		flag.setFlag(true);
		if (hs.get(body.getMessage().getEntity().getUniqueKeyId()) == null) {
			hs.put(body.getMessage().getEntity().getUniqueKeyId(), "ok");
		}
		if (body.getMessage().getNetworkParticipant() != null) {
			body.getMessage().getNetworkParticipant().forEach(np -> {
				if (np.getSellerOnRecord() != null) {
					np.getSellerOnRecord().forEach(seller -> {
						if (flag.isFlag()) {
							if (hs.get(seller.getUniqueKeyId()) == null) {
								hs.put(seller.getUniqueKeyId(), "ok");
							} else {
								flag.setFlag(false);
								setError(response, Constants.UNIQUE_KEY_ID_ERROR,
										Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_SELLER_ON_RECORDS_MASTER,
										seller.getUniqueKeyId());
								log.info("Validation failed,Unique key id already exists : "
										+ Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_SELLER_ON_RECORDS_MASTER);

							}
						}
					});
				}
			});
		}
		return flag.isFlag();
	}

	public boolean verifyUniqueKeyID(SubscribeBody reqSubscribe, ErrorResponse response, String uniqueKeyId,
			Integer index) {
		log.info("Validating Unique key id : ", uniqueKeyId);
		try {
			if (uniqueKeyId != null) {
				int opNo = getOpNo(reqSubscribe);
				boolean existsByUniqueKeyIdInEM = false;
				// boolean existsByUniqueKeyIdInSeller = false;
				if (opNo >= 1 && opNo <= 5) {
					existsByUniqueKeyIdInEM = entityMasterRepository.existsByUniqueKeyIdIgnoreCase(uniqueKeyId);
				}
				if ((opNo >= 1 && opNo <= 5) && !existsByUniqueKeyIdInEM) {
					existsByUniqueKeyIdInEM = sellerOnRecordMasterRepository.existsByUniqueKeyIdIgnoreCase(uniqueKeyId);
				}

				if (((opNo == 9 || opNo == 10))) {
					existsByUniqueKeyIdInEM = entityMasterRepository.existsByUniqueKeyIdIgnoreCase(uniqueKeyId);
					if (!existsByUniqueKeyIdInEM)
						existsByUniqueKeyIdInEM = sellerOnRecordMasterRepository
								.existsByUniqueKeyIdIgnoreCase(uniqueKeyId);
				}

				if (existsByUniqueKeyIdInEM) {
					if (index == -1) {
						setError(response, Constants.UNIQUE_KEY_ID_ERROR,
								Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_ENTITY_MASTER);
						log.info("Validation failed,Unique key id already exists : "
								+ Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_ENTITY_MASTER);
						return false;
					} else {
						setError(response, Constants.UNIQUE_KEY_ID_ERROR,
								Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_SELLER_ON_RECORDS_MASTER, index);
						log.info("Validation failed,Unique key id already exists : "
								+ Constants.ERROR_UNIQUE_KEY_ID_ALREADY_PRESENT_IN_SELLER_ON_RECORDS_MASTER);
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			reqSubscribe.getMsgError().put("verifyUniqueKeyID_Exception", e.getMessage());
			log.error("ERROR URL  error {} ", e.getMessage());
		}
		log.info("Validation successful for Unique key id.");
		return true;
	}

	boolean validateCityCode(ErrorResponse response, List<String> list) {

		for (String element : list) {
			if ((element == null) || ((element.indexOf("std:") != 0) && !"*".equals(element))) {
				setErrorURL(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_CITYCODE_INVALID,
						element);
				return false;
			}
		}
		return true;
	}

	boolean validateNetworkParticipant(SubscribeBody reqSubscribe, ErrorResponse response, String type, boolean msn) {
		int opNo = getOpNo(reqSubscribe);
		if ((opNo != 6) && ((reqSubscribe.getMessage().getNetworkParticipant() == null)
				|| (reqSubscribe.getMessage().getNetworkParticipant().size() == 0))) {
			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_Network_Participant_INVALID);
			return false;
		}

		for (int i = 0; i < reqSubscribe.getMessage().getNetworkParticipant().size(); i++) {
			NetworkParticipant item = reqSubscribe.getMessage().getNetworkParticipant().get(i);
			if ((item.getType() == null) || "".equals(item.getType()) || (type.indexOf(item.getType()) == -1)) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_Type_INVALID, i);
				return false;
			}

			if ((item.getSubscriberUrl() == null) || "".equals(item.getSubscriberUrl())
					|| (item.getSubscriberUrl().indexOf("/") != 0)) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_SUBSCRIBER_URL_INVALID, i);
				return false;
			}
			if ((opNo != 10) && (opNo != 12) && (opNo != 7)
					&& ((item.getCityCode() == null) || (item.getCityCode().size() == 0))) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_CITY_CODE_INVALID, i);
				return false;
			}

			if ((opNo != 10) && (opNo != 12) && (opNo != 7) && (!validateCityCode(response, item.getCityCode()))) {
				return false;
			}
//			if ((item.getCallbackUrl() == null) || "".equals(item.getCallbackUrl())) {
//				setError(response, Constants.JSON_SCHEMA_ERROR,
//						Constants.JSON_SCHEMA_ERROR_Network_Participant_CALLBACK_URL_INVALID, i);
//				return false;
//			}

//			if ((item.getDomain() == null) || "".equals(item.getDomain())
//					|| (Constants.mapDomain.get(item.getDomain()) == null)) {
//				setError(response, Constants.JSON_SCHEMA_ERROR,
//						Constants.JSON_SCHEMA_ERROR_Network_Participant_DOMAIN_INVALID, i);
//				return false;
//			}

			if ((opNo != 5) && (opNo != 11) && (msn != item.isMsn())) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_MSN_INVALID, i);
				return false;
			} else if ((((opNo == 5) && ((Constants.SELLER_APP.equals(item.getType()) && !item.isMsn())))
					|| (Constants.BUYER_APP.equals(item.getType()) && item.isMsn()))) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_MSN_INVALID, i);
				return false;
			} else if (((opNo == 11)) && (Constants.BUYER_APP.equals(item.getType()) && item.isMsn())) {
				setError(response, Constants.JSON_SCHEMA_ERROR,
						Constants.JSON_SCHEMA_ERROR_Network_Participant_MSN_INVALID, i);
				return false;
			}
			if (((opNo == 1) || (opNo == 2) || (opNo == 3) || (opNo == 4) || (opNo == 5) || (opNo == 8) || (opNo == 9))
					&& (Constants.BUYER_APP.equals(item.getType()) || !item.isMsn())
					&& ((item.getSellerOnRecord() != null))) {
				setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SELLER_Not_VALID, i);
				return false;

			}
		}
		return true;
	}

	boolean validateNoSellerOnRecord(SubscribeBody reqSubscribe, ErrorResponse response) {
		Integer opNo = getOpNo(reqSubscribe);
		for (int i = 0; i < reqSubscribe.getMessage().getNetworkParticipant().size(); i++) {
			NetworkParticipant np = reqSubscribe.getMessage().getNetworkParticipant().get(i);
			if ((opNo != 11) && np.isMsn()) {
				setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_MSN_INVALID, i);
				return false;
			}
			if ((opNo != 11) && ((np.getSellerOnRecord() != null) && (np.getSellerOnRecord().size() > 0))) {
				setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SELLER_NO_INVALID, i);
				return false;
			}

		}
		return true;
	}

	boolean validateNoNetworkParticipent(SubscribeBody reqSubscribe, ErrorResponse response) {
		if (reqSubscribe.getMessage().getNetworkParticipant() != null) {

			setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_NO_NP_INVALID);
			return false;
		}
		return true;

	}

	Integer getOpNo(SubscribeBody reqSubscribe) {
		if ((reqSubscribe.getContext() != null) && (reqSubscribe.getContext().getOperation() != null)) {
			return reqSubscribe.getContext().getOperation().getOpsNo();
		} else {
			return 0;
		}

	}

	boolean validateSellerOnRecord(SubscribeBody reqSubscribe, ErrorResponse response) {
		int opNo = getOpNo(reqSubscribe);
		for (int j = 0; j < reqSubscribe.getMessage().getNetworkParticipant().size(); j++) {
			NetworkParticipant np = reqSubscribe.getMessage().getNetworkParticipant().get(j);
			if (np.isMsn() && ((np.getSellerOnRecord() == null) || (np.getSellerOnRecord().size() == 0))) {
				setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SELLER_INVALID, j);
				return false;

			}
			if (np.getSellerOnRecord() != null) {
				for (int i = 0; i < np.getSellerOnRecord().size(); i++) {
					SellerOnRecord item = np.getSellerOnRecord().get(i);
					if ((item.getUniqueKeyId() == null) || "".equals(item.getUniqueKeyId())) {
						setError(response, Constants.JSON_SCHEMA_ERROR,
								Constants.JSON_SCHEMA_ERROR_SELLER_UNIQUE_INVALID, j, i);
						return false;
					}
					if (!verifyUniqueKeyID(reqSubscribe, response, item.getUniqueKeyId(), i)) {
						return false;
					}
					if ((opNo != 7) && ((item.getCityCode() == null) || (item.getCityCode().size() == 0))) {
						setError(response, Constants.JSON_SCHEMA_ERROR, Constants.JSON_SCHEMA_ERROR_SELLER_CITY_INVALID,
								j, i);
						return false;
					}

					if ((opNo != 7) && !validateCityCode(response, item.getCityCode())) {
						return false;
					}
					if (((opNo >= 1) && (opNo <= 5)) || (opNo == 7) || (opNo == 10) || (opNo == 9)) {
						if (item.getKeyPair() == null) {
							setError(response, Constants.JSON_SCHEMA_ERROR,
									Constants.JSON_SCHEMA_SELLER_ERROR_SELLER_KEYPAIR_INVALID, j, i);
							return false;
						}

						if ((item.getKeyPair().getEncryptionPublicKey() == null)
								|| "".equals(item.getKeyPair().getEncryptionPublicKey())) {
							setError(response, Constants.JSON_SCHEMA_ERROR,
									Constants.JSON_SCHEMA_SELLER_ERROR_EncryptionPublicKey_INVALID, j, i);
							return false;
						}
						if ((item.getKeyPair() == null) || (item.getKeyPair().getSigningPublicKey() == null)
								|| "".equals(item.getKeyPair().getSigningPublicKey())) {
							setError(response, Constants.JSON_SCHEMA_ERROR,
									Constants.JSON_SCHEMA_ERROR_SELLER_SigningPublicKey_INVALID, j, i);
							return false;
						}
						String error = CommonUtl.checkDate(item.getKeyPair().getValidFrom(),
								item.getKeyPair().getValidUntil());
						if (error != null) {
							setError(response, Constants.JSON_SCHEMA_ERROR,
									Constants.JSON_SCHEMA_ERROR_SELLER_KEYPAIRDATE_INVALID, j, i);
							return false;

						}
					}
				}
			}

		}

		return true;
	}

	boolean validateSchema(SubscribeBody reqSubscribe, ErrorResponse response) {
		if ((reqSubscribe.getContext() == null) || (reqSubscribe.getContext().getOperation() == null)
				|| (reqSubscribe.getContext().getOperation().getOpsNo() == null)
				|| ((reqSubscribe.getContext().getOperation().getOpsNo() <= 0)
						|| (reqSubscribe.getContext().getOperation().getOpsNo() > 12))) {
			setError(response, Constants.CONTEXT_ERROR, Constants.SUBSRCIBE_CONTEXT_INVALID);
			return false;
		}

		int optionNo = reqSubscribe.getContext().getOperation().getOpsNo() - 1;

		if (((reqSubscribe.getContext().getOperation().getOpsNo() >= 6)
				&& (reqSubscribe.getContext().getOperation().getOpsNo() <= 12))) {
			reqSubscribe.setEdit(true);
		}
		if (Constants.optionFlag[optionNo][3] && !validateEntity(reqSubscribe, response)) {
			return false;
		}

		if (this.whitelistDomain && (this.domainMasterRepository
				.isDomain(reqSubscribe.getMessage().getEntity().getSubscriberId()) == 0)) {
			setError(response, Constants.POLICY_ERROR, Constants.POLICY_ERROR_DOMAIN_INVALID);
			return false;

		}
		List<EntityMaster> list = this.entityMasterRepository
				.findBySubscriberId(reqSubscribe.getMessage().getEntity().getSubscriberId());

		if ((list.size() > 0) && ((reqSubscribe.getContext().getOperation().getOpsNo() >= 1)
				&& (reqSubscribe.getContext().getOperation().getOpsNo() <= 5))) {
			setError(response, Constants.POLICY_ERROR, Constants.POLICY_ERROR_SUBSCRIBEID_EXITS_INVALID);
			return false;

		} else if ((list.size() == 0) && ((reqSubscribe.getContext().getOperation().getOpsNo() >= 9)
				&& (reqSubscribe.getContext().getOperation().getOpsNo() <= 11))) {
			setError(response, Constants.POLICY_ERROR, Constants.POLICY_ERROR_SUBSCRIBEID_NFOUND_INVALID);

			return false;
		}
		if (list.size() > 0) {
			EntityMaster em = list.get(0);
			if (reqSubscribe.getMessage().getEntity().getKeyPair() == null) {
				reqSubscribe.getMessage().getEntity()
						.setKeyPair(new KeyPair(em.getSigningPublicKey(), em.getEncryptionPublicKey()));
			}
			if (reqSubscribe.getMessage().getEntity().getCallbackUrl() == null) {
				reqSubscribe.getMessage().getEntity().setCallbackUrl(em.getCallbackUrl());
			}
		}
		StringBuilder type = new StringBuilder("gateway,")
				.append(Constants.optionFlag[optionNo][0] ? Constants.BUYER_APP : "");
		type.append(Constants.optionFlag[optionNo][1] ? Constants.SELLER_APP : "");

		if (Constants.optionFlag[optionNo][4] && !validateNetworkParticipant(reqSubscribe, response, type.toString(),
				Constants.optionFlag[optionNo][2])) {
			return false;
		}
		if (((optionNo + 1) == 6)) {
			if (!validateNoNetworkParticipent(reqSubscribe, response)) {
				return false;
			}
		} else if (Constants.optionFlag[optionNo][5] && !validateSellerOnRecord(reqSubscribe, response)) {
			return false;
		} else if (!Constants.optionFlag[optionNo][5] && !validateNoSellerOnRecord(reqSubscribe, response)) {
			return false;
		}

		return true;
	}

	boolean validOCSP(SubscribeBody reqSubscribe, ErrorResponse response) {
		Map<String, Boolean> map = new HashMap<>();
		// for (NetworkParticipant np :
		// reqSubscribe.getMessage().getNetworkParticipant()) {

//			if (map.get() == null) {
		String ocspStatus = this.cryptoService.checkOCSP(
				new Web("https://" + reqSubscribe.getMessage().getEntity().getSubscriberId()),
				reqSubscribe.getMsgError());
		if (Constants.OCSP_NOT_VALID.equals(ocspStatus)) {
			setErrorURL(response, Constants.DOMAIN_ERROR, Constants.DOMAIN_ERROR_OCSP_INVALID,
					"https://" + reqSubscribe.getMessage().getEntity().getSubscriberId());
			return false;
		}
//				map.put(np.getSubscriberUrl(), true);
//			}
		// }
		return true;
	}

	boolean validateDataEntity(boolean newDataNP, boolean newDataSeller, SubscribeBody reqSubscribe,
			ErrorResponse response) {
		final EntityMaster em;
		Map<String, NetworkParticipantMaster> mapNP = new HashMap<>();
		Map<String, SellerOnRecordMaster> mapSeller = new HashMap<>();
		int opno = reqSubscribe.getContext().getOperation().getOpsNo();
		List<EntityMaster> list = this.entityMasterRepository
				.findBySubscriberId(reqSubscribe.getMessage().getEntity().getSubscriberId());

		if (list.size() > 0) {
			em = list.get(0);
			if ((em.getNetworkParticipantMasters().size() > 0)) {
				em.getNetworkParticipantMasters().forEach(item -> {
					mapNP.put(item.getKey(), item);
					item.getSellerOnRecordMasters().forEach(seller -> {
						mapSeller.put(seller.getKey(), seller);

					});
				});
			}
		}
		if (reqSubscribe.getMessage().getNetworkParticipant() != null) {
			for (int i = 0; i < reqSubscribe.getMessage().getNetworkParticipant().size(); i++) {
				NetworkParticipant item = reqSubscribe.getMessage().getNetworkParticipant().get(i);
				NetworkParticipantMaster np = mapNP
						.get(item.getKey(reqSubscribe.getMessage().getEntity().getSubscriberId()));
				if (newDataNP && (np != null)) {
					setError(response, Constants.DOMAIN_ERROR, Constants.JSON_SCHEMA_NP_VALID, i);
					return false;
				} else if (!newDataNP && (np == null)) {
					setError(response, Constants.DOMAIN_ERROR, Constants.JSON_SCHEMA_NP_INVALID, i);
					return false;
				}
				if (item.getSellerOnRecord() != null) {
					for (int j = 0; j < item.getSellerOnRecord().size(); j++) {
						SellerOnRecord sellerParam = item.getSellerOnRecord().get(j);
						SellerOnRecordMaster seller = mapSeller
								.get(sellerParam.getKey(reqSubscribe.getMessage().getEntity().getSubscriberId()));
						if (newDataSeller && (seller != null)) {
							setError(response, Constants.DOMAIN_ERROR, Constants.JSON_SCHEMA_SELLER_VALID, i, j);
							return false;
						} else if (!newDataSeller && (seller == null)) {
							setError(response, Constants.DOMAIN_ERROR, Constants.JSON_SCHEMA_SELLER_INVALID, i, j);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	void converBodyToEntityMaster(SubscribeBody reqSubscribe) {
		final EntityMaster em;
		Map<String, NetworkParticipantMaster> mapNP = new HashMap<>();
		Map<String, SellerOnRecordMaster> mapSeller = new HashMap<>();
		int opno = reqSubscribe.getContext().getOperation().getOpsNo();
		List<EntityMaster> list = this.entityMasterRepository
				.findBySubscriberId(reqSubscribe.getMessage().getEntity().getSubscriberId());

		if (list.size() > 0) {
			em = list.get(0);
			if ((em.getNetworkParticipantMasters().size() > 0)) {
				em.getNetworkParticipantMasters().forEach(item -> {
					mapNP.put(item.getKey(), item);
					item.getSellerOnRecordMasters().forEach(seller -> {
						mapSeller.put(seller.getKey(), seller);

					});
				});
			}
		} else {
			em = new EntityMaster();

		}
		if (opno <= 6) {
			em.setAllData(reqSubscribe);
			this.securityUtils.initCommonProperties(em);
			em.setPanStatus(reqSubscribe.getPanStatus());
			em.setPanStatusError(reqSubscribe.getPanStatusError());
			em.setGstStatus(reqSubscribe.getGstStatus());
			em.setGstStatusError(reqSubscribe.getGstStatusError());

			this.entityMasterRepository.save(em);
		}

		if (reqSubscribe.getMessage().getNetworkParticipant() != null) {
			reqSubscribe.getMessage().getNetworkParticipant().forEach(item -> {
				final NetworkParticipantMaster npFinal;
				NetworkParticipantMaster np = mapNP
						.get(item.getKey(reqSubscribe.getMessage().getEntity().getSubscriberId()));
				if (np == null) {
					np = new NetworkParticipantMaster();
					em.getNetworkParticipantMasters().add(np);

					np.setEntityMaster(em);
				}
				this.securityUtils.initCommonProperties(np);
				np.setAllData(reqSubscribe, item);
				mapNP.put(np.getKey(), np);
				npFinal = np;
				this.networkParticipantMasterRepository.save(np);
				if (item.getSellerOnRecord() != null) {
					item.getSellerOnRecord().forEach(sellerParam -> {

						SellerOnRecordMaster seller = mapSeller
								.get(sellerParam.getKey(reqSubscribe.getMessage().getEntity().getSubscriberId()));
						if (seller == null) {
							seller = new SellerOnRecordMaster();
							npFinal.getSellerOnRecordMasters().add(seller);
							seller.setNetworkParticipantMaster(npFinal);
						}
						this.securityUtils.initCommonProperties(seller);
						seller.setAllData(reqSubscribe, sellerParam);
						this.sellerOnRecordMasterRepository.save(seller);
					});

				}
			});
		}

		// return em;
	}

	@Override
	public Map<String, String> getPublicKey() {
		Map<String, String> map = new HashMap<>();
		System.out.println(RegistryEnum.ENCRYPTION.toString());
		List<RegistryKeys> list = this.registryRepository
				.findFirstByTypeOrderByCreatedDateDesc(RegistryEnum.ENCRYPTION);
		map.put("protean_public_key", list.get(0).getPublicKey());
		return map;
	}

	@Override
	public ResponseText getEncryptText(RequestText req) {
		ISubscribeEncryptDecrypt encr;
		if ("GCM".equals(encAlgo)) {
			encr = new SubscribeEncryptDecryptGCM();
		} else {
			encr = new SubscribeEncryptDecrypt();
		}
		List<RegistryKeys> listReg = this.registryRepository
				.findFirstByTypeOrderByCreatedDateDesc(RegistryEnum.ENCRYPTION);

		return new ResponseText(
				encr.encrypt(req.getClientPublicKey(), listReg.get(0).getPrivateKey(), req.getChallenge()));
	}

	@Override
	public ResponseText decryptText(RequestText req) {
		ISubscribeEncryptDecrypt encr;
		if ("GCM".equals(encAlgo)) {
			encr = new SubscribeEncryptDecryptGCM();
		} else {
			encr = new SubscribeEncryptDecrypt();
		}
		List<RegistryKeys> listReg = this.registryRepository
				.findFirstByTypeOrderByCreatedDateDesc(RegistryEnum.ENCRYPTION);

		return new ResponseText(
				encr.decrypt(req.getClientPrivateKey(), listReg.get(0).getPublicKey(), req.getChallenge()));
	}

	@Override
	public String initRKey() {
		KeyData key = this.cryptoService.generateSignKey();
		NPApiLogs logs = saveLogs(null);
		RegistryKeys rKey = new RegistryKeys();
		this.securityUtils.initCommonProperties(rKey);
		rKey.setPrivateKey(key.getPrivateKey());
		rKey.setPublicKey(key.getPublicKey());
		rKey.setType(RegistryEnum.SIGNING);
		this.registryRepository.save(rKey);
        if("GCM".equals(encAlgo)) {
			key = new ObjectMapper().convertValue(GCMKeyUtil.generateKeyPair(), KeyData.class);
		} else {
			key = this.cryptoService.generateEncrypDecryptKey();
		}
		rKey = new RegistryKeys();
		this.securityUtils.initCommonProperties(rKey);
		rKey.setPrivateKey(key.getPrivateKey());
		rKey.setPublicKey(key.getPublicKey());
		rKey.setType(RegistryEnum.ENCRYPTION);
		this.registryRepository.save(rKey);

		return "ok";
	}

	@Override
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
		// NPApiLogs logs = saveLogs(reqLookup);
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

		} else {
			reqLookup.setUkId(reqLookup.getUkId().trim());
		}

		if (reqLookup.getUniqueKeyId() == null) {
			if (flag)
				cnt++;
		} else {
			reqLookup.setUkId(reqLookup.getUniqueKeyId().trim());
		}

		if ((6 - cnt) < 2) {
			return false;
		}
		return true;
	}

	@Override
	public List<ResponsEntityMaster> lookup(RequestSearch request) {

		List<ResponsEntityMaster> oList = new ArrayList<ResponsEntityMaster>();
		try {

			RequestSearchParam reqLookup = request.getSearchParameters();
			NPApiLogs logs = saveLogs(request);
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
				// NPApiLogs logs = saveLogs(reqLookup);
				Date dt = new Date();
				List<ApiEntityMasterProjection> list = null;
				if (request.getSearchParameters().getSorRequired()) {
					list = this.entityMasterRepository.findByAll(reqLookup.getCountry(), reqLookup.getCity(),
							reqLookup.getType(), "", reqLookup.getDomain(), reqLookup.getSubscriberId());
				} else {
					list = this.entityMasterRepository.findByAllWithoutSOR(reqLookup.getCountry(), reqLookup.getCity(),
							reqLookup.getType(), "", reqLookup.getDomain(), reqLookup.getSubscriberId());
				}
				Map<String, Integer> map = new HashMap();
				CommonUtl.logInfo("DB CAL time :" + (new Date().getTime() - dt.getTime()));

				list.forEach(item -> {
					String key = item.getSUBSCRIBERID();
					if (map.get(key) == null) {
						ResponsEntityMaster rp = new ResponsEntityMaster(item);
						oList.add(rp);
						map.put(key, oList.size() - 1);
					}
					oList.get(map.get(key)).addNp(item);
				});
				CommonUtl.logInfo("OBJ CAL time :" + (new Date().getTime() - dt.getTime()));

			}
		} catch (Exception e) {
			// TODO: handle exception
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

			NPApiLogs logs = saveLogs(reqLookup);
	
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
				Date dt = new Date();
				List<ApiEntityMasterProjection> list = this.entityMasterRepository.findByAll(reqLookup.getCountry(),
						reqLookup.getCity(), reqLookup.getType(), reqLookup.getUkId(), reqLookup.getDomain(),
						reqLookup.getSubscriberId());

				CommonUtl.logInfo("DB time :" + (new Date().getTime() - dt.getTime()));

				dt = new Date();
				List<ResponsOldEntityParentMaster> oList = new ArrayList<ResponsOldEntityParentMaster>();
				Map<String, Integer> map = new HashMap();
				list.stream().forEach(item -> {
					String key = item.getUNIQUEKEYID() + ":" + item.getSUBSCRIBERID() + ":" + item.getTYPE() + ":"
							+ item.getDOMAIN() + ":" + item.getSUBSCRIBERURL();

				 	if (map.get(key) == null && ("".equals(reqLookup.getUkId())
							|| reqLookup.getUkId().equalsIgnoreCase(item.getUNIQUEKEYID()))) {
						ResponsOldEntityParentMaster rp = new ResponsOldEntityParentMaster(item, "np",
								reqLookup.getCity());
						oList.add(rp);
						map.put(key, oList.size() - 1);
						 
					}
					String keySeller = "Seller" + item.getSELLERUNIQUEKEYID() + ":" + item.getSUBSCRIBERID() + ":"
							+ item.getTYPE() + ":" + item.getDOMAIN() + ":" + item.getSUBSCRIBERURL() + ":"
							+ item.getMSN();
			 		if (item.getSELLERUNIQUEKEYID() != null && map.get(keySeller) == null
							&& ("".equals(reqLookup.getUkId())
									|| item.getSELLERUNIQUEKEYID().equalsIgnoreCase(reqLookup.getUkId()))) {

						ResponsOldEntityParentMaster rp = null;

						rp = new ResponsOldEntityParentMaster(item, "seller", reqLookup.getCity());

						oList.add(rp);
						map.put(keySeller, oList.size() - 1);
						 
					}
				});

				CommonUtl.logInfo("OBJ CAL time :" + (new Date().getTime() - dt.getTime()));

				return oList;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("lookup: exception : {} , request : {}", CommonUtl.getPrintStackString(e), gson.toJson(reqLookup));
			sendError.sendError(e, reqLookup, Constants.HIGH);
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResponsOldEntityParentMaster> lookupKomn(RequestOldSearch reqLookup) {
		List<ResponsOldEntityParentMaster> returnList = new ArrayList<>();
		try {

			NPApiLogs logs = saveLogs(reqLookup);
			validateOldLookup(reqLookup);

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
			Date dt = new Date();
			List<ApiEntityMasterProjection> list = this.entityMasterRepository.findByAll(reqLookup.getCountry(),
					reqLookup.getCity(), reqLookup.getType(), reqLookup.getUkId(), reqLookup.getDomain(),
					reqLookup.getSubscriberId());

			CommonUtl.logInfo("DB time :" + (new Date().getTime() - dt.getTime()));

			dt = new Date();
			List<ResponsOldEntityParentMaster> oList = new ArrayList<ResponsOldEntityParentMaster>();
			Map<String, Integer> map = new HashMap();
			list.stream().forEach(item -> {
				String key = item.getUNIQUEKEYID() + ":" + item.getSUBSCRIBERID() + ":" + item.getTYPE() + ":"
						+ item.getDOMAIN() + ":" + item.getSUBSCRIBERURL();

				// boolean flag=false;
				if (map.get(key) == null) {
					ResponsOldEntityParentMaster rp = new ResponsOldEntityParentMaster(item, "np", reqLookup.getCity());
					oList.add(rp);
					map.put(key, oList.size() - 1);
					// flag=true;
				}
				String keySeller = "Seller" + item.getSELLERUNIQUEKEYID() + ":" + item.getSUBSCRIBERID() + ":"
						+ item.getTYPE() + ":" + item.getDOMAIN() + ":" + item.getSUBSCRIBERURL() + ":" + item.getMSN();
				if (item.getSELLERUNIQUEKEYID() != null && map.get(keySeller) == null) {

					ResponsOldEntityParentMaster rp = null;

					rp = new ResponsOldEntityParentMaster(item, "seller", reqLookup.getCity());

					oList.add(rp);
					map.put(keySeller, oList.size() - 1);
					// flag=true;
				}
			});

			CommonUtl.logInfo("OBJ CAL time :" + (new Date().getTime() - dt.getTime()));

			return oList;

		} catch (Exception e) {
			// TODO: handle exception
			log.info("lookup: exception : {} , request : {}", CommonUtl.getPrintStackString(e), gson.toJson(reqLookup));
			sendError.sendError(e, reqLookup, Constants.HIGH);
		}
		return returnList;
	}

	@Override
	public ErrorResponse onSubscribe(SubscribeBody reqSubscribe) {
		// TODO Auto-generated method stub
		ErrorResponse response = new ErrorResponse();

		NPApiLogs logs = saveLogs(reqSubscribe);
		response.getMessage().getAck().setStatus("NACK");
		try {

			if (!validateSchema(reqSubscribe, response)) {
				return response;
			}

			if (!verifyUniqueKeyID(reqSubscribe, response, reqSubscribe.getMessage().getEntity().getUniqueKeyId(),
					-1)) {
				return response;
			}

			if (!verifyUniqueKeyID(reqSubscribe, response)) {
				return response;
			}

			reqSubscribe.getMsgError().put("Subscibe_validateSchema", "ok");
			int opNo = reqSubscribe.getContext().getOperation().getOpsNo();
			if (!validOCSP(reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateOCSP", "ok");
			if (!verifyDomain(reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDomain", "ok");

			reqSubscribe.getMsgError().put("Subscibe_validateUniqueKeyID", "ok");

			if ((reqSubscribe.getContext().getOperation().getOpsNo() <= 7) && !valdidateKeys(response, reqSubscribe)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_valdidateKeys", "ok");

			if ((opNo == 8) && !validateDataEntity(true, false, reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDataEntity_op8", "ok");

			if ((opNo == 9) && !validateDataEntity(true, true, reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDataEntity_op9", "ok");
			if ((opNo == 10) && !validateDataEntity(false, true, reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDataEntity_op10", "ok");
			if (((opNo == 11) || (opNo == 12)) && !validateDataEntity(false, false, reqSubscribe, response)) {
				return response;
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDataEntity_op11_12", "ok");
			if (((opNo == 7)) && !validateDataEntity(false, false, reqSubscribe, response)) {
				return response;
			}
			if (opNo < 6) {
				if (this.panFlag && (reqSubscribe.getMessage().getEntity().getPan() != null)) {
					this.isPanVerify(reqSubscribe.getMessage().getEntity().getPan(), reqSubscribe);
					// setError(response, Constants.VALIDATION_ERROR,
					// Constants.VALIDATION_ERROR_PAN_INVALID);
					if (!"BM".equals(reqSubscribe.getPanStatus())) {
						setError(response, Constants.VALIDATION_ERROR,
								new String[] { reqSubscribe.getPanStatusError().substring(2, 6),
										reqSubscribe.getPanStatusError().substring(6) });
						return response;
					}

				}
				if (this.gstFlag && (reqSubscribe.getMessage().getEntity().getGst() != null)) {
					this.isGstVerify(reqSubscribe.getMessage().getEntity().getGst(), reqSubscribe);
					// setError(response, Constants.VALIDATION_ERROR,
					// Constants.VALIDATION_ERROR_GST_INVALID);
					if (!"MM".endsWith(reqSubscribe.getGstStatus())) {
						setError(response, Constants.VALIDATION_ERROR,
								new String[] { reqSubscribe.getGstStatusError().substring(2, 6),
										reqSubscribe.getGstStatusError().substring(6) });
						return response;
					}

				}
			}
			reqSubscribe.getMsgError().put("Subscibe_validateDataEntity_op7", "ok");

			converBodyToEntityMaster(reqSubscribe);
			reqSubscribe.getMsgError().put("Subscibe_Save", "ok");

			this.applicationContext.publishEvent(new OnSubscribeEvent(this, this.cacheReloadUrl));
			// saveMaster(em);
			response.getMessage().getAck().setStatus("ACK");
		} catch (Exception e) {
			// TODO: handle exception

			sendError.sendError(e, reqSubscribe, Constants.HIGH);
			reqSubscribe.getMsgError().put("Subscibe_Exception", e.getMessage());

			e.printStackTrace();
			log.info(e.getMessage());
			setError(response, Constants.RESPONSE_ERROR, new String[] { "500", e.getMessage() });

		}
		return response;
	}

	public void isPanVerify(EntityPan entityPan, SubscribeBody reqSubscribe) {
		CloseableHttpResponse response = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault();) {

			String body = "{" + "\"name\": \"" + entityPan.getNameAsPerPan() + "\"," + "" + "  \"pan\": \""
					+ entityPan.getPanNo() + "\"," + "  \"date\": \"" + entityPan.getDateOfIncorporation() + "\"" + "}";
			StringEntity requestEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
			log.info("panUrl" + this.panUrl + ":" + body);
			HttpPost postMethod = new HttpPost(this.panUrl);
			postMethod.setEntity(requestEntity);

			response = httpClient.execute(postMethod);
			if (response.getStatusLine().getStatusCode() == 200) {
				log.info("panUrl:", this.panUrl + ":ok");
				String error = EntityUtils.toString(response.getEntity(), "UTF-8");
//				String error = response.getStatusLine().toString();
				if (error != null && error.length() > 5) {
					reqSubscribe.setPanStatus(error.substring(2, 4));
					reqSubscribe.setPanStatusError(error);
					return;
				}

			} else {
				log.info("panUrl:", this.panUrl + ":ERROR:" + response.getStatusLine().getStatusCode()
						+ EntityUtils.toString(response.getEntity(), "UTF-8"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		reqSubscribe.setPanStatus("NW");
		reqSubscribe.setPanStatusError("NW:2006:Please PAN try again later.");

	}

	public void isGstVerify(EntityGst entityGst, SubscribeBody reqSubscribe) {
		CloseableHttpResponse response = null;

		try(CloseableHttpClient httpClient = HttpClients.createDefault();) {

			String body = "{" + "\"name\": \"" + entityGst.getLegalEntityName() + "\"," + "" + "  \"gst\": \""
					+ entityGst.getGstNo() + "\"," + "  \"address\": \"" + entityGst.getBusinessAddress() + "\"" + "}";
			StringEntity requestEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
			log.info("gsturl" + this.gstUrl + ":" + body);
			HttpPost postMethod = new HttpPost(this.gstUrl);
			postMethod.setEntity(requestEntity);

			response = httpClient.execute(postMethod);
			if (response.getStatusLine().getStatusCode() == 200) {
				log.info("gsturl:", this.gstUrl + ":ok");
				String error = EntityUtils.toString(response.getEntity(), "UTF-8");
				// String error = response.getStatusLine().toString();
				if (error != null && error.length() > 2) {
					reqSubscribe.setGstStatus(error.substring(0, 2));
					reqSubscribe.setGstStatusError(error);
				}

			} else {
				log.info("gsturl:", this.gstUrl + ":ERROR:" + response.getStatusLine().getStatusCode()
						+ response.getStatusLine().toString());
				reqSubscribe.setGstStatus("NW");
				reqSubscribe.setGstStatus("NW:2006:Please GST try again later.");
			}

		} catch (Exception e) {
			// TODO: handle exception
			reqSubscribe.setGstStatus("NW");
			reqSubscribe.setGstStatus("NW:2006:Please GST try again later.");
			log.error(e.getMessage());
		} finally {
			try {
				if(response != null){
					response.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

//				return karzaReturn;
	}

	@Override
	public String health() {
		try {

			if (this.entityMasterRepository.health() > 0) {
				return "OK";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "NOK";
	}

//	public static List<ApiEntityMasterProjection> listGlobal;
//	public static List<ResponsEntityMaster> oListGlobal;
//
//	@Override
//	public List<ResponsEntityMaster> lookupTest(RequestSearch request, boolean hirachy) {
//
//		  List<ResponsEntityMaster> oList = new ArrayList<ResponsEntityMaster>();
//		try {
//
//			RequestSearchParam reqLookup = request.getSearchParameters();
//			NPApiLogs logs = saveLogs(request);
//			List<EntityMaster> listS = this.entityMasterRepository
//					.findBySubscriberId(request.getSender_subscriber_id());
//			request.setStatusCode("400");
//
//			if (!CommonUtl.validTimeDate(request.getTimestamp(), this.windowTimestamp)) {
//				request.setStatusCode("411");
//				request.setMsg(Constants.POLICY_ERROR_TIMESTAMP_INVALID[0] + ":"
//						+ Constants.POLICY_ERROR_TIMESTAMP_INVALID[1]);
//			} else if ((listS.size() == 0)) {
//				request.setStatusCode("412");
//				request.setMsg(Constants.JSON_SCHEMA_ERROR_SENDER_SUBSCRIBER_INVALID[0] + ":"
//						+ Constants.JSON_SCHEMA_ERROR_SENDER_SUBSCRIBER_INVALID[1]);
//
//			} else if (!validateLookup(reqLookup)) {
//				request.setStatusCode("416");
//				request.setMsg(Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[0] + ":"
//						+ Constants.JSON_SCHEMA_LOOKUP_AUTHHEADER_INVALID[1]);
//
//			} else if (!verifyAuthheader(request.getSignature(), reqLookup.getData(),
//					listS.get(0).getSigningPublicKey())) {
//				request.setMsg(Constants.JSON_SCHEMA_ERROR_LOOKUP_INVALID[0] + ":"
//						+ Constants.JSON_SCHEMA_ERROR_LOOKUP_INVALID[1]);
//				request.setStatusCode("401");
//			} else {
//				request.setStatusCode("200");
//				MessageResponse reponse = new MessageResponse();
//				// NPApiLogs logs = saveLogs(reqLookup);
//				Date dt = new Date();
//				List<ApiEntityMasterProjection> list = null;
//				if (listGlobal == null) {
//					if (request.getSearchParameters().getSorRequired()) {
//						list = this.entityMasterRepository.findByAll(reqLookup.getCountry(), reqLookup.getCity(),
//								reqLookup.getType(), "", reqLookup.getDomain(), reqLookup.getSubscriberId());
//					} else {
//						list = this.entityMasterRepository.findByAllWithoutSOR(reqLookup.getCountry(),
//								reqLookup.getCity(), reqLookup.getType(), "", reqLookup.getDomain(),
//								reqLookup.getSubscriberId());
//					}
//					listGlobal = this.entityMasterRepository.findByAll(reqLookup.getCountry(), reqLookup.getCity(),
//							reqLookup.getType(), "", reqLookup.getDomain(), reqLookup.getSubscriberId());
//					
//
//				} else {
//
//					list = listGlobal;
//				}
//				if (!hirachy) {
//					Map<String, Integer> map = new HashMap();
//					CommonUtl.logInfo("DB CAL time :" + (new Date().getTime() - dt.getTime()));
//
//					list.forEach(item -> {
//						String key = item.getSUBSCRIBERID();
//						if (map.get(key) == null) {
//							ResponsEntityMaster rp = new ResponsEntityMaster(item);
//							oList.add(rp);
//							map.put(key, oList.size() - 1);
//						}
//						oList.get(map.get(key)).addNp(item);
//					});
//					oListGlobal = oList;
//				} else {
//					return oListGlobal;
//				}
//				CommonUtl.logInfo("OBJ CAL time :" + (new Date().getTime() - dt.getTime()));
//
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			log.info("lookup: exception : {} , request : {}", CommonUtl.getPrintStackString(e),
//					CommonUtl.toJsonStr(request));
//			sendError.sendError(e, request, Constants.HIGH);
//		}
//		return oList;
//
//	}

}
