package com.vacchk;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.Twilio;

public class WhatsSend {

	public WhatsSend(String ACCOUNT_SID, String AUTH_TOKEN) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	public void sendMessage(String string) {
		try {
			Runtime.getRuntime().exec("curl https://notify.run/iOH -d \""+string+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(new java.util.Date()+" msg: " + string);
		Message message = Message.creator(new com.twilio.type.PhoneNumber("whatsapp:+to-number"),
				new com.twilio.type.PhoneNumber("whatsapp:+fromnumber"), string).create();

		System.out.println(message.getSid());
	}

	public void sendMsg(List ret, String date) {
		
		String message = "AAYAAA AVAILBALE " + date;
		//sendMessage(message);
		Set set = new HashSet(ret);
		System.out.println(ret);
		for (Object object : set) {
			Map centerInfo = (Map) object;
			Integer pincode = (Integer) centerInfo.get("pincode");
			message += "centr: " + centerInfo.get("name").toString() + " pin: " + centerInfo.get("pincode").toString();
			if(pincode==560068) {
				System.out.println(new java.util.Date()+" PIIIIIIN "+pincode+" - "+centerInfo);
			}
		}
		message = message.length()<1000?message:message.substring(0, 1000);
		
		sendMessage(message);
	}
}
