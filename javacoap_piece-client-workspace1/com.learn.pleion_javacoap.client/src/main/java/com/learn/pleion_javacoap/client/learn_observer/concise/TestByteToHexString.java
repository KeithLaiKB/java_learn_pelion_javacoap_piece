package com.learn.pleion_javacoap.client.learn_observer.concise;

public class TestByteToHexString {
	public static void main(String[] args) {
		byte[] a= {119,-75};
		String result = byteArray2HexString(a);
		System.out.println(result);
	}
	public static String byteArray2HexString(byte[] array) {

        StringBuilder builder = new StringBuilder();
        for (byte b : array) {

            String s = Integer.toHexString(b & 0xff);
            if (s.length() < 2) {
                builder.append("0");
            }
            builder.append(s);
        }

        return builder.toString().toUpperCase();
    }

}
