package com.learn.pleion_javacoap.server_dtls.mydemo.observerdemo;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.learn.pleion_javacoap.server_dtls.mydemo.observerdemo.myresc.MyObserverResource;
import com.mbed.coap.observe.SimpleObservableResource;
import com.mbed.coap.packet.BlockSize;
import com.mbed.coap.server.CoapServer;
import com.mbed.coap.server.CoapServerBuilder;
import com.mbed.coap.transmission.SingleTimeout;
import com.mbed.coap.transport.InMemoryCoapTransport;

public class TestObserver_Simple {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//
	    SimpleObservableResource obsResource = null;

		// 如果不填参数，则默认端口是5683
		// 这里我尝试自己定义一个端口5656
		//CoapServer server = CoapServer.builder().transport(5683).build();
		//CoapServer server = CoapServerBuilder.newBuilder().transport(new InMemoryCoapTransport(5683))
        //        .timeout(new SingleTimeout(500)).build();
	    //
	    // ref:java-coap/coap-core/src/test/java/protocolTests/ObservationTest.java 
	    /*
		CoapServer server = CoapServerBuilder.newBuilder().transport(5683)
                .timeout(new SingleTimeout(500)).blockSize(BlockSize.S_128).build();
	     */
	    
		//CoapServer server = CoapServer.builder().transport(5683).build();
		CoapServer server = CoapServer.builder().transport(5683).build();

		//
		MyObserverResource OBS_RESOURCE_1 = new MyObserverResource("hlllloooo", server);
		
		
		
		
		// 注意 这里的 hello 大小写是敏感的
		// 因为 client那边 是根据 coap://localhost:5656/hello 来发送请求的
		//server.add(new MyObserverResource("hello_observer"));
		server.addRequestHandler("/hello_observer", OBS_RESOURCE_1);
		try {
			server.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
}
