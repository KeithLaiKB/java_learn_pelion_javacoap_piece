package com.learn.pleion_javacoap.server.mydemo;

import java.io.IOException;

import com.mbed.coap.server.CoapServer;

public class TestMain_Simple {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CoapServer server = CoapServer.builder().transport(5683).build();
		//
		
		MyResource myobResc1 = new MyResource();
		//------------------------operate server-------------------------------------
		//
		//server.addRequestHandler("/obs", myobResc1);
		server.addRequestHandler("/hello", myobResc1);
		//
		//
		try {
			server.start();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			System.out.println("kk");
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("gg");
			e1.printStackTrace();
		}
	
		//
		//		
		// does all the magic
		//
		//
		// 停留一段时间 让server继续运行
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		// destroy server
		// because the resource use the timer
		//
		server.stop();
		// 停留一段时间 让server继续运行
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		//System.out.println("destroy the server and stop the resource timer finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	
}
