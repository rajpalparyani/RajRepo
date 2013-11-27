/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StringConverter_en_USTest.java
 *
 */
package com.telenav.res.converter;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.i18n.ResourceBundle;
import com.telenav.module.AppConfigHelper;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 * @author bmyang
 * @date 2011-7-7
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceManager.class, AppConfigHelper.class})
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
public class StringConverter_en_USTest extends TestCase
{
	StringConverter_en_US stringConverter_en_US = new StringConverter_en_US();
	ResourceBundle resourceBundle;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		resourceBundle = PowerMock.createMock(ResourceBundle.class);
		ResourceManager resourceManager = PowerMock.createMock(ResourceManager.class);
		PowerMock.mockStatic(ResourceManager.class);
		EasyMock.expect(ResourceManager.getInstance()).andReturn(resourceManager).anyTimes();
		EasyMock.expect(resourceManager.getCurrentBundle()).andReturn(resourceBundle).anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_METER, IStringCommon.FAMILY_COMMON)).andReturn("meter").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_FEET, IStringCommon.FAMILY_COMMON)).andReturn("feet").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_KILOMETER, IStringCommon.FAMILY_COMMON)).andReturn("km").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_MILE, IStringCommon.FAMILY_COMMON)).andReturn("mile").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_NORTH, IStringCommon.FAMILY_COMMON)).andReturn("North").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_NORTH_EAST, IStringCommon.FAMILY_COMMON)).andReturn("North East").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_NORTH_WEST, IStringCommon.FAMILY_COMMON)).andReturn("North West").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_SOUTH, IStringCommon.FAMILY_COMMON)).andReturn("South").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_SOUTH_EAST, IStringCommon.FAMILY_COMMON)).andReturn("South East").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_SOUTH_WEST, IStringCommon.FAMILY_COMMON)).andReturn("South West").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_EAST, IStringCommon.FAMILY_COMMON)).andReturn("East").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_WEST, IStringCommon.FAMILY_COMMON)).andReturn("West").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_KPH, IStringCommon.FAMILY_COMMON)).andReturn("KPH").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_MPH, IStringCommon.FAMILY_COMMON)).andReturn("MPH").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_LAT, IStringCommon.FAMILY_COMMON)).andReturn("LAT").anyTimes();
		EasyMock.expect(resourceBundle.getString(IStringCommon.RES_LON, IStringCommon.FAMILY_COMMON)).andReturn("LON").anyTimes();
		PowerMock.replayAll();
		super.setUp();
	}
	
	/*
	 * Test checkPhoneNumber
	 * 
	 */
	public void testCheckPhoneNumber_A()
	{
	    assertEquals("", stringConverter_en_US.convertPhoneNumber(null));
        assertEquals("", stringConverter_en_US.convertPhoneNumber(""));
	}
	
	

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistance(long, int)}.
	 */
	public void testConvertDistance()
	{
		assertEquals("", stringConverter_en_US.convertDistance(0, -1));
		assertEquals("", stringConverter_en_US.convertDistance(0, StringConverter.DEG2SHORT.length + 1));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistance(long, int)}.
	 */
	public void testConvertDistance_short_unit()
	{
		assertEquals("333 meter", stringConverter_en_US.convertDistance(300, 0));
		assertEquals("146 feet", stringConverter_en_US.convertDistance(40, StringConverter.DEG2SHORT.length - 1));
		PowerMock.verifyAll();
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistance(long, int)}.
	 */
	public void testConvertDistance_long_unit()
	{
		assertEquals("1.7 km", stringConverter_en_US.convertDistance(1500, 0));
		assertEquals("0.3 mile", stringConverter_en_US.convertDistance(400, StringConverter.DEG2SHORT.length - 1));
		PowerMock.verifyAll();
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistanceMeterToMile(long, int)}.
	 */
	public void testConvertDistanceMeterToMile()
	{
		assertEquals("", stringConverter_en_US.convertDistanceMeterToMile(-1, 0));
		assertEquals("", stringConverter_en_US.convertDistanceMeterToMile(300, StringConverter.UNIT_KM));
		assertEquals("", stringConverter_en_US.convertDistanceMeterToMile(300, StringConverter.UNIT_FOOT));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistanceMeterToMile(long, int)}.
	 */
	public void testConvertDistanceMeterToMile_meter_long()
	{
		//distanceInFeet > DISTANCE_UNIT_CRITICAL_POINT
		assertEquals("0.3 km", stringConverter_en_US.convertDistanceMeterToMile(300, StringConverter.UNIT_METER));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistanceMeterToMile(long, int)}.
	 */
	public void testConvertDistanceMeterToMile_meter_short()
	{
		//distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT
		assertEquals("30 meter", stringConverter_en_US.convertDistanceMeterToMile(30, StringConverter.UNIT_METER));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistanceMeterToMile(long, int)}.
	 */
	public void testConvertDistanceMeterToMile_mile_long()
	{
		//distanceInFeet > DISTANCE_UNIT_CRITICAL_POINT
		assertEquals("0.2 mile", stringConverter_en_US.convertDistanceMeterToMile(300, StringConverter.UNIT_MILE));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertDistanceMeterToMile(long, int)}.
	 */
	public void testConvertDistanceMeterToMile_mile_short()
	{
		//distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT
		assertEquals("98 feet", stringConverter_en_US.convertDistanceMeterToMile(30, StringConverter.UNIT_MILE));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertHeading(int)}.
	 */
	public void testConvertHeading()
	{
		assertEquals("North", stringConverter_en_US.convertHeading(20));
		assertEquals("North East", stringConverter_en_US.convertHeading(40));
		assertEquals("East", stringConverter_en_US.convertHeading(80));
		assertEquals("South East", stringConverter_en_US.convertHeading(120));
		assertEquals("South", stringConverter_en_US.convertHeading(160));
		assertEquals("South West", stringConverter_en_US.convertHeading(240));
		assertEquals("West", stringConverter_en_US.convertHeading(280));
		assertEquals("North West", stringConverter_en_US.convertHeading(320));
		assertEquals("North", stringConverter_en_US.convertHeading(-20));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSpeed(int, int)}.
	 */
	public void testConvertSpeed()
	{
		assertEquals("NA", stringConverter_en_US.convertSpeed(-20, 0));
		assertEquals("NA", stringConverter_en_US.convertSpeed(-20, 1));
		assertEquals("100 KPH", stringConverter_en_US.convertSpeed(100, 0));
		assertEquals("62 MPH", stringConverter_en_US.convertSpeed(100, 1));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertCostTime(long)}.
	 */
	public void testConvertCostTime()
	{
		assertEquals("0min", stringConverter_en_US.convertCostTime(-200));
		assertEquals("0min", stringConverter_en_US.convertCostTime(200));
		assertEquals("1min", stringConverter_en_US.convertCostTime(2000));
		assertEquals("5hr 33min", stringConverter_en_US.convertCostTime(20000000));
		assertEquals("55555hr", stringConverter_en_US.convertCostTime(200000000234L));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertTime(long)}.
	 */
	public void testConvertTime()
	{
		Date date0 = new Date();
		date0.setHours(19);
		date0.setMinutes(33);
		
		Date date1 = new Date();
		date1.setHours(5);
		date1.setMinutes(07);
		
		Date date2 = new Date();
		date2.setHours(11);
		date2.setMinutes(58);
		
		
		assertEquals("7:33 pm", stringConverter_en_US.convertTime(date0.getTime()));
		assertEquals("5:07 am", stringConverter_en_US.convertTime(date1.getTime()));
		assertEquals("11:58 am", stringConverter_en_US.convertTime(date2.getTime()));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertTimeToDay(long)}.
	 */
	public void testConvertTimeToDay()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(1976, 4, 3);
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2011, 6, 10);
		
		assertEquals("May 3, 1976", stringConverter_en_US.convertTimeToDay(calendar.getTimeInMillis()));
		assertEquals("Jul 10, 2011", stringConverter_en_US.convertTimeToDay(calendar1.getTimeInMillis()));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress()
	{
		Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setFirstLine("1130 kifer rd");
        stop.setId(2100);
        stop.setIsGeocoded(false);
        stop.setLabel("1130 kifer rd");
        stop.setLat(3737392);
        stop.setLon(-12201074);
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setStreetName("1130 kifer rd");
        stop.setType(Stop.STOP_RECENT);
        assertEquals("1130 kifer rd, sunnyvale, CA 94086", stringConverter_en_US.convertAddress(stop, false));
        assertEquals("1130 kifer rd, sunnyvale, CA 94086", stringConverter_en_US.convertAddress(stop, true));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_show_Label()
	{
		Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setFirstLine("1130 kifer rd");
        stop.setCountry("United States");
        stop.setLabel("home");
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        stop.setType(Stop.STOP_RECENT);
        assertEquals("home, 1130 kifer rd, sunnyvale, CA 94086", stringConverter_en_US.convertAddress(stop, true));
        assertEquals("1130 kifer rd, sunnyvale, CA 94086", stringConverter_en_US.convertAddress(stop, false));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_wrong_PostalCode()
	{
		Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setCountry("United States");
        stop.setFirstLine("1130 kifer rd");
        stop.setIsGeocoded(false);
        stop.setLabel("1130 kifer rd");
        stop.setPostalCode("000000");
        stop.setProvince("CA");
        assertEquals("1130 kifer rd, sunnyvale, CA", stringConverter_en_US.convertAddress(stop, true));
        stop.setPostalCode("abbcdd");
        assertEquals("1130 kifer rd, sunnyvale, CA, abbcdd", stringConverter_en_US.convertAddress(stop, false));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_LAT_LON()
	{
		Stop stop = new Stop();
        stop.setLat(3737392);
        stop.setLon(-12201074);
        assertEquals("LAT 37.37392, LON -122.01074", stringConverter_en_US.convertAddress(stop, true));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_LAT_LON_1()
	{
		Stop stop = new Stop();
        stop.setLat(34578);
        stop.setLon(-12201074);
        assertEquals("LAT 0.34578, LON -122.01074", stringConverter_en_US.convertAddress(stop, true));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_LAT_LON_2()
	{
		Stop stop = new Stop();
		stop.setLat(-67890);
		stop.setLon(-12201074);
		assertEquals("LAT -0.67890, LON -122.01074", stringConverter_en_US.convertAddress(stop, true));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_LAT_LON_3()
	{
		Stop stop = new Stop();
		stop.setLat(-6891);
		stop.setLon(12345);
		assertEquals("LAT -0.06891, LON 0.12345", stringConverter_en_US.convertAddress(stop, true));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertAddress(com.telenav.data.datatypes.address.Stop, boolean)}.
	 */
	public void testConvertAddress_LAT_LON_4()
	{
		Stop stop = new Stop();
		stop.setLat(-890);
		stop.setLon(567);
		assertEquals("LAT -0.00890, LON 0.00567", stringConverter_en_US.convertAddress(stop, true));
	}


	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine()
	{
		Stop stop = new Stop();
        stop.setCity("sunnyvale");
        stop.setPostalCode("94086");
        stop.setProvince("CA");
        assertEquals("sunnyvale, CA 94086", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_City()
	{
		Stop stop = new Stop();
        stop.setCity("sunnyvale");
        assertEquals("sunnyvale", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_Province()
	{
		Stop stop = new Stop();
		stop.setProvince("CA");
        assertEquals("CA", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_PostalCode()
	{
		Stop stop = new Stop();
		stop.setPostalCode("94086");
        assertEquals("94086", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_City_PostalCode()
	{
		Stop stop = new Stop();
		stop.setCity("sunnyvale");
		stop.setPostalCode("94086");
        assertEquals("sunnyvale 94086", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_City_Province()
	{
		Stop stop = new Stop();
		stop.setCity("sunnyvale");
		stop.setProvince("CA");
        assertEquals("sunnyvale, CA", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_Province_PostalCode()
	{
		Stop stop = new Stop();
		stop.setPostalCode("94086");
		stop.setProvince("CA");
        assertEquals("CA 94086", stringConverter_en_US.convertSecondLine(stop));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_Null()
	{
        assertNull(stringConverter_en_US.convertSecondLine(null));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertSecondLine(com.telenav.data.datatypes.address.Stop)}.
	 */
	public void testConvertSecondLine_Empty()
	{
		assertEquals("", stringConverter_en_US.convertSecondLine(new Stop()));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertPhoneNumber(java.lang.String)}.
	 */
	public void testConvertPhoneNumber_Empty()
	{
        assertEquals("", stringConverter_en_US.convertPhoneNumber(null));
        assertEquals("", stringConverter_en_US.convertPhoneNumber(""));
        assertEquals("", stringConverter_en_US.convertPhoneNumber("((((((((((((((("));
        assertEquals("", stringConverter_en_US.convertPhoneNumber("*()-+)))))))))))))))))"));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertPhoneNumber(java.lang.String)}.
	 */
	public void testConvertPhoneNumber_LT_OR_EQ_7()
	{
        assertEquals("234", stringConverter_en_US.convertPhoneNumber("234A"));
        assertEquals("23478", stringConverter_en_US.convertPhoneNumber("23478B"));
        assertEquals("234-6784", stringConverter_en_US.convertPhoneNumber("2346784"));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertPhoneNumber(java.lang.String)}.
	 */
	public void testConvertPhoneNumber_8_9_10()
	{
        assertEquals("(2) 346-7844", stringConverter_en_US.convertPhoneNumber("23467844"));//8 digit
        assertEquals("(23) 467-8445", stringConverter_en_US.convertPhoneNumber("234678445"));//9 digit
        assertEquals("(234) 678-4456", stringConverter_en_US.convertPhoneNumber("2346784456"));//10 digit
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#convertPhoneNumber(java.lang.String)}.
	 */
	public void testConvertPhoneNumber_GT_OR_EA_11_A()
	{
        assertEquals("(346) 784-4555", stringConverter_en_US.convertPhoneNumber("23467844555"));//11 digit
        assertEquals("(346) 784-4555", stringConverter_en_US.convertPhoneNumber("11123467844555"));//13 digit
        assertEquals("(346) 784-4555", stringConverter_en_US.convertPhoneNumber("-(_ +23)467844555"));//11 digit with special character
        assertEquals("(346) 784-4555", stringConverter_en_US.convertPhoneNumber("1(112)3467-844555*"));//13 digit with special character
        assertEquals("(408) 718-1954", stringConverter_en_US.convertPhoneNumber("(408) 718-1954"));
        assertEquals("(408) 718-1954", stringConverter_en_US.convertPhoneNumber("(408)) 718-1954)"));
        assertEquals("(408) 718-1954", stringConverter_en_US.convertPhoneNumber("((((408))    718-1954)"));
	}
	

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#formatToInt(double)}.
	 */
	public void testFormatToInt()
	{
		assertEquals("11", stringConverter_en_US.formatToInt(11));
		assertEquals("0", stringConverter_en_US.formatToInt(0));
	}

	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#formatToInt(double)}.
	 */
	public void testFormatToInt_Double()
	{
		assertEquals("11", stringConverter_en_US.formatToInt(11.));
		assertEquals("1", stringConverter_en_US.formatToInt(1.49));
		assertEquals("2", stringConverter_en_US.formatToInt(1.64));
		assertEquals("3", stringConverter_en_US.formatToInt(2.5));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#formatToOneDecimal(double)}.
	 */
	public void testFormatToOneDecimal()
	{
		assertEquals("11.0", stringConverter_en_US.formatToOneDecimal(11));
		assertEquals("0.0", stringConverter_en_US.formatToOneDecimal(0));
	}
	
	/**
	 * Test method for {@link com.telenav.res.converter.StringConverter_en_US#formatToOneDecimal(double)}.
	 */
	public void testFormatToOneDecimal_Double()
	{
		assertEquals("11.0", stringConverter_en_US.formatToOneDecimal(11.));
		assertEquals("1.5", stringConverter_en_US.formatToOneDecimal(1.49));
		assertEquals("1.6", stringConverter_en_US.formatToOneDecimal(1.64));
		assertEquals("1.5", stringConverter_en_US.formatToOneDecimal(1.45));
	}

}
