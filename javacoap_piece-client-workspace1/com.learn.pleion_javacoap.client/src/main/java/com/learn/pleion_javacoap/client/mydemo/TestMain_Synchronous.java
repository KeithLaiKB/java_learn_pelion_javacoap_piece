package com.learn.pleion_javacoap.client.mydemo;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;

public class TestMain_Synchronous {
	public static void main(String[] args) {
		// 如果想要ctrl+鼠标右键看更多信息, 它貌似看不了, 
		// 它需要你去把github源码下载下来, 然后external folder过去 
		CoapClient client=null;
		try {
			InetSocketAddress inetSocketAddr = new InetSocketAddress("localhost",5683);
			client = CoapClientBuilder.newBuilder(inetSocketAddr).build();
	        //
	        //CoapResponse response;
			//
			//response = client.get();
			//String xml = client.get(MediaTypeRegistry.APPLICATION_XML).getResponseText();
	        CoapPacket coapResp = client.resource("/obs").sync().get();
	        // 
	        //
	        //
	        if (coapResp!=null) {
	        
	        	System.out.println( coapResp.getCode().name() );
	        	//System.out.println( coapResp.getOptions() );
	        	//System.out.println( "response text:" + coapResp.getResponseText() );
	        	System.out.println( "payload:" + new String(coapResp.getPayload()) );
	        	//System.out.println(xml);
	        	
	        } else {
	        	
	        	System.out.println("Request failed");
	        	
	        }	
	        //
	        //
		} catch (CoapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 停留一段时间 让server继续运行
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		client.close();
		// 停留一段时间 让server继续运行
		
		
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}