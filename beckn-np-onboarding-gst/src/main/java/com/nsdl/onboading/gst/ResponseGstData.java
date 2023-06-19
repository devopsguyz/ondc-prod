package com.nsdl.onboading.gst;

import java.util.List;

import lombok.Data;

@Data
public class ResponseGstData {

	// {"rgdt":"01/07/2017","ctb":"Proprietorship","sts":"Active","stj":"BANDRA","ctj":"",
	// "lgnm":"Protean eGov Technologies
	// Limited","dty":"Regular","einvoiceStatus":"No","cxdt":"",
	// "gstin":"27AVXPM6863B1Z0","nba":["Export"],"adadr":"","addr":""}

	String stjCd;
	String lgnm;
	String stj;
	String dty;
	String cxdt;
	String gstin;
	String einvoiceStatus;
	List<String> nba;
	String lstupdt;
	String rgdt;
	String ctb;
	String sts;
	String ctjCd;
	String ctj;
	String tradeNam;
	Adadr adadr[];
	Adadr pradr;

}

@Data
class Adadr {
	Addr addr;
	String ntr;
} 

@Data
class Addr {
	String bnm;
	String st;
	String loc;
	String bno;
	String stcd;
	String flno;
	String lt;
	String lg;
	String pncd;
}
