package com.learn.pleion_javacoap.server.minimalexample.myresc;

import java.util.Timer;
import java.util.TimerTask;

import com.mbed.coap.CoapConstants;
import com.mbed.coap.exception.CoapCodeException;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.observe.AbstractObservableResource;
import com.mbed.coap.observe.NotificationDeliveryListener;
import com.mbed.coap.packet.Code;
import com.mbed.coap.packet.MediaTypes;
import com.mbed.coap.server.CoapExchange;
import com.mbed.coap.server.CoapServer;

/**
 * 这个mwe 直接使用notifyChange, 而不需要用myNotifyChange
 * 
 * @author laipl
 *
 */
public class MyObserverResource_Con_Mwe extends AbstractObservableResource{

	Timer timer = null;
	private int int_connect_get_num=0;
	private int int_mytask_used=0;
	
	public MyObserverResource_Con_Mwe(CoapServer coapServer) {
		super(coapServer);
		// TODO Auto-generated constructor stub
		//
		//
		this.setConNotifications(false);		// configure the notification type to NONs, 如果不写这个默认的是 CON
		//----------------------------------------
		//
		// schedule a periodic update task, otherwise let events call changed()
		//Timer timer = new Timer();
		timer = new Timer();
		// 每10000ms 则去 执行一次 里面那个run 的 changed 从而通知所有的client, 通知的时候调用handleGet
		timer.schedule(new UpdateTask(),0, 5000);
	}
    
	
	@Override
	public void get(CoapExchange exchange) throws CoapCodeException {
		// TODO Auto-generated method stub
		int_connect_get_num = int_connect_get_num +1;
		System.out.println("connect num: "+int_connect_get_num);
		System.out.println("task used num: "+int_mytask_used);
		
		
		//exchange.setResponseBody("helllo, i am server");
		//exchange.setResponseCode(Code.C205_CONTENT);
		//
		//exchange.respond(ResponseCode.CONTENT, "task used num:"+int_mytask_used);
		exchange.setResponseBody("task used num:"+int_mytask_used);
		exchange.setResponseCode(Code.C205_CONTENT);
		exchange.sendResponse();
	}
	
	/**
	 * 这里面 每一次changed 代表, 要去通知所有的client
	 * 则会调用handelGet
	 * 
	 * @author laipl
	 *
	 */
	private class UpdateTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("UpdateTask-------name:"+MyObserverResource_Con_Mwe.this.getClass().getName());
			//
			int_mytask_used = int_mytask_used+1;
			// .. periodic update of the resource
			//myNotifyChange(new String("kalloooo!"+int_mytask_used).getBytes(CoapConstants.DEFAULT_CHARSET),MediaTypes.CT_TEXT_PLAIN);
			try {
				notifyChange(new String("kalloooo!"+int_mytask_used).getBytes(CoapConstants.DEFAULT_CHARSET),MediaTypes.CT_TEXT_PLAIN);
			} catch (CoapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ref: java-coap/coap-core/src/main/java/com/mbed/coap/observe/SimpleObservableResource.java
	/*
	public void myNotifyChange(byte[] bytes, Short c205Content) {
		// TODO Auto-generated method stub
		try {
			notifyChange(bytes, c205Content);
		} catch (CoapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
    //---------------------------------------------------------------------
	//
	//把timer 停止了, 如果只是server.destory 是不会把这个 resource的 Timer结束的
	public int stopMyResource(){
		this.timer.cancel();
		return 1;
	}

}
