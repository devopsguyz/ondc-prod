package com.nsdl.onboading.gst;

import lombok.Data;

@Data
public class ResponseAspSecret {
	String auth_token;
	String sek;
	  
	Integer status_cd;
}
