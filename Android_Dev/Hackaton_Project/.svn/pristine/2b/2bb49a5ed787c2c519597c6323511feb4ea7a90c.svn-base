package com.telenav.navservice.location;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.TnLocationManager;
import com.telenav.navservice.TestUtil;

public class GpsRecorderTest
{
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testSetParameters() throws Exception
    {
        GpsRecorder recorder = new GpsRecorder(TnLocationManager.GPS_179_PROVIDER, new LocationBuffer(1, null));
        recorder.setParameters(0, 0, 0);
        assertFalse(recorder.isBurstMode);
        
        recorder.setParameters(1, 10, 10);
        assertFalse(recorder.isBurstMode);

        recorder.setParameters(2, 10, 10);
        assertTrue(recorder.isBurstMode);
        
        recorder.setParameters(5, 10, 10);
        assertTrue(recorder.isBurstMode);
    }
    
    @Test
    public void testSetPauseFor() throws Exception
    {
        GpsRecorder recorder = new GpsRecorder(TnLocationManager.GPS_179_PROVIDER, new LocationBuffer(1, null));
        recorder.pauseFor(10);
        long diff = recorder.pauseUntilMillis - System.currentTimeMillis();
        assertTrue(diff > 9000 && diff < 11000);
    }
}
