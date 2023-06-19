package com.nsdl.beckn.np.model.response;

import java.sql.Timestamp;

public interface MatViewResponse {
	
	public String getID();
	
	public String getCITYCODE();
	
	public String getECITYCODE();
	
	public String getSELLERCITYCODE();
	
	public String getCALLBACK();
	
	public String getENCRYPT();
	
	public String getCOUNTRY();
	
	public String getSIGNING();
	
	public String getSTATUS();
	
	public String getSUBSCRIBERID();
	
	public String getUNIQUEKEYID();
	
	public String getDOMAIN();
	
	public Timestamp getVALIDFROM();
	
	public Timestamp getVALIDUNTIL();
	
	public Timestamp getCREATED();
	
	public Timestamp getUPDATED();
	
	public String getSELLERENCRYPTION();
	
	public String getSELLERSIGNING();
	
	public String getSELLERUNIQUEKEYID();
	
	public Timestamp getSELLERVALIDFROM();
	
	public Timestamp getSELLERVALIDUNTIL();
	
	public String getSUBSCRIBERURL();
	
	public boolean getMSN();
	
	public String getTYPE();

}
