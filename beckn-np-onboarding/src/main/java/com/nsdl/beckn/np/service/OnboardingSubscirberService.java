package com.nsdl.beckn.np.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.nsdl.beckn.np.model.NPApiLogs;
import com.nsdl.beckn.np.model.request.ErrorResponse;
import com.nsdl.beckn.np.model.request.RequestOldSearch;
import com.nsdl.beckn.np.model.request.RequestSearch;
import com.nsdl.beckn.np.model.request.RequestText;
import com.nsdl.beckn.np.model.request.SubscribeBody;
import com.nsdl.beckn.np.model.response.ResponsEntityMaster;
import com.nsdl.beckn.np.model.response.ResponsOldEntityParentMaster;
import com.nsdl.beckn.np.model.response.ResponseText;

public interface OnboardingSubscirberService {	
    /**
     * It is a saves logs response time by time.
     * @param time It is a local time.
     */
	void saveLogsResponseTime(Integer time,NPApiLogs logs);
    /**
     * It is a saves logs response 
     * @param resonse the log response.
     */
	
	public NPApiLogs saveLogs(Object req);
	void saveLogsResponse(ResponseEntity resonse,NPApiLogs logs);
    /**
     * Create a error response with reqSubscribe. 
     * @param reqSubscribe the Subscribe  Body.
     * @return Returns SubscribeBody with context, massage and msgerror.
     */
	ErrorResponse onSubscribe(SubscribeBody reqSubscribe);
    /**
     * Gets public key.
     * @return Returns Public key.
     */
	Map<String, String> getPublicKey();

	String initRKey();

	List<ResponsEntityMaster> lookup(RequestSearch reqLookup);

	boolean checkAuthorization(String authorization, String data, String subscriberId);

	ResponseText getEncryptText(RequestText req);

	ResponseText decryptText(RequestText req);

	void caheRefresh(String url);

	List<ResponsOldEntityParentMaster> lookupOld(RequestOldSearch request);
	public String health();
	public List<ResponsOldEntityParentMaster> lookupKomn(RequestOldSearch reqLookup) ;
	
//	public List<ResponsEntityMaster> lookupTest(RequestSearch request, boolean hirachy);
	 
}
