package com.learn.pleion_javacoap.client.mydemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.utils.Callback;

public class TestMain_Asynchronous {
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
	        //CompletableFuture<CoapPacket> coapResp = client.resource("/obs").get();
	        client.resource("/obs").get(new Callback<CoapPacket>() {
				@Override
				public void call(CoapPacket t) {
					// TODO Auto-generated method stub
					System.out.println( t.getCode().name() );
		        	//System.out.println( coapResp.getOptions() );
		        	//System.out.println( "response text:" + coapResp.getResponseText() );
		        	System.out.println( "payload:" + new String(t.getPayload()) );
				}
				@Override
	            public void callException(Exception ex) {
					System.err.println("Failed");
	            }

	
	        });
	        // 
	        //
	        //
	        //
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 你可以在server 那边的myresource 的get方法打个断点, 让server返回的慢一点
		// 这样你就可以在client这边先看到 hello!!!! 先出现在server返回的playload之前了, 
		// 也就是能够证明是异步了
		System.out.println("hello!!!!!!!!!!!!!!!!!!!!!");
		
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