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
		server.addRequestHandler("/obs", myobResc1);
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
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		//
		//
		// 因为我们的resource用了 timer,
		// 所以我们 destroy 了server以后 , resource还是在运行的
		// in my opinion, we should apply a standard process
		// so we need to stop the resource
		//myobResc1.stopMyResource();
		//
		//
		// 再让Main函数 运行一段时间, 我们可以发现resource没有输出了, 也就意味着 确实结束了
		// 其实 这后面的可以不用, 只是用来判断resource是否结束了,
		// 如果resource 没关掉, 就可以 在这段时间内 发现有resource的输出
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		// destroy server
		// because the resource use the timer

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
