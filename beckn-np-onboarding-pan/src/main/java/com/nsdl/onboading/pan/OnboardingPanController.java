package com.nsdl.onboading.pan;

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
public class OnboardingPanController {

	@Autowired
	Gson gson;

	@Value("${pan.url}")
	String panUrl;

	@Value("${pan.certificate.file}")
	String certificateFile;

	@Value("${pan.certificate.pwd}")
	String certificatePwd;

	@Value("${pan.certificate.jks.file}")
	String jksFile;

	@Value("${pan.userid}")
	String userId;

	@PostMapping("/check/pan")
	public ResponseEntity<String> cheackPan(@RequestBody Pan body) {
		log.info("PAN: of request body: {}", gson.toJson(body));
		String ret = "";
		try {
			p2j.createSig(certificateFile, certificatePwd, jksFile);
			String sig = pkcs7gen.genSig(jksFile, certificatePwd, userId, body.getPan());
			String data = APIBased.getPanDtl(panUrl, userId, body.getPan(), sig);
			if (data.substring(0, 1).equals("1")) {
				ret = validate(data, body);
			} else {
				ret = "NW:2006:Please try again later. (status:"+data+") ";
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error("PAN:"+e.getMessage()); 
			ret = "NW:2006:Please PAN try again later.";
		}
		return ResponseEntity.ok(ret);
	} 
	
	
	public String validate(String data, Pan body) {
		String ret = "";
		String dataArray[] = data.split("\\^");
		if (dataArray.length >= 2 && !compareString(body.getPan(), dataArray[1])) {
			ret = "NP:2001:PAN Input mismatch (GN)";
		}
		if (dataArray.length >= 4 && !compareString("E", dataArray[3])) {
			ret = "NP:2001:PAN Status " + dataArray[3];
		}
		boolean nameFlag = false, dateFlag = false;
		if (dataArray.length >= 6) {
			nameFlag = compareNameString(new String[] { dataArray[3], dataArray[4], dataArray[5] }, body.getName());
		}
		if (dataArray.length >= 8) {
			dateFlag = compareString(body.getDate(), dataArray[7]);
		}
		if (!nameFlag && !dateFlag) {
			ret = "ND:2002:Enter valid PAN number.";
		} else if (nameFlag && !dateFlag) {
			ret = "PD:2003:Enter Date of Incorporation as per PAN.";
		} else if (!nameFlag && dateFlag) {
			ret = "PN:2004:Enter Name as per PAN.";
		} else if (nameFlag && dateFlag) {
			ret = "BM:2005:Name and Address shared correctly. PAN is verified.";
		}
		ret = "1:" + ret;
		return ret;
	}

	public boolean compareNameString(String str[], String name) {
		try {

			String nameArray[] = name.split(" ");
			for (int i = 0; i < str.length; i++) {
				boolean flag = false;
				for (int j = 0; j < nameArray.length; j++) {
					if (compareString(str[i], nameArray[j])) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					return false;
				}
			}
			if(str.length > 0 && str.length==nameArray.length) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public boolean compareString(String str1, String str2) {
		try {

			return str1.toLowerCase().trim().equals(str2.toLowerCase().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
