package com.telenav.network.android;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidHttpConnection.class)
public class AndroidHttpConnectionTest 
{
	private AndroidHttpConnection conn;
	private HttpURLConnection urlConn;
	
	@Before
	public void setUp() throws Exception 
	{
		urlConn = PowerMock.createMock(HttpURLConnection.class);
		conn = new AndroidHttpConnection(urlConn);
	}
	
	@Test
	public void testgetData() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getDate()).andReturn(100L);
		PowerMock.replayAll();
		long date = conn.getDate();
		PowerMock.verifyAll();
		assertEquals(date, 100L);
	}
	
	@Test
	public void testgetExpiration() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getExpiration()).andReturn(100L);
		PowerMock.replayAll();
		long date = conn.getExpiration();
		PowerMock.verifyAll();
		assertEquals(date, 100L);
	}
	
	@Test
	public void testGetFile()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getFile()).andReturn("file");
		PowerMock.replayAll();
		String file = conn.getFile();
		PowerMock.verifyAll();
		assertEquals(file, "file");
	}
	
	@Test
	public void testGetHeaderField_Int() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getHeaderField(20)).andReturn("field");
		PowerMock.replayAll();
		String field = conn.getHeaderField(20);
		PowerMock.verifyAll();
		assertEquals(field, "field");
	}
	
	@Test
	public void testGetHeaderField_String() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getHeaderField("test")).andReturn("field");
		PowerMock.replayAll();
		String field = conn.getHeaderField("test");
		PowerMock.verifyAll();
		assertEquals(field, "field");
	}
	
	@Test
	public void testGetHeaderFieldDate() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getHeaderFieldDate("name", 100L)).andReturn(100L);
		PowerMock.replayAll();
		long re = conn.getHeaderFieldDate("name", 100L);
		PowerMock.verifyAll();
		assertEquals(re, 100L);
	}
	
	@Test
	public void testGetHeaderFieldInt() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getHeaderFieldInt("name", 100)).andReturn(100);
		PowerMock.replayAll();
		int re = conn.getHeaderFieldInt("name", 100);
		PowerMock.verifyAll();
		assertEquals(re, 100);
	}
	
	@Test
	public void testGetHeaderFieldKey() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getHeaderFieldKey(100)).andReturn("key");
		PowerMock.replayAll();
		String key = conn.getHeaderFieldKey(100);
		PowerMock.verifyAll();
		assertEquals(key, "key");
	}
	
	@Test
	public void testGetHost()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getHost()).andReturn("host");
		PowerMock.replayAll();
		String host = conn.getHost();
		PowerMock.verifyAll();
		assertEquals(host, "host");
	}
	
	@Test
	public void testGetLastModified() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getLastModified()).andReturn(100L);
		PowerMock.replayAll();
		long re = conn.getLastModified();
		PowerMock.verifyAll();
		assertEquals(re, 100L);
	}
	
	@Test
	public void testGetPort()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getPort()).andReturn(8080);
		PowerMock.replayAll();
		int port = conn.getPort();
		PowerMock.verifyAll();
		assertEquals(port, 8080);
	}

	@Test
	public void testGetProtocol()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getProtocol()).andReturn("protocol");
		PowerMock.replayAll();
		String protocol = conn.getProtocol();
		PowerMock.verifyAll();
		assertEquals(protocol, "protocol");
	}
	
	@Test
	public void testGetQuery()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getQuery()).andReturn("query");
		PowerMock.replayAll();
		String query = conn.getQuery();
		PowerMock.verifyAll();
		assertEquals(query, "query");
	}

	@Test
	public void testGetRef()
	{
		PowerMock.resetAll();
		URL url = PowerMock.createMock(URL.class);
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		EasyMock.expect(url.getRef()).andReturn("ref");
		PowerMock.replayAll();
		String ref = conn.getRef();
		PowerMock.verifyAll();
		assertEquals(ref, "ref");
	}
	
	@Test
	public void testGetRequestMethod()
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getRequestMethod()).andReturn("method");
		PowerMock.replayAll();
		String method = conn.getRequestMethod();
		PowerMock.verifyAll();
		assertEquals(method, "method");
	}
	
	@Test
	public void testGetProperty()
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getRequestProperty("key")).andReturn("property");
		PowerMock.replayAll();
		String property = conn.getRequestProperty("key");
		PowerMock.verifyAll();
		assertEquals(property, "property");
	}

	@Test
	public void testGetResponseCode() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getResponseCode()).andReturn(100);
		PowerMock.replayAll();
		int code = conn.getResponseCode();
		PowerMock.verifyAll();
		assertEquals(code, 100);
	}
	
	@Test
	public void testGetResponseMsg() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getResponseMessage()).andReturn("msg");
		PowerMock.replayAll();
		String msg = conn.getResponseMessage();
		PowerMock.verifyAll();
		assertEquals(msg, "msg");
	}
	
	@Test
	public void testGetUrl() throws Exception
	{
		PowerMock.resetAll();
		URL url = new URL("http://telenav.com");
		EasyMock.expect(urlConn.getURL()).andReturn(url);
		PowerMock.replayAll();
		String re = conn.getURL();
		PowerMock.verifyAll();
		assertEquals(re, "http://telenav.com");
	}
	
	@Test
	public void testSetRequestMethod() throws Exception
	{
		PowerMock.resetAll();
		urlConn.setRequestMethod("method");
		PowerMock.replayAll();
		conn.setRequestMethod("method");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetRequestProperty() throws Exception
	{
		PowerMock.resetAll();
		urlConn.setRequestProperty("key", "value");
		PowerMock.replayAll();
		conn.setRequestProperty("key", "value");
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetEncoding()
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getContentEncoding()).andReturn("Encoding");
		PowerMock.replayAll();
		conn.getEncoding();
		PowerMock.verifyAll();
	}

	@Test
	public void testGetLength()
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getContentLength()).andReturn(100);
		PowerMock.replayAll();
		long len = conn.getLength();
		PowerMock.verifyAll();
		assertEquals(len, 100);
	}
	
	@Test
	public void testGetType()
	{
		PowerMock.resetAll();
		EasyMock.expect(urlConn.getContentType()).andReturn("type");
		PowerMock.replayAll();
		String type = conn.getType();
		PowerMock.verifyAll();
		assertEquals(type, "type");
	}
	
	@Test
	public void testOpenInputStream() throws Exception
	{
		PowerMock.resetAll();
		InputStream stream = PowerMock.createMock(InputStream.class);
		EasyMock.expect(urlConn.getInputStream()).andReturn(stream);
		PowerMock.replayAll();
		InputStream re = conn.openInputStream();
		PowerMock.verifyAll();
		assertEquals(re, stream);
	}
	
	@Test
	public void testOpenOutputStream() throws Exception
	{
		PowerMock.resetAll();
		OutputStream stream = PowerMock.createMock(OutputStream.class);
		EasyMock.expect(urlConn.getOutputStream()).andReturn(stream);
		PowerMock.replayAll();
		OutputStream out = conn.openOutputStream();
		PowerMock.verifyAll();
		assertEquals(out, stream);
	}
	
	@Test
	public void testClose() throws Exception
	{
		PowerMock.resetAll();
		urlConn.disconnect();
		PowerMock.replayAll();
		conn.close();
		PowerMock.verifyAll();
	}
}
