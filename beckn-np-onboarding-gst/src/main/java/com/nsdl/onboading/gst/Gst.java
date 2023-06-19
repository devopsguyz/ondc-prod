package com.nsdl.onboading.gst;

import lombok.Data;

@Data
public class Gst {
  String timestamp;
  String gst;
  String date;
  String name;
  String address;
  
  String sign;
  String encKey;
  String enc_asp_secret;
  String aspId;
  String state_cd;
  String ip_usr;
  String filler1;
  String filler2;
  String action;
  String username;
  String searchGstAction;
  ResponseGetKey responseGstKey;
  ResponseAspSecret responseAspSecret;
  ResponseGstSearch responseGstSearch;
}
