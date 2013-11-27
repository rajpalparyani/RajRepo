/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodeExtraInfoSerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;

import org.junit.Assert;

import com.telenav.data.datatypes.map.MapDataUpgradeInfo;

import junit.framework.TestCase;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-6-22
 */
public class TxNodeExtraInfoSerializableTest extends TestCase
{
    
    MapDataUpgradeInfo mapData = null;
    byte[] mapDataBytes = {48, -99, -101, 58, 1, 2, 2, 16, 24, 109, 97, 112, 100, 97, 116, 97, 95, 110, 97, 109, 101, 30, 109, 97, 112, 100, 97, 116, 97, 95, 118, 101, 114, 115, 105, 111, 110, 38, 109, 97, 112, 100, 97, 116, 97, 95, 98, 117, 105, 108, 100, 78, 117, 109, 98, 101, 114, 28, 109, 97, 112, 100, 97, 116, 97, 95, 114, 101, 103, 105, 111, 110, 26, 109, 97, 112, 100, 97, 116, 97, 95, 115, 116, 97, 116, 101, 22, 109, 97, 112, 100, 97, 116, 97, 95, 117, 114, 108, 30, 109, 97, 112, 100, 97, 116, 97, 95, 115, 117, 109, 109, 97, 114, 121, 24, 109, 97, 112, 100, 97, 116, 97, 95, 115, 105, 122, 101, 0, 0};
    
    
    protected void setUp() throws Exception
    {
        mapData = new MapDataUpgradeInfo();
        mapData.setUpgradeMode(1);
        mapData.setName("mapdata_name");
        mapData.setVersion("mapdata_version");
        mapData.setBuildNumber("mapdata_buildNumber");
        mapData.setRegion("mapdata_region");
        mapData.setState("mapdata_state");
        mapData.setUrl("mapdata_url");
        mapData.setSummary("mapdata_summary");
        mapData.setMapDataSize("mapdata_size");
        super.setUp();
    }
    
    public void testToBytes()
    {
        TxNodeExtraInfoSerializable extraInfo = new TxNodeExtraInfoSerializable();
        Assert.assertArrayEquals(mapDataBytes, extraInfo.toBytes(mapData));
    }
    
    public void testToBytesNull()
    {
        TxNodeExtraInfoSerializable extraInfo = new TxNodeExtraInfoSerializable();
        assertNull(extraInfo.toBytes(null));
    }
    
    public void testCreateMapDataUpgradeInfo()
    {
        TxNodeExtraInfoSerializable extraInfo = new TxNodeExtraInfoSerializable();
        MapDataUpgradeInfo actual = extraInfo.createMapDataUpgradeInfo(mapDataBytes);
        assertEquals(mapData.getUpgradeMode() , actual.getUpgradeMode());
        assertEquals(mapData.getName() , actual.getName());
        assertEquals(mapData.getVersion() , actual.getVersion());
        assertEquals(mapData.getBuildNumber() , actual.getBuildNumber());
        assertEquals(mapData.getRegion() , actual.getRegion());
        assertEquals(mapData.getState() , actual.getState());
        assertEquals(mapData.getUrl() , actual.getUrl());
        assertEquals(mapData.getSummary() , actual.getSummary());
        assertEquals(mapData.getMapDataSize() , actual.getMapDataSize());
    }
    
    public void testCreateMapDataUpgradeInfoNull()
    {
        TxNodeExtraInfoSerializable extraInfo = new TxNodeExtraInfoSerializable();
        assertNull(extraInfo.createMapDataUpgradeInfo(null));
    }
}
