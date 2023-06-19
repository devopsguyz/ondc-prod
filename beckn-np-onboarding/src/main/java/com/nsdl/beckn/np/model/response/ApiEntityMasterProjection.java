package com.nsdl.beckn.np.model.response;
 

import java.sql.Timestamp;
import java.util.UUID;

public interface ApiEntityMasterProjection {

//	@Column(name = "Id", columnDefinition = "char(36)")
//	@Type(type = "org.hibernate.type.UUIDCharType")
	public String getID();

	public Timestamp getCREATED();

	public Timestamp getUPDATED();

	public String getCALLBACK();

	public String getCITYCODE();

	public String getECITYCODE();

	public String getCOUNTRY();

	public String getENCRYPT();

	public String getSIGNING();

	public String getSTATUS();

	public String getSUBSCRIBERID();

	public String getUNIQUEKEYID();

	public Timestamp getVALIDFROM();

	public Timestamp getVALIDUNTIL();

	public String getDOMAIN();

	public String getTYPE();

	public String getSELLERENCRYPTION();

	public String getSELLERSIGNING();

	public String getSELLERCITYCODE();

	public String getSELLERUNIQUEKEYID();

	public Timestamp getSELLERVALIDFROM();

	public Timestamp getSELLERVALIDUNTIL();

	public String getSUBSCRIBERURL();

	public boolean getMSN();
}
