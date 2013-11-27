package com.telenav.network.android;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidDatagram.class)
public class AndroidDatagramTest 
{
	private AndroidDatagram datagram;
	private DatagramPacket datagramPacket;
	
	@Before
	public void setUp() throws Exception 
	{
		datagram = new AndroidDatagram(new byte[20], 20);
		datagramPacket = PowerMock.createMock(DatagramPacket.class);
		datagram.datagramPacket = datagramPacket;
	}
	
	@Test
	public void testGetData()
	{
		PowerMock.resetAll();
		byte[] data = new byte[20];
		EasyMock.expect(datagramPacket.getData()).andReturn(data);
		PowerMock.replayAll();
		byte[] buf = datagram.getData();
		PowerMock.verifyAll();
		assertEquals(buf, data);
		
	}
	
	@Test
	public void testGetLength()
	{
		PowerMock.resetAll();
		EasyMock.expect(datagramPacket.getLength()).andReturn(20);
		PowerMock.replayAll();
		int len = datagram.getLength();
		PowerMock.verifyAll();
		assertEquals(20, len);	
	}

	@Test
	public void testGetOffset()
	{
		PowerMock.resetAll();
		EasyMock.expect(datagramPacket.getOffset()).andReturn(20);
		PowerMock.replayAll();
		int len = datagram.getOffset();
		PowerMock.verifyAll();
		assertEquals(20, len);	
	}
	
	@Test
	public void testSetData()
	{
		PowerMock.resetAll();
		byte[] buffer = new byte[20];
		datagramPacket.setData(buffer, 20, 40);
		PowerMock.replayAll();
		datagram.setData(buffer, 20, 40);
		PowerMock.verifyAll();
	}

	@Test
	public void testSetLength()
	{
		PowerMock.resetAll();
		datagramPacket.setLength(20);
		PowerMock.replayAll();
		datagram.setLength(20);
		PowerMock.verifyAll();
	}
}
