package com.nsdl.beckn.lm.audit.event;

import java.awt.desktop.FilesEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
	
	private final ApplicationEventPublisher eventPublisher;
	
	public EventPublisher(ApplicationEventPublisher Publisher) {
		this.eventPublisher = Publisher;
	}
	public void PublishFileEvent(FileEvent event) {
		eventPublisher.publishEvent(event);
	}

}
