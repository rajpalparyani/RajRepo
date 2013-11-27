package com.telenav.gps;

import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;

public class TnGpsFilter
{
    private TnLocation[] m_oldFixes = new TnLocation[OLD_FIX_COUNT];        // baseline/history buffer
    
    /** maximum tolerance for discrepancy between gps time and system time between 2 fixes */
    protected static int MAX_TIME_COHERENCE_THRESHOLD = 3; //RM - this is ratio, so no units are applicable

    /** number of fixes used as baseline/history */
    private static final int OLD_FIX_COUNT = 5;                        

    /** in ms unit, 2 min. [DW]:use 50 ms to ensure reset buffer every min */
    private static final int MAX_PREV_FIX_AGE_MS = 50000;    
    
    /** 1 meter = 9 DM6 */
    private static final int METER_DM6_FACTOR = 9;
    
    /** 50 m/s -> 450 DM6/s -> 112 mile/hour */
    private static final int MAX_SPEED = 50 * METER_DM6_FACTOR;
    
    /** 5 m/s -> 45 DM6/s -> 11 mile/hour */
    private static final int SPEED_ERROR = 5 * METER_DM6_FACTOR;    
    
    private static final int MIN_DISTANCE_TOLERANCE = 50 * METER_DM6_FACTOR * 1000; //in 1000*DM6 unit
    
    private int m_oldFixIdx = -1;
    
    public TnGpsFilter()
    {
        // [DW]: Init old fixes buffer
        for(int i=0; i<OLD_FIX_COUNT; i++)
            m_oldFixes[i] = new TnLocation("");

        resetOldFixes();
    }
    
    public boolean eliminateGpsNoise(TnLocation gpsdata)
    {
        return checkPrecise(gpsdata);
    }
    
