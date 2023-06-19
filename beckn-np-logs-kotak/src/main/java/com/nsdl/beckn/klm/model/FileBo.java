package com.nsdl.beckn.klm.model;

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
