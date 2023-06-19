package com.nsdl.beckn.klm.model.projection;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Data;

 

public interface ApiAuditEntityBuyerProjection {
	   
	public String getBuyerId();
   
	public  Integer getCount();
 }