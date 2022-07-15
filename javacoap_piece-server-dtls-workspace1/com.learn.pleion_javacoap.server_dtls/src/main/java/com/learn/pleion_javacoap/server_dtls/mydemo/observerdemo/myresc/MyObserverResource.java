package com.learn.pleion_javacoap.server_dtls.mydemo.observerdemo.myresc;

import com.mbed.coap.exception.CoapCodeException;
import com.mbed.coap.observe.AbstractObservableResource;
import com.mbed.coap.packet.Code;
import com.mbed.coap.server.CoapExchange;
import com.mbed.coap.server.CoapServer;

public class MyObserverResource extends AbstractObservableResource{

	public MyObserverResource(CoapServer coapServer) {
		super(coapServer);
		// TODO Auto-generated constructor stub
	}
	
    public MyObserverResource(String body, CoapServer coapServer) {
        super(coapServer);
        //this.body = body.getBytes(DEFAULT_CHARSET);
    }

    public MyObserverResource(String body, CoapServer coapServer, boolean includeObservableFlag) {
        super(coapServer, includeObservableFlag);
        //this.body = body.getBytes(DEFAULT_CHARSET);
    }

    
	@Override
	public void get(CoapExchange exchange) throws CoapCodeException {
		// TODO Auto-generated method stub
		exchange.setResponseBody("helllo, i am server");
		exchange.setResponseCode(Code.C205_CONTENT);
		exchange.sendResponse();
	}

}
