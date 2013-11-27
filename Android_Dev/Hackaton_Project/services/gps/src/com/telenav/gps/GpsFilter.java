package com.telenav.gps;

import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;

/**
 * @author bduan
 * @date 2008-05-20
 */
public class GpsFilter
{
    private long lastFixTime = -1;

    private TnLocation lastFix;
    private TnLocation prevLastFix;
    private long goodFixes = 0;
    private long discardedFixes = 0;
//    private GpsData fakeData =  new GpsData();
    
    /** maximum tolerance for discrepancy between gps time and system time between 2 fixes */
    protected static int MAX_TIME_COHERENCE_THRESHOLD = 3; //RM - this is ratio, so no units are applicable

    /** maximum number of fixes discarded, before we try to take them */
    private static int MAX_NUMBER_DISCARDED_FIXES = 180;

    /** minimum number of fixes to be considered before attempting accepting "discarded" fixes */
    private static int MIN_STAT_FIXES = 30;

    private static int MAX_DELTAT_FOR_DISTANCE_TEST = 20000;//20 sec

    public GpsFilter()
    {
        lastFix = new TnLocation("");
        prevLastFix = new TnLocation("");
    }
    
    public boolean eliminateGpsNoise(TnLocation gpsdata)
    {
        
        if(gpsdata == null)
            return false;
        else
        {
            if (gpsdata.getTime() <= 0)
                return false;
            
            if (Math.abs(gpsdata.getLatitude()) > 90 * 100000 || Math.abs(gpsdata.getLongitude()) > 180 * 100000)
                return false;
            
            if (gpsdata.getSpeed() < 0 || gpsdata.getSpeed() > 4000)
                return false;
            
            if (gpsdata.getHeading() < 0 || gpsdata.getHeading() > 360)
                return false;
            
            if (gpsdata.getAccuracy() > 500)
                return false;
            
            TnLocation data = gpsdata;
//            System.out.println(" Start : " + System.currentTimeMillis());
//            System.out.println("Filter ====== " + data.timeTag );
            if (lastFixTime == -1 || !lastFix.isValid()) 
            {
                //lastFixTime = fix[Tracking.TIME];
                lastFixTime = data.getTime();

                //RM - initialize last fix
                lastFix.set(data);
                
                //RM - don't return this, we'll need at least one more fix
                //[XR]we need return true, otherwise if the first fix is valid, it will be filtered out 
                return true;
            }
            else
            {
//                System.out.println("last   ====== " + lastFix.timeTag);
                //RM - 02/15/07 - adding time coherence test to prevent gps timestamp jumps
                //and positional jumps
                long timeCoherenceTest = 0; //default ok
                long distanceTest = 0;//default ok
                long deltat = 0; //default ok

                long prevDistanceTest = 0; //default ok
                long prevDeltaT = 0;//default ok

                if (lastFix.isValid())
                {
                    //calculate deltat between last 2 gps timestamps (10ms)
                    long deltatG = data.getTime() - lastFix.getTime();
                    //calculate deltat between last 2 system timestamps and convert to 10ms
                    long deltatS = (data.getLocalTimeStamp() - lastFix.getLocalTimeStamp()) / 10;

                    if (deltatS != 0) timeCoherenceTest = deltatG / deltatS;

                    distanceTest = Math.abs(data.getLatitude() - lastFix.getLatitude()) +
                    DataUtil.xCosY(Math.abs(data.getLongitude() - lastFix.getLongitude()), ((21 * lastFix.getLatitude()) >> 21));
                    distanceTest <<= 1;//RM - multiply by to so we compare to 45 m/s equivalent

//                    int test = DataUtil.gpsDistance(Math.abs(data.lat - lastFix.lat), Math.abs(data.lon - lastFix.lon), DataUtil.getCosLat(lastFix.lat));
                    
                    deltat = data.getTime() - lastFix.getTime();//in 10 ms units

                    if (prevLastFix.isValid())
                    {
                        prevDistanceTest = Math.abs(data.getLatitude() - prevLastFix.getLatitude()) +
                        DataUtil.xCosY(Math.abs(data.getLongitude() - prevLastFix.getLongitude()), ((21 * lastFix.getLatitude()) >> 21));
                        prevDistanceTest <<= 1;//RM - multiply by to so we compare to 45 m/s equivalent

                        prevDeltaT = data.getTime() - prevLastFix.getTime();//in 10ms units
                        if (prevDeltaT < 0)
                        {
                            prevDeltaT = 0;
                            prevDistanceTest = 0;
                        }
                    }
                }

                if (takeDiscardedFix()) // see if we need to take the fix unconditionally
                {
//                      System.out.println("fix#" + (fixCounter-1) + " " + "taking fix unconditionally");
                    goodFixes = 1;
                    //reset the counters
                    discardedFixes = 0;
//                      discardedTimeFixes = 0;
                    //reset distance test
                    distanceTest = 0;
                    deltat = 0;
                    prevDistanceTest = 0;
                    prevDeltaT = 0;
                    //reset last fix
                    lastFix.setValid(false);
                    lastFixTime = -1;
                }

                if (data.getTime() <= lastFixTime)//negative deltat
                {
                    discardedFixes++;
                }
                else if (timeCoherenceTest >= MAX_TIME_COHERENCE_THRESHOLD) //time coherence test failed
                {
                    discardedFixes++;
                }
                else if (distanceTest > deltat || prevDistanceTest > prevDeltaT) //distance test failed
                {
                    //RM - only increment bad fixes here
                    discardedFixes++;
                    //RM - discard last fix as well
                    lastFix.setValid(false);
                }
                //delta is in 10 ms 
                //Rus 2008-04-29
                else if (deltat * 10 >= MAX_DELTAT_FOR_DISTANCE_TEST)
                {
                    //last fix is too old, re-init
                    lastFix.set(data);
                    lastFixTime = data.getTime();
//                    System.out.println("last fix too old - init fix");                            
                    
                }
                else//really valid fix
                {
                    if (!isGpsValid(data))
                    {
                        discardedFixes++;
                        lastFix.setValid(false);
                        return false;
                    }
                    
                    lastFixTime = data.getTime();
//                    prevLastFix = lastFix = data;
                    prevLastFix.set(data);
                    lastFix.set(data);
                    goodFixes++;
                    if (takeDiscardedFix()) //if we took discarded fix unconditionally
                    {
                        //reset the counters
                        goodFixes = 1;
                        discardedFixes = 0;
                    }
                    //Rus: the change is to correct the return code that comes back from filter into the tracking module. 
                    //After: move the "ret = GPS_POSITION into //really good fix clause
                    //Li Shoulong 2008-01-11
                    return true;
                }
                
                if (!isGpsValid(data))
                {
                    discardedFixes++;
                    lastFix.setValid(false);
                    return false;
                }
                
                //if curr gps not available set Fake data.
                data.setTime(lastFix.getTime());
                data.setLatitude(lastFix.getLatitude());
                data.setLongitude(lastFix.getLongitude());
                data.setSpeed(lastFix.getSpeed());
                data.setHeading(lastFix.getHeading());
                data.setAccuracy(lastFix.getAccuracy());
                data.setAltitude(lastFix.getAltitude());
                data.setSatellites(lastFix.getSatellites());
                data.setEncrypt(lastFix.isEncrypt());
                data.setValid(true);
                return true;
                
            }         
            
        }
    }
    private boolean takeDiscardedFix()
    {
        boolean takeFix = false; //discard by default
        if (    goodFixes >= MIN_STAT_FIXES ||
                discardedFixes >= MIN_STAT_FIXES
        )
        {
            //we have enough stats to judge the majority, check if
            //discarded fixes > 1.2 * good fixes
            if ((discardedFixes * 5) > (goodFixes * 6))
            {
                //invert - take the "bad" fix
                takeFix = true;
            }
        }
        return takeFix;
    }

    public boolean isGpsValid(TnLocation fix)
    {
        if (fix == null)
            return false;
        else
            return (fix.getLatitude() != 0 && fix.getLongitude() != 0);
    }
}
