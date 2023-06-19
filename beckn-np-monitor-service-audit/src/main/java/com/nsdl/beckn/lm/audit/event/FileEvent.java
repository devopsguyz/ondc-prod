package com.nsdl.beckn.lm.audit.event;

public class FileEvent {

	private String startDt;
	private String endDt;
	private String userId;
	String date;
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public FileEvent(String startDt, String endDt, String userId) {
		super();
		this.startDt = startDt;
		this.endDt = endDt;
		this.userId = userId;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		// TODO Auto-generated method stub
		return "testing ";
	}

	

}
