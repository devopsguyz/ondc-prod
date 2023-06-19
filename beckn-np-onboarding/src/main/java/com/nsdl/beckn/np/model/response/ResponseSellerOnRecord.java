package com.nsdl.beckn.np.model.response;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.nsdl.beckn.np.utl.CommonUtl;

import lombok.Data;

@Data
public class ResponseSellerOnRecord {

	@JsonProperty("unique_key_id")
	String uniqueKeyId;

	@JsonProperty("key_pair")
	ResponseKeyPair keyPair;

	@JsonProperty("city_code")

	List<String> cityCode;

	public ResponseSellerOnRecord(ApiEntityMasterProjection obj) {
		this.uniqueKeyId = obj.getSELLERUNIQUEKEYID();
		this.keyPair = new ResponseKeyPair(obj.getSELLERSIGNING(), obj.getSELLERENCRYPTION(),
				 CommonUtl.getSQLDateString(obj.getSELLERVALIDFROM()) , CommonUtl.getSQLDateString(obj.getSELLERVALIDUNTIL()) );
		this.cityCode = new Gson().fromJson(obj.getSELLERCITYCODE(), List.class);
	}
	public ResponseSellerOnRecord(Object[] obj) {
		this.uniqueKeyId = obj[18].toString();
		this.keyPair = new ResponseKeyPair(obj[17].toString(), obj[16].toString(),
				 CommonUtl.getDateString((OffsetDateTime) obj[19]) , CommonUtl.getDateString((OffsetDateTime) obj[20])) ;
		this.cityCode = (List)obj[3];
	}
	
	public ResponseSellerOnRecord(MatViewResponse obj) {
		this.uniqueKeyId = obj.getSELLERUNIQUEKEYID().toString();
		this.keyPair = new ResponseKeyPair(obj.getSELLERSIGNING(), obj.getSELLERENCRYPTION(),
				 CommonUtl.getDateString(obj.getSELLERVALIDFROM()) , CommonUtl.getDateString(obj.getSELLERVALIDUNTIL()));
		this.cityCode = Arrays.asList(new Gson().fromJson(obj.getCITYCODE(), String[].class));
	}

	
}
