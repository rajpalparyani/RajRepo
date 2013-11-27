package com.telenav.navservice.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.navservice.MockLocationManager;
import com.telenav.navservice.TestUtil;
import com.telenav.navservice.MockLocationManager.TestLocationProvider;

public class GpsRecorderImplTest
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

    @Test
    public void testSetParameters() throws Exception
    {
        GpsRecorderImpl recorder = new GpsRecorderImpl(TnLocationManager.GPS_179_PROVIDER, new LocationBuffer(1, null));
        recorder.setParameters(5, 60, 120, 1000);
        assertEquals(recorder.sampleSize, 5);
        assertEquals(recorder.sampleIntervalInMillis, 60 * 1000);
        assertEquals(recorder.idleTimeBeforeStopInMillis, 120 * 1000);
        assertEquals(recorder.pauseUntilMillis, 1000);
    }
    
    @Test
    public void testStop() throws Exception
    {
        MockLocationManager.getInstance().setTestLocationProvider(provider);
        LocationBuffer gpsBuffer = new LocationBuffer(100, null);
        GpsRecorderImpl recorder = new GpsRecorderImpl(gps, gpsBuffer);
        recorder.setParameters(2, 5, 10000, 0);
        recorder.start();
        
        recorder.stop();
        assertFalse(recorder.isGettingGps);
    }
}
