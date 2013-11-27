package com.telenav.network.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidSocketConnection.class)
public class AndroidSocketConnectionTest 
{
	private AndroidSocketConnection conn;
	private Socket socket;
	private InetSocketAddress addr;

	@Before
	public void setUp() throws Exception 
	{
		addr = PowerMock.createMock(InetSocketAddress.class);
		socket = PowerMock.createMock(Socket.class);
		PowerMock.expectNew(Socket.class).andReturn(socket);
		PowerMock.replayAll();
		conn = new AndroidSocketConnection(addr, true);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetAddress() throws Exception
	{
		PowerMock.resetAll();
		InetAddress mockAddr = PowerMock.createMock(InetAddress.class);
		EasyMock.expect(socket.getInetAddress()).andReturn(mockAddr);
		EasyMock.expect(mockAddr.getHostName()).andReturn("hostName");
		PowerMock.replayAll();
		String str = conn.getAddress();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetLocalAddress() throws Exception
	{
		PowerMock.resetAll();
		InetAddress mockAddr = PowerMock.createMock(InetAddress.class);
		EasyMock.expect(socket.getLocalAddress()).andReturn(mockAddr);
		EasyMock.expect(mockAddr.getHostName()).andReturn("hostName");
		PowerMock.replayAll();
		String str = conn.getLocalAddress();
		PowerMock.verifyAll();
		assertEquals(str, "hostName");
	}
	
	@Test
	public void testGetLocalPort() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getLocalPort()).andReturn(8080);
		PowerMock.replayAll();
		int port = conn.getLocalPort();
		PowerMock.verifyAll();
		assertEquals(port, 8080);
	}

	@Test
	public void testGetPort() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getPort()).andReturn(8080);
		PowerMock.replayAll();
		int port = conn.getPort();
		PowerMock.verifyAll();
		assertEquals(port, 8080);
	}
	
	@Test
	public void testGetSocketOption_Delay() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getTcpNoDelay()).andReturn(true);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)0);
		PowerMock.verifyAll();
		assertEquals(re, 1);	
	}

	@Test
	public void testGetSocketOption_NoDelay() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getTcpNoDelay()).andReturn(false);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)0);
		PowerMock.verifyAll();
		assertEquals(re, 0);	
	}
	
	@Test
	public void testGetSocketOption_Linger() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getSoLinger()).andReturn(20);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)1);
		PowerMock.verifyAll();
		assertEquals(re, 20);	
	}
	
	@Test
	public void testGetSocketOption_Alive() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getKeepAlive()).andReturn(true);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)2);
		PowerMock.verifyAll();
		assertEquals(re, 1);	
	}
	
	@Test
	public void testGetSocketOption_NotAlive() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getKeepAlive()).andReturn(false);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)2);
		PowerMock.verifyAll();
		assertEquals(re, 0);	
	}
	
	@Test
	public void testGetSocketOption_ReBufSize() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getReceiveBufferSize()).andReturn(20);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)3);
		PowerMock.verifyAll();
		assertEquals(re, 20);	
	}

	@Test
	public void testGetSocketOption_SendBufSize() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getSendBufferSize()).andReturn(20);
		PowerMock.replayAll();
		int re = conn.getSocketOption((byte)4);
		PowerMock.verifyAll();
		assertEquals(re, 20);	
	}
	
	@Test(expected=IOException.class)
	public void testGetSocketOption_Exception() throws Exception
	{
		int re = conn.getSocketOption((byte)5);
	}
	
	@Test
	public void testSetSocketOption_TcpDelay() throws Exception
	{
		PowerMock.resetAll();
		socket.setTcpNoDelay(false);
		PowerMock.replayAll();
		conn.setSocketOption((byte)0, -1);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_NotTcpDelay() throws Exception
	{
		PowerMock.resetAll();
		socket.setTcpNoDelay(true);
		PowerMock.replayAll();
		conn.setSocketOption((byte)0, 1);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_SoLingerTrue() throws Exception
	{
		PowerMock.resetAll();
		socket.setSoLinger(true, 2);
		PowerMock.replayAll();
		conn.setSocketOption((byte)1, 2);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_SoLingerFalse() throws Exception
	{
		PowerMock.resetAll();
		socket.setSoLinger(false, -1);
		PowerMock.replayAll();
		conn.setSocketOption((byte)1, -1);
		PowerMock.verifyAll();
	}
	@Test
	public void testSetSocketOption_AliveTrue() throws Exception
	{
		PowerMock.resetAll();
		socket.setKeepAlive(true);
		PowerMock.replayAll();
		conn.setSocketOption((byte)2, 2);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_AliveFalse() throws Exception
	{
		PowerMock.resetAll();
		socket.setKeepAlive(false);
		PowerMock.replayAll();
		conn.setSocketOption((byte)2, -1);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_ReBufSize() throws Exception
	{
		PowerMock.resetAll();
		socket.setReceiveBufferSize(20);
		PowerMock.replayAll();
		conn.setSocketOption((byte)3, 20);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetSocketOption_SeBufSize() throws Exception
	{
		PowerMock.resetAll();
		socket.setSendBufferSize(20);
		PowerMock.replayAll();
		conn.setSocketOption((byte)4, 20);
		PowerMock.verifyAll();
	}
	
	@Test(expected=IOException.class)
	public void testSetSocketOption_Exception() throws Exception
	{	
		conn.setSocketOption((byte)5, 20);
	}
	
	@Test
	public void testOpenInputStream() throws Exception
	{
		PowerMock.resetAll();
		InputStream s = PowerMock.createMock(InputStream.class);
		EasyMock.expect(socket.getInputStream()).andReturn(s);
		PowerMock.replayAll();
		InputStream re = conn.openInputStream();
		PowerMock.verifyAll();
		assertEquals(s, re);
	}
	
	@Test
	public void testClose() throws Exception
	{
		PowerMock.resetAll();
		socket.shutdownInput();
		socket.shutdownOutput();
		socket.close();
		PowerMock.replayAll();
		conn.close();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testOpenOutputStream() throws Exception
	{
		PowerMock.resetAll();
		OutputStream s = PowerMock.createMock(OutputStream.class);
		socket.setSoTimeout(30000);
		socket.connect(addr, 30000);
		EasyMock.expect(socket.getOutputStream()).andReturn(s);
		PowerMock.replayAll();
		OutputStream re = conn.openOutputStream();
		PowerMock.verifyAll();
		assertEquals(s, re);
	}
	
	@Test
	public void testOpenOutputStream_NoTimeout() throws Exception
	{
		PowerMock.resetAll();
		addr = PowerMock.createMock(InetSocketAddress.class);
		socket = PowerMock.createMock(Socket.class);
		PowerMock.expectNew(Socket.class).andReturn(socket);
		PowerMock.replayAll();
		conn = new AndroidSocketConnection(addr, false);
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		OutputStream s = PowerMock.createMock(OutputStream.class);
		socket.connect(addr);
		EasyMock.expect(socket.getOutputStream()).andReturn(s);
		PowerMock.replayAll();
		OutputStream re = conn.openOutputStream();
		PowerMock.verifyAll();
		assertEquals(s, re);
	}
}
