package com.nsdl.beckn.lm.registry.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class FileBo {
     
	@Id
	@GeneratedValue
	private int id;
	String fileName;
	String blob;
	String fileType;
	
}
