package com.nsdl.onboading.pan;

public class test {
	public static void main(String[] args) {
		Pan pan = new Pan();
		pan.setDate("25/04/2020");
		pan.setName("VINITA H BHANUSHALI");
		pan.setPan("AAAPW9785A");
		System.out.println(new test().validate("1^AAAPW9785A^E^BHANUSHALI^VINITA^H^Smt^25/04/2020^JAI WANTI^NA^", pan));
	}

	public String validate(String data, Pan body) {
		String ret = "";
		String dataArray[] = data.split("\\^");
		if (dataArray.length >= 2 && !compareString(body.getPan(), dataArray[1])) {
			ret = "NP:PAN Input mismatch (GN)";
		}
		if (dataArray.length >= 3 && !compareString("E", dataArray[3])) {
			ret = "NP:PAN Status " + dataArray[3];
		}
		boolean nameFlag = false, dateFlag = false;
		if (dataArray.length >= 5) {
			nameFlag = compareNameString(new String[] { dataArray[3], dataArray[4], dataArray[5] }, body.getName());
		}
		if (dataArray.length >= 7) {
			dateFlag = compareString(body.getDate(), dataArray[7]);
		}
		if (!nameFlag && !dateFlag) {
			ret = "ND:Name and Date of Inc. do not match (ND)";
		} else if (nameFlag && !dateFlag) {
			ret = "PD:Name matches but Date doesn’t match (PA)";
		} else if (!nameFlag && dateFlag) {
			ret = "PN:Date matches but Name doesn’t match (PN)";
		} else if (nameFlag && dateFlag) {
			ret = "BM:Name and Date of Inc match (BM)";
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
