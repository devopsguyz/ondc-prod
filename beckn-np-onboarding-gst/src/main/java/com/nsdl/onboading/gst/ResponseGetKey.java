package com.nsdl.onboading.gst;

import lombok.Data;

@Data
public class ResponseGetKey {
	String enc_key;
	String session_id;
	String message_id;
	String message;
	Integer status_cd;
}
