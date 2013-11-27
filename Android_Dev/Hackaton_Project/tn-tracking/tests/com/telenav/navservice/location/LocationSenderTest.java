package com.telenav.navservice.location;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.navservice.TestUtil;
import com.telenav.navservice.model.App;
import com.telenav.navservice.network.TnNetwork;
import com.telenav.navservice.util.Constants;
import com.telenav.radio.TnCellInfo;

public class LocationSenderTest
{
    private TnNetwork network;
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
        network = createMock(TnNetwork.class);
        expect(network.sendUdp((String)anyObject(), (byte[])anyObject())).andReturn(true).anyTimes();
        
        replay(network);
    }

    @After
    public void tearDown() throws Exception
    {
        network = null;
    }

    @Test
    public void testSetGpsSendingInterval() throws Exception
    {
        LocationSender sender = new LocationSender(new LocationBuffer(100, null), new LocationBuffer(100, null), null);
        sender.app = new App();
        sender.network = network;
        sender.setGpsSendingInterval(10, true);
        assertEquals(sender.gpsSendingIntervalInMillis, 10 * 1000);
        assertTrue(sender.isAttachCell);
        assertTrue(sender.lastGpsSendingTime > 0);
    }
    
    @Test
    public void testSetCellSendingInterval() throws Exception
    {
        LocationSender sender = new LocationSender(new LocationBuffer(100, null), new LocationBuffer(100, null), null);
        sender.app = new App();
        sender.network = network;
        sender.setCellSendingInterval(15);
        assertEquals(sender.cellSendingIntervalInMillis, 15 * 1000);
        assertTrue(sender.lastCellSendingTime > 0);
    }
    
    @Test
    public void testStart() throws Exception
    {
        LocationSender sender = new LocationSender(new LocationBuffer(1, null), new LocationBuffer(1, null), null);
        sender.app = new App();
        sender.network = network;
        sender.start();
        assertTrue(sender.isRunning);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRestart() throws Exception
    {
        LocationSender sender = new LocationSender(new LocationBuffer(1, null), new LocationBuffer(1, null), null);
        sender.app = new App();
        sender.network = network;
        sender.start();
        sender.stop();
        sender.start();
    }
    
    @Test
    public void testCreatePayload() throws Exception
    {
        Vector gpsFixes = new Vector();
        TnLocation loc = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        loc.setTime(123456L);
        loc.setAltitude(37371211);
        loc.setLongitude(-122005612);
        loc.setAltitude(10);
        loc.setSpeed(12);
        loc.setHeading(45);
        loc.setAccuracy(5);
        loc.setLocalTimeStamp(123459L);
        loc.setValid(true);
        gpsFixes.addElement(loc);

        loc = new TnLocation(TnLocationManager.GPS_179_PROVIDER);
        loc.setTime(123457L);
        loc.setAltitude(37371216);
        loc.setLongitude(-122005615);
        loc.setAltitude(18);
        loc.setSpeed(6);
        loc.setHeading(32);
        loc.setAccuracy(9);
        loc.setLocalTimeStamp(123460L);
        loc.setValid(true);
        gpsFixes.addElement(loc);
        
        Vector cellFixes = new Vector();
        loc = new TnLocation(TnLocationManager.NETWORK_PROVIDER);
        loc.setTime(123459L);
        loc.setAltitude(37371453);
        loc.setLongitude(-122005975);
        loc.setAltitude(0);
        loc.setSpeed(0);
        loc.setHeading(0);
        loc.setAccuracy(100);
        loc.setLocalTimeStamp(123466L);
        loc.setValid(true);
        
        TnCellLocation cellLoc = new TnCellLocation(loc);
        TnCellInfo cellInfo = new TnCellInfo();
        cellInfo.cellId = "123";
        cellInfo.baseStationId = "ATT";
        cellInfo.networkType = ""+Constants.NETWORK_TYPE_EDGE;
        cellInfo.networkRadioMode = Constants.RADIO_TYPE_GSM;
        cellInfo.networkCode = ""+Constants.ALLTEL;
        cellInfo.locationAreaCode = "408";
        cellLoc.setCellInfo(cellInfo);
        cellFixes.addElement(cellLoc);
        
        LocationSender sender = new LocationSender(new LocationBuffer(1, null), new LocationBuffer(1, null), null);
        sender.network = network;
        App app = new App();
        sender.setParameters("", app);
        
        byte[] b = sender.createPayload(gpsFixes, cellFixes);
        byte[] expected = new byte[]{3, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 55, 71, 107, -71, 0, 0, 0, 0, 0, 18, -42, -128, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, -128, 0, 0, 45, 0, 10, 1, 0, 5, 1, 0, 0, 0, 10, 0, 0, 127, -1, 0, 32, 0, 18, 0, 0, 9, 4, 0, 0, 0, 30, 0, 0, 0, 12, 0, 0, 1, -104, 0, 0, 0, 123, 0, 0, 0, 0, 0, 0, 0, 127, -1};
        assertArrayEquals(b, expected);
    }
}
