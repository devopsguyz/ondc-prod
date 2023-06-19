package com.nsdl.beckn.klm.dto;

public class TransactionDTO {

	private String transactionId;
	private String typeValue;
	private String serverValue;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public String getServerValue() {
		return serverValue;
	}
	public void setServerValue(String serverValue) {
		this.serverValue = serverValue;
	}
	@Override
	public String toString() {
		return "TransactionDTO [transactionId=" + transactionId + ", typeValue=" + typeValue + ", serverValue="
				+ serverValue + "]";
	}
	
	 
	
}
