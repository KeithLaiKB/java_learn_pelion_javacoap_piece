package com.learn.pleion_javacoap.server_dtls.v5_0_0.tryjks;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.learn.pleion_javacoap.server_dtls.v5_0_0.myresc.MyObserverResource_Con_Mwe;
import com.mbed.coap.server.CoapServer;
import com.mbed.coap.transport.javassl.CoapSerializer;


/**
 * 
 * 
 * 
 * @author laipl
 *
 * 这里用这个 null
 * context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
 * 但是好像不Ok
 *
 */
public class TestObserverMain_Mwe_try {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//
		//String 	myuri1_hostaddr   				= "localhost";
		int 	myuri1_port 	  				= 5656;
		String 	myuri1_path   					= "/hello_observer";
		
		
		String serverCaCrt_file					="s_cacert.crt";
		String serverCaCrt_file_dir				="/mycerts/oneway_jks/myca";
		String serverCaCrt_file_loc = null;
		
		String serverKey_file					="server_cert.jks";
		String serverKey_file_dir				="/mycerts/oneway_jks/mycerts";
		String serverKey_file_loc = null;
		
		String serverCrt_file					="server_cert.crt";
		String serverCrt_file_dir				="/mycerts/oneway_jks/mycerts";
		String serverCrt_file_loc = null;
		
		
		//--------------------------------------
		String myusr_path = System.getProperty("user.dir");
		serverCaCrt_file_loc 							= 	myusr_path	+ serverCaCrt_file_dir		+"/" + 	serverCaCrt_file;

		serverKey_file_loc								= 	myusr_path	+ serverKey_file_dir		+"/" + 	serverKey_file;

		serverCrt_file_loc								= 	myusr_path	+ serverCrt_file_dir		+"/" + 	serverCrt_file;

        
        X509Certificate serverCaCrt = null;


        //////////////////// file->FileInputStream->BufferedInputStream->X509Certificate //////////////////////////////////////
        // ref: https://gist.github.com/erickok/7692592
        
        FileInputStream fis= null;
        CertificateFactory cf = null;
        Certificate ca=null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			// From https://www.washington.edu/itconnect/security/ca/load-der.crt
			fis = new FileInputStream(serverCaCrt_file_loc);
			InputStream caInput = new BufferedInputStream(fis);
			
			try {
				ca = cf.generateCertificate(caInput);
				// System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
			} finally {
				caInput.close();
			}
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
        InputStream ksInputStream = null;
		try {
			//cf = CertificateFactory.getInstance("X.509");
			// From https://www.washington.edu/itconnect/security/ca/load-der.crt
			fis = new FileInputStream(serverKey_file_loc);
			ksInputStream = new BufferedInputStream(fis);
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
        
		
		// Create a KeyStore containing our trusted CAs
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore=null;
		TrustManagerFactory tmf = null;
		try {
			// Create a KeyStore containing our trusted CAs
			keyStoreType = KeyStore.getDefaultType();
			keyStore = KeyStore.getInstance(keyStoreType);
			
            //KeyStore keystore = KeyStore.getInstance("JKS");
			keyStore.load(ksInputStream, "SksOneAdmin".toCharArray());
			
			//keyStore.load(null,null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		// finally, create SSL socket factory
		SSLContext context=null;
		SSLSocketFactory mysocketFactory=null;
		try {
			//context = SSLContext.getInstance("SSL");
			//ref: https://datatracker.ietf.org/doc/html/rfc6347
			// This document updates
			// DTLS 1.0 to work with TLS version 1.2.
			context = SSLContext.getInstance("TLSv1.2");
			
			//context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			//context.init(null,tmf.getTrustManagers(), new java.security.SecureRandom());
			//context.init(null,tmf.getTrustManagers(), null);
			context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mysocketFactory = context.getSocketFactory();
				
        
		
		//--------------------------------------------------
		
		/*
		//ref :coap-core/src/test/java/com/mbed/coap/transport/javassl/SSLSocketClientTransportTest.java
        CoapServer srv = CoapServer.builder()
                .transport(new SingleConnectionSSLSocketServerTransport(srvSslContext, 0, CoapSerializer.UDP))
                .build().start();
		 */
		// ref:java-coap/coap-core/src/test/java/protocolTests/ObservationTest.java 
		//CoapServer serverq = CoapServer.builder().transport(new SingleConnectionSSLSocketServerTransport(srvSslContext, 0, )).build();
		
		//ref: java-coap/coap-core/src/test/java/com/mbed/coap/transport/javassl/SingleConnectionSSLSocketServerTransport.java
		SingleConnectionSocketServerTransport serverTransport1=null;
		try {
			serverTransport1 = new SingleConnectionSocketServerTransport(context.getServerSocketFactory().createServerSocket(myuri1_port), CoapSerializer.UDP);
			//((SSLServerSocket)(serverTransport1.serverSocket)).setNeedClientAuth(true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		


		// ref:java-coap/coap-core/src/test/java/protocolTests/ObservationTest.java 
		//CoapServer server = CoapServer.builder().transport(myuri1_port).build();
		CoapServer server = CoapServer.builder().transport(serverTransport1).build();

		
		
		
		
	    //
		MyObserverResource_Con_Mwe myobResc1 = new MyObserverResource_Con_Mwe(server);
		// 注意 这里的 hello 大小写是敏感的
		// 因为 client那边 是根据 coap://localhost:5656/hello 来发送请求的
		server.addRequestHandler(myuri1_path, myobResc1);
		//server.setObservationHandler(myobResc1);
		try {
			server.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//
		// 停留一段时间 让server继续运行
		try {
			//Thread.sleep(30000);
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		//
		// 因为我们的resource用了 timer,
		// 所以我们 destroy 了server以后 , resource还是在运行的
		// in my opinion, we should apply a standard process
		// so we need to stop the resource
		myobResc1.stopMyResource();
		//
		// 再让Main函数 运行一段时间, 我们可以发现resource没有输出了, 也就意味着 确实结束了
		// 其实 这后面的可以不用, 只是用来判断resource是否结束了,
		// 如果resource 没关掉, 就可以 在这段时间内 发现有resource的输出
		try {
			//Thread.sleep(10000);
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// destroy server
		// because the resource use the timer
		server.stop();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("destroy the server and stop the resource timer finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
}
