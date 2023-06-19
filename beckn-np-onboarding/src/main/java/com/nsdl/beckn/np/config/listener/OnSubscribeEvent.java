package com.nsdl.beckn.np.config.listener;

import java.io.Serializable;

import org.springframework.context.ApplicationEvent;

import lombok.Data;

@Data
public class OnSubscribeEvent extends ApplicationEvent implements Serializable {

	String url;

	public OnSubscribeEvent(Object source, String url) {
		super(source);
		this.url = url;
		// TODO Auto-generated constructor stub
	}

}