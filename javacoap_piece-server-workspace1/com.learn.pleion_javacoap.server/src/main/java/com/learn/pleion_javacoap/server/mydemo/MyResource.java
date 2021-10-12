package com.learn.pleion_javacoap.server.mydemo;

import com.mbed.coap.exception.CoapCodeException;
import com.mbed.coap.packet.Code;
import com.mbed.coap.server.CoapExchange;
import com.mbed.coap.utils.CoapResource;

public class MyResource extends CoapResource {
    private String body="Hello World";
    
    public MyResource() {
        // restricted instantiation rights\
    	super();
    }
    
    @Override
    public void get(CoapExchange ex) throws CoapCodeException {
        ex.setResponseBody("Hello World");
        ex.setResponseCode(Code.C205_CONTENT);
        ex.sendResponse();
    }
    
    @Override
    public void put(CoapExchange ex) throws CoapCodeException {
      body = ex.getRequestBodyString();        
        ex.setResponseCode(Code.C204_CHANGED);
        ex.sendResponse();
    }
}