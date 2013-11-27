package com.telenav.gps;

/**
 *@author jyxu
 *@date 2011-7-8
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.location.TnLocation;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReplayLocationProvider.class, TnFileConnection.class, TnPersistentManager.class})
public class ReplayLocationProviderTest extends TestCase
{
    ReplayLocationProvider provider;
    long currentTime;
    public void setUp()throws Exception
    {
        provider = new ReplayLocationProvider("test");
        currentTime = System.currentTimeMillis();
    }

//    public void testLoadGpsSourceFiles() throws Exception
//    {
//        TnLocation loc1 = new TnLocation("ReplayLocationProvider");
//        loc1.setTime(currentTime);
//        loc1.setLocalTimeStamp(currentTime + 100);
//        loc1.setSpeed(100);
//        loc1.setAccuracy(1);
//        loc1.setHeading(30);
//        loc1.setLatitude(100001);
//        loc1.setLongitude(100002);
//        loc1.setValid(true);
//        loc1.setSatellites(4);
//        
//        TnLocation loc2 = new TnLocation("ReplayLocationProvider");
//        loc2.setTime(currentTime+100);
//        loc2.setLocalTimeStamp(currentTime + 200);
//        loc2.setSpeed(100);
//        loc2.setAccuracy(2);
//        loc2.setHeading(30);
//        loc2.setLatitude(100003);
//        loc2.setLongitude(100004);
//        loc2.setValid(true);
//        loc2.setSatellites(5);
//        
//        String log = loc1.toString()+"\r"+loc2.toString()+"\r";
//        byte[] data = log.getBytes();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
//        
//        Vector fileNames = new Vector();
//        fileNames.add("file1");
//        
//        TnFileConnection fileMock = PowerMock.createMock(TnFileConnection.class);
//        EasyMock.expect(fileMock.openInputStream()).andReturn(inputStream);
//        fileMock.close();
//        EasyMock.expectLastCall();
//       
//       
//        PowerMock.mockStatic(TnPersistentManager.class);
//        TnPersistentManager persistentManagerMock = PowerMock.createMock(TnPersistentManager.class);
//        EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persistentManagerMock);
//        EasyMock.expect(persistentManagerMock.openFileConnection("file1", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(fileMock);
//
//        PowerMock.replayAll();
//        provider.loadGpsSourceFiles(fileNames);
//        PowerMock.verifyAll();
//        Field gpsRecordsField = Whitebox.getField(ReplayLocationProvider.class, "gpsRecords");
//        try
//        {
//            Vector gpsRecords = (Vector)gpsRecordsField.get(provider);
//            TnLocation actualLoc2 = (TnLocation)gpsRecords.elementAt(1);
//            assertEquals(loc2.getLatitude(), actualLoc2.getLatitude());
//            assertEquals(loc2.getLongitude(), actualLoc2.getLongitude());
//        }
//        catch (IllegalArgumentException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IllegalAccessException e)
//        {
//            e.printStackTrace();
//        }
//    }

    public void testSetDeltTime()
    {
        provider.setDeltTime(100L);
        Field deltTimeField = Whitebox.getField(ReplayLocationProvider.class, "deltTime");
        try
        {
            long actualDeltTime = deltTimeField.getLong(provider);
            assertEquals(100L, actualDeltTime);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    
    public void testGetLocationDelegate() throws Exception
    {
        TnLocation loc1 = new TnLocation("ReplayLocationProvider");
        loc1.setTime(currentTime*100);
        loc1.setLocalTimeStamp(currentTime*100);
        loc1.setSpeed(100);
        loc1.setAccuracy(1);
        loc1.setHeading(30);
        loc1.setLatitude(100001);
        loc1.setLongitude(100002);
        loc1.setValid(true);
        loc1.setSatellites(4);
        
        TnLocation loc2 = new TnLocation("ReplayLocationProvider");
        loc2.setTime(currentTime*100);
        loc2.setLocalTimeStamp(currentTime*100);
        loc2.setSpeed(100);
        loc2.setAccuracy(2);
        loc2.setHeading(30);
        loc2.setLatitude(100003);
        loc2.setLongitude(100004);
        loc2.setValid(true);
        loc2.setSatellites(5);
        
        String log = loc1.toString()+"\r"+loc2.toString()+"\r";
        byte[] data = log.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        
        Vector fileNames = new Vector();
        fileNames.add("file1");
        
        TnFileConnection fileMock = PowerMock.createMock(TnFileConnection.class);
        EasyMock.expect(fileMock.openInputStream()).andReturn(inputStream);
        fileMock.close();
        EasyMock.expectLastCall();
       
       
        PowerMock.mockStatic(TnPersistentManager.class);
        TnPersistentManager persistentManagerMock = PowerMock.createMock(TnPersistentManager.class);
        EasyMock.expect(TnPersistentManager.getInstance()).andReturn(persistentManagerMock);
        EasyMock.expect(persistentManagerMock.openFileConnection("file1", "", TnPersistentManager.FILE_SYSTEM_EXTERNAL)).andReturn(fileMock);

        PowerMock.replayAll();
        provider.loadGpsSourceFiles(fileNames);
        PowerMock.verifyAll();
        provider.setDeltTime(1000000L);
    }
}
