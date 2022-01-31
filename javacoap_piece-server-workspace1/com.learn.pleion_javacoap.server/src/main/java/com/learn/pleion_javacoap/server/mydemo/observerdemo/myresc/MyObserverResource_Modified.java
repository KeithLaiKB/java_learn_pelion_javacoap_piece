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

/**
 * 如果coap client 那边断了, 这边会有一段时间说 类似
 *  Could not deliver notification to /127.0.0.1:60775, previous still not confirmed
 * 然后一段时间后
 * Observation removed [#1169 /127.0.0.1:60775]
 * Notification response timeout: /127.0.0.1:60775
 * 
 * 然后我猜这个 client 就不在observation 里面了
 * 
 * @author laipl
 *
 */
public class MyObserverResource_Modified extends AbstractObservableResource{

	Timer timer = null;
	private int int_connect_get_num=0;
	private int int_mytask_used=0;
	
	public MyObserverResource_Modified(CoapServer coapServer) {
		super(coapServer);
		// TODO Auto-generated constructor stub
		//
		//
		//this.setConNotifications(false);		// configure the notification type to NONs, 如果不写这个默认的是 CON
		this.setConNotifications(true);	
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
    
	
	/**
	 * 当某个client第一次连接 这个resource的时候  会走一次 get(CoapExchange exchange), 
	 * 但是后面就不经过这个get了
	 * 注意 有意思的是
	 * 这里写了 setResponseBody("task used num:"+int_mytask_used)
	 * 对于 observe的那个client 只有第一次 observe的时候 会得到这个内容
	 * 但对于 后续的 notification 
	 * 那个client的 是无法获得 get() 里面这个内容的  "task used num:"+int_mytask_used  
	 * 而那个client获得的是 notifyChange() 里面的内容
	 * 
	 * 但是值得注意的是, 这部分还是要写的因为
	 * 
	 * 假如数据从 1 2 3 4 5...开始发送
	 * 第一次client 获取到的数据 "1"  是从 get这里返回的
	 * 剩下的 2 3 4 5... 才是从notifyChange那里返回
	 * 
	 * 你可以用 两个client 尝试就好了
	 * 
	 * ref: java-coap/coap-core/src/test/java/com/mbed/coap/server/ServerIntegrationTest.java
	 */
	@Override
	public void get(CoapExchange exchange) throws CoapCodeException {
		// TODO Auto-generated method stub
		System.out.println("--------------------------------------------------------------------");
		System.out.println("--------- server side get(CoapExchange exchange) start -------------");
		System.out.println("start get(CoapExchange exchange):");
		int_connect_get_num = int_connect_get_num +1;
		System.out.println("connect num: "+int_connect_get_num);
		System.out.println("task used num: "+int_mytask_used);
		
		
		//exchange.setResponseBody("helllo, i am server");
		//exchange.setResponseCode(Code.C205_CONTENT);
		//
		//exchange.respond(ResponseCode.CONTENT, "task used num:"+int_mytask_used);
		//
		exchange.setResponseBody("task used num:"+int_mytask_used);
        exchange.getResponseHeaders().setContentFormat(MediaTypes.CT_TEXT_PLAIN);
		exchange.setResponseCode(Code.C205_CONTENT);								//如果不写这个也可以, 它在CoapPacket中的create response 自带 Code.C205_CONTENT
		exchange.sendResponse();
		System.out.println("--------- server side get(CoapExchange exchange) end ---------------");
		System.out.println("--------------------------------------------------------------------");

	}
	
	
	/**
	 * ref: java-coap/coap-core/src/test/java/com/mbed/coap/server/ServerIntegrationTest.java
	 * 暂时发现好像并没有像 californium那样 有一个 coapResource 自己的 delete的方法
	 *
	 */
    @Override
    public void delete(CoapExchange exchange) throws CoapCodeException {
        //payload = "";
        //contentType = 0;
    	//delete(exchange);
    	/*
    	exchange.setResponseBody("ideleted");
        exchange.setResponseCode(Code.C202_DELETED);
        exchange.sendResponse();
        */
    	
        //super.delete(exchange);
    }
	/**
	 * 这里面 每一次notifyChange 代表, 要去通知所有的client
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
			myNotifyChange(new String("kalloooo!"+int_mytask_used).getBytes(CoapConstants.DEFAULT_CHARSET),MediaTypes.CT_TEXT_PLAIN);
		}
	}



	// ref: java-coap/coap-core/src/main/java/com/mbed/coap/observe/SimpleObservableResource.java
	public void myNotifyChange(byte[] bytes, Short c205Content) {
		// TODO Auto-generated method stub
		try {
			notifyChange(bytes, c205Content);
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
	// ref: java-coap/coap-core/src/main/java/com/mbed/coap/observe/SimpleObservableResource.java
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
	
	/*
    public void setConfirmNotification(boolean confirmNotification) {
        this.setConNotifications(confirmNotification);
    }*/
    //---------------------------------------------------------------------
	//
	//把timer 停止了, 如果只是server.destory 是不会把这个 resource的 Timer结束的
	public int stopMyResource(){
		this.timer.cancel();
		return 1;
	}

}

