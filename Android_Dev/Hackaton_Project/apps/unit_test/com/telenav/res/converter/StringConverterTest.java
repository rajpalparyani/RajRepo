/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * StringConverterTest.java
 *
 */
package com.telenav.res.converter;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.res.ResourceManager;

/**
 *@author jxue
 *@date 2011-6-24
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerializableManager.class,Address.class})

public class StringConverterTest extends TestCase
{
    StringConverter converter;
    
    public void setUp() throws Exception
    {
        converter = ResourceManager.getInstance().getStringConverter();
        super.setUp();
    }

    public void testConvert()
    {
        String[] values = {"4085056710"};
        String stringConvert = converter.convert("hello ${0}, are you ok?", values);
        Assert.assertEquals("hello 4085056710, are you ok?", stringConvert);
    }
    
    public void testIsWordMatched()
    {
        Assert.assertFalse(converter.isWordMatched("d", "STX: Henry e Rohlsen"));
        Assert.assertTrue(converter.isWordMatched("d", "MAZ: Eugenio Maria De Hostos"));
        Assert.assertTrue(converter.isWordMatched(" ", "STX: Henry e Rohlsen"));
        Assert.assertTrue(converter.isWordMatched("d", "STX: Denry e Rohlsen"));
        Assert.assertFalse(converter.isWordMatched("d", "STX: Hdnry e Rohlsen"));
    }
    
    public void testConvertAirportOneParemeter()
    {
        Assert.assertEquals("Grand Canyon Caverns", converter.convertAirport("grand canyon caverns"));
    }
    
    public void testConvertAirportTwoParemeter()
    {
        Assert.assertEquals("Grand Canyon Caverns", converter.convertAirport("grand canyon caverns", " "));
    }
    
    public void testConvertAirportLabel()
    {
        Assert.assertEquals("1G4: Grand Canyon Caverns", converter.converAirportLabel("1G4: GRAND CANYON CAVERNS"));
    }

    public void testConvertDataSizeString()
    {
        long size;
        
        size = 0;
        
        assertEquals("0B", converter.convertDataSizeString(size, 0, true));
        assertEquals("0B", converter.convertDataSizeString(size, 1, true));
        assertEquals("0B", converter.convertDataSizeString(size, 2, true));
        
        size = 1;
        assertEquals("1B", converter.convertDataSizeString(size, 0, true));
        assertEquals("1B", converter.convertDataSizeString(size, 1, true));
        assertEquals("1B", converter.convertDataSizeString(size, 2, true));
        
        size = 1023;
        assertEquals("1023B", converter.convertDataSizeString(size, 0, true));
        assertEquals("1023B", converter.convertDataSizeString(size, 1, true));
        assertEquals("1023B", converter.convertDataSizeString(size, 2, true));
        
        size = 1024;
        assertEquals("1KB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1KB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1KB", converter.convertDataSizeString(size, 2, true));
        
        size = 1025;
        assertEquals("1KB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1KB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1KB", converter.convertDataSizeString(size, 2, true));
        assertEquals("1.001KB", converter.convertDataSizeString(size, 3, true));
        
        size = 1048575;
        assertEquals("1MB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1MB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1MB", converter.convertDataSizeString(size, 2, true));
        assertEquals("1023.999KB", converter.convertDataSizeString(size, 3, true));
        
        size = 1048576;
        assertEquals("1MB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1MB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1MB", converter.convertDataSizeString(size, 2, true));
        assertEquals("1MB", converter.convertDataSizeString(size, 3, true));
        
        size = 1;
        assertEquals("1B", converter.convertDataSizeString(size, 0, true));
        assertEquals("1B", converter.convertDataSizeString(size, 1, true));
        assertEquals("1B", converter.convertDataSizeString(size, 2, true));
        assertEquals("1B", converter.convertDataSizeString(size, 3, true));
        
        size = 1073741824;
        assertEquals("1GB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 2, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 3, true));
        
        size = 1073741823;
        assertEquals("1GB", converter.convertDataSizeString(size, 0, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 1, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 2, true));
        assertEquals("1GB", converter.convertDataSizeString(size, 3, true));
        
        size = 10737418;
        assertEquals("11MB", converter.convertDataSizeString(size, 0, true));
        assertEquals("10.3MB", converter.convertDataSizeString(size, 1, true));
        assertEquals("10.24MB", converter.convertDataSizeString(size, 2, true));
        assertEquals("10.24MB", converter.convertDataSizeString(size, 3, true));
        
        size = 2884253;
        assertEquals("2.75MB", converter.convertDataSizeString(size, 2, true));
    }
}
