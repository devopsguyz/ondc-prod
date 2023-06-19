package com.nsdl.beckn.np.utl;

public class KarzaErrors {
//errors
	public static int ERROR_200 = 200;
	public static int ERROR_400 = 400;
	public static int ERROR_401 = 401;
	public static int ERROR_402 = 402;
	public static int ERROR_500 = 500;
	public static int ERROR_503 = 503;
	public static int ERROR_504 = 504;
	public static int ERROR_204 = 204;
	public static int ERROR_205 = 205;

	public static String[] ERROR_DESCRIPTION_200 = new String[] { "200", "Request Successful" };
	public static String[] ERROR_DESCRIPTION_400 = new String[] { "400",
			"Bad Request - Mandatory fields are missing / invalid" };
	public static String[] ERROR_DESCRIPTION_401 = new String[] { "401",
			"Unauthorized Access - API Key is missing or invalid." };
	public static String[] ERROR_DESCRIPTION_402 = new String[] { "402",
			"Insufficient Credits - Credits to access the APIs expired." };
	public static String[] ERROR_DESCRIPTION_500 = new String[] { "500",
			"Internal Server Error - Internal processing " };
	public static String[] ERROR_DESCRIPTION_503 = new String[] { "503",
			"Source Unavailable - The source for authentication is down for maintenance or inaccessible." };
	public static String[] ERROR_DESCRIPTION_504 = new String[] { "504",
			"Endpoint Request Timed Out - The response latency from the source for authentication is >30sec." };
	public static String[] ERROR_DESCRIPTION_204 = new String[]{"204","Invalid Pan Number"};
	public static String[] ERROR_DESCRIPTION_205 = new String[]{"205","Invalid GST Number"};

}
