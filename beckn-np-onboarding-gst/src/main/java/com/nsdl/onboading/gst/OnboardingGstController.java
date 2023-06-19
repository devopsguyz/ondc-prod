package com.nsdl.onboading.gst;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OnboardingGstController {

	@Autowired
	Gson gson;

	@Value("${gst.get.key.url}")
	String gstGetKeyUrl;

	@Value("${gst.authenticate.url}")
	String gstAuthenticateUrl;
	@Value("${gst.search.url}")
	String gstSearchUrl;

	@Value("${aspid}")
	String aspid;
	@Value("${state-cd}")
	String stateCd;
	@Value("${ip-usr}")
	String ipUsr;

	@Value("${filler1}")
	String filler1;
	@Value("${filler2}")
	String filler2;

	@Value("${gst.key.jks.file}")
	String gstKeyJksFile;

	@Value("${JKSPassword}")
	String JKSPassword;
	@Value("${PFXPassword}")
	String PFXPassword;

	@Value("${asp.secret}")
	String aspSeret;

	@Value("${action}")
	String action;

	@Value("${gst.username}")
	String username;

	@Value("${search.gst.action}")
	String searchGstAction;

	@PostMapping("/check/gst")
	public ResponseEntity<String> cheackPan(@RequestBody Gst gst) {
		log.info("GST Request: of request body: {}", gson.toJson(gst));
		String ret = "";

		gst.aspId = aspid;
//		gst.gst = "27AVXPM6863B1Z0";
		String asp_secret = aspSeret;
		gst.state_cd = stateCd;

		gst.ip_usr = ipUsr;
		gst.filler1 = filler1;
		gst.filler2 = filler2;

		gst.action = action;
 
		// gst.encKey="J6wVWToLIxe5SJJustrMe61bG4ddQD8r5p2EoXAMqTsP9md31Sw7ogc9jmPXDrui";
		gst.searchGstAction = searchGstAction;
		gst.username = username;

		CryptoGetKeyGenerateSignature.getKeySignature(gst.aspId, JKSPassword, PFXPassword, gstKeyJksFile, gst);

		try {
			if("NOADDR".equals(gst.getAddress())) {
				gst.setAddress("");
			}
			APIGetKey.getGstgetKeyDtl(gstGetKeyUrl, gst);
			log.info(gst.responseGstKey.enc_key);

			gst.encKey = gst.responseGstKey.enc_key;

			CryptoAspSecretAESEncryption.createAspScreate(gst, asp_secret);
			log.info("asp_secret:" + gst.enc_asp_secret);
			APIAspSecret.getGstAspSecret(gstAuthenticateUrl, gst);
			log.info("auth_token:" + gst.responseAspSecret.auth_token);

			APISearchGst.getGstSearch(gstSearchUrl, gst);

			log.info(gst.responseGstSearch.data);
		//	log.info(new String(Base64.getDecoder().decode(gst.responseGstSearch.data)));
			ret = validate(gst);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("GST Response: of request body: {}", e.getMessage());
			ret = "NW:2006:Please GST try again later.";
			log.info("GST Response Error: of request body: {}", gson.toJson(gst));
		}
		log.info("GST Response: of request body: {}", gson.toJson(gst));

		return ResponseEntity.ok(ret);
	}

	public static String validate(Gst body) {
		String ret = "";
		if ("1".equals(body.getResponseGstSearch().getStatus_cd())) {
			String dcodeData=new String(Base64.getDecoder().decode(body.getResponseGstSearch().getData()));
			log.info(dcodeData);
			ResponseGstData data = new Gson().fromJson(dcodeData
					, ResponseGstData.class);
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
		} else if("0".equals(body.getResponseGstSearch().getStatus_cd())) {
			ret = "NG:2001:Entered GSTIN not available in GST database";
		}else {
			ret = "EXCEPTION:"+body.getResponseGstSearch().getStatus_cd();
		}
		System.out.println(ret);
		return ret;
	}

	public static boolean compareAddresString(Gst gst, ResponseGstData data) {
		try {
			if (data.getAdadr() == null && "".equals(gst.getAddress())) {
				return true;
			} else if (data.getPradr() == null  ) {
				return false;
			} else {
				gst.address = gst.address.toLowerCase().trim();
				boolean flag = compareIndexString(gst.address, data.getPradr().getAddr().getBnm());
				log.info("Address Bnm:"+flag);
				if (flag) {
					
					flag = compareIndexString(gst.address, data.getPradr().getAddr().getBno());
					log.info("Address Bno:"+flag);
					if (flag) {
						flag = compareIndexString(gst.address, data.getPradr().getAddr().getFlno());
						log.info("Address Flno:"+flag);
						if (flag) {
							flag = compareIndexString(gst.address, data.getPradr().getAddr().getLoc());
							log.info("Address Loc:"+flag);
							if (flag) {
								flag = compareIndexString(gst.address, data.getPradr().getAddr().getPncd());
								log.info("Address Pncd:"+flag);
								if (flag) {
									flag = compareIndexString(gst.address, data.getPradr().getAddr().getSt());
									log.info("Address st:"+flag);
									if (flag) {
										flag = compareIndexString(gst.address, data.getPradr().getAddr().getStcd());
										log.info("Address stcd:"+flag);
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

}
