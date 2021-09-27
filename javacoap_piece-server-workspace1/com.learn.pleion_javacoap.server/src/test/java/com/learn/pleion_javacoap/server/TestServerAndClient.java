package com.learn.pleion_javacoap.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.learn.pleion_javacoap.server.mydemo.MyResource;
import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.server.CoapServer;

class TestServerAndClient {

	CoapServer server 		= null;
	MyResource myobResc1 	= null;
	
	InetSocketAddress inetSocketAddr = null;
	CoapClient client=null;
	@BeforeEach
	void beforesomething() {
		server = CoapServer.builder().transport(5683).build();
		//
		//
		//
		MyResource myobResc1 = new MyResource();
		server.addRequestHandler("/obs", myobResc1);
		
		
		//
		//--------------------------------------------------------------------
		inetSocketAddr = new InetSocketAddress("localhost",5683);
		try {
			client = CoapClientBuilder.newBuilder(inetSocketAddr).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterEach
	void aftersomething() {

		//
		//
		//
		//server.stop();
		// 停留一段时间 让server继续运行
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		//
		//--------------------------------------------------------------------
		//client.close();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	void mytest() {
		//--------------------------------------------------------------------
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
		//--------------------------------------------------------------------
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//--------------------------------------------------------------------
		//--------------------------------------------------------------------
		CoapPacket coapResp = null;
		try {
			coapResp = client.resource("/obs").sync().get();
		} catch (CoapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (coapResp!=null) {
        
        	System.out.println( coapResp.getCode().name() );
        	//System.out.println( coapResp.getOptions() );
        	//System.out.println( "response text:" + coapResp.getResponseText() );
        	System.out.println( "payload:" + new String(coapResp.getPayload()) );
        	
        	//System.out.println(xml);
        	
        } else {
        	
        	System.out.println("Request failed");
        	
        }	
		
		
		
	}
	
	
	@Test
	void mytest2() throws Exception {

		throw new Exception("ss");
		
		
	}
	
	
	
	@Test
	void test() {
		int a[] = {0,1,2,3};
		int i = 0;
		for(i=0; i<=a.length-1 ;i++) {
			System.out.println(a[i]);
		}
	}
	
}
