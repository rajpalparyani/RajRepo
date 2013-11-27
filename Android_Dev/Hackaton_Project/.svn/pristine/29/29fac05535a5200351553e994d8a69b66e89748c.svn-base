/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodeLocationSerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.radio.TnCellInfo;

import org.junit.Assert;
import junit.framework.TestCase;

/**
 *@author jxue
 *@date 2011-6-28
 */
public class TxNodeLocationSerializableTest extends TestCase
{
	TxNodeLocationSerializable txNodeLocationSerializable;
	byte[] dataNull = null;
	byte[] dataNotNull;
	Node node;
	
    public void setUp() throws Exception
    {
        txNodeLocationSerializable = new TxNodeLocationSerializable();
        super.setUp();
    }

    public void testCreateTnCellInfoNull()
    {
        assertNull(txNodeLocationSerializable.createTnCellInfo(dataNull));
    }
    
    public void testCreateTnCellInfoNotNull()
    {
        node = new Node();
        node.addString("baseStationId");
        node.addString("cellId");
        node.addString("countryCode");
        node.addString("locationAreaCode");
        node.addString("networkCode");
        node.addString("networkOperatorName");
        node.addString("networkType");
        dataNotNull = node.toBinary();
        TnCellInfo cellInfo = new TnCellInfo();
        cellInfo = txNodeLocationSerializable.createTnCellInfo(dataNotNull);
        assertEquals("baseStationId", cellInfo.baseStationId);
        assertEquals("cellId", cellInfo.cellId);
        assertEquals("countryCode", cellInfo.countryCode);
        assertEquals("locationAreaCode", cellInfo.locationAreaCode);
        assertEquals("networkCode", cellInfo.networkCode);
        assertEquals("networkOperatorName", cellInfo.networkOperatorName);
        assertEquals("networkType", cellInfo.networkType);
        
    }

    public void testCreateTnLocationNull()
    {
        assertNull(txNodeLocationSerializable.createTnLocation(dataNull));
    }
    
    public void testCreateTnLocationNotNull()
    {
        node = new Node();
        node.addValue(20110712);
        node.addValue(1111111);
        node.addValue(9999999);
        node.addValue(25);
        node.addValue(3);
        node.addValue(0);
        node.addValue(100);
        node.addValue(578889888);
        dataNotNull = node.toBinary();
        TnLocation location = new TnLocation("gps-179");
        location = txNodeLocationSerializable.createTnLocation(dataNotNull);
        assertEquals(20110712, location.getTime());
        assertEquals(1111111, location.getLatitude());
        assertEquals(9999999, location.getLongitude());
        assertEquals(25, location.getSpeed());
        assertEquals(3, location.getHeading());
        assertEquals(100, location.getAccuracy());
        assertEquals(578889888, location.getLocalTimeStamp());
    }
    
    public void testToBytes_TnLocationNull()
    {
        TnLocation location = null;
        assertNull(txNodeLocationSerializable.toBytes(location));
    }
    
    public void testToBytes_TnLocationNotNull()
    {
        TnLocation location = new TnLocation("gps-179");
        location.setTime(20110712);
        location.setLatitude(111111);
        location.setLongitude(999999);
        location.setSpeed(25);
        location.setHeading(7);
        location.setAccuracy(100);
        location.setLocalTimeStamp(788776887);
        dataNotNull = txNodeLocationSerializable.toBytes(location);
        node = new Node();
        node.addValue(20110712);
        node.addValue(111111);
        node.addValue(999999);
        node.addValue(25);
        node.addValue(7);
        node.addValue(0);
        node.addValue(100);
        node.addValue(788776887);
        Assert.assertArrayEquals(node.toBinary(), dataNotNull);
    }
    
    public void testToBytes_TnCellInfoNull()
    {
        TnCellInfo info = null;
        assertNull(txNodeLocationSerializable.toBytes(info));
    }
    
    public void testToBytes_TnCellInfoNotNull()
    {
        TnCellInfo info = new TnCellInfo();
        info.networkRadioMode = 11;
        info.baseStationId = "baseStationId";
        info.cellId = "cellId";
        info.countryCode = "countryCode";
        info.locationAreaCode = "locationAreaCode";
        info.networkCode = "networkCode";
        info.networkOperatorName = "networkOperatorName";
        info.networkType = "networkType";
        node = new Node();
        node.addValue(11);
        node.addString("baseStationId");
        node.addString("cellId");
        node.addString("countryCode");
        node.addString("locationAreaCode");
        node.addString("networkCode");
        node.addString("networkOperatorName");
        node.addString("networkType");
        Assert.assertArrayEquals(node.toBinary(), txNodeLocationSerializable.toBytes(info));
    }
   
    public void testGetProvider()
    {
        assertEquals(TnLocationManager.GPS_179_PROVIDER,txNodeLocationSerializable.getProvider(txNodeLocationSerializable.TYPE_POSITION));
        assertEquals(TnLocationManager.ALONGROUTE_PROVIDER,txNodeLocationSerializable.getProvider(txNodeLocationSerializable.TYPE_FAKE));
        assertEquals(TnLocationManager.NETWORK_PROVIDER,txNodeLocationSerializable.getProvider(txNodeLocationSerializable.TYPE_CELLSITE));
        assertEquals(TnLocationManager.GPS_179_PROVIDER,txNodeLocationSerializable.getProvider(10));
    }
    
    public void testGetFixType()
    {
        TnLocation location_GPS_179_PROVIDER = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        TnLocation location_ALONGROUTE_PROVIDER = new TnLocation(TnLocationManager.ALONGROUTE_PROVIDER);
        TnLocation location_MVIEWER_PROVIDER = new TnLocation(TnLocationManager.MVIEWER_PROVIDER);
        TnLocation location_NETWORK_PROVIDER = new TnLocation(TnLocationManager.NETWORK_PROVIDER);
        TnLocation location_TN_NETWORK_PROVIDER = new TnLocation(TnLocationManager.TN_NETWORK_PROVIDER);
        TnLocation location_other = new TnLocation("other");
        
        assertEquals (txNodeLocationSerializable.TYPE_POSITION, txNodeLocationSerializable.getFixType(location_GPS_179_PROVIDER));
        assertEquals (txNodeLocationSerializable.TYPE_FAKE, txNodeLocationSerializable.getFixType(location_ALONGROUTE_PROVIDER));
        assertEquals (txNodeLocationSerializable.TYPE_FAKE, txNodeLocationSerializable.getFixType(location_MVIEWER_PROVIDER));
        assertEquals (txNodeLocationSerializable.TYPE_CELLSITE, txNodeLocationSerializable.getFixType(location_NETWORK_PROVIDER));
        assertEquals (txNodeLocationSerializable.TYPE_CELLSITE, txNodeLocationSerializable.getFixType(location_TN_NETWORK_PROVIDER));
        assertEquals (txNodeLocationSerializable.TYPE_POSITION, txNodeLocationSerializable.getFixType(location_other));
        
        
    }

}
