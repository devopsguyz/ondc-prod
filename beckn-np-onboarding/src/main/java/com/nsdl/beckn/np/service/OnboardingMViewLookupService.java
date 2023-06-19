package com.nsdl.beckn.np.service;

import java.util.List;

import com.nsdl.beckn.np.model.request.RequestOldSearch;
import com.nsdl.beckn.np.model.request.RequestSearch;
import com.nsdl.beckn.np.model.response.ResponsEntityMaster;
import com.nsdl.beckn.np.model.response.ResponsOldEntityParentMaster;

public interface OnboardingMViewLookupService {

	List<ResponsEntityMaster> lookup(RequestSearch reqLookup);

	List<ResponsOldEntityParentMaster> lookupOld(RequestOldSearch request);
}
