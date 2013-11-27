/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidRadioUtilTest.java
 *
 */
package com.telenav.radio.android;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.telenav.radio.TnCellInfo;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-6
 */
public class AndroidRadioUtilTest
{
    AndroidRadioUtil androidRadioUtil;
    @Before
    public void setUp()
    {
        androidRadioUtil = new AndroidRadioUtil();
    }
    
    @Test
    public void testRetrieveOtherCellLocation()
    {
        android.telephony.cdma.CdmaCellLocation mockCdmaCellLocation = PowerMock.createMock(android.telephony.cdma.CdmaCellLocation.class);
        EasyMock.expect(mockCdmaCellLocation.getNetworkId()).andReturn(11);
        EasyMock.expect(mockCdmaCellLocation.getBaseStationId()).andReturn(22);
        EasyMock.expect(mockCdmaCellLocation.getSystemId()).andReturn(33);
        PowerMock.replayAll();
        
        TnCellInfo cellInfo = new TnCellInfo();
        AndroidRadioUtil.retrieveOtherCellLocation(mockCdmaCellLocation, cellInfo);
        assertEquals("11", cellInfo.baseStationId);
        assertEquals("22", cellInfo.cellId);
        assertEquals("33", cellInfo.locationAreaCode);
        assertEquals(TnCellInfo.MODE_CDMA, cellInfo.networkRadioMode);
        PowerMock.verifyAll();
    }
    
    @Test
    public void testRetrieveOtherCellLocationNoCdma()
    {
        android.telephony.CellLocation mockCellLocation = PowerMock.createMock(android.telephony.CellLocation.class);
        PowerMock.replayAll();
        
        TnCellInfo cellInfo = new TnCellInfo();
        AndroidRadioUtil.retrieveOtherCellLocation(mockCellLocation, cellInfo);
        PowerMock.verifyAll();
    }
}
