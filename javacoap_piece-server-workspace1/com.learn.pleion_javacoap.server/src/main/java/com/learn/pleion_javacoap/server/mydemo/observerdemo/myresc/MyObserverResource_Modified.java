package com.learn.pleion_javacoap.server.mydemo.observerdemo.myresc;

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

public class MyObserverResource_Modified extends AbstractObservableResource{

	Timer timer = null;
	private int int_connect_get_num=0;
	private int int_mytask_used=0;
	
	public MyObserverResource_Modified(CoapServer coapServer) {
		super(coapServer);
		// TODO Auto-generated constructor stub
		//
		//----------------------------------------
		//
		// schedule a periodic update task, otherwise let events call changed()
		//Timer timer = new Timer();
		timer = new Timer();
		// 每10000ms 则去 执行一次 里面那个run 的 changed 从而通知所有的client, 通知的时候调用handleGet
		timer.schedule(new UpdateTask(),0, 5000);
	}
	/*
    public MyObserverResource_Modified(String body, CoapServer coapServer) {
        super(coapServer);
        //this.body = body.getBytes(DEFAULT_CHARSET);
		//
		//----------------------------------------
		//
		// schedule a periodic update task, otherwise let events call changed()
		//Timer timer = new Timer();
		timer = new Timer();
		// 每10000ms 则去 执行一次 里面那个run 的 changed 从而通知所有的client, 通知的时候调用handleGet
		timer.schedule(new UpdateTask(),0, 5000);
    }

    public MyObserverResource_Modified(String body, CoapServer coapServer, boolean includeObservableFlag) {
        super(coapServer, includeObservableFlag);
        //this.body = body.getBytes(DEFAULT_CHARSET);
		//
		//----------------------------------------
		//
		// schedule a periodic update task, otherwise let events call changed()
		//Timer timer = new Timer();
		timer = new Timer();
		// 每10000ms 则去 执行一次 里面那个run 的 changed 从而通知所有的client, 通知的时候调用handleGet
		timer.schedule(new UpdateTask(),0, 5000);
        
    }
*/
    
	
	@Override
	public void get(CoapExchange exchange) throws CoapCodeException {
		// TODO Auto-generated method stub
		int_connect_get_num = int_connect_get_num +1;
		System.out.println("connect num: "+int_connect_get_num);
		System.out.println("task used num: "+int_mytask_used);
		
		
		//exchange.setResponseBody("helllo, i am server");
		//exchange.setResponseCode(Code.C205_CONTENT);
		//
		int_connect_get_num = int_connect_get_num +1;
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
			System.out.println("UpdateTask-------name:"+MyObserverResource_Modified.this.getClass().getName());
			//
			int_mytask_used = int_mytask_used+1;
			// .. periodic update of the resource
			//changed(); // notify all observers
			//((AbstractObservableResource)(MyObserverResource_Modified.this)).notifyChange(new String("kalloooo!"+int_mytask_used).getBytes(),Code.C205_CONTENT);
			//MyObserverResource_Modified.this.mynotifyChange(new String("kalloooo!"+int_mytask_used).getBytes(CoapConstants.DEFAULT_CHARSET),MediaTypes.CT_TEXT_PLAIN);
			mynotifyChange(new String("kalloooo!"+int_mytask_used).getBytes(CoapConstants.DEFAULT_CHARSET),MediaTypes.CT_TEXT_PLAIN);
		}
	}



	// ref: java-coap/coap-core/src/main/java/com/mbed/coap/observe/SimpleObservableResource.java
	public void mynotifyChange(byte[] bytes, Short c205Content) {
		// TODO Auto-generated method stub
		try {
			super.notifyChange(bytes, c205Content);
		} catch (CoapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------
	
	
    /**
     * Changes body for this resource, sends notification to all subscribers.
     *
     * @param body new payload in bytes
     * @throws CoapException coap exception
     */
    public void setBody(byte[] body, Short contentType) throws CoapException {
        //this.body = body;

        notifyChange(body, contentType);
    }

    public void setBody(String body, NotificationDeliveryListener deliveryListener) throws CoapException {
        //this.body = body.getBytes(DEFAULT_CHARSET);
        notifyChange(body.getBytes(CoapConstants.DEFAULT_CHARSET), null, null, null, deliveryListener);
    }

    public void setBody(byte[] body, NotificationDeliveryListener deliveryListener) throws CoapException {
        //this.body = body;
        notifyChange(body, null, null, null, deliveryListener);
    }
	
	
    public void setConfirmNotification(boolean confirmNotification) {
        this.setConNotifications(confirmNotification);
    }
    //---------------------------------------------------------------------
	//
	//把timer 停止了, 如果只是server.destory 是不会把这个 resource的 Timer结束的
	public int stopMyResource(){
		this.timer.cancel();
		return 1;
	}

}

