package com.telenav.data.datatypes.address;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *@author gbwang
 *@date 2011-6-21
 */

public class StopTest extends TestCase
{
	public void testGetFirstLine()
	{
//		Stop stop = new Stop();		
		String expect = "";		
//		assertEquals(expect, stop.getFirstLine());
//		
//		Stop stop2 = new Stop();		
//		stop2.setStreetName("kifer rd");
//		expect = "kifer rd";
//		assertEquals(expect, stop2.getFirstLine());
//		
//		Stop stop3 = new Stop();		
//		stop3.setHouseNumber("1130");	
//		stop3.setStreetName("kifer rd");
//		expect = "1130 kifer rd";		
//		assertEquals(expect, stop3.getFirstLine());
//		
//		Stop stop4 = new Stop();
//		stop4.setStreetName("kifer rd");
//		stop4.setCrossStreetName("Wolf Rd");
//		expect = "Wolf Rd at kifer rd";
//		assertEquals(expect, stop4.getFirstLine());
//		
		Stop stop5 = new Stop();
		stop5.setFirstLine("1130 kifer rd");
		expect = "1130 kifer rd";
		assertEquals(expect, stop5.getFirstLine());
//		
//		Stop stop6 = new Stop();
//		stop6.setStreetName("kifer rd\\ test");
//		expect = "kifer rd";
//		assertEquals(expect, stop6.getFirstLine());
//		
//		Stop stop7 = new Stop();
//		stop7.setStreetName("kifer rd");
//		stop7.setCrossStreetName("Wolf Rd\\");
//		expect = "Wolf Rd at kifer rd";
//		assertEquals(expect, stop7.getFirstLine());
	}
	
	public void testParseFirstLine()
	{
		String expect = "";
		Stop stop = new Stop();
		stop.setFirstLine("1130 kifer rd");
		stop.parseFirstLine(stop.getFirstLine());
		expect = "1130";	
		assertEquals(expect, stop.getHouseNumber());
		expect = "kifer rd";
		assertEquals(expect, stop.getStreetName());
		
		Stop stop2 = new Stop();		
		stop2.setFirstLine("Wolf Rd at kifer rd");
		stop2.parseFirstLine(stop2.getFirstLine());
		expect = "Wolf Rd";
		assertEquals(expect, stop2.getCrossStreetName());
		expect = "kifer rd";
		assertEquals(expect, stop2.getStreetName());
		
		Stop stop3 = new Stop();	
		stop3.setFirstLine("Wolf Rd @ kifer rd");
		stop3.parseFirstLine(stop3.getFirstLine());		
		expect = "Wolf Rd";
		assertEquals(expect, stop3.getCrossStreetName());
		expect = "kifer rd";
		assertEquals(expect, stop3.getStreetName());	
	}
	
	public void testEqualsIgnoreCase_A()
	{
//		Stop stop1 = new Stop();
//		Stop stop2 = new Stop();
//		
//		stop1.setFirstLine("1130 kifer rd");
//		stop2.setHouseNumber("1130");	
//		stop2.setStreetName("kifer rd");
//		boolean actual = stop1.equalsIgnoreCase(stop2);
//		Assert.assertTrue(actual);
	}
	
	public void testEqualsIgnoreCase_B()
	{
		Stop stop1 = new Stop();
		Stop stop2 = new Stop();
		
		stop1.setFirstLine("1130 kifer rd");
		stop1.setCity("sunnyvale");
		stop2.setFirstLine("1130 kifer rd");
		stop2.setCity("Newwork");
		
		boolean actual = stop1.equalsIgnoreCase(stop2);
		Assert.assertFalse(actual);
	}
	
	public void testEqualsIgnoreCase_C()
	{
		Stop stop1 = new Stop();
		Stop stop2 = new Stop();
		
		stop1.setFirstLine("1130 kifer rd");
		stop1.setStreetName("kifer rd");
		stop1.setCrossStreetName("Wolf Rd");
		
		stop2.setFirstLine("Wolf Rd at kifer rd");
		
		boolean actual = stop1.equalsIgnoreCase(stop2);
		Assert.assertTrue(actual);
	}
	
	public void testEqualsIgnoreCase_D()
	{
		Stop stop1 = new Stop();
		Stop stop2 = new Stop();
		
		stop1.setFirstLine("1130 kifer rd");
		stop1.setStreetName("kifer rd");
		stop1.setCrossStreetName("Wolf Rd");
		
		stop2.setFirstLine("Wolf Rd at kifer rd");
		
		boolean actual = stop1.equalsIgnoreCase(stop2);
		Assert.assertTrue(actual);
	}
	
	public void testFirstLineAudio()
	{
		Stop stop = new Stop();
		byte[] buf = "1000".getBytes();
		stop.setFirstLineAudio(buf);
		Assert.assertEquals(buf, stop.getFirstLineAudio());
	}
	
	public void testLastLineAudio()
	{
		Stop stop = new Stop();
		byte[] buf = "1000".getBytes();
		stop.setLastLineAudio(buf);
		Assert.assertEquals(buf, stop.getLastLineAudio());
	}
	
	public void testSharedAddress()
	{
		Stop stop = new Stop();
		stop.setSharedAddress(false);
		Assert.assertFalse(stop.isSharedAddress());
	}
	
	public void testCounty()
	{
		Stop stop = new Stop();
		stop.setCounty("county");
		Assert.assertEquals("county", stop.getCounty());
	}
	
	public void testIsValid()
	{
		Stop stop = new Stop();
		stop.setLat(3737890);
		stop.setLon(-12201074);
		Assert.assertTrue(stop.isValid());
	}
	
	public void testLabel()
	{
		Stop stop = new Stop();
		stop.setLabel("KFC");
		Assert.assertEquals("KFC", stop.getLabel());
	}
	
	public void testID()
	{
		Stop stop = new Stop();
		stop.setId(10000);
		Assert.assertEquals(10000, stop.getId());
	}
	
	public void testCountry()
	{
		Stop stop = new Stop();
		stop.setCountry("China");
		Assert.assertEquals("China", stop.getCountry());
	}
	
	public void testProvince()
	{
		Stop stop = new Stop();
		stop.setProvince("CA");
		Assert.assertEquals("CA", stop.getProvince());
	}
	
	public void testPostalCode()
	{
		Stop stop = new Stop();
		stop.setPostalCode("90994");
		Assert.assertEquals("90994", stop.getPostalCode());
	}
	
	public void testType()
	{
		Stop stop = new Stop();
		byte b = 0;
		stop.setType(b);
		Assert.assertEquals(b, stop.getType());
	}
	
	public void testStatus()
	{
		Stop stop = new Stop();
		byte b = 0;
		stop.setStatus(b);
		Assert.assertEquals(b, stop.getStatus());
	}
	
	public void testIsGeocoded()
	{
		Stop stop = new Stop();
		stop.setIsGeocoded(false);
		Assert.assertFalse(stop.isGeocoded());
	}
}



