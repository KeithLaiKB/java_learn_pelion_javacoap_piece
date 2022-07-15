package com.learn.pleion_javacoap.client_dtls.learn_observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Scanner;
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
import com.mbed.coap.packet.MessageType;
import com.mbed.coap.transport.InMemoryCoapTransport;
import com.mbed.coap.transport.TransportContext;
import com.mbed.coap.transport.udp.DatagramSocketTransport;
import com.mbed.coap.utils.Callback;

public class TestMain_RequestObserverOne_Modified {

	
	public static void main(String[] args) {
		String port1 = "coap://localhost:5656/hello";
		String port2 = "coap://160.32.219.56:5656/hello";		//有线连接树莓派, 路由给的地址是192.168.50.178
																// 我把它的192.168.50.178:5656 映射成160.32.219.56:5656
		String port3 = "coap://160.32.219.56:5657/hello";		//无线连接树莓派, 路由给的地址是192.168.50.179
																// 我把它的192.168.50.179:5656 映射成160.32.219.56:5657
		
		//String 	myuri1_hostaddr   				= "135.0.237.84";
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
		 Callback<CoapPacket> myDeleteHandler = new Callback<CoapPacket>() {


			@Override
			public void call(CoapPacket t) {
				// TODO Auto-generated method stub
            	System.out.println("---------------------------------------------------");
            	System.out.println("-------- delete handler onload start --------------");
            	//System.out.println("result from server:" + t.isSuccess() );
				//
            	//System.out.println("on load: " + t.getResponseText());
            	System.out.println("on load: " + t.getPayloadString());
            	System.out.println("get code: " + t.getCode().name());
            	System.out.println("---------- delete handler onload end --------------");
            	System.out.println("---------------------------------------------------");
			}

			@Override
			public void callException(Exception ex) {
				// TODO Auto-generated method stub
				System.out.println("callException!!!!!!");
			}
        };


		
		CompletableFuture<CoapPacket> resp = null;
		try {
			resp = client.resource(myuri1_path).observe(new MyObservationListener());
			//
			if(resp != null) {
				//用来获取 第一次得到的数据
				System.out.println("kkkk:"+resp.get().getPayloadString());
			}
			//
		} catch (CoapException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		
		//
        //
        //
        Scanner in =new Scanner(System.in) ;
        int int_choice = 0;
        while(int_choice!=-1) {
        	System.out.println("here is the choice:");
        	System.out.println("-1: to exit");
        	System.out.println("1: to delete");
        	System.out.println("2: to reactiveCancel");
        	System.out.println("3: to proactiveCancel");
        	System.out.println("4: to observe again");
        	System.out.println("enter the choice:");
        	// input
        	int_choice = in.nextInt();
        	String str_absorbEnter = in.nextLine();
        	if(int_choice==-1) {
        		//System.exit(0);
        		break;
        	}
        	/**
        	 * delete这边 只是发送请求, 并不保证他那里必须会删除
        	 */
        	else if(int_choice==1) {
        		//
        		System.out.println("deleteing record");
        		//System.out.println("deleting resources");
        		//
        		//
        		// 我认为 delete 挺重要的 所以我这选择的是同步
        		//client.delete();				// 用的是 同步, 对面没回应, 就不能继续往下走
        		//client.resource(myuri1_path).delete(Callback.IGNORE); // 用的是 异步
        		client.resource(myuri1_path).delete(myDeleteHandler);
        		//
        		// 注意 这个delete 
        		// 可以是让 	服务器删除 这个资源
        		// 也可以是让	服务器删除 某个记录(比如server那边 连了个数据库)
        		// 这取决于 server 那边的 handleDelete 里的操作
        		//
        		// 如果 是让服务器删除 这个资源
        		// 以后 client 不会再收到这个resource的内容, 但是 server还是在运行 
        		// 所以server那边需要 把timer关掉, 此外还有可能要 remove(Resource resource)
        		//
        		//
        		//System.out.println("deleted resources");
        		//System.out.println("deleted record");
        	}
        	/**
        	 * 暂时找不到 reactiveCancel功能的api
        	 * 
        	 */
        	else if(int_choice==2) {
        		
        		// ref https://datatracker.ietf.org/doc/html/rfc7641#section-3.6
        		// When the server then
        		//  sends the next notification, the client will not recognize the token
        		//  in the message and thus will return a Reset message
        		//
        		// in order words it means
        		// send a RST when next notification arrives
        		// 
        		// 也就是 说 等到下一次 server发送 Notification过来的时候 
        		// 这个subscriber 才发送 RST给server 来取消观察这个消息
        		//resp.cancel(true);				//取消观察状态
        		/*
        		// ref : java-coap/coap-core/src/main/java/com/mbed/coap/client/ObservationHandlerImpl.java
                CoapPacket resetResponse = new CoapPacket(t.getRemoteAddress());
                resetResponse.setMessageType(MessageType.Reset);
                resetResponse.setMessageId(t.getRequest().getMessageId());
                */
        		/*
        		// ref: java-coap/coap-core/src/test/java/protocolTests/ClientServerNONTest.java
                CoapPacket badReq = new CoapPacket(Code.C404_NOT_FOUND, MessageType.NonConfirmable, serverAddr);
                badReq.setToken("1".getBytes());

                CoapPacket resp1 = client.makeRequest(badReq).get();
                */
        		CoapPacket resetResponse = new CoapPacket(inetSocketAddr);
        		resetResponse.setMessageType(MessageType.Reset);
        		//resetResponse.
        		//resp.get().createResponse(Code.)
        		
        		InetSocketAddress a1 = new InetSocketAddress(myuri1_hostaddr,myuri1_port);
        		DatagramSocketTransport trans = new DatagramSocketTransport(0);
        		try {
					trans.sendPacket0(resetResponse, inetSocketAddr, TransportContext.NULL);
				} catch (CoapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		//client.resource(myuri1_path).payload(payload)
        		
        		/*
        		try {
        			//CoapPacket cpTmp1 = resp.get().createResponse();
        			//cpTmp1.setMessageType(MessageType.Reset);
        			//resp.get().createResponse().
        			
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
        		System.out.println("reactiveCancel");
        		
        	}
        	/**
        	 * 暂时找不到proactiveCancel功能的api
        	 * 
        	 */
        	else if(int_choice==3) {

        		// ref https://datatracker.ietf.org/doc/html/rfc7641#section-3.6
        		// In some circumstances, it may be desirable to cancel an observation
        		//   and release the resources allocated by the server to it more eagerly.
        		//   In this case, a client MAY explicitly deregister by issuing a GET
        		//   request that has the Token field set to the token of the observation
        		//   to be cancelled and includes an Observe Option with the value set to
        		//   1 (deregister)
        		//
        		// in order words it means
        		// send another cancellation request, with an Observe Option set to 1 (deregister)
        		// 
        		// 也就是 说  	不必 		等到下一次 server发送 Notification过来的时候,  这个subscriber 才发送 RST给server 来取消观察这个消息
        		// 而是直接	发送 一个 get请求	给server 来取消观察这个消息 
        		//coapObRelation1.proactiveCancel();				//取消观察状态
        		System.out.println("proactiveCancel");
        	}
        	else if(int_choice==4) {
        		try {
					resp = client.resource(myuri1_path).observe(new MyObservationListener());
				} catch (CoapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //取消观察状态后 还是可以继续observe的
        		System.out.println("observe again");
        	}
        }
        //
        //---------------------------------------------
        in.close();
		
		
		
		
		
		/*
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
		System.out.println("CANCELLATION FINISHED");*/
		client.close();
	
	}
	
	/**
	 * ObservationListener
	 * ref: java-coap/coap-core/src/test/java/protocolTests/ObservationTest.java
	 * 
	 * @author laipl
	 *
	 */
    public static class MyObservationListener implements ObservationListener {

        @Override
        public void onObservation(CoapPacket obsPacket) throws CoapException {
            System.out.println("ADD!!!!!!!"+obsPacket.getPayloadString());
        }

        @Override
        public void onTermination(CoapPacket obsPacket) throws CoapException {
        	System.out.println("term!!!!!!!"+obsPacket.getPayloadString());
        }
    }
}

