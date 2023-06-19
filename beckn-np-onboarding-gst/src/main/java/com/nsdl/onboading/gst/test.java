package com.nsdl.onboading.gst;
 
import java.util.Base64;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {

	public static String validate(Gst body) {
		String ret = "";
		if ("1".equals(body.getResponseGstSearch().getStatus_cd())) {
			ResponseGstData data = new Gson().fromJson(
					new String(Base64.getDecoder().decode(body.getResponseGstSearch().getData())),
					ResponseGstData.class);
			boolean nameFlag = compareString(body.getName(), data.getLgnm());
			boolean addressFlag = compareAddresString(body, data);
			;
			if (!nameFlag && !addressFlag) {
				ret = "NA:2002:Enter Name and Address as per GST certificate";
			} else if (nameFlag && !addressFlag) {
				ret = "GA:2003:Enter Address as per GST certificate.";
			} else if (!nameFlag && addressFlag) {
				ret = "GN:2004:Enter Name as per GST certificate.";
			} else if (nameFlag && addressFlag) {
				ret = "MM:2005:Name and Address shared correctly.";
			} else {
				ret = "NW:2006:Please try again later.";
			}
		} else {
			ret = "NG:2001:Entered GSTIN not available in GST database";
		}
System.out.println(ret);
		return ret;
	}
	
	public static boolean compareAddresString(Gst gst, ResponseGstData data) {
		try {
			if (data.getAdadr() == null && "".equals(gst.getAddress())) {
				return true;
			}else if(data.getAdadr() ==null || data.getAdadr().length==0) {
				return false;
			} else {
				gst.address=gst.address.toLowerCase().trim();
				boolean flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getBnm());
				if (flag) {
					flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getBno());
					if (flag) {
						flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getFlno());
						if (flag) {
							flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getLoc());
							if (flag) {
								flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getPncd());
								if (flag) {
									flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getSt());
									if (flag) {
										flag = compareIndexString(gst.address, data.getAdadr()[0].getAddr().getStcd());

									}
								}
							}
						}
					}
				}
				return flag;
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.info(e.getMessage());
		}
		return false;
	}

	public static boolean compareString(String str1, String str2) {
		try {

			return str1.toLowerCase().trim().equals(str2.toLowerCase().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static boolean compareIndexString(String str1, String str2) {
		try {

			return str1.indexOf(str2.toLowerCase().trim()) != -1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public static void main(String[] args) {
	 
		Gst gst=new Gson().fromJson("{"
				+ "    \"name\": \"MS CORPORATION\","
				+ "    \"date\": \"01/07/2017\","
				+ "    \"gst\": \"05ABNTY3290P8ZA\","
				+ "    \"address\":\"1ST FLOOR,ELPHINSTONE BUILDING,10, VEER NARIMAN ROAD,FORT 10,Rajasthan,400001\""
				+ "}",Gst.class);
		gst.responseGstSearch=new ResponseGstSearch();
		gst.responseGstSearch.status_cd="1";
		gst.responseGstSearch.data =Base64.getEncoder().encodeToString(("{"
				+ "  \"stjCd\": \"AP003\","
				+ "  \"lgnm\": \"MS CORPORATION\","
				+ "  \"stj\": \"BCP KODIKONDA\","
				+ "  \"dty\": \"Regular\","
				+ "  \"cxdt\": \"\","
				+ "  \"gstin\": \"05ABNTY3290P8ZA\","
				+ "  \"einvoiceStatus\": \"Yes\","
				+ "  \"nba\": ["
				+ "    \"Bonded Warehouse\","
				+ "    \"EOU / STP / EHTP\","
				+ "    \"Factory / Manufacturing\","
				+ "    \"Input Service Distributor (ISD)\","
				+ "    \"Leasing Business\""
				+ "  ],"
				+ "  \"lstupdt\": \"05/01/2017\","
				+ "  \"rgdt\": \"05/05/2017\","
				+ "  \"ctb\": \"Foreign LLP\","
				+ "  \"sts\": \"Provisional\","
				+ "  \"ctjCd\": \"AP004\","
				+ "  \"ctj\": \"BCP THUMMAKUNTA\","
				+ "  \"tradeNam\": \"ALTON PLASTIC PRIVATE LTD\","
				+ "  \"adadr\": ["
				+ "    {"
				+ "      \"addr\": {"
				+ "        \"bnm\": \"ELPHINSTONE BUILDING\","
				+ "        \"st\": \"10, VEER NARIMAN ROAD\","
				+ "        \"loc\": \"FORT\","
				+ "        \"bno\": \"10\","
				+ "        \"stcd\": \"Rajasthan\","
				+ "        \"flno\": \"1ST FLOOR\","
				+ "        \"lt\": \"74.2179\","
				+ "        \"lg\": \"27.0238\","
				+ "        \"pncd\": \"400001\""
				+ "      },"
				+ "      \"ntr\": ["
				+ "        \"Wholesale Business\""
				+ "      ]"
				+ "    }"
				+ "  ],"
				+ "  \"pradr\": {"
				+ "    \"addr\": {"
				+ "      \"bnm\": \"KATGARA HOUSE\","
				+ "      \"st\": \"15, L JAGMOHANDAS MARG\","
				+ "      \"loc\": \"MALABAR HILL\","
				+ "      \"bno\": \"5\","
				+ "      \"stcd\": \"Maharashtra\","
				+ "      \"flno\": \"4TH FLOOR\","
				+ "      \"lt\": \"74.2179\","
				+ "      \"lg\": \"27.0238\","
				+ "      \"pncd\": \"400006\""
				+ "    },"
				+ "    \"ntr\": ["
				+ "      \"Wholesale Business\""
				+ "    ]"
				+ "  }"
				+ "}").getBytes());
		validate(gst);
		
	}

}