    /**
     * Check if a GPS fix can successfully pass GPS filter and be used as a valid fix by application
     * @param fix - GPS fix to be passed through filter
     * @return true - fix is valid, false - fix is invalid
     */
    public boolean checkPrecise(TnLocation fix)
    {
        if (isOldFixesEmpty())
        {
            // simply add fix into buffer if buffer is empty
            m_oldFixes[++m_oldFixIdx].set(fix);
            
            //fix bug TNSIXTWO-748
            //In some condition, the second fix takes 1 min after first fix arrived.
            //so we use first fix to get route.
            return true;
        }
        else
        {
            if (! passesMaxAgeTest(m_oldFixes[m_oldFixIdx], fix))
            {
                //always reset the buffer if the previous fix inn the buffer is too old.
                resetOldFixes();                
                
                //[DW]:set new fix as the first fix in buffer.
                m_oldFixes[++m_oldFixIdx].set(fix);
                
                //we can start navigation with only one fix
                return true;
            }
            else if ( ! passesTimeValidityTest(m_oldFixes[m_oldFixIdx], fix))
            {                
                if (isOldFixesFull() == false)
                {
                    //reset the buffer only if the old fixes buffer is not full, otherwise discard the current fix only
                    resetOldFixes();
                }
            }
            else
            {
                boolean hasPassedDistanceTest = true;
                for (int i = 0; i < OLD_FIX_COUNT; ++i)
                {
                    if (!m_oldFixes[i].isValid())
                    {
                        break;
                    }
                    
                    //distance test with every fix in the buffer, and fail if any one test fail
                    if (! passesDistanceTest(m_oldFixes[i], fix))
                    {
                        hasPassedDistanceTest = false;
                        break;
                    }
                }
                
                if (! hasPassedDistanceTest)
                {
                    if (! isOldFixesFull())
                    {
                        //reset the buffer only if the old fixes buffer is not full, otherwise discard the current fix only
                        resetOldFixes();
                    }
                }
                else    //valid fix
                {
                    //circular buffer, wrap back to the head if buffer is full
                    m_oldFixIdx = (m_oldFixIdx + 1) % OLD_FIX_COUNT;
                    m_oldFixes[m_oldFixIdx].set(fix);
                    
                    // Consider a fix valid only if it passes all the test with a full buffer.
                    if (isOldFixesFull())
                    {
                        return true;
                    }
                    else 
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "oldFixes is not full");
                    }
                }//end else
            }//end else
        }//end else
        
        return false;
    }
    
    private boolean isSpeedValid(TnLocation gpsData)
    {
        if (gpsData == null) return false;
        
        return gpsData.getSpeed() >= 0 ;
    }
    
    /**
     * Check if the current GPS fix can pass the distance test against the previous fix
     * @param last - the GPS fix in the buffer to be compared with
     * @param cur - the candidate GPS fix to check
     * @return true - pass, false - fail
     */
    private boolean passesDistanceTest(TnLocation last, TnLocation cur)
    {        
        if (last.getTime() == cur.getTime()) 
        {
            //they are same fixes, no need to do distance test
            return true;
        }
        
        int startSpeed = MAX_SPEED;
        if (isSpeedValid(last))
        {
            startSpeed = last.getSpeed() + SPEED_ERROR;
            if (startSpeed >= 21 * METER_DM6_FACTOR)
            {
                startSpeed = MAX_SPEED;
            }
        }
        
        int endSpeed = MAX_SPEED;        
        if (isSpeedValid(cur))
        {
            endSpeed = cur.getSpeed() + SPEED_ERROR;            
            if (endSpeed >= 21 * METER_DM6_FACTOR)
            {
                endSpeed = MAX_SPEED;
            }
        }
        
        int speed = Math.max(startSpeed, endSpeed);            //in DM6/s unit        
        long deltat = (cur.getTime() - last.getTime()) * 10;    //in ms unit
        long maxDistance = speed * deltat;                    //in 1000*DM6 unit        
        if (maxDistance < MIN_DISTANCE_TOLERANCE)
            maxDistance = MIN_DISTANCE_TOLERANCE;
        
        // calculate the actual distance in meters
        //long distance = Math.abs(cur.lat - last.lat) + DataUtil.xCosY(Math.abs(cur.lon - last.lon), ((21 * last.lat) >> 21));
        // [DW]: Calculate the distance between two fixes using the RSS approach.
        long distance = 0;
        try
        {
             distance = DataUtil.gpsDistance(last.getLatitude() - cur.getLatitude(),
                                             last.getLongitude() - cur.getLongitude(),
                                             DataUtil.getCosLat(last.getLatitude())); 
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        distance *= METER_DM6_FACTOR * 1000;    //in 1000*DM6 unit
        if (distance > maxDistance)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "not pass distance test, "+distance+","+maxDistance);
        }
        
        return distance <= maxDistance ? true : false;
    }
    
    
    /**
     * Check if the current GPS fix is less than 2 min later than the previous fix
     * @param last - the GPS fix in the buffer to be compared with
     * @param cur - the candidate GPS fix to check
     * @return true - pass, false - fail
     */
    private boolean passesMaxAgeTest(TnLocation last, TnLocation cur)
    {
        long deltat = (cur.getTime() - last.getTime()) * 10;    //in ms unit
        return deltat < MAX_PREV_FIX_AGE_MS ? true : false;
    }
    
    /**
     * Check if the current GPS fix can pass time coherence test
     * @param last - the GPS fix in the buffer to be compared with
     * @param cur - the candidate GPS fix to check
     * @return true - pass, false - fail
     */
    private boolean passesTimeValidityTest(TnLocation last, TnLocation cur)
    {
        //mguo: not necessary because we use device time for GPS time
        //return true;
        //shal: RIM needs it, it does hurt if we have it in J2ME
        // negative deltat test
        if (cur.getTime() < last.getTime() || 
                cur.getLocalTimeStamp() < last.getLocalTimeStamp())
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "not pass time validity test, ("+cur.getTime()+","+last.getTime()+"), ("+cur.getLocalTimeStamp()+","+last.getLocalTimeStamp()+")");
            return false;
        }
        
        return true;
        
        /*
        
        // time coherence test
        
        //RM - 02/15/07 - adding time coherence test to prevent gps timestamp jumps
        //and positional jumps
        // [nathant] - Extend time coherence test.  Now we check 2 things:
        // 1. GPS time delta =< (MAX_TIME_COHERENCE_RATIO * system time delta)
        // 2. GPS time delta >= (MAX_TIME_COHERENCE_RATIO/10 * system time delta)
        
        //calculate deltat between last 2 gps timestamps (ms)
        long gpsDeltaT = (cur.timeTag - last.timeTag) * 10;
        
        //calculate deltat between last 2 system timestamps (ms)
        long systemDeltaT = cur.localTime - last.localTime;
        
        return (gpsDeltaT < systemDeltaT * MAX_TIME_COHERENCE_RATIO &&
                systemDeltaT < gpsDeltaT * MAX_TIME_COHERENCE_RATIO) ? true : false;
    */
    }
    
    /**
     * Reset the baseline (history buffer).
     */
    private void resetOldFixes()
    {
        m_oldFixIdx = -1;
        for (int i = 0; i < OLD_FIX_COUNT; ++i)
        {
            m_oldFixes[i].setValid(false);
        }
    }
    
    /**
     * Check if the baseline buffer is empty
     * @return true - empty, false - not empty
     */
    private boolean isOldFixesEmpty()
    {
        return m_oldFixIdx == -1 ? true : false;
    }
    
    /**
     * Check if the baseline buffer is full
     * @return true - full, false - not full
     */
    private boolean isOldFixesFull()
    {
        return m_oldFixes[OLD_FIX_COUNT -1].isValid();
    }
}
