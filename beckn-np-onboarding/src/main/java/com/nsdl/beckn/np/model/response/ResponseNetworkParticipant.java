package com.nsdl.beckn.np.model.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.Data;

@Data
public class ResponseNetworkParticipant {
	@JsonProperty("subscriber_url")
	String subscriberUrl;
	@JsonProperty("domain")
	String domain;
//	@JsonProperty("callback_url")
//	String callbackUrl;
	@JsonProperty("type")
	String type;
	@JsonProperty("msn")
	boolean msn;
	@JsonProperty("city_code")
	List<String> cityCode;
	@JsonProperty("seller_on_record")
	List<ResponseSellerOnRecord> sellerOnRecordMasters = new ArrayList<>();

	@JsonIgnore()
	Map<String, Integer> mapSeller = new HashMap();

	public ResponseNetworkParticipant(ApiEntityMasterProjection obj) {

		this.domain = obj.getDOMAIN();
		this.cityCode = Arrays.asList(new Gson().fromJson(obj.getCITYCODE(), String[].class));
		this.subscriberUrl = obj.getSUBSCRIBERURL();
		this.type = obj.getTYPE();
		this.msn = obj.getMSN();

	}

	public ResponseNetworkParticipant(Object[] obj) {

		this.domain = obj[11].toString();
		this.cityCode = (List) obj[1];
		this.subscriberUrl = obj[21].toString();
		this.type = obj[23].toString();
		this.msn = (Boolean) obj[22];

	}
	
	public ResponseNetworkParticipant(MatViewResponse obj) {

		this.domain = obj.getDOMAIN();
		this.cityCode = Arrays.asList(new Gson().fromJson(obj.getCITYCODE(), String[].class));;
		this.subscriberUrl = obj.getSUBSCRIBERURL();
		this.type = obj.getTYPE();
		this.msn = obj.getMSN();

	}

	public void addSeller(ApiEntityMasterProjection obj) {
		String key = obj.getSELLERUNIQUEKEYID();
		if (mapSeller.get(key) == null) {
			ResponseSellerOnRecord np = new ResponseSellerOnRecord(obj);
			this.sellerOnRecordMasters.add(np);
			mapSeller.put(key, this.sellerOnRecordMasters.size() - 1);
		}

	}
	
	public void addSeller(Object[] obj) {
		String key = obj[18].toString();
		if (mapSeller.get(key) == null) {
			ResponseSellerOnRecord np = new ResponseSellerOnRecord(obj);
			this.sellerOnRecordMasters.add(np);
			mapSeller.put(key, this.sellerOnRecordMasters.size() - 1);
		}

	}
	
	public void addSeller(MatViewResponse obj) {
		String key = obj.getSELLERUNIQUEKEYID();
		if (mapSeller.get(key) == null) {
			ResponseSellerOnRecord np = new ResponseSellerOnRecord(obj);
			this.sellerOnRecordMasters.add(np);
			mapSeller.put(key, this.sellerOnRecordMasters.size() - 1);
		}

	}

}
