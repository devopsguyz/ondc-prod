package com.nsdl.beckn.np.model.response;

import com.nsdl.beckn.np.utl.Constants;

import lombok.Data;

@Data
public class MessageCustomeResponse<T> extends MessageResponse {

	T list;
	
	public MessageCustomeResponse (T message){
		this.list=message;
	}
 
 	
}