package com.telenav.navservice.location;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationProvider;
import com.telenav.navservice.MockLocationManager;
import com.telenav.navservice.TestUtil;
import com.telenav.navservice.MockLocationManager.TestLocationProvider;

public class GpsRecorderImplTestTiming
{
    private static String gps = "gpsimpl";
    
    private TestLocationProvider provider = new TestLocationProvider()
    {
        public TnLocationProvider getProviderDelegate(String provider)
        {
            if (gps.equals(provider))
            {
                return new TnLocationProvider(gps)
                {
                    private boolean isRunning;
                    private TnLocationProvider instance;
                    
                    @Override
                    protected void reset()
                    {
                        isRunning = false;
                    }
                    
                    @Override
                    protected void requestLocationUpdates(long minTime, float minDistance,
                            int timeout, int maxAge, ITnLocationListener listener)
                    {
                        instance = this;
                        isRunning = true;
                        final ITnLocationListener lis = listener;
                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                while(isRunning)
                                {
                                    TnLocation loc = new TnLocation(gps);
                                    loc.setLatitude(3737123);
                                    loc.setLongitude(-12234567);
                                    loc.setTime(System.currentTimeMillis()/10);
                                    loc.setLocalTimeStamp(System.currentTimeMillis());
                                    loc.setValid(true);
                                    lis.onLocationChanged(instance, loc);
                                    try{
                                        Thread.sleep(1000);
                                    }catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }

                    @Override
                    protected TnLocation getLastKnownLocation()
                            throws TnLocationException
                    {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    protected TnLocation getLocation(int timeout)
                            throws TnLocationException
                    {
                        // TODO Auto-generated method stub
                        return null;
                    }
                };
            }
            return null;
        }
        
    };

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

    @Test(timeout = 10000)
    public void testStart() throws Exception
    {
        MockLocationManager.getInstance().setTestLocationProvider(provider);
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        GpsRecorderImpl recorder = new GpsRecorderImpl(gps, gpsBuffer);
        recorder.setParameters(2, 5, 10000, 0);
        recorder.start();
        long time = System.currentTimeMillis();
        while(gpsBuffer.size() < 2)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time < 5000);
        assertEquals(gpsBuffer.size(), 2);
        
        while(gpsBuffer.size() < 4)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time < 10000);
        assertEquals(gpsBuffer.size(), 4);
        recorder.stop();
    }
    
    @Test(timeout = 20000)
    public void testIdleTime() throws Exception
    {
        MockLocationManager.getInstance().setTestLocationProvider(provider);
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        GpsRecorderImpl recorder = new GpsRecorderImpl(gps, gpsBuffer);
        recorder.setParameters(2, 10, 5, 0);
        recorder.start();
        
        long time = System.currentTimeMillis();
        assertTrue(recorder.isGettingGps);
        
        while(recorder.isGettingGps)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time < 10000);
        assertFalse(recorder.isGettingGps);
        
        while(!recorder.isGettingGps)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time < 12000);
        assertTrue(recorder.isGettingGps);
        
        while(recorder.isGettingGps)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time < 20000);
        assertFalse(recorder.isGettingGps);
        
        recorder.stop();
    }
    
    @Test(timeout = 15000)
    public void testWaitTime() throws Exception
    {
        MockLocationManager.getInstance().setTestLocationProvider(provider);
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        GpsRecorderImpl recorder = new GpsRecorderImpl(gps, gpsBuffer);
        recorder.setParameters(1, 5, 1, 0);
        recorder.start();
        
        long time = System.currentTimeMillis();
        while(recorder.isGettingGps)
            Thread.sleep(100);
        
        recorder.isRunning = false;
        while(!recorder.isStopped)
            Thread.sleep(100);
        assertTrue(System.currentTimeMillis() - time > 5000);
        assertTrue(recorder.isStopped);
        
        recorder.stop();
    }
    
    @Test(timeout = 15000)
    public void testPauseGpsTime() throws Exception
    {
        MockLocationManager.getInstance().setTestLocationProvider(provider);
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        GpsRecorderImpl recorder = new GpsRecorderImpl(gps, gpsBuffer);
        recorder.setParameters(1, 5, 1, System.currentTimeMillis() + 5 * 1000);
        recorder.start();
        
        long time = System.currentTimeMillis();
        while(!recorder.isGettingGps)
            Thread.sleep(100);
        
        assertTrue(System.currentTimeMillis() - time >= 4000);
    }
}
