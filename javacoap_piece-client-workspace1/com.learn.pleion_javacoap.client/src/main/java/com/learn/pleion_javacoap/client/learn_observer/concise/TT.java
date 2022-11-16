package com.learn.pleion_javacoap.client.learn_observer.concise;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
//
/*
 * 
 * 也就是说0xFFFF 控制了最大的范围就是 0~65535
 * 
 * 然后 用65535 求得 他的len最大也就是2
 * 
 * */
public class TT {
	public static final void main(String[] args) {
		AtomicLong token;
		token = new AtomicLong(0xFFFF & (new Random()).nextLong());		//所以可以看到这只有两个字节 范围65536
		//DataConvertingUtility.convertVariableUInt(token.incrementAndGet());
		long token_incre = token.incrementAndGet();
		//
		
		//byte[] byte_token = convertVariableUInt(15127L);
		//byte[] byte_token = convertVariableUInt(65536L);
		byte[] byte_token = convertVariableUInt(65535L);
		System.out.println(byte_token);
		
	}
	 public static byte[] convertVariableUInt(long value) {
	        int len = 1;
	        if (value > 0) {
	        	double a = Math.log10(value + 1);
	        	double b = Math.log10(2);
	        	double c = a/b;
	            len = (int) Math.ceil(c / 8); //calculates needed minimum length
	        }

	        byte[] data = new byte[len];
	        for (int i = 0; i < len; i++) {
	            data[i] = (byte) (0xFF & (value >> 8 * (len - (i + 1))));
	        }
	        return data;
	    }
}
