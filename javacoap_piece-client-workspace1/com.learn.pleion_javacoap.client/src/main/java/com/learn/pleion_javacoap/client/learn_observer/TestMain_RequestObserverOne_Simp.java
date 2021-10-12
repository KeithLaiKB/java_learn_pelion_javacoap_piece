package com.learn.pleion_javacoap.client.learn_observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.client.ObservationListener;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.transport.InMemoryCoapTransport;

public class TestMain_RequestObserverOne_Simp {

	
	public static void main(String[] args) {
		String port1 = "coap://localhost:5656/hello";
		String port2 = "coap://160.32.219.56:5656/hello";		//有线连接树莓派, 路由给的地址是192.168.50.178
																// 我把它的192.168.50.178:5656 映射成160.32.219.56:5656
		String port3 = "coap://160.32.219.56:5657/hello";		//无线连接树莓派, 路由给的地址是192.168.50.179
																// 我把它的192.168.50.179:5656 映射成160.32.219.56:5657
		
		String 	myuri1_hostaddr   				= "localhost";
		int 	myuri1_port 	  				= 5656;
		String 	myuri1_path   					= "/hello_observer";
		
		//CoapClient client2 = new CoapClient(port1);
		/*
		CoapClient client = CoapClientBuilder.newBuilder(InMemoryCoapTransport.createAddress(5683))
                .transport(new InMemoryCoapTransport())
                .timeout(1000).build();
		*/
		InetSocketAddress inetSocketAddr = new InetSocketAddress(myuri1_hostaddr,myuri1_port);
		CoapClient client=null;
		try {
			client = CoapClientBuilder.newBuilder(inetSocketAddr).build();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//

		//
		//Request req1 = new Request(null);
		//req1.setToken(token);
		


		
		CompletableFuture<CoapPacket> resp = null;
		try {
			resp = client.resource(myuri1_path).observe(new SyncObservationListener());
			/*
			if(resp != null) {
				try {
					//这样 就相当于 又发送了一次get请求
					System.out.println("kkkk:"+resp.get().getPayloadString());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
		} catch (CoapException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		try {
			CoapPacket resp = client.resource("/hello_observer").sync().observe(new SyncObservationListener());
			if(resp != null) {
				System.out.println("kkkk:"+resp.getPayloadString());
			}
		} catch (CoapException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		// 你可以在server 那边的myresource 的get方法打个断点, 让server返回的慢一点
		// 这样你就可以在client这边先看到 hello!!!! 先出现在server返回的playload之前了, 
		// 也就是能够证明是异步了
		System.out.println("hello!!!!!!!!!!!!!!!!!!!!!");
		
		//---------------------------------------------
		// 因为 异步，是要等待回传的，等待是需要时间的，
		// 所以 我不能让程序那么快结束
		// 所以 我让你输入回车再结束，也就是说 你不输入回车，那么这个总main函数没走完
		// 从而 有时间 让client等到 传回来的 数据
		// 不然的话 在等待的过程中，总函数已经运行完了, 所以里面的这些变量啊 线程啊 也有可能没有了？
        System.out.println("enter to exit!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
				br.readLine(); 
		} 
		catch (IOException e) { }
		System.out.println("CANCELLATIONING");
		//resp.proactiveCancel();
		//resp.cancel(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CANCELLATION FINISHED");
		client.close();
	
	}
	
	
    public static class SyncObservationListener implements ObservationListener {

        BlockingQueue<CoapPacket> queue = new LinkedBlockingQueue<>();

        @Override
        public void onObservation(CoapPacket obsPacket) throws CoapException {
            System.out.println("ADD!!!!!!!"+obsPacket.getPayloadString());
            queue.add(obsPacket);
        }

        public CoapPacket take() throws InterruptedException {
            System.out.println("TAKE!!!!!!!");
            return queue.poll(5, TimeUnit.SECONDS); // avoid test blocking
            //            return queue.take();
        }

        public CoapPacket take(int timeout, TimeUnit timeUnit) throws InterruptedException {
            return queue.poll(timeout, timeUnit);
        }

        @Override
        public void onTermination(CoapPacket obsPacket) throws CoapException {
        	System.out.println("term!!!!!!!"+obsPacket.getPayloadString());
            queue.add(obsPacket);
        }
    }
}

