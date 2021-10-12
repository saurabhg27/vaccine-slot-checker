package com.vacchk;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

public class VacChekr extends Thread {

	Logger logger = LoggerFactory.getLogger(VacChekr.class);
	public static void main(String[] args) {
		
		String urlToCheck = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=560068&date=13-05-2021";
		List li = new VacChekr(null, 45, 100000).check(urlToCheck );
		System.out.println(li);
		//java -jar TWILIO_KEY TWILIO_API_ID 1 45
	}
	
	RestTemplate rt;
	// String url =
	// "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=294&date=02-05-2021";
	String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=294&date=";
	Integer ageToCheck;
	WhatsSend whatssender;
	public boolean goOn = true;
	long sleepTime;
	int aliveCounter = 0;

	public VacChekr(WhatsSend sender, int age, long slpTime) {
		this.whatssender = sender;
		this.rt = new RestTemplate();
		this.ageToCheck = age;
		this.sleepTime = slpTime;
		
		this.rt.getInterceptors().add(new ClientHttpRequestInterceptor() {
			
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				request.getHeaders().set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
	            return execution.execute(request, body);
			}
		});
	}

	@Override
	public void run() {

		try {
			while (goOn) {

				String todaysDate = getTodaysDate();
				for (int bla = 0; bla < 14; bla+=7) {
					int foo = (Integer.parseInt(todaysDate) + bla);
					String date = foo + "-05-2021";
					String urlToCheck = url + date;
					logger.info("checking: "+date+" url:" + urlToCheck);
					List ret = check(urlToCheck);
			
					if (ret.size() != 0) {
						whatssender.sendMsg(ret, date);
						logger.info("retList --> {}",ret);
//					goOn = false;
						break;
					}
				}

				
				if (aliveCounter == 0 || aliveCounter == 50) {
		//			whatssender.sendMessage("alive @ " + new Date());
					aliveCounter = 0;
				}
				aliveCounter++;
				Thread.sleep(sleepTime);

			}
		} catch (Exception e) {
			throw new RuntimeException("loop failed", e);
		}
	}

	private List check(String urlToCheck) {
		List retList = new ArrayList();
		Map json = rt.getForObject(urlToCheck, Map.class);
		List li = (List) json.get("centers");
		for (int i = 0; i < li.size(); i++) {
			Map centerInfo = (Map) li.get(i);
			String feeType = (String) centerInfo.get("fee_type");
			List sessionList = (List) centerInfo.get("sessions");
			Integer pincode = (Integer) centerInfo.get("pincode");
			//System.out.println("pincode: "+pincode);
			for (int k = 0; k < sessionList.size(); k++) {
				Map sessionInfo = (Map) sessionList.get(k);
				Integer sessionAge = (Integer) sessionInfo.get("min_age_limit");
				Integer cap = tryParse(sessionInfo.get("available_capacity").toString());

				if (sessionAge <= ageToCheck && cap > 0) {
					//System.out.println( "aaya: " +i+sessionInfo.get("available_capacity")+" "+ sessionInfo);
					if (feeType.equalsIgnoreCase("Paid")) {
						retList.add(centerInfo);
					} else {
						retList.add(centerInfo);
					}

				}
			}
			// System.out.println(centerInfo.get("name"));
		}
		return retList;
	}

	DateFormat dateFormat = new SimpleDateFormat("dd");// yyyy/MM/dd HH:mm:ss

	private String getTodaysDate() {

		return dateFormat.format(new Date());
	}

	private static Integer tryParse(String obj) {
		Integer retVal;
		try {
			retVal = Integer.parseInt(obj);
		} catch (NumberFormatException nfe) {
			retVal = -1;
		}
		return retVal;
	}
}
