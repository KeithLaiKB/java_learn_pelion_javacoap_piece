package com.learn.pleion_javacoap.client_dtls;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;
/**
 * 
 * 
 * @author laipl
 *
 *	asynchronous, 消息还没来,就不可以输出hello!!!
 *
 *
 */
public class TestMain_Synchronous {
	public static void main(String[] args) {
		// 如果想要ctrl+鼠标右键看更多信息, 它貌似看不了, 
		// 它需要你去把github源码下载下来, 然后external folder过去 
		CoapClient client=null;
		try {
			InetSocketAddress inetSocketAddr = new InetSocketAddress("localhost",5683);
			client = CoapClientBuilder.newBuilder(inetSocketAddr).build();
	        //
	        CoapPacket coapResp = client.resource("/obs").sync().get();
	        // 
	        if (coapResp!=null) {
	        	//
	        	System.out.println( coapResp.getCode().name() );
	        	//System.out.println( coapResp.getOptions() );
	        	//System.out.println( "response text:" + coapResp.getResponseText() );
	        	System.out.println( "payload:" + new String(coapResp.getPayload()) );
	        	//System.out.println(xml);
	        	
	        } 
	        else {
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
		
		// 你可以在server 那边的myresource 的get方法打个断点, 让server返回的慢一点
		// 这样你就  在server返回的playload之后 才可以在client这边先看到 hello!!!!, 
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