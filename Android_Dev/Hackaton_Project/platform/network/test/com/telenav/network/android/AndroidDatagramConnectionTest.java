package com.telenav.network.android;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.network.TnDatagram;


@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidDatagramConnection.class)
public class AndroidDatagramConnectionTest 
{
	private AndroidDatagramConnection conn;
	private DatagramSocket socket;
	
	@Before
	public void setUp() throws Exception 
	{
		SocketAddress addr = PowerMock.createMock(SocketAddress.class);
		socket = PowerMock.createMock(DatagramSocket.class);
		PowerMock.expectNew(DatagramSocket.class).andReturn(socket);
		socket.connect(addr);
		socket.setSoTimeout(30000);
		PowerMock.replayAll();
		conn = new AndroidDatagramConnection(addr, true);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetLocalAddress() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getLocalAddress()).andReturn(null);
		PowerMock.replayAll();
		String address = conn.getLocalAddress();
		PowerMock.verifyAll();
		assertEquals(address, null);
		
		/****************************************************/
		
		PowerMock.resetAll();
		InetAddress addr = PowerMock.createMock(InetAddress.class);
		EasyMock.expect(addr.getHostAddress()).andReturn("address");
		EasyMock.expect(socket.getLocalAddress()).andReturn(addr);
		PowerMock.replayAll();
		address = conn.getLocalAddress();
		PowerMock.verifyAll();
		assertEquals(address, "address");
	}
	
	@Test
	public void testGetLocalPort() throws Exception
	{
		PowerMock.resetAll();
		EasyMock.expect(socket.getLocalPort()).andReturn(800);
		PowerMock.replayAll();
		int port = conn.getLocalPort();
		PowerMock.verifyAll();
		assertEquals(800, port);
	}
	
	@Test
	public void testNewDatagram() throws Exception
	{
		PowerMock.resetAll();
		byte[] buf = new byte[20];
		AndroidDatagram datagram = PowerMock.createMock(AndroidDatagram.class);
		PowerMock.expectNew(AndroidDatagram.class, buf, 20).andReturn(datagram);
		PowerMock.replayAll();
		TnDatagram gram = conn.newDatagram(buf, 20);
		PowerMock.verifyAll();
		assertEquals(gram, datagram);
	}
	
	@Test
	public void testNewDatagram2() throws Exception
	{
//		PowerMock.resetAll();
//		AndroidDatagram datagram = PowerMock.createMock(AndroidDatagram.class);
//		PowerMock.expectNew(byte[], arguments)
//		PowerMock.expectNew(AndroidDatagram.class, EasyMock.anyObject(byte[].class), 20).andReturn(datagram);
//		PowerMock.replayAll();
		conn.newDatagram(20);
//		PowerMock.verifyAll();
	}
	
	@Test
	public void testReceive() throws Exception
	{
		PowerMock.resetAll();
		AndroidDatagram pack = PowerMock.createMock(AndroidDatagram.class);
		socket.receive(EasyMock.anyObject(DatagramPacket.class));
		PowerMock.replayAll();
		conn.receive(pack);
		PowerMock.verifyAll();
	}

	@Test
	public void testSend() throws Exception
	{
		PowerMock.resetAll();
		AndroidDatagram pack = PowerMock.createMock(AndroidDatagram.class);
		socket.send(EasyMock.anyObject(DatagramPacket.class));
		PowerMock.replayAll();
		conn.send(pack);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testClose() throws Exception
	{
		PowerMock.resetAll();
		socket.disconnect();
		socket.close();
		PowerMock.replayAll();
		conn.close();
		PowerMock.verifyAll();
	}
	
	
}
