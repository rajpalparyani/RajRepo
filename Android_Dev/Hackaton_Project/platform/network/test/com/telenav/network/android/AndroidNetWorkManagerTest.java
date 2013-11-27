package com.telenav.network.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.network.TnConnection;
import com.telenav.network.TnConnectionNotFoundException;
import com.telenav.network.TnNetworkManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidNetworkManager.class, SSLContext.class, HttpsURLConnection.class})
public class AndroidNetWorkManagerTest 
{
	private AndroidNetworkManager manager;
	
	@Before
	public void setUp() throws Exception 
	{
		manager = new AndroidNetworkManager();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testOpenConn_NameIsNull() throws Exception
	{
		manager.openConnection(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testOpenConn_NameIsNull2() throws Exception
	{
		manager.openConnection("");
	}
	
	@Test
	public void testOpenConn_Socket() throws Exception
	{
		PowerMock.resetAll();
		IllegalArgumentException e = new IllegalArgumentException();
		AndroidSocketConnection conn = PowerMock.createMock(AndroidSocketConnection.class);
		InetSocketAddress addr = PowerMock.createMock(InetSocketAddress.class);
		PowerMock.expectNew(InetSocketAddress.class, "172.10.29.109", 2020).andReturn(addr);
		PowerMock.expectNew(AndroidSocketConnection.class, addr, false).andReturn(conn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("socket://172.10.29.109:2020");
		PowerMock.verifyAll();
		assertEquals(tnConn, conn);
	}
	
	@Test(expected=TnConnectionNotFoundException.class)
	public void testOpenConn_Socket_Expect() throws Exception
	{
		PowerMock.resetAll();
		IllegalArgumentException e = new IllegalArgumentException();
		AndroidSocketConnection conn = PowerMock.createMock(AndroidSocketConnection.class);
		InetSocketAddress addr = PowerMock.createMock(InetSocketAddress.class);
		PowerMock.expectNew(InetSocketAddress.class, "172.10.29.109", 2020).andThrow(e);//andReturn(addr);
		//PowerMock.expectNew(AndroidSocketConnection.class, addr, false).andThrow(arg0)//andReturn(conn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("socket://172.10.29.109:2020");
		PowerMock.verifyAll();
		//assertEquals(tnConn, conn);
	}
	
	
	@Test
	public void testOpenConn_Datagram() throws Exception
	{
		PowerMock.resetAll();
		AndroidDatagramConnection conn = PowerMock.createMock(AndroidDatagramConnection.class);
		InetSocketAddress addr = PowerMock.createMock(InetSocketAddress.class);
		PowerMock.expectNew(InetSocketAddress.class, "172.10.29.109", 2020).andReturn(addr);
		PowerMock.expectNew(AndroidDatagramConnection.class, addr, false).andReturn(conn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("datagram://172.10.29.109:2020");
		PowerMock.verifyAll();
		assertEquals(tnConn, conn);
	}
	
	@Test(expected=TnConnectionNotFoundException.class)
	public void testOpenConn_Datagram_Exception() throws Exception
	{
		PowerMock.resetAll();
		IllegalArgumentException e = new IllegalArgumentException();
		AndroidDatagramConnection conn = PowerMock.createMock(AndroidDatagramConnection.class);
		InetSocketAddress addr = PowerMock.createMock(InetSocketAddress.class);
		PowerMock.expectNew(InetSocketAddress.class, "172.10.29.109", 2020).andThrow(e);
		//PowerMock.expectNew(AndroidDatagramConnection.class, addr, false).andReturn(conn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("datagram://172.10.29.109:2020");
		PowerMock.verifyAll();
		//assertEquals(tnConn, conn);
	}
	
	@Test(expected=TnConnectionNotFoundException.class)
	public void testOpenConn_Https_Exception() throws Exception
	{
		PowerMock.resetAll();
		SSLContext sc = PowerMock.createMock(SSLContext.class);
		PowerMock.mockStatic(SSLContext.class);
		EasyMock.expect(SSLContext.getInstance("TLS")).andReturn(sc);
		sc.init(EasyMock.anyObject(KeyManager[].class), EasyMock.anyObject(TrustManager[].class), EasyMock.anyObject(SecureRandom.class));
		SSLSocketFactory fac = PowerMock.createMock(SSLSocketFactory.class);
		EasyMock.expect(sc.getSocketFactory()).andReturn(fac);
		PowerMock.mockStatic(HttpsURLConnection.class);
		HttpsURLConnection.setDefaultSSLSocketFactory(fac);
		HttpsURLConnection.setDefaultHostnameVerifier(EasyMock.anyObject(HostnameVerifier.class));
		URL url = PowerMock.createMock(URL.class);
		PowerMock.expectNew(URL.class, EasyMock.anyObject(String.class)).andReturn(url);
		URLConnection conn = PowerMock.createMock(URLConnection.class);
		EasyMock.expect(url.openConnection()).andReturn(conn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("https://172.10.29.109:2020");
    	PowerMock.verifyAll();
	}
	
	@Test
	public void testOpenConn_Https_RW() throws Exception
	{
		PowerMock.resetAll();
		SSLContext sc = PowerMock.createMock(SSLContext.class);
		PowerMock.mockStatic(SSLContext.class);
		EasyMock.expect(SSLContext.getInstance("TLS")).andReturn(sc);
		sc.init(EasyMock.anyObject(KeyManager[].class), EasyMock.anyObject(TrustManager[].class), EasyMock.anyObject(SecureRandom.class));
		SSLSocketFactory fac = PowerMock.createMock(SSLSocketFactory.class);
		EasyMock.expect(sc.getSocketFactory()).andReturn(fac);
		PowerMock.mockStatic(HttpsURLConnection.class);
		HttpsURLConnection.setDefaultSSLSocketFactory(fac);
		HttpsURLConnection.setDefaultHostnameVerifier(EasyMock.anyObject(HostnameVerifier.class));
		URL url = PowerMock.createMock(URL.class);
		PowerMock.expectNew(URL.class, EasyMock.anyObject(String.class)).andReturn(url);
		URLConnection conn = PowerMock.createMock(HttpURLConnection.class);
		EasyMock.expect(url.openConnection()).andReturn(conn);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		AndroidHttpConnection httpConn = PowerMock.createMock(AndroidHttpConnection.class);
		PowerMock.expectNew(AndroidHttpConnection.class, conn).andReturn(httpConn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("https://172.10.29.109:2020", 1, true);
		PowerMock.verifyAll();
	}
	@Test
	public void testOpenConn_Https_R() throws Exception
	{
		PowerMock.resetAll();
		SSLContext sc = PowerMock.createMock(SSLContext.class);
		PowerMock.mockStatic(SSLContext.class);
		EasyMock.expect(SSLContext.getInstance("TLS")).andReturn(sc);
		sc.init(EasyMock.anyObject(KeyManager[].class), EasyMock.anyObject(TrustManager[].class), EasyMock.anyObject(SecureRandom.class));
		SSLSocketFactory fac = PowerMock.createMock(SSLSocketFactory.class);
		EasyMock.expect(sc.getSocketFactory()).andReturn(fac);
		PowerMock.mockStatic(HttpsURLConnection.class);
		HttpsURLConnection.setDefaultSSLSocketFactory(fac);
		HttpsURLConnection.setDefaultHostnameVerifier(EasyMock.anyObject(HostnameVerifier.class));
		URL url = PowerMock.createMock(URL.class);
		PowerMock.expectNew(URL.class, EasyMock.anyObject(String.class)).andReturn(url);
		URLConnection conn = PowerMock.createMock(HttpURLConnection.class);
		EasyMock.expect(url.openConnection()).andReturn(conn);
		conn.setDoInput(true);
		//conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		AndroidHttpConnection httpConn = PowerMock.createMock(AndroidHttpConnection.class);
		PowerMock.expectNew(AndroidHttpConnection.class, conn).andReturn(httpConn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("https://172.10.29.109:2020", 0, true);
		PowerMock.verifyAll();
	}
	@Test
	public void testOpenConn_Https_W() throws Exception
	{
		PowerMock.resetAll();
		SSLContext sc = PowerMock.createMock(SSLContext.class);
		PowerMock.mockStatic(SSLContext.class);
		EasyMock.expect(SSLContext.getInstance("TLS")).andReturn(sc);
		sc.init(EasyMock.anyObject(KeyManager[].class), EasyMock.anyObject(TrustManager[].class), EasyMock.anyObject(SecureRandom.class));
		SSLSocketFactory fac = PowerMock.createMock(SSLSocketFactory.class);
		EasyMock.expect(sc.getSocketFactory()).andReturn(fac);
		PowerMock.mockStatic(HttpsURLConnection.class);
		HttpsURLConnection.setDefaultSSLSocketFactory(fac);
		HttpsURLConnection.setDefaultHostnameVerifier(EasyMock.anyObject(HostnameVerifier.class));
		URL url = PowerMock.createMock(URL.class);
		PowerMock.expectNew(URL.class, EasyMock.anyObject(String.class)).andReturn(url);
		URLConnection conn = PowerMock.createMock(HttpURLConnection.class);
		EasyMock.expect(url.openConnection()).andReturn(conn);
		//conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		AndroidHttpConnection httpConn = PowerMock.createMock(AndroidHttpConnection.class);
		PowerMock.expectNew(AndroidHttpConnection.class, conn).andReturn(httpConn);
		PowerMock.replayAll();
		TnConnection tnConn = manager.openConnection("https://172.10.29.109:2020", 2, true);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testBase()
	{
		PowerMock.resetAll();
		assertNull(manager.getInstance());
		TnNetworkManager ma = PowerMock.createMock(TnNetworkManager.class);
		manager.init(ma);
		assertEquals(ma, manager.getInstance());
		manager.init(ma);
	}
}
