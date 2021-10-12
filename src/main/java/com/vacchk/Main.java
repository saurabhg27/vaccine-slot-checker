package com.vacchk;

public class Main {
	
	public static void main(String[] args) {
		if(args.length < 4) {
			System.err.println("wrong args, sid,tok,slptimne(mins),age");
			System.exit(1);
		}
		WhatsSend sender = new WhatsSend(args[0], args[1]);
		long time = Integer.parseInt(args[2]) * 1000 *60;
		//long time = 4*1000;
		int age = Integer.parseInt(args[3]);
		System.out.println("started with age:"+age+" time:"+time);
		VacChekr obj = new VacChekr(sender ,age, time );
		try {
			obj.start();
		}catch (Exception e) {
			sender.sendMessage("Excep in main: "+e.getMessage());
		}
	}

}
